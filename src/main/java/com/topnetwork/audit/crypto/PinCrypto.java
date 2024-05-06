package com.topnetwork.audit.crypto;

import com.topnetwork.core.utils.NumericUtil;

import org.bitcoinj.core.Sha256Hash;

import java.nio.charset.StandardCharsets;

/**
 * Encrypt the pin with salt
 */
public class PinCrypto {

    private static String TAG = "PinCrypto";

    /**
     * Password and salting parameters
     *
     * @param pin
     * @param randNum
     * @return
     */
    public static String getPinCrypto(String pin, String randNum, boolean isOnceHash) {
        //Generated salt value
        String saltingStr = pin + randNum;  // Original password + random salt
        byte[] saltingByte = NumericUtil.hexToBytes(saltingStr);
        byte[] bytes = Sha256Hash.hash(saltingByte);
        String hashStr = NumericUtil.bytesToHex1(bytes);
        if (isOnceHash) {
            return hashStr;
        }
        byte[] hashStrByte = NumericUtil.hexToBytes(hashStr);
        byte[] hashStrBytes = Sha256Hash.hash(hashStrByte);
        String finalHashStr = NumericUtil.bytesToHex1(hashStrBytes);
        return finalHashStr;
    }

    /**
     * Check password correctness (pin password plus salt data store hash)
     *
     * @param pin
     * @param randNum
     * @param hash
     * @return
     */
    public static boolean checkPin(String pin, String randNum, String hash, boolean isOnceHash) {
        String hashStr = getPinCrypto(pin, randNum, isOnceHash);
        if (hashStr.equals(hash)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get password Utf-8 Hash value (Identity database password)
     * Hash once
     */
    public static String getDatabasePassword(String password) {
        byte[] bytes = Sha256Hash.hash(password.getBytes(StandardCharsets.UTF_8));
        return NumericUtil.bytesToHex1(bytes);
    }

    /**
     * Gets the string for Pin code verification
     * Hash twice
     */
    public static String getVerifyPin(String salt, String password) {
        return PinCrypto.getPinCrypto(password, salt, false);
    }
}
