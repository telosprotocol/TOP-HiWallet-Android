package com.topnetwork.core.model;

import com.topnetwork.core.keystore.model_keystore.HDKeystore;
import com.topnetwork.core.keystore.model_keystore.TopKeyStore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class WalletManager {
    public static byte[] getBtcPriKey(String keystoreJson, String password) {
        try {
            HDKeystore hdKeystore = getObjectMapper().readValue(keystoreJson, HDKeystore.class);
            return hdKeystore.decryptCiphertext(password);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    public static byte[] getTopPrikey(String keystoreJson, String password) {
        try {
            TopKeyStore topKeyStore = getObjectMapper().readValue(keystoreJson, TopKeyStore.class);
            return topKeyStore.decryptCiphertext(password);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
