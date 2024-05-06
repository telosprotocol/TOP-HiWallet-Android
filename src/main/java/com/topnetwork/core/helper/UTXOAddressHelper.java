package com.topnetwork.core.helper;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;


public class UTXOAddressHelper {
    public static String getAddress(DeterministicKey deterministicKey, int version) {
        byte[] sha256Bytes = Utils.sha256hash160(deterministicKey.getPubKey());
        return Base58.encodeChecked(version, sha256Bytes);
    }

}
