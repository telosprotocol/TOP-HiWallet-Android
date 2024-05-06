package com.topnetwork.audit.keystore.compat_keystore.compat;

import com.topnetwork.core.crypto.IPFS;
import com.topnetwork.audit.crypto.PinCrypto;
import com.topnetwork.core.identity.DecryptIpFsRootIdentity;
import com.topnetwork.core.identity.WalletInfo;
import com.topnetwork.audit.keystore.compat_keystore.CompatAliaskeyStoreHelper;
import com.topnetwork.core.model.WalletManager;

import java.io.IOException;

/**
 * The old version of the current new user does not have this logic, this is to adapt to the old user
 */
public class AliasKeyStoreV1 implements IAliaskeyStore {
    @Override
    public void Encryption(String content) {

    }
    @Override
    public String Decrypt(String cipherText) {
        DecryptIpFsRootIdentity decryptIpFsRootIdentity = null;
        try {
            decryptIpFsRootIdentity = WalletManager.getObjectMapper()
                    .readValue(WalletInfo.getInstance().getRootIdentity().getDecryptIpFsParamsStr(), DecryptIpFsRootIdentity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return IPFS.decryptDataFromIPFS(cipherText, decryptIpFsRootIdentity);
    }

    @Override
    public String getCipherText() {
        return WalletInfo.getInstance().getPin();
    }

    @Override
    public boolean verify(String password, String salt) {
        String iv = CompatAliaskeyStoreHelper.getInstance().getIv();
        IAliaskeyStore aliaskeyStore = CompatAliaskeyStoreHelper.getInstance().getAliaskeyStoreByIv(iv);
        String cipherText = aliaskeyStore.getCipherText();
        String pinHash = aliaskeyStore.Decrypt(cipherText);
        return PinCrypto.checkPin(password, salt, pinHash,true);
    }

    @Override
    public String decryptSalt(String saltCipherText) {
        return "";
    }

    @Override
    public void clear() {

    }

}
