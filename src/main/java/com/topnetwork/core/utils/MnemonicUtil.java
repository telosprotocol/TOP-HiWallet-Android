package com.topnetwork.core.utils;

import org.bitcoinj.crypto.MnemonicCode;
import java.util.List;

//tools for mnemonic generation
public class MnemonicUtil {

    public static boolean validateMnemonics(List<String> mnemonicCodes) {
        try {
            MnemonicCode.INSTANCE.check(mnemonicCodes);
        } catch (org.bitcoinj.crypto.MnemonicException.MnemonicLengthException e) {
            return true;
        } catch (org.bitcoinj.crypto.MnemonicException.MnemonicWordException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public static List<String> randomMnemonicCodes() {
        return toMnemonicCodes(NumericUtil.generateRandomBytes(16));
    }

    private static List<String> toMnemonicCodes(byte[] entropy) {
        try {
            return MnemonicCode.INSTANCE.toMnemonic(entropy);
        } catch (org.bitcoinj.crypto.MnemonicException.MnemonicLengthException e) {
//            throw new TokenException(Messages.MNEMONIC_INVALID_LENGTH);
            return null;
        } catch (Exception e) {
//            throw new TokenException(Messages.MNEMONIC_CHECKSUM);
            return null;
        }
    }

}
