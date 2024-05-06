package com.topnetwork.core.model;


import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class PushReceiverBean implements Parcelable {
    @Id(autoincrement = true)
    private Long myId;

    private String type;
    @Unique
    public String id;
    private String txhash;
    private String url;
    private long time;
    private String desc;
    private String title;
    private boolean isRead;
    private Long pid; // Foreign key
    @Generated(hash = 354209761)
    public PushReceiverBean(Long myId, String type, String id, String txhash,
            String url, long time, String desc, String title, boolean isRead,
            Long pid) {
        this.myId = myId;
        this.type = type;
        this.id = id;
        this.txhash = txhash;
        this.url = url;
        this.time = time;
        this.desc = desc;
        this.title = title;
        this.isRead = isRead;
        this.pid = pid;
    }

    @Generated(hash = 1831208662)
    public PushReceiverBean() {
    }

    protected PushReceiverBean(Parcel in) {
        if (in.readByte() == 0) {
            myId = null;
        } else {
            myId = in.readLong();
        }
        type = in.readString();
        id = in.readString();
        txhash = in.readString();
        url = in.readString();
        time = in.readLong();
        desc = in.readString();
        title = in.readString();
        isRead = in.readByte() != 0;
        if (in.readByte() == 0) {
            pid = null;
        } else {
            pid = in.readLong();
        }
    }

    public static final Creator<PushReceiverBean> CREATOR = new Creator<PushReceiverBean>() {
        @Override
        public PushReceiverBean createFromParcel(Parcel in) {
            return new PushReceiverBean(in);
        }

        @Override
        public PushReceiverBean[] newArray(int size) {
            return new PushReceiverBean[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxhash() {
        return txhash;
    }

    public void setTxhash(String txhash) {
        this.txhash = txhash;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Long getPid() {
        return this.pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public boolean getIsRead() {
        return this.isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Long getMyId() {
        return this.myId;
    }

    public void setMyId(Long myId) {
        this.myId = myId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (myId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(myId);
        }
        dest.writeString(type);
        dest.writeString(id);
        dest.writeString(txhash);
        dest.writeString(url);
        dest.writeLong(time);
        dest.writeString(desc);
        dest.writeString(title);
        dest.writeByte((byte) (isRead ? 1 : 0));
        if (pid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(pid);
        }
    }
}
