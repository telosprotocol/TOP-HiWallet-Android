package com.topnetwork.core.keystore.model_keystore;

import com.topnetwork.core.crypto.Crypto;
import com.google.common.base.Strings;
import com.topnetwork.core.model.Metadata;

import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.MainNetParams;

import java.nio.charset.Charset;
import java.util.UUID;

public class HDKeystore extends Keystore {

    private String address;
    private Metadata metadata;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public static HDKeystore create(Metadata metadata, String password, DeterministicKey parent) {
        return new HDKeystore(metadata, password, parent, null,true);
    }

    public static HDKeystore create(Metadata metadata, String password, DeterministicKey parent, NetworkParameters networkParameters) {
        return new HDKeystore(metadata, password, parent, networkParameters,true);
    }

    public static HDKeystore createNotInitializeAddress(Metadata metadata, String password, DeterministicKey parent, NetworkParameters networkParameters) {
        return new HDKeystore(metadata, password, parent, networkParameters,false);
    }

    public HDKeystore() {
        super();
    }

    private HDKeystore(Metadata metadata, String password, DeterministicKey parent, NetworkParameters networkParameters,boolean isHaveAddress) {
        if (networkParameters == null) {
            networkParameters = MainNetParams.get();
        }
        String xprv = parent.serializePrivB58(networkParameters);
//        Log.d("HDKeystore",parent.getPublicKeyAsHex());
        if(isHaveAddress){
            byte[] sha256Bytes = Utils.sha256hash160(parent.getPubKey());
            LegacyAddress address = new LegacyAddress(networkParameters,false, sha256Bytes);
            this.address = address.toBase58();
        }
        else {
            this.address = "";
        }
        //LogUtils.eTag("HDKeystore", address.toBase58());
        this.crypto = Crypto.createPBKDF2CryptoWithKDFCached(password, xprv.getBytes(Charset.forName("UTF-8")));
        this.metadata = metadata;
        this.crypto.clearCachedDerivedKey();
        this.id = Strings.isNullOrEmpty(id) ? UUID.randomUUID().toString() : id;
    }


}
