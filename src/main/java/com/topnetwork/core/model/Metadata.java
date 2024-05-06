package com.topnetwork.core.model;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Metadata {
    public static final String P2WPKH = "P2WPKH";
    public static final String NONE = "NONE";
    public static final String NORMAL = "NORMAL";
    public static final String FROM_MNEMONIC = "MNEMONIC";
    public static final String FROM_WIF = "WIF";
    public static final String FROM_NEW_IDENTITY = "NEW_IDENTITY";
    public static final String FROM_RECOVERED_IDENTITY = "RECOVERED_IDENTITY";

    private String source = FROM_MNEMONIC;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @JsonIgnore
    public Boolean isMainNet() {
        return true;
    }
}
