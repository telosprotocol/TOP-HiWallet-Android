package com.topnetwork.core.identity;

import com.topnetwork.core.crypto.EncPair;
import com.topnetwork.core.keystore.model_keystore.Keystore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//The identity of the store Keystore class
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentityKeystore extends Keystore {
    private String TAG = "IdentityKeystore";
    private EncPair encMnemonic; //Mnemonic word
    private String ipfsId;
    private String encKey;

    public EncPair getEncMnemonic() {
        return encMnemonic;
    }

    public void setEncMnemonic(EncPair encMnemonic) {
        this.encMnemonic = encMnemonic;
    }

    public String getIpfsId() {
        return ipfsId;
    }

    public void setIpfsId(String ipfsId) {
        this.ipfsId = ipfsId;
    }

    public String getEncKey() {
        return encKey;
    }

    public void setEncKey(String encKey) {
        this.encKey = encKey;
    }

    public IdentityKeystore() {
        super();
    }

    @Override
    public String toString() {
        return "IdentityKeystore{" +
                "encMnemonic=" + encMnemonic +
                ", ipfsId='" + ipfsId + '\'' +
                ", encKey='" + encKey + '\'' +
                ", id='" + id + '\'' +
                ", version=" + version +
                ", crypto=" + crypto +
                '}';
    }
}
