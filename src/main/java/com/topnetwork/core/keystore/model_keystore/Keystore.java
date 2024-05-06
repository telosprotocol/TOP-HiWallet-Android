package com.topnetwork.core.keystore.model_keystore;

import com.topnetwork.core.crypto.Crypto;

import java.util.UUID;

public abstract class Keystore {
    public String id;
    public int version = 1;

    public Crypto crypto;

    public Keystore() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public boolean verifyPassword(String password) {
        return getCrypto().verifyPassword(password);
    }

    public byte[] decryptCiphertext(String password) {
        return getCrypto().decrypt(password);
    }
}
