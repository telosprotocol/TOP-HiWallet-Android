package com.topnetwork.core.utils;

import org.bitcoinj.core.Sha256Hash;

import java.util.Arrays;

public class ByteUtil {
    private static byte[] trimLeadingBytes(byte[] bytes, byte b) {
        int offset = 0;
        for (; offset < bytes.length - 1; offset++) {
            if (bytes[offset] != b) {
                break;
            }
        }
        return Arrays.copyOfRange(bytes, offset, bytes.length);
    }

    public static byte[] trimLeadingZeroes(byte[] bytes) {
        return trimLeadingBytes(bytes, (byte) 0);
    }

    public static byte[] concat(byte[] b1, byte[] b2) {
        byte[] result = Arrays.copyOf(b1, b1.length + b2.length);
        System.arraycopy(b2, 0, result, b1.length, b2.length);
        return result;
    }

    public static byte[] toBytes(boolean[] arrs) {
        int f = 0;
        byte b = 0;
        byte[] ret = new byte[arrs.length / 7 + (arrs.length % 7 == 0 ? 0 : 1)];
        for (int i = 0; i < arrs.length; i += 8) {
            for (int j = 0; j < 8; j++) {
                if (i + j < arrs.length && arrs[i + j]) {
                    b += (1 << (7 - j));
                }
                if (j == 7) {
                    ret[f++] = b;
                    b = 0;
                }
            }
        }
        return ret;
    }

    public static boolean[] toBoolean(byte[] bs, int length) {
        boolean[] arrs = new boolean[8 * bs.length];
        for (int i = 0; i < bs.length; i++) {
            int indx = i * 8;
            byte b = bs[i];
            for (int j = 7; j >= 0; j--) {
                arrs[indx + j] = (b & 1) == 1;
                b = (byte) (b >> 1);
            }
        }
        return Arrays.copyOfRange(arrs, 0, length);
    }

    //Generates a Boolean value of 128 random entropy
    public static String getRandomEntropyHex() {
        byte[] entropy = NumericUtil.generateRandomBytes(16);
        byte[] hash = Sha256Hash.hash(entropy);
        boolean[] hashBits = bytesToBits(hash);

        boolean[] entropyBits = bytesToBits(entropy);
        return NumericUtil.bytesToHex(toBytes(entropyBits));
    }

    private static boolean[] bytesToBits(byte[] data) {
        boolean[] bits = new boolean[data.length * 8];
        for (int i = 0; i < data.length; ++i)
            for (int j = 0; j < 8; ++j)
                bits[(i * 8) + j] = (data[i] & (1 << (7 - j))) != 0;
        return bits;
    }

}
