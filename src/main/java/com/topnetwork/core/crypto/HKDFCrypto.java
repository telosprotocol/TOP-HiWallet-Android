package com.topnetwork.core.crypto;

import com.topnetwork.core.utils.NumericUtil;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;

import static java.nio.charset.StandardCharsets.US_ASCII;

class HKDFCrypto extends Crypto<HKDFParams> {
    static final String HKDF = "hkdf";

    HKDFCrypto() {
        super();
        this.kdf = HKDF;
    }

    public static HKDFCrypto createHKDFCrypto() {
        HKDFCrypto crypto = new HKDFCrypto();
        byte[] salt = NumericUtil.generateRandomBytes(SALT_LENGTH);
        HKDFParams hkdfParams = HKDFParams.createPBKDFParams();
        hkdfParams.setSalt(NumericUtil.bytesToHexWithPrefix(salt));

        byte[] info = NumericUtil.generateRandomBytes(8);
        hkdfParams.setInfo(NumericUtil.bytesToHexWithPrefix(info));
        crypto.kdfparams = hkdfParams;

        return crypto;
    }

    @Override
    public byte[] generateDerivedKey(String password) {
//        Digest hash = new SHA3Digest(512);
//        byte[] ikm = "1111111a".getBytes(US_ASCII);
//        byte[] salt = NumericUtil.hexToBytes("0xcc11d3b7605b1b487a9c07fb5426f897aa3e1575879496fe151ca24a83d5787c");
//        byte[] info = NumericUtil.hexToBytes("0xd77d82c8e439f2d9");
//        int l = 64;
//        byte[] okm = new byte[64];
//
//        HKDFParameters params = new HKDFParameters(ikm, salt, info);
//
//        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(hash);
//        hkdf.init(params);
//        hkdf.generateBytes(okm, 0, l);
//        Log.e("HKDFCrypto",NumericUtil.bytesToHex(okm));

        HKDFParams params = this.kdfparams;
        Digest hash = new SHA3Digest(512);
        byte[] ikm = password.getBytes(US_ASCII);
        byte[] salt = NumericUtil.hexToBytes(params.getSalt());
        byte[] info = NumericUtil.hexToBytes(params.getInfo());
        int l = params.getDklen();
        byte[] okm = new byte[ params.getDklen()];

        HKDFParameters hkdfParameters = new HKDFParameters(ikm, salt, info);

        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(hash);
        hkdf.init(hkdfParameters);
        hkdf.generateBytes(okm, 0, l);
       // Log.e("HKDFCrypto",NumericUtil.bytesToHexWithPrefix(okm));
        return  okm;
    }

}
