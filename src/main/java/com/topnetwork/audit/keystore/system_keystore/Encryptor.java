package com.topnetwork.audit.keystore.system_keystore;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Encryptor {
    byte [] iv1 = new byte[]{
            -78,-56,50,65,-82,-70,91,-41,-117,-25,-60,-82
    };
    byte [] iv2 = new byte[]{
            -78,-56,50,65,-82,-70,91,-41,-117,-25,-60,-82
    };
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private byte[] encryption;
    private byte[] iv;

    private byte[]v1 = new byte[]{
            100,-98,107,-103,111,-82,0,-114,-50,33,83,-3
    };
    private byte[]v2 = new byte[]{
            -9,-58,58,43,-73,-57,75,57,-18,-69,-61,63,-33,1,116,51,49
    };

    public Encryptor() {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public byte[] encryptText(final String alias, final String textToEncrypt,ICipherIvCallBack iCipherIvCallBack)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, SignatureException, BadPaddingException,
            IllegalBlockSizeException {

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias));

        iv = cipher.getIV();
        if(iCipherIvCallBack != null){
            iCipherIvCallBack.setCipherIv(iv);
        }
        encryption = cipher.doFinal(textToEncrypt.getBytes("UTF-8"));
        return encryption;
    }

    private SecretKey getSecretKey1(final String alias) throws NoSuchAlgorithmException,
            UnrecoverableEntryException, KeyStoreException {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);

        try {
            keyStore.load(null);
            return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    private SecretKey getSecretKey(final String alias) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {
        if (isHaveKeyStore(alias)) {
            try {
                return getSecretKey1(alias);
            } catch (UnrecoverableEntryException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }
        final KeyGenerator keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

        keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build());

        return keyGenerator.generateKey();
    }

    public byte[] getEncryption() {
        return encryption;
    }

    public byte[] getIv() {
        return iv;
    }

    public static boolean isHaveKeyStore(String mAlias) {
        try {
            KeyStore ks = KeyStore.getInstance(ANDROID_KEY_STORE);
            ks.load(null);
            // Load the key pair from the Android Key Store
            KeyStore.Entry entry = ks.getEntry(mAlias, null);

            if (entry == null) {
                return false;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return false;
        } catch (CertificateException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public interface ICipherIvCallBack{
        void setCipherIv(byte[] iv);
    }
}
