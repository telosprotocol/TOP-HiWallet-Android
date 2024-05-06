package com.topnetwork.core.sign.top;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topnetwork.core.crypto.Crypto;
import com.topnetwork.core.helper.CoinEnum;
import com.topnetwork.core.keystore.model_keystore.DefaultKeystore;
import com.topnetwork.core.keystore.model_keystore.HDKeystore;
import com.topnetwork.core.keystore.model_keystore.TopKeyStore;
import com.topnetwork.core.keystore.model_keystore.TopKeyStoreV3;
import com.topnetwork.core.model.Wallet;
import com.topnetwork.core.model.WalletManager;
import com.topnetwork.core.utils.IdentityUtils;
import com.topnetwork.core.utils.NumericUtil;

import org.bitcoinj.crypto.DeterministicKey;
import org.topj.account.Account;
import org.topj.b_a_topj.BuildConfig;
import org.topj.methods.Model.RequestBody;
import org.topj.methods.Model.RequestModel;
import org.topj.methods.property.XTransactionType;

import org.topj.methods.response.XTransaction;
import org.topj.secp256K1Native.Secp256k1Helper;
import org.topj.utils.BufferUtils;
import org.topj.utils.StringUtils;
import org.topj.utils.TopUtils;
import org.topj.utils.TopjConfig;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static kotlin.text.Charsets.US_ASCII;

public class TopSign {

    /**
     * Signature transaction
     *
     * @param pri   Private key
     * @param basic Basic parameter
     * @param extra Additional parameter
     * @return
     */
    public static String signTransfer(String pri, BasicTransactionParams basic, ExtraTransactionParams extra) {
        try {
            RequestModel requestModel = getDefaultArgs(basic, extra);
            setRequestModelSign(pri, requestModel, basic, extra);

            return TopUtils.toJsonStr(requestModel);

        } catch (Exception e) {
        }
        return "";
    }

    /**
     * Pledge signature data
     *
     * @param pri
     * @param transferParams
     * @param extra
     * @return
     */
    public static String signStakeTransfer(String pri, BasicTransactionParams transferParams, ExtraTransactionParams extra) {
        try {

            RequestModel requestModel = getDefaultArgs(transferParams, extra);
            XTransaction xTransaction = requestModel.getRequestBody().getxTransaction();
            xTransaction.setTxType(XTransactionType.PledgeTokenTgas);
            xTransaction.setNote(transferParams.getNote());
            xTransaction.setTxDeposit(transferParams.getDeposit());
            BufferUtils bufferUtils = new BufferUtils();
            byte[] actionParamBytes = bufferUtils.stringToBytes(transferParams.getCoinType())
                    .BigIntToBytes(new BigInteger(transferParams.amount), 64).pack();
            String actionParamHex = "0x" + StringUtils.bytesToHex(actionParamBytes);
            xTransaction.setReceiverAccount(transferParams.getTo());
            xTransaction.setReceiverActionParam(actionParamHex);

            setTransactionSignResult(pri, xTransaction);
            return TopUtils.toJsonStr(requestModel);

        } catch (Exception e) {
        }
        return "";

    }

    /**
     * Call signature data
     *
     * @param pri
     * @param transferParams
     * @param extra
     * @return
     */
    public static String signUnstakeTransfer(String pri, BasicTransactionParams transferParams, ExtraTransactionParams extra) {
        try {

            RequestModel requestModel = getDefaultArgs(transferParams, extra);
            XTransaction xTransaction = requestModel.getRequestBody().getxTransaction();
            xTransaction.setTxType(XTransactionType.RedeemTokenTgas);
            xTransaction.setNote(transferParams.getNote());
            xTransaction.setTxDeposit(transferParams.getDeposit());
            BufferUtils bufferUtils = new BufferUtils();
            byte[] actionParamBytes = bufferUtils.stringToBytes(transferParams.getCoinType())
                    .BigIntToBytes(new BigInteger(transferParams.amount), 64).pack();
            String actionParamHex = "0x" + StringUtils.bytesToHex(actionParamBytes);
            xTransaction.setReceiverAccount(transferParams.getTo());
            xTransaction.setReceiverActionParam(actionParamHex);

            setTransactionSignResult(pri, xTransaction);
            return TopUtils.toJsonStr(requestModel);
        } catch (Exception e) {
        }
        return "";
    }

    private static RequestModel getDefaultArgs(BasicTransactionParams basic, ExtraTransactionParams extra) {
        try {
            String METHOD_NAME = basic.methodName;

            RequestModel requestModel = new RequestModel();
            RequestBody requestBody = new RequestBody();

            requestModel.setVersion(TopjConfig.getVersion());
            requestModel.setAccountAddress(basic.accountAddr);
            requestModel.setMethod(METHOD_NAME);
            requestModel.setSequenceId(basic.getSequenceId());
            requestModel.setToken(extra.identityToken);

            XTransaction xTransaction = new XTransaction();
            xTransaction.setLastTxNonce(extra.nonce);
            xTransaction.setSendTimestamp(BigInteger.valueOf(new Date().getTime()/1000));
            xTransaction.setTxExpireDuration(TopjConfig.getExpireDuration());
            xTransaction.setTxDeposit(TopjConfig.getDeposit());
            xTransaction.setTxStructureVersion(BigInteger.valueOf(2));

            xTransaction.setReceiverAccount(basic.accountAddr);
            xTransaction.setReceiverActionParam("0x");
            xTransaction.setSenderAccount(basic.accountAddr);
            xTransaction.setSenderActionParam("0x");

            requestBody.setxTransaction(xTransaction);
            requestModel.setRequestBody(requestBody);

            return requestModel;
        } catch (Exception e) {

        }
        return null;
    }

    public static void setRequestModelSign(String pri, RequestModel requestModel, BasicTransactionParams basic,
                                           ExtraTransactionParams extra) {
        try {

            XTransaction xTransaction = requestModel.getRequestBody().getxTransaction();
            xTransaction.setTxType(XTransactionType.Transfer);
            xTransaction.setNote(basic.note);
            xTransaction.setTxDeposit(basic.getDeposit());
            xTransaction.setAmount(new BigInteger(basic.amount));
            xTransaction.setReceiverAccount(basic.to);

            setTransactionSignResult(pri, xTransaction);
        } catch (Exception e) {
//            Log.e("TopSign", e.getMessage() + "");
        }
    }

    private static void setTransactionSignResult(String privateKey, XTransaction xTransaction) throws Exception {
        try {
            byte[] dataBytes = xTransaction.set_digest();

            BigInteger privKey = new BigInteger(privateKey, 16);
            String authHex = signData(dataBytes, privKey);
            xTransaction.setAuthorization(authHex);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


    /**
     * Sign bytes
     *
     * @param dataBytes
     * @param privKey
     * @return
     */
    public static String signData(byte[] dataBytes, BigInteger privKey) {
        try {
            return Secp256k1Helper.signData(dataBytes, privKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Underlying transaction parameters
    public static class BasicTransactionParams {
        private String accountAddr;//Account address
        private String to;  //Receiving address
        private String coinType = "";
        private String amount; //amount
        private String note = ""; //remark

        private String deposit;

        String methodName = "sendTransaction";

        public BasicTransactionParams(String accountAddr, String to, String coinType, String amount, String note) {
            this(accountAddr, to, coinType, amount, note, BuildConfig.deposit);
        }

        public BasicTransactionParams(String accountAddr, String to, String coinType, String amount, String note, String deposit) {
            this.accountAddr = accountAddr;
            this.to = to;
            this.coinType = coinType;
            this.amount = amount;
            this.note = note;
            this.deposit = deposit;
        }

        public String getAccountAddr() {
            return accountAddr;
        }

        public void setAccountAddr(String accountAddr) {
            this.accountAddr = accountAddr;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getCoinType() {
            return coinType;
        }

        public void setCoinType(String coinType) {
            this.coinType = coinType;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getSequenceId() {
            return String.valueOf(System.currentTimeMillis());
        }

        public BigInteger getDeposit() {
            return new BigInteger(deposit);
        }

        public void setDeposit(String deposit) {
            this.deposit = deposit;
        }
    }

    //Additional transaction parameters
    public static class ExtraTransactionParams {
        BigInteger ActionType;  //Action type
        String identityToken;  //Token
        String lastHashXxhash64;  //hash
        BigInteger nonce;  //nonce

        public ExtraTransactionParams(BigInteger actionType
                , String identityToken, String lastHashXxhash64,
                                      BigInteger nonce) {
            ActionType = actionType;
            this.identityToken = identityToken;
            this.lastHashXxhash64 = lastHashXxhash64;
            this.nonce = nonce;
        }

        public BigInteger getActionType() {
            return ActionType;
        }

        public void setActionType(BigInteger actionType) {
            ActionType = actionType;
        }


        public String getIdentityToken() {
            return identityToken;
        }

        public void setIdentityToken(String identityToken) {
            this.identityToken = identityToken;
        }

        public String getLastHashXxhash64() {
            return lastHashXxhash64;
        }

        public void setLastHashXxhash64(String lastHashXxhash64) {
            this.lastHashXxhash64 = lastHashXxhash64;
        }

        public BigInteger getNonce() {
            return nonce;
        }

        public void setNonce(BigInteger nonce) {
            this.nonce = nonce;
        }
    }

    /**
     * Sign the string
     *
     */
    public static String signMessage(Wallet wallet,String hexStr){
        byte[] bytes = NumericUtil.hexToBytes(hexStr);
        String priKey = getTopPriKey(wallet);
        BigInteger bigInteger = new BigInteger(priKey,16);
        return signData(bytes, bigInteger);
    }

    /**
     * Obtain the top private key
     */
    public static String getTopPriKey(Wallet wallet){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DefaultKeystore hdKeystore = null;
        try {
            hdKeystore = objectMapper.readValue(wallet.keystore, DefaultKeystore.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        byte[] bytes = hdKeystore.decryptCiphertext(IdentityUtils.getIdentityPin());
        String xprv = new String(bytes, Charset.forName("UTF-8"));
        DeterministicKey xprvKey = DeterministicKey.deserializeB58(xprv, CoinEnum.TOP.getNetworkParameters());
        return xprvKey.getPrivateKeyAsHex();
    }

    /**
     * Get the keystore of top
     */
    public static String getTopKeyStore(Wallet wallet,String password) {
       int version = getTopKeyStoreVersion(wallet);
       if(version == 1){
           return getTopKeyStoreV1(wallet,password);
       }if(version == 3){
           return getTopKeyStoreV3(wallet,password);
       }else {
           return "";
       }
    }


    /**
     * Obtain the topv1 keystore
     */
    public static String getTopKeyStoreV1(Wallet wallet,String password) {
        TopKeyStore topKeyStore = new TopKeyStore();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        HDKeystore hdKeystore;
        try {
            hdKeystore = objectMapper.readValue(wallet.keystore, HDKeystore.class);
            byte[] bytes = hdKeystore.decryptCiphertext(IdentityUtils.getIdentityPin());
            String xprv = new String(bytes, Charset.forName("UTF-8"));
            DeterministicKey xprvKey = DeterministicKey.deserializeB58(xprv, CoinEnum.TOP.getNetworkParameters());

            byte[] origin = xprvKey.getPrivKeyBytes();
            Crypto crypto = Crypto.createHKDFCryptoWithKDFCached(password, org.bouncycastle.util.encoders.Base64.toBase64String(origin).getBytes(US_ASCII));
            topKeyStore.setCrypto(crypto);
            topKeyStore.setHint("");
            topKeyStore.setKey_type("owner");
            topKeyStore.setAddress(wallet.address);
            Account account = new Account(xprvKey.getPrivateKeyAsHex());
            topKeyStore.setPublic_key(org.bouncycastle.util.encoders.Base64.toBase64String(NumericUtil.hexToBytes(account.getPublicKey())));
            topKeyStore.setCrypto(crypto);
            return WalletManager.getObjectMapper().writeValueAsString(topKeyStore);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Obtain the topv3 keystore
     */
    public static String getTopKeyStoreV3(Wallet wallet,String password) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            TopKeyStoreV3 keyStoreV3 = objectMapper.readValue(wallet.keystore, TopKeyStoreV3.class);
            byte[] bytes = keyStoreV3.decryptCiphertext(IdentityUtils.getIdentityPin());
            String xprv = new String(bytes, Charset.forName("UTF-8"));
            DeterministicKey xprvKey = DeterministicKey.deserializeB58(xprv, CoinEnum.TOP.getNetworkParameters());

            byte[] origin = xprvKey.getPrivKeyBytes();
            Crypto crypto = Crypto.createSCryptCrypto(password, origin);
            crypto.clearCachedDerivedKey();
            keyStoreV3.setCrypto(crypto);

            return objectMapper.writeValueAsString(keyStoreV3);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the version of top and distinguish the version of top. It is currently Version3
     *
     */
    public static int getTopKeyStoreVersion(Wallet wallet) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            DefaultKeystore keystore = objectMapper.readValue(wallet.keystore, DefaultKeystore.class);
            return keystore.version;
        } catch (Exception e) {
            return 0;
        }
    }

}
