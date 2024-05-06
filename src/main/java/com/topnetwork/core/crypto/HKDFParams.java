package com.topnetwork.core.crypto;

import com.google.common.base.Strings;
import com.topnetwork.core.model.eth.Messages;
import com.topnetwork.core.model.eth.TokenException;

public class HKDFParams implements KDFParams {
    static final String PRF = "sha3-256";
    private int dklen = 0;
    private String info = "";
    private String prf = "";
    private String salt;

    public HKDFParams() {
    }

    public static HKDFParams createPBKDFParams() {
        HKDFParams params = new HKDFParams();
        params.dklen = 64;
        params.prf = PRF;
        return params;
    }

    public String getPrf() {
        return prf;
    }

    @Override
    public int getDklen() {
        return dklen;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String getSalt() {
        return salt;
    }

    @Override
    public void validate() {
        if (dklen == 0 || Strings.isNullOrEmpty(salt) || Strings.isNullOrEmpty(prf)) {
            throw new TokenException(Messages.KDF_PARAMS_INVALID);
        }
    }
}
