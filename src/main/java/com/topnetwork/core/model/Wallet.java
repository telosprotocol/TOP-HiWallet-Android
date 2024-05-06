package com.topnetwork.core.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Wallet implements Parcelable {

    @Id(autoincrement = true)
    private Long id; // Increment primary key
    private Long wid; // Foreign key
    private int index;  // Add wallet for BIP44 proposed index
    public String balance = "0.00"; //balance
    public String value = "0.00"; //value

    private String iconUrl;
    public String accountName;
    public String address;
    private String contractAddress = "";
    public String chainType = "";
    public boolean mainChain; //

    private String tokenCoinIconUrl;//Picture (not required for main coin, token is a picture of main coin

    private String detailUrl;  // Details Link url
    public String fullName;
    public String symbol;

    private String aliasName;
    public String keystore;

    private double price; //Currency
    @Transient
    private static int DECIMAL = 18;
    public int decimals = DECIMAL;
    private boolean isHide = false;
    private boolean isDefaultSetCoinForToken = false;

    private String transactionRecord;

    private boolean needActivate; //Whether the account needs to be activated true The account needs to be activated. false The account does not need to be activated

    public Wallet() {

    }

    public Wallet(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            wid = null;
        } else {
            wid = in.readLong();
        }
        index = in.readInt();
        balance = in.readString();
        value = in.readString();
        iconUrl = in.readString();
        accountName = in.readString();
        address = in.readString();
        contractAddress = in.readString();
        chainType = in.readString();
        mainChain = in.readByte() != 0;
        tokenCoinIconUrl = in.readString();
        detailUrl = in.readString();
        fullName = in.readString();
        symbol = in.readString();
        aliasName = in.readString();
        keystore = in.readString();
        price = in.readDouble();
        decimals = in.readInt();
        isHide = in.readByte() != 0;
        isDefaultSetCoinForToken = in.readByte() != 0;
        transactionRecord = in.readString();
        needActivate = in.readByte() != 0;
//        skinUrl = in.readString();
//        skinBase64 = in.readString();
    }

    @Generated(hash = 1380363400)
    public Wallet(Long id, Long wid, int index, String balance, String value,
                  String iconUrl, String accountName, String address,
                  String contractAddress, String chainType, boolean mainChain,
                  String tokenCoinIconUrl, String detailUrl, String fullName,
                  String symbol, String aliasName, String keystore, double price,
                  int decimals, boolean isHide, boolean isDefaultSetCoinForToken,
                  String transactionRecord, boolean needActivate) {
        this.id = id;
        this.wid = wid;
        this.index = index;
        this.balance = balance;
        this.value = value;
        this.iconUrl = iconUrl;
        this.accountName = accountName;
        this.address = address;
        this.contractAddress = contractAddress;
        this.chainType = chainType;
        this.mainChain = mainChain;
        this.tokenCoinIconUrl = tokenCoinIconUrl;
        this.detailUrl = detailUrl;
        this.fullName = fullName;
        this.symbol = symbol;
        this.aliasName = aliasName;
        this.keystore = keystore;
        this.price = price;
        this.decimals = decimals;
        this.isHide = isHide;
        this.isDefaultSetCoinForToken = isDefaultSetCoinForToken;
        this.transactionRecord = transactionRecord;
        this.needActivate = needActivate;
    }


    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWid() {
        return wid;
    }

    public void setWid(Long wid) {
        this.wid = wid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

//    public String getSkinBase64() {
//        return skinBase64;
//    }
//
//    public void setSkinBase64(String skinBase64) {
//        this.skinBase64 = skinBase64;
//    }
//
//    public String getSkinUrl() {
//        return skinUrl;
//    }
//
//    public void setSkinUrl(String skinUrl) {
//        this.skinUrl = skinUrl;
//    }

    public boolean isMainChain() {
        return mainChain;
    }

    public void setMainChain(boolean mainChain) {
        this.mainChain = mainChain;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getTokenCoinIconUrl() {
        return tokenCoinIconUrl;
    }

    public void setTokenCoinIconUrl(String tokenCoinIconUrl) {
        this.tokenCoinIconUrl = tokenCoinIconUrl;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public boolean isShow() {
        return getIsShow();
    }

    public boolean isHide() {
        return isHide;
    }

    public String getChainType() {
        return chainType;
    }

    public void setChainType(String chainType) {
        this.chainType = chainType;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }

    public boolean getIsShow() {
        return !this.isHide;
    }

    public boolean isDefaultSetCoinForToken() {
        return isDefaultSetCoinForToken;
    }

    public void setDefaultSetCoinForToken(boolean defaultSetCoinForToken) {
        isDefaultSetCoinForToken = defaultSetCoinForToken;
    }


    public boolean getIsDefaultSetCoinForToken() {
        return this.isDefaultSetCoinForToken;
    }

    public void setIsDefaultSetCoinForToken(boolean isDefaultSetCoinForToken) {
        this.isDefaultSetCoinForToken = isDefaultSetCoinForToken;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean getIsHide() {
        return this.isHide;
    }


    public void setIsHide(boolean isHide) {
        this.isHide = isHide;
    }


    public boolean getMainChain() {
        return this.mainChain;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        if (wid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(wid);
        }
        dest.writeInt(index);
        dest.writeString(balance);
        dest.writeString(value);
        dest.writeString(iconUrl);
        dest.writeString(accountName);
        dest.writeString(address);
        dest.writeString(contractAddress);
        dest.writeString(chainType);
        dest.writeByte((byte) (mainChain ? 1 : 0));
        dest.writeString(tokenCoinIconUrl);
        dest.writeString(detailUrl);
        dest.writeString(fullName);
        dest.writeString(symbol);
        dest.writeString(aliasName);
        dest.writeString(keystore);
        dest.writeDouble(price);
        dest.writeInt(decimals);
        dest.writeByte((byte) (isHide ? 1 : 0));
        dest.writeByte((byte) (isDefaultSetCoinForToken ? 1 : 0));
        dest.writeString(transactionRecord);
        dest.writeByte((byte) (needActivate ? 1 : 0));
//        dest.writeString(skinUrl);
//        dest.writeString(skinBase64);
    }

    public String getTransactionRecord() {
        return this.transactionRecord;
    }

    public void setTransactionRecord(String transactionRecord) {
        this.transactionRecord = transactionRecord;
    }

    public boolean getNeedActivate() {
        return this.needActivate;
    }

    public void setNeedActivate(boolean needActivate) {
        this.needActivate = needActivate;
    }

    public String getAccountName() {
        if (!TextUtils.isEmpty(accountName)) {
            return accountName;
        }
        return address;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", wid=" + wid +
                ", index=" + index +
                ", balance='" + balance + '\'' +
                ", value='" + value + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", accountName='" + accountName + '\'' +
                ", address='" + address + '\'' +
                ", contractAddress='" + contractAddress + '\'' +
                ", chainType='" + chainType + '\'' +
                ", mainChain=" + mainChain +
                ", tokenCoinIconUrl='" + tokenCoinIconUrl + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                ", fullName='" + fullName + '\'' +
                ", symbol='" + symbol + '\'' +
                ", aliasName='" + aliasName + '\'' +
                ", keystore='" + keystore + '\'' +
                ", price=" + price +
                ", decimals=" + decimals +
                ", isHide=" + isHide +
                ", isDefaultSetCoinForToken=" + isDefaultSetCoinForToken +
                ", transactionRecord='" + transactionRecord + '\'' +
                ", needActivate=" + needActivate +
                '}';
    }
}
