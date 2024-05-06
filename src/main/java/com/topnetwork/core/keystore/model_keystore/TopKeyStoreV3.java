package com.topnetwork.core.keystore.model_keystore;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.topnetwork.core.crypto.Crypto;
import com.topnetwork.core.model.Metadata;
import com.topnetwork.core.utils.NumericUtil;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.MainNetParams;
import org.topj.account.Account;

import java.nio.charset.Charset;

public class TopKeyStoreV3 extends Keystore {
    private String hint = "";
    private String address;
    public Crypto crypto;
    private String key_type = "owner";
    private String public_key;
    private String accountAddress;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }
    @JsonGetter("account address")
    public String getAccountAddress() {
        return accountAddress;
    }

    @JsonSetter("account address")
    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
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

    public static TopKeyStoreV3 create(Metadata metadata, String password, DeterministicKey deterministicKey, NetworkParameters networkParameters) {
        return new TopKeyStoreV3(metadata,password, deterministicKey,networkParameters);
    }

    public TopKeyStoreV3() { }
    private TopKeyStoreV3(Metadata metadata, String password, DeterministicKey deterministicKey, NetworkParameters networkParameters) {
        if (networkParameters == null) {
            networkParameters = MainNetParams.get();
        }
        String xprv = deterministicKey.serializePrivB58(networkParameters);
        byte[] origin= xprv.getBytes(Charset.forName("UTF-8"));
        this.version = 3;
        this.crypto = Crypto.createSCryptCrypto(password, origin);
        this.crypto.clearCachedDerivedKey();
        Account account = new Account(deterministicKey.getPrivateKeyAsHex());

        this.public_key = org.bouncycastle.util.encoders.Base64.toBase64String(NumericUtil.hexToBytes(account.getPublicKey()));
        this.accountAddress = account.getAddress();
        //T80000
        this.address = account.getAddress().replace("T80000","");

    }

    public byte[] decryptCiphertext(String password) {
        return getCrypto().decrypt(password);
    }
}
