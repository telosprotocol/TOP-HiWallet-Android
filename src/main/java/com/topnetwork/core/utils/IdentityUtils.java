package com.topnetwork.core.utils;

import com.topnetwork.audit.keystore.compat_keystore.CompatAliaskeyStoreHelper;
import com.topnetwork.audit.keystore.compat_keystore.compat.AliasKeyStoreV2;
import com.topnetwork.audit.keystore.compat_keystore.compat.IAliaskeyStore;

public class IdentityUtils {

    /**
     * Get IdentityPin
     * Get the real pin code for decryption
     * @return
     */
    public static String getIdentityPin() {
        String iv = CompatAliaskeyStoreHelper.getInstance().getIv();
        IAliaskeyStore aliaskeyStore = CompatAliaskeyStoreHelper.getInstance().getAliaskeyStoreByIv(iv);
        String cipherText = aliaskeyStore.getCipherText();
        return aliaskeyStore.Decrypt(cipherText);
    }

    //Obtain the password to unlock the database
    public static String getIdentityDaoPassword() {
        String v1 = CompatAliaskeyStoreHelper.getInstance().getIv();
        IAliaskeyStore aliaskeyStore = CompatAliaskeyStoreHelper.getInstance().getAliaskeyStoreByIv(v1);
        String cipherText = aliaskeyStore.getCipherText();
        String identityDaoPassword = aliaskeyStore.Decrypt(cipherText);
        String sat = "";
        if (aliaskeyStore instanceof AliasKeyStoreV2) {
            if (StringUtils.isEmpty(((AliasKeyStoreV2) aliaskeyStore).getSaltCipherText())) {
                sat = "";
            } else {
                sat = aliaskeyStore.decryptSalt(((AliasKeyStoreV2) aliaskeyStore).getSaltCipherText());
            }
        }
        return identityDaoPassword + sat;
    }
}
