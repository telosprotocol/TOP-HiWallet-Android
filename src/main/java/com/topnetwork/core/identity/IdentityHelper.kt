package com.topnetwork.core.identity

import android.content.Context
import com.topnetwork.core.model.eth.Messages
import com.topnetwork.core.model.eth.TokenException
import com.topnetwork.core.sign.eth.EthereumSign
import com.topnetwork.core.utils.ByteUtil
import com.topnetwork.core.utils.MnemonicUtil
import com.topnetwork.core.utils.NumericUtil
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Joiner
import com.topnetwork.audit.crypto.PinCrypto.getVerifyPin
import com.topnetwork.core.crypto.*
import com.topnetwork.core.database.DaoManager
import com.topnetwork.core.database.DataBaseManager
import com.topnetwork.audit.keystore.compat_keystore.compat.AliasKeyStoreV2
import com.topnetwork.audit.keystore.compat_keystore.CompatAliaskeyStoreHelper
import com.topnetwork.audit.keystore.compat_keystore.compat.IAliaskeyStore
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Utils
import org.bitcoinj.core.VarInt
import org.bitcoinj.crypto.HDKeyDerivation
import org.bitcoinj.wallet.DeterministicSeed
import java.io.IOException
import java.nio.charset.Charset
import java.security.SignatureException
import java.util.*
import kotlin.collections.ArrayList

const val TAG = "IdentityHelper"

/**
 * Newly created wallet identity
 * If it is new, it should be encrypted with the new version of V2
 */

fun createIdentity(password: String, mnemonicArray: ArrayList<String>, context: Context): Boolean {
    // Generate mnemonics
    val mnemonicCodes = (if (mnemonicArray.size > 0) {
        mnemonicArray
    } else {
        MnemonicUtil.randomMnemonicCodes() as ArrayList<String>
    })
    //LogUtils.dTag(TAG, "mnemonicCodes >>>$mnemonicCodes")
    // Generate seeds, use real cryptography to do symmetric encryption
    val identityKeystore = createSeed(password, mnemonicCodes) ?: return false
    //LogUtils.dTag(TAG, "identityKeystore >>>$identityKeystore")
    val keystoreStr: String = try {
        getObjectMapper().writeValueAsString(identityKeystore)
    } catch (e: JsonProcessingException) {
        e.printStackTrace()
        return false
    }
    //LogUtils.json(TAG, keystoreStr)
    // Create identity information
    val newIdentity = Identity().apply {
        keystore = keystoreStr
    }
    var iKeyStore: IAliaskeyStore = CompatAliaskeyStoreHelper.getInstance().getAliaskeyStoreByVersionCode(context);
    iKeyStore.Encryption(password)
    //LogUtils.json(TAG, newIdentity)
    // Create a multi-identity database
    val ipfsPin = encryptDataToIPFS(password, keystoreStr)
    val newSeedId = createSeedId(identityKeystore, password)
    //LogUtils.dTag(TAG, "newSeedId$newSeedId")
    // Code salting
    val newSaltPassword = getVerifyPin((iKeyStore as AliasKeyStoreV2).salt, password) // 密码和盐两次sha256has的16进制字符
    val newRootIdentity = createRootIdentity(identityKeystore, ipfsPin, newSeedId, newSaltPassword)
        ?: return false
    if (mnemonicArray.size > 0) {
        newRootIdentity.setBackUpMnemonic(true)
    }
    //LogUtils.json(TAG, newRootIdentity)
    var indentityPassWord = password + iKeyStore.salt
    DataBaseManager.getInstance().openIdentityDataBase(indentityPassWord, newSeedId, context)
    DaoManager.getInstance().insertRootIdentity(newRootIdentity)
    WalletInfo.getInstance().rootIdentity = newRootIdentity
    DaoManager.getInstance().setCurrentIdentity(newIdentity)
    DaoManager.getInstance().insertIdentity(newIdentity)
    WalletInfo.getInstance().run {
        saltPassword = newSaltPassword
        identity = newIdentity
        rootIdentity = newRootIdentity
        currentIdentityDataBaseName = newSeedId
        pin = ipfsPin
        importMnemonicArray = mnemonicCodes
    }
    iKeyStore.clear()
    return true
}

/**
 * Generate key seeds
 * @param saltPassword A code with salt
 * @param mnemonicCodes Mnemonic word
 */
private fun createSeed(saltPassword: String, mnemonicCodes: List<String>): IdentityKeystore? {
    //Get seed
    val creationTimeSeconds = System.currentTimeMillis() / 1000
    val seed = DeterministicSeed(mnemonicCodes, null, "", creationTimeSeconds)
    val seedBytes = seed.seedBytes ?: return null
    //LogUtils.dTag(TAG, "seedBytes >>>${Numeric.toHexString(seedBytes)}")
    val masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seedBytes)  // Generate key
    val masterKey = masterPrivateKey.privKeyBytes
    // LogUtils.dTag(TAG, "masterKey >>>${Numeric.toHexString(masterKey)}")
    val backupSalt = "Automatic Backup Key Mainnet"
    val backupKey = Hash.hmacSHA256(masterKey, backupSalt.toByteArray(Charset.forName("ASCII")))
        ?: return null
    //LogUtils.dTag(TAG, "backupKey >>>${Numeric.toHexString(backupKey)}")
    val encKeySalt = "Encryption Key"
    val encKeyFullBytes = Hash.hmacSHA256(backupKey, encKeySalt.toByteArray(Charset.forName("UTF-8")))
        ?: return null
    //LogUtils.dTag(TAG, "encKeyFullBytes >>>${Numeric.toHexString(encKeyFullBytes)}")
    val ecKey = ECKey.fromPrivate(encKeyFullBytes, false)
    //LogUtils.dTag(TAG, seedBytes[0].toInt() + seedBytes[1].toInt() + seedBytes[3].toInt() + seedBytes[10].toInt())
    return IdentityKeystore().apply {
        encKey = NumericUtil.bytesToHex(encKeyFullBytes)
        ipfsId = Multihash(Multihash.Type.sha2_256, Hash.sha256(ecKey.pubKey)).toBase58()
        crypto = Crypto.createPBKDF2CryptoWithKDFCached(saltPassword, seedBytes)
        encMnemonic = crypto.deriveEncPair(saltPassword, Joiner.on(" ").join(mnemonicCodes).toByteArray())
        id = id ?: UUID.randomUUID().toString()
    }
}

/**
 * Create RootIdentity
 */
private fun createRootIdentity(
    identityKeystore: IdentityKeystore, ipfs: String, newSeedId: String,
    saltPasswordStr: String
): RootIdentity? {
    val decryptIpFsRootIdentity = DecryptIpFsRootIdentity()
    decryptIpFsRootIdentity.encKey = identityKeystore.encKey
    decryptIpFsRootIdentity.ipfsId = identityKeystore.ipfsId
    val decryptIpFsRootIdentityStr = getObjectMapper().writeValueAsString(decryptIpFsRootIdentity)
    return RootIdentity().apply {
        seedId = newSeedId
        setIsSetDefaultWallet(false)
        setCurrentShowIdentity(true)
        setIsSetTouchId(false)
        iv = WalletInfo.getInstance().iv
        saltIv = WalletInfo.getInstance().satIv
        cipherText = WalletInfo.getInstance().cipherText
        saltCipherText = WalletInfo.getInstance().saltCipherText
        pinVerifyStr = saltPasswordStr
        decryptIpFsParamsStr = decryptIpFsRootIdentityStr
    }
}

/**
 * The Hash of the seed Id serves as the database name
 * @param identityKeystore
 * @param saltPassword A code with salt
 */
private fun createSeedId(identityKeystore: IdentityKeystore, saltPassword: String): String {
    val bytes = Sha256Hash.hash(
        identityKeystore
            .decryptCiphertext(saltPassword)
    )
    return NumericUtil.bytesToHex1(bytes)
}

/**
 * 加密密码
 * @param saltPassword A code with salt
 * @param keystoreStr IdentityKeyStore Generated information
 */
private fun encryptDataToIPFS(saltPassword: String, keystoreStr: String): String {
    val unixTime = Utils.currentTimeSeconds()
    val headerLength = 21
    val toSign = ByteArray(headerLength + 32)
    val version: Byte = 0x03
    toSign[0] = version
    val timestamp = ByteArray(4)

    //timestampAdd timestamp to byte array
    Utils.uint32ToByteArrayLE(unixTime, timestamp, 0)

    //The timestamp word is stored in toSign
    System.arraycopy(timestamp, 0, toSign, 1, 4)

    //Get the key
    var encryptionKey = ByteArray(0)
    try {
        encryptionKey = NumericUtil.hexToBytes(getObjectMapper().readValue<IdentityKeystore>(keystoreStr, IdentityKeystore::class.java).encKey)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    val iv = NumericUtil.generateRandomBytes(16)
    //Add the extra string to the tosign
    System.arraycopy(iv, 0, toSign, 5, 16)

    //Get 16 bits of the key
    val encKey = encryptionKey.copyOf(16)

    //Encrypted data（encKey, iv）；
    val ciphertext = AES.encryptByCBC(saltPassword.toByteArray(Charset.forName("UTF-8")), encKey, iv)

    //Generates the length of the encrypted data VarInt；
    val ciphertextLength = VarInt(ciphertext.size.toLong())

    //tosiginjoinHash.merkleHash(ciphertext)section
    System.arraycopy(Hash.merkleHash(ciphertext), 0, toSign, headerLength, 32)

    //Signing is also a key
    val signature = EthereumSign.sign(NumericUtil.bytesToHex(toSign), encryptionKey)

    //Get the byte after the signature
    val signatureBytes = NumericUtil.hexToBytes(signature)
    //LogUtils.dTag(TAG, "signature >>>$signature")
    //LogUtils.dTag(TAG, "length：" + +signatureBytes.size)

    // totalLen Total bytes
    val totalLen = (headerLength.toLong() + ciphertextLength.sizeInBytes.toLong() + ciphertextLength.value + 65).toInt()
    val payload = ByteArray(totalLen)
    var destPos = 0
    System.arraycopy(toSign, 0, payload, destPos, headerLength)
    destPos += headerLength
    System.arraycopy(ciphertextLength.encode(), 0, payload, destPos, ciphertextLength.sizeInBytes)
    destPos += ciphertextLength.sizeInBytes
    System.arraycopy(ciphertext, 0, payload, destPos, ciphertextLength.value.toInt())
    destPos += ciphertextLength.value.toInt()

    System.arraycopy(signatureBytes, 0, payload, destPos, 65)

    //All the calculated bytes are added again to form a string
    return NumericUtil.bytesToHex(payload)
}

/**
 * Decryption code
 * @param encryptedData The pin is overdense
 */
fun decryptDataFromIPFS(encryptedData: String): String {
    val headerLength = 21

    val payload = NumericUtil.hexToBytes(encryptedData)

    val version = payload[0]
    if (version.toInt() != 0x03) {
        throw TokenException(Messages.UNSUPPORT_ENCRYPTION_DATA_VERSION)
    }
    var srcPos = 1
    val toSign = ByteArray(headerLength + 32)
    System.arraycopy(payload, 0, toSign, 0, headerLength)

    val timestamp = ByteArray(4)
    System.arraycopy(payload, srcPos, timestamp, 0, 4)
    srcPos += 4

    val encryptionKey = NumericUtil.hexToBytes(getIdentityKeystore()!!.encKey)
    val iv = ByteArray(16)
    System.arraycopy(payload, srcPos, iv, 0, 16)
    srcPos += 16
    val ciphertextLength = VarInt(payload, srcPos)
    srcPos += ciphertextLength.sizeInBytes
    val ciphertext = ByteArray(ciphertextLength.value.toInt())
    System.arraycopy(payload, srcPos, ciphertext, 0, ciphertextLength.value.toInt())
    System.arraycopy(Hash.merkleHash(ciphertext), 0, toSign, headerLength, 32)
    srcPos += ciphertextLength.value.toInt()
    val encKey = Arrays.copyOf(encryptionKey, 16)
    val content = String(AES.decryptByCBC(ciphertext, encKey, iv), Charset.forName("UTF-8"))

    val signature = ByteArray(65)
    System.arraycopy(payload, srcPos, signature, 0, 65)
    try {
        val pubKey = EthereumSign.ecRecover(NumericUtil.bytesToHex(toSign), NumericUtil.bytesToHex(signature))
        val ecKey = ECKey.fromPublicOnly(ByteUtil.concat(byteArrayOf(0x04), NumericUtil.bigIntegerToBytesWithZeroPadded(pubKey, 64)))
        val recoverIpfsID = Multihash(Multihash.Type.sha2_256, Hash.sha256(ecKey.pubKey)).toBase58()

        if (getIdentityKeystore()!!.ipfsId != recoverIpfsID) {
            throw TokenException(Messages.INVALID_ENCRYPTION_DATA_SIGNATURE)
        }

    } catch (e: SignatureException) {
        throw TokenException(Messages.INVALID_ENCRYPTION_DATA_SIGNATURE)
    } catch (e: NullPointerException) {
        e.printStackTrace()
    }

    return content
}

fun getIdentityKeystore(): IdentityKeystore? {
    try {
        return getObjectMapper().readValue<IdentityKeystore>(WalletInfo.getInstance().identity.keystore, IdentityKeystore::class.java)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

/**
 * Gets a list of mnemonics based on the password
 */
fun getMnemonicCodes(password: String): List<String>? {
    val identityKeystore = getIdentityKeystore() ?: return null
    val mnemonic = String(identityKeystore.crypto.decryptEncPair(password, identityKeystore.encMnemonic))
    return listOf(*mnemonic.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
}

/**
 * Load user identity data from the database
 */
fun tryLoadFromDataBase(password: String, context: Context): Identity? {
    DataBaseManager.getInstance().openIdentityDataBase(password, WalletInfo.getInstance().currentIdentityDataBaseName, context)
    return getIdentity()
}

fun tryLoadFromDataBaseByForceUpDataV1(): Identity? {
    return getIdentity()
}

fun getIdentity(): Identity? {
    val identities = DaoManager.getInstance().queryAllIdentityData()
    //LogUtils.eTag(TAG, identities)
    val size = identities.size
    //LogUtils.eTag(TAG, size.toString())
    if (size <= 0) return null
    DaoManager.getInstance().setCurrentIdentity(identities[0])
    //LogUtils.eTag(TAG, identities[0])
    return identities[0]
}

/**
 * Get IdentityPin
 *
 * @return
 */
fun getIdentityPin(): String? {
    WalletInfo.getInstance().rootIdentity ?: return null
    var decryptIpFsRootIdentity: DecryptIpFsRootIdentity? = null
    try {
        decryptIpFsRootIdentity = getObjectMapper().readValue<DecryptIpFsRootIdentity>(
            WalletInfo.getInstance().rootIdentity.decryptIpFsParamsStr,
            DecryptIpFsRootIdentity::class.java
        )
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return IPFS.decryptDataFromIPFS(WalletInfo.getInstance().pin, decryptIpFsRootIdentity)
}

/**
 * Get ObjectMapper
 */
private fun getObjectMapper(): ObjectMapper {
    return ObjectMapper().apply {
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }
}

/**
 * Get seeds
 *
 * @param password
 * @return
 */
fun getSeeds(password: String): ByteArray? {
    val keystore: IdentityKeystore?
    try {
        keystore = getIdentityKeystore()
        return keystore!!.decryptCiphertext(password)
    } catch (e: Exception) {
        return null
    }

}