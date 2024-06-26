package com.topnetwork.core.crypto;
import android.util.Log;

import com.topnetwork.core.model.eth.Messages;
import com.topnetwork.core.model.eth.TokenException;
import com.topnetwork.core.utils.ByteUtil;
import com.topnetwork.core.utils.NumericUtil;

import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.jcajce.provider.digest.SHA3;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Hash {

    public static String keccak256(String hex) {
        byte[] bytes = NumericUtil.hexToBytes(hex);
        byte[] result = keccak256(bytes);
        return NumericUtil.bytesToHex(result);
    }

    public static byte[] keccak256(byte[] input) {
        return keccak256(input, 0, input.length);
    }

    public static byte[] generateMac(byte[] derivedKey, byte[] cipherText) {
        byte[] result = new byte[16 + cipherText.length];

        System.arraycopy(derivedKey, 16, result, 0, 16);
        System.arraycopy(cipherText, 0, result, 16, cipherText.length);
        return Hash.keccak256(result);
    }

    public static byte[] generateMac32(byte[] derivedKey, byte[] cipherText) {
        byte[] result = new byte[32 + cipherText.length];

        System.arraycopy(derivedKey, 0, result, 0, 32);
        System.arraycopy(cipherText, 0, result, 32, cipherText.length);

        Digest hash = new SHA3Digest(256);
        hash.update(result,0,result.length);
        byte[] hashResult = new byte[32];
        hash.doFinal(hashResult,0);
        return hashResult;
    }

    public static String sha256(String hexInput) {
        byte[] bytes = NumericUtil.hexToBytes(hexInput);
        byte[] result = sha256(bytes);
        return NumericUtil.bytesToHex(result);
    }

    public static byte[] sha256(byte[] input) {
        return sha256(input, 0, input.length);
    }

    private static byte[] keccak256(byte[] input, int offset, int length) {

        Keccak keccak = new Keccak(256);
        keccak.update(input, offset, length);

        return keccak.digest().array();
    }

    private static byte[] sha256(byte[] input, int offset, int length) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input, offset, length);
            return md.digest();
        } catch (Exception ex) {
            throw new TokenException(Messages.WALLET_SHA256);
        }
    }

    public static byte[] hmacSHA256(byte[] key, byte[] data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
            sha256_HMAC.init(secret_key);

            return sha256_HMAC.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
//      throw new TokenException(Messages.WALLET_SHA256);
            return null;
        }
    }

    public static byte[] merkleHash(byte[] oriData) {

        if (oriData == null || oriData.length == 0) {
            throw new IllegalArgumentException("data should not be null");
        }
        int chunkSize = 1024;
        List<byte[]> hashes = new ArrayList<>();
        for (int pos = 0; pos < oriData.length; pos += chunkSize) {
            int end = Math.min(pos + chunkSize, oriData.length);
            hashes.add(Sha256Hash.hashTwice(Arrays.copyOfRange(oriData, pos, end)));
        }

        int j = 0;
        for (int size = hashes.size(); size > 1; size = (size + 1) / 2) {
            for (int i = 0; i < size; i += 2) {
                int i2 = Math.min(i + 1, size - 1);
                hashes.add(Sha256Hash.hashTwice(ByteUtil.concat(hashes.get(j + i), hashes.get(j + i2))));
            }
            j += size;
        }

        return hashes.get(hashes.size() - 1);
    }
}
