package com.topnetwork.audit.keystore.compat_keystore.compat;

import android.util.Base64;

import com.topnetwork.audit.crypto.PinCrypto;
import com.topnetwork.core.identity.WalletInfo;
import com.topnetwork.audit.keystore.compat_keystore.CompatAliaskeyStoreHelper;
import com.topnetwork.audit.keystore.system_keystore.Decryptor;
import com.topnetwork.audit.keystore.system_keystore.Encryptor;
import com.topnetwork.core.utils.ByteUtil;
import com.topnetwork.core.utils.StringUtils;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * New version
 */
public class AliasKeyStoreV2 implements IAliaskeyStore {
    private String alias = "hi_wallet";

    private String salt = ""; //Random salt for database encryption concatenation

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * Crypt salt
     *
     * @param salt
     */
    public void encryptionSalt(String salt) {
        handleEncryption(false, salt);

    }

    /**
     * Deciphering salt
     *
     * @param cipherText
     * @return
     */
    @Override
    public String decryptSalt(String cipherText) {
        return handleDecrypt(false, cipherText);

    }

    @Override
    public void clear() {
        salt = "";
    }

    /**
     * Get random salt (128-bit random entropy)
     */
    public String getSaltRandom() {
        salt = ByteUtil.getRandomEntropyHex();
        return salt;
    }

    private void handleEncryption(boolean isPassword, String content) {
        Encryptor encryptor = new Encryptor();
        try {
            byte[] data = encryptor.encryptText(alias, content, iv -> {
                String ivStr = Base64.encodeToString(iv, Base64.DEFAULT);
                //cipher Decryption will be used
                if (isPassword) {
                    WalletInfo.getInstance().setIv(ivStr, true);
                } else {
                    WalletInfo.getInstance().setSaltIv(ivStr, true);
                }

            });
            //The encrypted string
            String cipherText = Base64.encodeToString(data, Base64.DEFAULT);
            if (isPassword) {
                WalletInfo.getInstance().setCipherText(cipherText, true);
            } else {
                WalletInfo.getInstance().setSaltCipherText(cipherText, true);
            }

        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void onlyEncryption(String content) {
        handleEncryption(true, content);
    }

    @Override
    public void Encryption(String content) {
        handleEncryption(true, content);
        encryptionSalt(getSaltRandom());
    }


    /**
     * 解密
     *
     * @param cipherText
     */
    @Override
    public String Decrypt(String cipherText) {
        return handleDecrypt(true, cipherText);
    }

    private String handleDecrypt(boolean isIv, String cipherText) {
        //need to obtain the ciphertext and iv from sp to decrypt the data
        try {
            String iv;
            if (isIv) {
                iv = WalletInfo.getInstance().getIv();
            } else {
                iv = WalletInfo.getInstance().getSatIv();
            }
            Decryptor decryptor = new Decryptor();
            //cipherText byte
            byte[] bytesCipherText = Base64.decode(cipherText, Base64.DEFAULT);

            byte[] bytesCipherIv = Base64.decode(iv, Base64.DEFAULT);

            return decryptor.decryptData(alias, bytesCipherText, bytesCipherIv);

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getCipherText() {
        return CompatAliaskeyStoreHelper.getInstance().getCipherText();
    }

    /**
     * Get 128 random salt ciphertext
     *
     * @return
     */
    public String getSaltCipherText() {
        return CompatAliaskeyStoreHelper.getInstance().getSaltCipherText();
    }

    // This logic has been changed again and again, so there will be some complex judgments on this side.
    // The latest set of pin verification has been changed to 128-bit random entropy
    // , and this field is no longer required for new users. Therefore, the new user is empty.
   //Pin check is correct (Hash twice)
    @Override
    public boolean verify(String password, String salt) {
        //Determine if the salt is empty
        boolean isEmpty = StringUtils.isEmpty(salt);
        //
        if (isEmpty) {
            AliasKeyStoreV2 aliaskeyStore = new AliasKeyStoreV2();
            salt = aliaskeyStore.decryptSalt(aliaskeyStore.getSaltCipherText());
        }
        return PinCrypto.checkPin(password, salt, WalletInfo.getInstance().getSaltPassword(), !isEmpty);
    }

}
