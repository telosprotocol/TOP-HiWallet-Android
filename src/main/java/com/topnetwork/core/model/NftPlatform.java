package com.topnetwork.core.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.topnetwork.core.identity.DaoSession;

@Entity
public class NftPlatform {
    @Id(autoincrement = true)
    public Long id; //Platform id
    public String platform; //Platform address
    public String description;//Description
    public String logo = ""; //Platform logo
    public String name = "";//Platform name
    public String website; //Platform website
    public String identityId;//identity id

    @ToMany(referencedJoinProperty = "pid")//Specifies the other classes associated with it id
    private List<NftPlatformAssets> assetList;//assemble
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1342711292)
    private transient NftPlatformDao myDao;

    public NftPlatform(List<NftPlatformAssets> assetList) {
        this.assetList = assetList;
    }

    public NftPlatform() {

    }

    @Generated(hash = 957123971)
    public NftPlatform(Long id, String platform, String description, String logo,
            String name, String website, String identityId) {
        this.id = id;
        this.platform = platform;
        this.description = description;
        this.logo = logo;
        this.name = name;
        this.website = website;
        this.identityId = identityId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIdentityId() {
        return this.identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public void setAssetList(List<NftPlatformAssets> assetList) {
        this.assetList = assetList;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1802401273)
    public List<NftPlatformAssets> getAssetList() {
        if (assetList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NftPlatformAssetsDao targetDao = daoSession.getNftPlatformAssetsDao();
            List<NftPlatformAssets> assetListNew = targetDao
                    ._queryNftPlatform_AssetList(id);
            synchronized (this) {
                if (assetList == null) {
                    assetList = assetListNew;
                }
            }
        }
        return assetList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 420606042)
    public synchronized void resetAssetList() {
        assetList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1801295869)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNftPlatformDao() : null;
    }




}
