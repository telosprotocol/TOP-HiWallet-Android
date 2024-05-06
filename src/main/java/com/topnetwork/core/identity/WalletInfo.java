package com.topnetwork.core.identity;

import com.topnetwork.core.database.DaoManager;
import com.topnetwork.core.utils.StringUtils;

import java.util.List;
public class WalletInfo {

    private static String TAG = "WalletInfo";

    private static WalletInfo instance;

    private Identity identity;
    private RootIdentity rootIdentity;

    private String identityName; //Identity name
    private boolean isLogin; //Login or not
    private String totalAssets = "0"; //Total assets
    private boolean isSetTouchId; //Whether to set TouchId
    private boolean isTouchPay; // Fingerprint payment
    private String pin = ""; //Pin Password pin is an ipfs encrypted password
    private String saltPassword; // A code with salt
    private String saltRandom; //The salt value of the Pin
    private String securityPassword;
    private boolean isBackupMnemonic; //Whether to prepare mnemonic words

    private boolean isImportIdentity; //Import identity or not
    private List<String> importMnemonicArray; //Imported mnemonics

    private String currencyUnit; //Monetary unit
    private String currentIdentityDataBaseName; //The name of the currently displayed identity database

    private String iv;
    private String cipherText;

    private String saltIv;
    private String saltCipherText;

    private WalletInfo() {
    }

    /**
     * Delete wallet data
     */
    public static void clearData() {
        instance = null;
    }

    /**
     * Initializes the wallet information identity
     */
    public void initParams() {
        List<RootIdentity> rootIdentities = DaoManager.getInstance().queryRootIdentityData();
        //LogUtils.json(TAG, rootIdentities);
        if (rootIdentities.size() == 0) {
            System.exit(0);
            return;
        }
        rootIdentity = rootIdentities.get(0);
        if (rootIdentity == null) {
//            NormalBaseConfig.setLogin(false);
            return;
        }
        //LogUtils.json(TAG, rootIdentity);
        setCurrentIdentityDataBaseName(rootIdentity.getSeedId());
        setIdentityName(rootIdentity.getName());
        setIsSetTouchId(rootIdentity.getIsSetTouchId(), false);
        setTouchPay(rootIdentity.getIsTouchPay(), false);
        setSaltPassword(rootIdentity.getPinVerifyStr());
        setBackupMnemonic(rootIdentity.getIsBackUpMnemonic());
        setIv(rootIdentity.getIv(), false);
        setSaltIv(rootIdentity.getSaltIv(), false);
        setCipherText(rootIdentity.getCipherText(), false);
        setSaltCipherText(rootIdentity.getSaltCipherText(), false);
        //LogUtils.dTag(TAG, "isBackupMnemonic >>>" + rootIdentity.getIsBackUpMnemonic());
        setTotalAssets(rootIdentity.getTotalAssets());
    }

    public static WalletInfo getInstance() {
        if (instance == null) {
            synchronized (WalletInfo.class) {
                if (instance == null) {
                    instance = new WalletInfo();
                }
            }
        }
        return instance;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public RootIdentity getRootIdentity() {
        return rootIdentity;
    }

    public void setRootIdentity(RootIdentity rootIdentity) {
        this.rootIdentity = rootIdentity;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(String totalAssets) {
        this.totalAssets = totalAssets;
    }

    public boolean isSetTouchId() {
        return isSetTouchId;
    }

    public void setIsSetTouchId(boolean isTouchId, boolean updateDb) {
        isSetTouchId = isTouchId;
        if (updateDb) {
            rootIdentity.setSetTouchId(isTouchId);
            DaoManager.getInstance().upDataRootIdentity(rootIdentity);
        }
    }

    public boolean isTouchPay() {
        return isTouchPay;
    }

    public void setTouchPay(boolean touchPay, boolean updateDb) {
        isTouchPay = touchPay;
        if (updateDb) {
            rootIdentity.setTouchPay(touchPay);
            DaoManager.getInstance().upDataRootIdentity(rootIdentity);
        }
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    //Setting Iv
    public void setIv(String iv, boolean isUpData) {
        if (rootIdentity != null && isUpData) {
            rootIdentity.setIv(iv);
            //LogUtils.eTag(TAG, "setIv - >" + iv);
            DaoManager.getInstance().upDataRootIdentity(rootIdentity);
        }
        this.iv = iv;
    }

    //Get Iv
    public String getIv() {
        //LogUtils.eTag(TAG, "getIv() - >" + iv);
        if (StringUtils.isEmpty(iv)) {
            if (rootIdentity != null) {
                iv = rootIdentity.getIv();
            }
        }
        return iv;
    }

    //Set ciphertext
    public void setCipherText(String cipherText, boolean isUpData) {
        if (rootIdentity != null && isUpData) {
            rootIdentity.setCipherText(cipherText);
            DaoManager.getInstance().upDataRootIdentity(rootIdentity);
        }
        this.cipherText = cipherText;
    }

    //Get ciphertext
    public String getCipherText() {
        if (StringUtils.isEmpty(cipherText)) {
            if (rootIdentity != null) {
                cipherText = rootIdentity.getCipherText();
            }
        }
        return cipherText;
    }


    //Set the iv of salt
    public void setSaltIv(String saltIv, boolean isUpData) {
        if (rootIdentity != null && isUpData) {
            rootIdentity.setSaltIv(saltIv);
            DaoManager.getInstance().upDataRootIdentity(rootIdentity);
        }
        this.saltIv = saltIv;
    }

    //I get iv of salt
    public String getSatIv() {
        if (StringUtils.isEmpty(saltIv)) {
            if (rootIdentity != null) {
                saltIv = rootIdentity.getSaltIv();
            }
        }
        return saltIv;
    }

    //Set the salt ciphertext
    public void setSaltCipherText(String saltCipherText, boolean isUpData) {
        if (rootIdentity != null && isUpData) {
            rootIdentity.setSaltCipherText(saltCipherText);
            DaoManager.getInstance().upDataRootIdentity(rootIdentity);
        }
        this.saltCipherText = saltCipherText;
    }

    //Get the salt ciphertext
    public String getSaltCipherText() {
        if (StringUtils.isEmpty(saltCipherText)) {
            if (rootIdentity != null) {
                saltCipherText = rootIdentity.getSaltCipherText();
            }
        }
        return saltCipherText;
    }


    public String getSaltRandom() {
        return saltRandom;
    }

    public void setSaltRandom(String saltRandom) {
        this.saltRandom = saltRandom;
    }

    public String getSecurityPassword() {
        return securityPassword;
    }

    public void setSecurityPassword(String securityPassword) {
        this.securityPassword = securityPassword;
    }

    public boolean isBackupMnemonic() {
        return isBackupMnemonic;
    }

    public void setBackupMnemonic(boolean backupMnemonic) {
        isBackupMnemonic = backupMnemonic;
    }

    public boolean isImportIdentity() {
        return isImportIdentity;
    }

    public void setImportIdentity(boolean importIdentity) {
        isImportIdentity = importIdentity;
    }

    public List<String> getImportMnemonicArray() {
        return importMnemonicArray;
    }

    public void setImportMnemonicArray(List<String> importMnemonicArray) {
        this.importMnemonicArray = importMnemonicArray;
    }


    public String getCurrencyUnit() {
        return currencyUnit;
    }

    public void setCurrencyUnit(String currencyUnit) {
        this.currencyUnit = currencyUnit;

    }

    public String getCurrentIdentityDataBaseName() {
        return currentIdentityDataBaseName;
    }

    public void setCurrentIdentityDataBaseName(String currentIdentityDataBaseName) {
        this.currentIdentityDataBaseName = currentIdentityDataBaseName;
    }

    public String getSaltPassword() {
        return saltPassword;
    }

    public void setSaltPassword(String saltPassword) {
        if (!StringUtils.isEmpty(saltPassword) && rootIdentity != null) {
            rootIdentity.setPinVerifyStr(saltPassword);
            DaoManager.getInstance().upDataRootIdentity(rootIdentity);
        }
        this.saltPassword = saltPassword;
    }

}
