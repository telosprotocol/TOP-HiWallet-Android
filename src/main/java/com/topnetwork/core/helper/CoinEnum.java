package com.topnetwork.core.helper;

import android.util.Log;

import com.topnetwork.core.BuildConfig;
import com.topnetwork.core.netparams.NetParams;

import org.bitcoinj.params.AbstractBitcoinNetParams;

public enum CoinEnum {

    BTC("BTC", "Bitcoin"),
    ETH("ETH", "Ethereum"),
    BCH("BCH", "Bitcoin Cash"),
    DASH("DASH", "Dash"),
    LTC("LTC", "LiteCoin"),
    DOGE("DOGE", "Dogecoin"),
    EOS("EOS", "EOS"),
    FIL("FIL", "Filecoin"),
    FIL_EVM("FIL_EVM", "Filecoin EVM"),
    TOP("TOP", "TOP Network");

    private String symbolName;
    private String fullName;
    private int version = 1;

    CoinEnum(String symbolName, String fullName) {
        this.symbolName = symbolName;
        this.fullName = fullName;
    }

    public static CoinEnum getTopCoinEnum(int version) {
        CoinEnum coinEnum = CoinEnum.TOP;
        coinEnum.setVersion(version);
        return coinEnum;
    }

    /**
     * @param symbolName
     * @return
     */
    public static CoinEnum getCoinEnum(String symbolName) {
        switch (symbolName) {
            case "ETH":
                return ETH;
            case "BTC":
                return BTC;
//            case "TOP":
//                return TOP;
            case "BCH":
                return BCH;
            case "LTC":
                return LTC;
            case "DASH":
                return DASH;
//            case "DOGE":
//                return DOGE;
            case "EOS":
                return EOS;
            case "FIL":
                return FIL;
            case "FIL_EVM":
                return FIL_EVM;
            case "TOP":
                return TOP;
            default:
                return null;
        }
    }

    public String getAssetId() {
        return (symbolName + symbolName).toLowerCase();
    }

    public String getSymbolName() {
        return symbolName;
    }

    public void setSymbolName(String symbolName) {
        this.symbolName = symbolName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Gets the path to create the coin
     *
     * @param index
     * @return
     */
    public String getPath(int index) {
        return "m/44'/" + getCoinType() + "'/" + index + "'/0/0";

    }

    /**
     * Gets the version number of the private key
     *
     * @return version number
     */
    public int getPrivateKeyVersion() {
        switch (this) {
            case LTC:
                return 0x019D9CFE;
            case BCH:
            case BTC:
            case EOS:
            case FIL:
            case FIL_EVM:
            case TOP:
                return 0x0488ADE4;
            case DASH:
                return 0x02FE52CC;
            case DOGE:
                return 0x0488E1F4;
            default:
                return -1;
        }
    }

    /**
     * the version to encode
     *
     * @return
     */
    public int getPublicKeyHash() {
        switch (this) {
            case LTC:
                return 0x30;
            case BCH:
            case BTC:
            case EOS:
            case FIL:
            case FIL_EVM:
            case TOP:
                return 0x00;
            case DASH:
                return 0x4C;
            case DOGE:
                return 0x1E;
            default:
                return -1;
        }
    }

    /**
     * Gets the prefix of the private key
     *
     * @return
     */
    public String getWifAddressPrefix() {
        switch (this) {
            case LTC:
                return "0xB0";
            case BCH:
            case BTC:
            case EOS:
                return "0x80";
            case DASH:
                return "0xCC";
            case DOGE:
                return "0x9E";
            default:
                return "";
        }
    }

    /**
     * Gets the currency type
     *
     * @return
     */
    public int getCoinType() {
        switch (this) {
            case LTC:
                return 2;
            case BCH:
                return 145;
            case BTC:
//            case TOP:
                return 0;
            case DASH:
                return 5;
            case FIL_EVM:
            case ETH:
                return 60;
            case DOGE:
                return 3;
            case EOS:
                return 194;
            case FIL:
                return 461;
            case TOP:
                return 562;
            default:
                return -1;
        }
    }

    /**
     * Whether it is formal or not
     *
     * @return
     */
    public boolean isTest() {
        switch (this) {
            //Added environment judgment (plus toggle) is currently not added
            case ETH:
            case EOS:
                return BuildConfig.DEBUG;
            case TOP:
            default:
                return false;
        }
    }

    public String getChainId() {
        switch (this) {
            //Added environment judgment (plus toggle) is currently not added
            case ETH:
                if (isTest()) {
                    return "4";
                } else {
                    return "1";
                }

            case FIL_EVM:
                if (BuildConfig.DEBUG) {
                    return "314159";
                } else {
                    return "314";
                }
            default:
                return "0";
        }
    }

    /**
     * Get network parameters
     *
     * @return
     */
    public AbstractBitcoinNetParams getNetworkParameters() {
        Log.d("NetworkParameters", getPublicKeyHash() + "");
        Log.d("NetworkParameters", getPrivateKeyVersion() + "");
        return NetParams.get(getPublicKeyHash(), getPrivateKeyVersion());
    }

}
