package com.topnetwork.core.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class NftPlatformAssets implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    private Long pid = 1L;//Foreign key

    public String animationOriginalUrl;//	Original platform animation URL
    public String animationUrl;// Animated URL
    public String assetContract;//	Contract address
    public String assetItemType;//	Resource types, description (IMAGE: pictures, VIDEO, VIDEO, AUDIO, AUDIO, MODEL3D: 3 d model), the available value: AUDIO, IMAGE, MODEL3D, VIDEO
    public String chain;//Chain: Description (ETH:ETH chain,BSC:BSC chain (not supported yet)). Available value :BSC,ETH
    public String currentPrice;//Listing price (use the latest transaction price or no sale price if it does not exist)
    public String externalLink;//	opensea Link to platform entry
    public String imagePreviewUrl;//	Show pictures
    public String imageThumbnailUrl;//thumbnail
    public String imageUrl;//Original picture
    public String name;//name
    public String permalink;//opensea Platform Details link

    public String schemaName;//Token types, description (ERC721: ERC721 ERC1155: ERC1155, CRYPTOPUNKS: CRYPTOPUNKS), available value: CRYPTOPUNKS, ERC1155, ERC721
    public String sellSymbol;//symbol
    public String tokenId;//token_Id

    public int currentState = 1;

    public NftPlatformAssets(){

    }

    public String buildSkinUrl(String assetContract,String tokenId){
        return assetContract + tokenId;
    }


    protected NftPlatformAssets(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            pid = null;
        } else {
            pid = in.readLong();
        }
        animationOriginalUrl = in.readString();
        animationUrl = in.readString();
        assetContract = in.readString();
        assetItemType = in.readString();
        chain = in.readString();
        currentPrice = in.readString();
        externalLink = in.readString();
        imagePreviewUrl = in.readString();
        imageThumbnailUrl = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        permalink = in.readString();
        schemaName = in.readString();
        sellSymbol = in.readString();
        tokenId = in.readString();
        currentState = in.readInt();
    }


    @Generated(hash = 656146570)
    public NftPlatformAssets(Long id, Long pid, String animationOriginalUrl, String animationUrl, String assetContract,
            String assetItemType, String chain, String currentPrice, String externalLink, String imagePreviewUrl,
            String imageThumbnailUrl, String imageUrl, String name, String permalink, String schemaName, String sellSymbol,
            String tokenId, int currentState) {
        this.id = id;
        this.pid = pid;
        this.animationOriginalUrl = animationOriginalUrl;
        this.animationUrl = animationUrl;
        this.assetContract = assetContract;
        this.assetItemType = assetItemType;
        this.chain = chain;
        this.currentPrice = currentPrice;
        this.externalLink = externalLink;
        this.imagePreviewUrl = imagePreviewUrl;
        this.imageThumbnailUrl = imageThumbnailUrl;
        this.imageUrl = imageUrl;
        this.name = name;
        this.permalink = permalink;
        this.schemaName = schemaName;
        this.sellSymbol = sellSymbol;
        this.tokenId = tokenId;
        this.currentState = currentState;
    }

    public static final Creator<NftPlatformAssets> CREATOR = new Creator<NftPlatformAssets>() {
        @Override
        public NftPlatformAssets createFromParcel(Parcel in) {
            return new NftPlatformAssets(in);
        }

        @Override
        public NftPlatformAssets[] newArray(int size) {
            return new NftPlatformAssets[size];
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
        if (pid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(pid);
        }
        dest.writeString(animationOriginalUrl);
        dest.writeString(animationUrl);
        dest.writeString(assetContract);
        dest.writeString(assetItemType);
        dest.writeString(chain);
        dest.writeString(currentPrice);
        dest.writeString(externalLink);
        dest.writeString(imagePreviewUrl);
        dest.writeString(imageThumbnailUrl);
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeString(permalink);
        dest.writeString(schemaName);
        dest.writeString(sellSymbol);
        dest.writeString(tokenId);
        dest.writeInt(currentState);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return this.pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getAssetContract() {
        return this.assetContract;
    }

    public void setAssetContract(String assetContract) {
        this.assetContract = assetContract;
    }

    public String getCurrentPrice() {
        return this.currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getExternalLink() {
        return this.externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getImagePreviewUrl() {
        return this.imagePreviewUrl;
    }

    public void setImagePreviewUrl(String imagePreviewUrl) {
        this.imagePreviewUrl = imagePreviewUrl;
    }

    public String getImageThumbnailUrl() {
        return this.imageThumbnailUrl;
    }

    public void setImageThumbnailUrl(String imageThumbnailUrl) {
        this.imageThumbnailUrl = imageThumbnailUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermalink() {
        return this.permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getSellSymbol() {
        return this.sellSymbol;
    }

    public void setSellSymbol(String sellSymbol) {
        this.sellSymbol = sellSymbol;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getAssetItemType() {
        return this.assetItemType;
    }

    public void setAssetItemType(String assetItemType) {
        this.assetItemType = assetItemType;
    }

    public String getSchemaName() {
        return this.schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public int getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public String getChain() {
        return this.chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }


    public String getAnimationOriginalUrl() {
        return this.animationOriginalUrl;
    }


    public void setAnimationOriginalUrl(String animationOriginalUrl) {
        this.animationOriginalUrl = animationOriginalUrl;
    }


    public String getAnimationUrl() {
        return this.animationUrl;
    }


    public void setAnimationUrl(String animationUrl) {
        this.animationUrl = animationUrl;
    }

}
