package com.topnetwork.core.walletfactory;

import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topnetwork.core.eos.EOSKey;
import com.topnetwork.core.identity.WalletInfo;
import com.topnetwork.core.keystore.model_keystore.HDKeystore;
import com.topnetwork.core.keystore.model_keystore.TopKeyStoreV3;
import com.topnetwork.core.utils.BIP44Util;
import com.topnetwork.core.helper.CoinEnum;
import com.topnetwork.core.model.Metadata;
import com.topnetwork.core.model.Wallet;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.topj.account.Account;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletFile;

import wallet.core.jni.AnyAddress;
import wallet.core.jni.CoinType;
import wallet.core.jni.FilecoinAddressConverter;
import wallet.core.jni.FilecoinAddressType;
import wallet.core.jni.PrivateKey;
import wallet.core.jni.PublicKey;

public class WalletFactory {

    /**
     * Generate wallet
     *
     * @param coinEnum
     * @param seeds
     * @param password
     * @param index
     * @return
     */
    public static Wallet getWallet(CoinEnum coinEnum, byte[] seeds, String password, int index) {
        Wallet wallet = null;
        switch (coinEnum) {
            case ETH:
                wallet = getEthWallet(coinEnum, seeds, password, index);
                break;
            case BTC:
            case LTC:
            case DASH:
            case BCH:
            case DOGE:
                wallet = getUTXOWallet(coinEnum, seeds, password, index);
                break;
            case EOS:
                wallet = getEOSWallet(coinEnum, seeds, password, index);
                wallet.setNeedActivate(true);
                break;
            case FIL:
                wallet = getFilWallet(coinEnum, seeds, password, index);
                break;
            case FIL_EVM:
                wallet = getFilEVMWallet(coinEnum, seeds, password, index);
                break;
            case TOP:
                if(coinEnum.getVersion() == 1){
                    wallet = getTopWallet(coinEnum, seeds, password, index);
                }else if(coinEnum.getVersion() == 3) {
                    wallet = getTopV3Wallet(coinEnum, seeds, password, index);
                }

                break;
            default:
                break;
        }
        return wallet;
    }

    /**
     * Generate Eth wallet
     *
     * @param coinEnum
     * @param seeds
     * @param password
     * @param index
     * @return
     */
    private static Wallet getEthWallet(CoinEnum coinEnum, byte[] seeds, String password, int index) {
        String path = coinEnum.getPath(index);
        Wallet wallet = new Wallet();
        try {
            String[] pathArray = path.split("/");
            DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seeds);
            for (int i = 1; i < pathArray.length; i++) {
                ChildNumber childNumber;
                if (pathArray[i].endsWith("'")) {
                    int number = Integer.parseInt(pathArray[i].substring(0, pathArray[i].length() - 1));
                    childNumber = new ChildNumber(number, true);
                } else {
                    int number = Integer.parseInt(pathArray[i]);
                    childNumber = new ChildNumber(number, false);
                }
                dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
            }
            ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());
            WalletFile walletFile = org.web3j.crypto.Wallet.create(password, keyPair, 1024, 1);
            String jsonWalletFile = "";
            try {
                jsonWalletFile = getObjectMapper().writeValueAsString(walletFile);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Credentials credentials = Credentials.create(keyPair);
            String address = Keys.toChecksumAddress(Keys.getAddress(credentials.getEcKeyPair().getPublicKey()));
            wallet.setIndex(index);
            wallet.setAddress(address);
            wallet.setKeystore(jsonWalletFile);
        } catch (CipherException e) {
            e.printStackTrace();
        }
        return wallet;
    }

    /**
     * Generate UTXO wallet
     *
     * @param coinEnum
     * @param seeds
     * @param password
     * @param index
     * @return
     */
    private static Wallet getUTXOWallet(CoinEnum coinEnum, byte[] seeds, String password, int index) {
        Wallet wallet = new Wallet();
        //Generate path based on index
        String path = coinEnum.getPath(index);
        DeterministicSeed seed = new DeterministicSeed(WalletInfo.getInstance().getImportMnemonicArray(), seeds, "", 0L);
        DeterministicKeyChain keyChain = DeterministicKeyChain.builder().seed(seed).build();
        DeterministicKey parent = keyChain.getKeyByPath(BIP44Util.generatePath(path), true);
        HDKeystore hdKeystore = HDKeystore.create(new Metadata(), password, parent, coinEnum.getNetworkParameters());

        wallet.setIndex(index);
        try {
            wallet.setKeystore(getObjectMapper().writeValueAsString(hdKeystore));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        wallet.setAddress(hdKeystore.getAddress());
        return wallet;
    }

    /**
     * Generate EOS wallet
     *
     * @param coinEnum
     * @param seeds
     * @param password
     * @param index
     * @return
     */
    private static Wallet getEOSWallet(CoinEnum coinEnum, byte[] seeds, String password, int index) {
        Wallet wallet = new Wallet();
        //Generate path based on index
        String path = coinEnum.getPath(index);
        DeterministicSeed seed = new DeterministicSeed(WalletInfo.getInstance().getImportMnemonicArray(), seeds, "", 0L);
        DeterministicKeyChain keyChain = DeterministicKeyChain.builder().seed(seed).build();
        DeterministicKey parent = keyChain.getKeyByPath(BIP44Util.generatePath(path), true);
        HDKeystore hdKeystore = HDKeystore.create(new Metadata(), password, parent, coinEnum.getNetworkParameters());
        wallet.setIndex(index);
        try {
            wallet.setKeystore(getObjectMapper().writeValueAsString(hdKeystore));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        EOSKey eosKey = EOSKey.fromPrivate(parent.getPrivKeyBytes());
        wallet.setAddress(eosKey.getPublicKeyAsHex());
        return wallet;
    }

    /**
     * Generate Fil wallet
     *
     * @param coinEnum
     * @param seeds
     * @param password
     * @param index
     * @return
     */
    private static Wallet getFilWallet(CoinEnum coinEnum, byte[] seeds, String password, int index) {
        Wallet wallet = new Wallet();
        //Generate path based on index
        String path = coinEnum.getPath(index);
        DeterministicSeed seed = new DeterministicSeed(WalletInfo.getInstance().getImportMnemonicArray(), seeds, "", 0L);
        DeterministicKeyChain keyChain = DeterministicKeyChain.builder().seed(seed).build();
        DeterministicKey parent = keyChain.getKeyByPath(BIP44Util.generatePath(path), true);
        HDKeystore hdKeystore = HDKeystore.createNotInitializeAddress(new Metadata(), password, parent, coinEnum.getNetworkParameters());
        wallet.setIndex(index);
        try {
            wallet.setKeystore(getObjectMapper().writeValueAsString(hdKeystore));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String address = CoinType.FILECOIN.deriveAddress(new PrivateKey(parent.getPrivKeyBytes()));
        wallet.setAddress(address);
        return wallet;
    }

    /**
     *  Generate Fil EVM wallet
     *
     * @param coinEnum
     * @param seeds
     * @param password
     * @param index
     * @return
     */
    private static Wallet getFilEVMWallet(CoinEnum coinEnum, byte[] seeds, String password, int index) {
        Wallet wallet = new Wallet();
        //Generate path based on index
        String path = coinEnum.getPath(index);
        DeterministicSeed seed = new DeterministicSeed(WalletInfo.getInstance().getImportMnemonicArray(), seeds, "", 0L);
        DeterministicKeyChain keyChain = DeterministicKeyChain.builder().seed(seed).build();
        DeterministicKey parent = keyChain.getKeyByPath(BIP44Util.generatePath(path), true);
        HDKeystore hdKeystore = HDKeystore.createNotInitializeAddress(new Metadata(), password, parent, coinEnum.getNetworkParameters());
        wallet.setIndex(index);
        try {
            wallet.setKeystore(getObjectMapper().writeValueAsString(hdKeystore));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //Generate address
        PrivateKey privateKey = new PrivateKey(parent.getPrivKeyBytes());
        PublicKey publicKey = privateKey.getPublicKeySecp256k1(false);
        AnyAddress anyAddress = new AnyAddress(publicKey, FilecoinAddressType.DELEGATED);
        String address = anyAddress.description();
        String ethereumAddress = FilecoinAddressConverter.convertToEthereum(address);
        wallet.setAddress(ethereumAddress);
        return wallet;
    }

    /**
     * Generate Top wallet
     *
     * @param coinEnum
     * @param seeds
     * @param password
     * @param index
     * @return
     */
    private static Wallet getTopWallet(CoinEnum coinEnum, byte[] seeds, String password, int index) {
        Wallet wallet = new Wallet();
        //Generate the path based on index
        String path = coinEnum.getPath(index);
        DeterministicSeed seed = new DeterministicSeed(WalletInfo.getInstance().getImportMnemonicArray(), seeds, "", 0L);
        DeterministicKeyChain keyChain = DeterministicKeyChain.builder().seed(seed).build();
        DeterministicKey parent = keyChain.getKeyByPath(BIP44Util.generatePath(path), true);
        HDKeystore hdKeystore = HDKeystore.createNotInitializeAddress(new Metadata(), password, parent, null);
        wallet.setIndex(index);
        try {
            wallet.setKeystore(getObjectMapper().writeValueAsString(hdKeystore));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Account account = new Account(Base64.encodeToString(parent.getPrivKeyBytes(),Base64.DEFAULT));
        wallet.setAddress(account.getAddress());
        return wallet;
    }


    private static Wallet getTopV3Wallet(CoinEnum coinEnum, byte[] seeds, String password, int index){
        Wallet wallet = new Wallet();
        String path = coinEnum.getPath(index);
        DeterministicSeed seed = new DeterministicSeed(WalletInfo.getInstance().getImportMnemonicArray(), seeds, "", 0L);
        DeterministicKeyChain keyChain = DeterministicKeyChain.builder().seed(seed).build();
        DeterministicKey parent = keyChain.getKeyByPath(BIP44Util.generatePath(path), true);
        TopKeyStoreV3 hdKeystore = TopKeyStoreV3.create(new Metadata(), password, parent, null);
        wallet.setIndex(index);
        try {
            wallet.setKeystore(getObjectMapper().writeValueAsString(hdKeystore));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Account account = new Account(parent.getPrivateKeyAsHex());

        wallet.setAddress(account.getAddress());
        return wallet;
    }

    /**
     * Get ObjectMapper
     *
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

}
