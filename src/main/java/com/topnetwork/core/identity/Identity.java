package com.topnetwork.core.identity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import com.topnetwork.core.model.Address;
import com.topnetwork.core.model.AddressDao;
import com.topnetwork.core.model.Wallet;
import com.topnetwork.core.model.WalletDao;

import java.util.List;


//Identity entity class
@Entity
public class Identity {
    @Id(autoincrement = true)
    private Long id;
    private String keystore; //keystore

    @ToMany(referencedJoinProperty = "wid")//Specifies the id of the other class associated with it
    private List<Wallet> wallets;

    @ToMany(referencedJoinProperty = "aid")//Specifies the id of the other class associated with it
    private List<Address> addresses;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 638081332)
    private transient IdentityDao myDao;

    public Identity() {
    }

    @Generated(hash = 1347970030)
    public Identity(Long id, String keystore) {
        this.id = id;
        this.keystore = keystore;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeystore() {
        return this.keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 6381310)
    public List<Wallet> getWallets() {
        if (wallets == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WalletDao targetDao = daoSession.getWalletDao();
            List<Wallet> walletsNew = targetDao._queryIdentity_Wallets(id);
            synchronized (this) {
                if (wallets == null) {
                    wallets = walletsNew;
                }
            }
        }
        return wallets;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1899338572)
    public synchronized void resetWallets() {
        wallets = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 947321292)
    public List<Address> getAddresses() {
        if (addresses == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AddressDao targetDao = daoSession.getAddressDao();
            List<Address> addressesNew = targetDao._queryIdentity_Addresses(id);
            synchronized (this) {
                if (addresses == null) {
                    addresses = addressesNew;
                }
            }
        }
        return addresses;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 238407379)
    public synchronized void resetAddresses() {
        addresses = null;
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
    @Generated(hash = 1338529557)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getIdentityDao() : null;
    }

}
