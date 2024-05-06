package com.topnetwork.audit.keystore.compat_keystore.compat;

public interface IAliaskeyStore {

    /**
     * encipher
     * @param content Encrypted content
     */
    void Encryption(String content);


    /**
     * decrypt
     * @param cipherText Decrypt content
     * @return
     */
    String Decrypt(String cipherText);

    /**
     * Get ciphertext
     */
    String getCipherText();


    /**
     * Check whether the Pin code is correct
     */
    boolean verify(String password,String salt);

    /**
     * Decrypt sat
     * @param saltCipherText
     * @return
     */
    String decryptSalt(String saltCipherText);

    /**
     * Empty variable
     */
    void clear();
}
