package com.topnetwork.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class WalletSkin implements Parcelable {
    @Id(autoincrement = true)
    private Long id; // Increment primary key
    public String walletAddress;
    public String skinBase64; //Receiving skin
    public String skinUrl; //Receiving skin
    public String identityId;

    public WalletSkin(){

    }

    public String buildSkinUrl(String assetContract,String tokenId){
        return assetContract + tokenId;
    }

    protected WalletSkin(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        walletAddress = in.readString();
        skinBase64 = in.readString();
        skinUrl = in.readString();
        identityId = in.readString();
    }

    @Generated(hash = 666836594)
    public WalletSkin(Long id, String walletAddress, String skinBase64,
            String skinUrl, String identityId) {
        this.id = id;
        this.walletAddress = walletAddress;
        this.skinBase64 = skinBase64;
        this.skinUrl = skinUrl;
        this.identityId = identityId;
    }
    
    public static final Creator<WalletSkin> CREATOR = new Creator<WalletSkin>() {
        @Override
        public WalletSkin createFromParcel(Parcel in) {
            return new WalletSkin(in);
        }

        @Override
        public WalletSkin[] newArray(int size) {
            return new WalletSkin[size];
        }
    };

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
        dest.writeString(walletAddress);
        dest.writeString(skinBase64);
        dest.writeString(skinUrl);
        dest.writeString(identityId);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWalletAddress() {
        return this.walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getSkinBase64() {
        return this.skinBase64;
    }

    public void setSkinBase64(String skinBase64) {
        this.skinBase64 = skinBase64;
    }

    public String getSkinUrl() {
        return this.skinUrl;
    }

    public void setSkinUrl(String skinUrl) {
        this.skinUrl = skinUrl;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }
}
