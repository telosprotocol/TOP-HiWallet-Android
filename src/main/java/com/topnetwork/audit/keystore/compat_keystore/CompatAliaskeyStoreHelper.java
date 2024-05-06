package com.topnetwork.audit.keystore.compat_keystore;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.topnetwork.core.crypto.Crypto;
import com.topnetwork.core.database.DaoManager;
import com.topnetwork.core.database.DataBaseManager;
import com.topnetwork.core.identity.Identity;
import com.topnetwork.core.identity.IdentityKeystore;
import com.topnetwork.core.identity.WalletInfo;
import com.topnetwork.audit.keystore.compat_keystore.compat.AliasKeyStoreV2;
import com.topnetwork.audit.keystore.compat_keystore.compat.IAliaskeyStore;
import com.topnetwork.core.utils.StringUtils;

import org.web3j.protocol.core.filters.Callback;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.topnetwork.core.model.WalletManager.getObjectMapper;

public class CompatAliaskeyStoreHelper {

    static CompatAliaskeyStoreHelper compatAliaskeyStoreHelper;

    public static CompatAliaskeyStoreHelper getInstance() {
        if (compatAliaskeyStoreHelper == null) {
            compatAliaskeyStoreHelper = new CompatAliaskeyStoreHelper();
        }
        return compatAliaskeyStoreHelper;
    }

    public IAliaskeyStore getAliaskeyStoreByVersionCode(Context context) {
//        IAliaskeyStore iAliaskeyStore;
//        if (getVersionCode(context) >= 125) {
//            iAliaskeyStore = new AliasKeyStoreV2();
//        } else {
//            iAliaskeyStore = new AliasKeyStoreV1();
//        }
        return new AliasKeyStoreV2();
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    public IAliaskeyStore getAliaskeyStoreByIv(String iv) {
//        IAliaskeyStore iAliaskeyStore;
//        if (StringUtils.isEmpty(iv)) {
//            iAliaskeyStore = new AliasKeyStoreV1();
//        } else {
//            iAliaskeyStore = new AliasKeyStoreV2();
//        }
        return new AliasKeyStoreV2();
    }

    /**
     * Get V1
     *
     * @return
     */
    public String getIv() {
        return WalletInfo.getInstance().getIv();
    }

    /**
     * Get ciphertext
     *
     * @return
     */
    public String getCipherText() {
        return WalletInfo.getInstance().getCipherText();
    }

    /**
     *  Get ciphertext
     *
     * @return
     */
    public String getSaltCipherText() {
        return WalletInfo.getInstance().getSaltCipherText();
    }

    /**
     * The new encryption rules must be forcibly updated from the old version to the new version
     */
    public void forceUpDataV1(String dataBase, String password, String originPassWord, Callback<Boolean> callback, Context context) {
        //LogUtils.eTag("CompatAliaskeyStoreHelper", originPassWord + "");
        String iv = getIv();
        if (StringUtils.isEmpty(iv)) {
            if (!DataBaseManager.getInstance().isOpenIdentityDataBase()) {
                DataBaseManager.getInstance().openIdentityDataBase(originPassWord, WalletInfo.getInstance().getCurrentIdentityDataBaseName(),context);
            }
            List<Identity> list = DaoManager.getInstance().queryAllIdentityData();
            DataBaseManager.getInstance().setOpenIdentityDataBase(false);
            DataBaseManager.getInstance().closeDbIdentity();
            String dataBaseName = dataBase;
            if (!dataBaseName.endsWith(".db")) {
                dataBaseName = dataBaseName + ".db";
            }

            File filePath = context.getDatabasePath(dataBaseName);
            boolean b = filePath.delete();
            if (b) {
                AliasKeyStoreV2 aliasKeyStoreV2 = new AliasKeyStoreV2();
                String salt = aliasKeyStoreV2.getSaltRandom();
                aliasKeyStoreV2.encryptionSalt(salt);
                DataBaseManager.getInstance().openIdentityDataBase(password + salt, dataBaseName,context);
                //LogUtils.eTag("CompatAliaskeyStoreHelper","CompatAliaskeyStoreHelper: DataBase password" +  password + sat);
                DaoManager.getInstance().setIdentityDao();
                for (Identity identity : list) {
                    try {
                        IdentityKeystore identityKeystore = getObjectMapper().readValue(identity.getKeystore(), IdentityKeystore.class);

                        byte[] seeds = identityKeystore.decryptCiphertext(originPassWord);

                        byte[] mnemonic = identityKeystore.crypto.decryptEncPair(originPassWord, identityKeystore.getEncMnemonic());
                        //String mnemonicStr = new String(mnemonic);

                        identityKeystore.crypto = Crypto.createPBKDF2CryptoWithKDFCached(password, seeds);
                        identityKeystore.setEncMnemonic(identityKeystore.crypto.deriveEncPair(password, mnemonic));
                        identity.setKeystore(getObjectMapper().writeValueAsString(identityKeystore));
                        DaoManager.getInstance().insertIdentity(identity);
                        WalletInfo.getInstance().setIdentity(identity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                WalletInfo.getInstance().setSaltPassword(originPassWord);
                aliasKeyStoreV2 .onlyEncryption(password);
                callback.onEvent(true);
            } else {
                callback.onEvent(false);
            }
        } else {
            callback.onEvent(false);
        }
    }

}
