package com.topnetwork.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Address implements Parcelable {

    @Id(autoincrement = true)
    private Long id;
    private Long aid = 1L;//Foreign key
    private String address;
    private String picUrl;
    private String type;
    private String remark;
    private boolean isCommonAddress;

    public Address(){

    }
    protected Address(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            aid = null;
        } else {
            aid = in.readLong();
        }
        address = in.readString();
        picUrl = in.readString();
        type = in.readString();
        remark = in.readString();
        isCommonAddress = in.readByte() != 0;
    }
    @Generated(hash = 824904745)
    public Address(Long id, Long aid, String address, String picUrl, String type,
            String remark, boolean isCommonAddress) {
        this.id = id;
        this.aid = aid;
        this.address = address;
        this.picUrl = picUrl;
        this.type = type;
        this.remark = remark;
        this.isCommonAddress = isCommonAddress;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        if (aid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(aid);
        }
        dest.writeString(address);
        dest.writeString(picUrl);
        dest.writeString(type);
        dest.writeString(remark);
        dest.writeByte((byte) (isCommonAddress ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isCommonAddress() {
        return isCommonAddress;
    }

    public void setCommonAddress(boolean commonAddress) {
        isCommonAddress = commonAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }


    public boolean getIsCommonAddress() {
        return this.isCommonAddress;
    }
    public void setIsCommonAddress(boolean isCommonAddress) {
        this.isCommonAddress = isCommonAddress;
    }
}
