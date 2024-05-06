package com.topnetwork.core.crypto;

import com.topnetwork.core.identity.DecryptIpFsRootIdentity;
import com.topnetwork.core.model.eth.Messages;
import com.topnetwork.core.model.eth.TokenException;
import com.topnetwork.core.sign.eth.EthereumSign;
import com.topnetwork.core.utils.ByteUtil;
import com.topnetwork.core.utils.NumericUtil;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.VarInt;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SignatureException;
import java.util.Arrays;

public class IPFS {
    public static String decryptDataFromIPFS(String encryptedData, DecryptIpFsRootIdentity decryptIpFsRootIdentity ) {
        int headerLength = 21;

        byte[] payload = NumericUtil.hexToBytes(encryptedData);

        byte version = payload[0];
        if (version != 0x03) {
            throw new TokenException(Messages.UNSUPPORT_ENCRYPTION_DATA_VERSION);
        }
        int srcPos = 1;
        byte[] toSign = new byte[headerLength + 32];
        System.arraycopy(payload, 0, toSign, 0, headerLength);

        byte[] timestamp = new byte[4];
        System.arraycopy(payload, srcPos, timestamp, 0, 4);
        srcPos += 4;

        byte[] encryptionKey = NumericUtil.hexToBytes(decryptIpFsRootIdentity.getEncKey());
        byte[] iv = new byte[16];
        System.arraycopy(payload, srcPos, iv, 0, 16);
        srcPos += 16;
        VarInt ciphertextLength = new VarInt(payload, srcPos);
        srcPos += ciphertextLength.getSizeInBytes();
        byte[] ciphertext = new byte[(int) ciphertextLength.value];
        System.arraycopy(payload, srcPos, ciphertext, 0, (int) ciphertextLength.value);
        System.arraycopy(Hash.merkleHash(ciphertext), 0, toSign, headerLength, 32);
        srcPos += ciphertextLength.value;
        byte[] encKey = Arrays.copyOf(encryptionKey, 16);
        String content = new String(AES.decryptByCBC(ciphertext, encKey, iv), Charset.forName("UTF-8"));

        byte[] signature = new byte[65];
        System.arraycopy(payload, srcPos, signature, 0, 65);
        try {
            BigInteger pubKey = EthereumSign.ecRecover(NumericUtil.bytesToHex(toSign), NumericUtil.bytesToHex(signature));
            ECKey ecKey = ECKey.fromPublicOnly(ByteUtil.concat(new byte[]{0x04}, NumericUtil.bigIntegerToBytesWithZeroPadded(pubKey, 64)));
            String recoverIpfsID = new Multihash(Multihash.Type.sha2_256, Hash.sha256(ecKey.getPubKey())).toBase58();

            if (!decryptIpFsRootIdentity.getIpfsId().equals(recoverIpfsID)) {
                throw new TokenException(Messages.INVALID_ENCRYPTION_DATA_SIGNATURE);
            }

        } catch (SignatureException e) {
            throw new TokenException(Messages.INVALID_ENCRYPTION_DATA_SIGNATURE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return content;
    }
}
