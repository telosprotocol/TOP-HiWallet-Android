package com.topnetwork.core.keystore.model_keystore;

import android.util.Base64;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.topnetwork.core.crypto.Crypto;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

import java.nio.charset.Charset;

public class TopKeyStore {

    public Crypto crypto;
    private String address;
    private String public_key;
    private String key_type;
    private String hint;

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }
    @JsonGetter("account address")
    public String getAddress() {
        return address;
    }

    @JsonSetter("account address")
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getKey_type() {
        return key_type;
    }

    public void setKey_type(String key_type) {
        this.key_type = key_type;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public static TopKeyStore create(String password, DeterministicKey deterministicKey) {
        return new TopKeyStore(password, deterministicKey);
    }

    public TopKeyStore() {
    }

    private TopKeyStore(String password, DeterministicKey deterministicKey) {
        NetworkParameters networkParameters = MainNetParams.get();
        String xprv = deterministicKey.serializePrivB58(networkParameters);
        byte[] origin= android.util.Base64.encode(xprv.getBytes(Charset.forName("UTF-8")), Base64.DEFAULT);
        this.crypto = Crypto.createHKDFCryptoWithKDFCached(password, origin);
        this.crypto.clearCachedDerivedKey();
    }

    public byte[] decryptCiphertext(String password) {
        return getCrypto().decrypt(password);
    }
}
