package com.topnetwork.core.identity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.DaoException;
import com.topnetwork.core.model.PushReceiverBean;
import com.topnetwork.core.model.PushReceiverBeanDao;

import java.util.List;

@Entity
public class RootIdentity {
    @Id(autoincrement = true)
    private Long id;
    private String seedId;
    private String name;
    private boolean isSetTouchId = false;
    private boolean isTouchPay = false; // Fingerprint payment
    private boolean isSetDefaultWallet = false;//Whether to set the default added wallet
    private boolean isBackUpMnemonic = false;
    private boolean isCurrentShowIdentity = false; //Whether to show current identity
    private String decryptIpFsParamsStr;
    private String totalAssets = "0";

    //Used to decrypt keystore V2 (actually iv, misnamed)
    private String iv;
    private String cipherText;

    //ivSat(misname)
    private String saltIv;
    private String saltCipherText;

    private String pinVerifyStr; //Pin Code verification will be used
    @ToMany(referencedJoinProperty = "pid")//Specifies the id of the other class associated with it

    private List<PushReceiverBean> pushReceiverBeans;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1016887011)
    private transient RootIdentityDao myDao;

    public RootIdentity() {
    }




    @Generated(hash = 1368126614)
    public RootIdentity(Long id, String seedId, String name, boolean isSetTouchId,
            boolean isTouchPay, boolean isSetDefaultWallet, boolean isBackUpMnemonic,
            boolean isCurrentShowIdentity, String decryptIpFsParamsStr, String totalAssets,
            String iv, String cipherText, String saltIv, String saltCipherText,
            String pinVerifyStr) {
        this.id = id;
        this.seedId = seedId;
        this.name = name;
        this.isSetTouchId = isSetTouchId;
        this.isTouchPay = isTouchPay;
        this.isSetDefaultWallet = isSetDefaultWallet;
        this.isBackUpMnemonic = isBackUpMnemonic;
        this.isCurrentShowIdentity = isCurrentShowIdentity;
        this.decryptIpFsParamsStr = decryptIpFsParamsStr;
        this.totalAssets = totalAssets;
        this.iv = iv;
        this.cipherText = cipherText;
        this.saltIv = saltIv;
        this.saltCipherText = saltCipherText;
        this.pinVerifyStr = pinVerifyStr;
    }




    public String getSeedId() {
        return seedId;
    }

    public void setSeedId(String seedId) {
        this.seedId = seedId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSetTouchId() {
        return isSetTouchId;
    }

    public void setSetTouchId(boolean setTouchId) {
        isSetTouchId = setTouchId;
    }

    public boolean isTouchPay() {
        return isTouchPay;
    }

    public void setTouchPay(boolean touchPay) {
        isTouchPay = touchPay;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsSetTouchId() {
        return this.isSetTouchId;
    }

    public void setIsSetTouchId(boolean isSetTouchId) {
        this.isSetTouchId = isSetTouchId;
    }

    public boolean isSetDefaultWallet() {
        return isSetDefaultWallet;
    }

    public void setSetDefaultWallet(boolean setDefaultWallet) {
        isSetDefaultWallet = setDefaultWallet;
    }

    public boolean getIsSetDefaultWallet() {
        return this.isSetDefaultWallet;
    }

    public void setIsSetDefaultWallet(boolean isSetDefaultWallet) {
        this.isSetDefaultWallet = isSetDefaultWallet;
    }

    public boolean isCurrentShowIdentity() {
        return isCurrentShowIdentity;
    }

    public void setCurrentShowIdentity(boolean currentShowIdentity) {
        isCurrentShowIdentity = currentShowIdentity;
    }

    public String getDecryptIpFsParamsStr() {
        return decryptIpFsParamsStr;
    }

    public void setDecryptIpFsParamsStr(String decryptIpFsParamsStr) {
        this.decryptIpFsParamsStr = decryptIpFsParamsStr;
    }

    public String getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(String totalAssets) {
        this.totalAssets = totalAssets;
    }

    public boolean getIsBackUpMnemonic() {
        return isBackUpMnemonic;
    }

    public void setBackUpMnemonic(boolean backUpMnemonic) {
        isBackUpMnemonic = backUpMnemonic;
    }

    public void setIsBackUpMnemonic(boolean isBackUpMnemonic) {
        this.isBackUpMnemonic = isBackUpMnemonic;
    }

    public boolean getIsCurrentShowIdentity() {
        return this.isCurrentShowIdentity;
    }

    public void setIsCurrentShowIdentity(boolean isCurrentShowIdentity) {
        this.isCurrentShowIdentity = isCurrentShowIdentity;
    }

    public boolean getIsTouchPay() {
        return this.isTouchPay;
    }

    public void setIsTouchPay(boolean isTouchPay) {
        this.isTouchPay = isTouchPay;
    }

    public boolean isBackUpMnemonic() {
        return isBackUpMnemonic;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getCipherText() {
        return cipherText;
    }

    public void setCipherText(String cipherText) {
        this.cipherText = cipherText;
    }

    public String getSaltIv() {
        return saltIv;
    }

    public void setSaltIv(String saltIv) {
        this.saltIv = saltIv;
    }

    public String getSaltCipherText() {
        return saltCipherText;
    }

    public void setSaltCipherText(String saltCipherText) {
        this.saltCipherText = saltCipherText;
    }

    public String getPinVerifyStr() {
        return pinVerifyStr;
    }

    public void setPinVerifyStr(String pinVerifyStr) {
        this.pinVerifyStr = pinVerifyStr;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 941945253)
    public List<PushReceiverBean> getPushReceiverBeans() {
        if (pushReceiverBeans == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PushReceiverBeanDao targetDao = daoSession.getPushReceiverBeanDao();
            List<PushReceiverBean> pushReceiverBeansNew = targetDao
                    ._queryRootIdentity_PushReceiverBeans(id);
            synchronized (this) {
                if (pushReceiverBeans == null) {
                    pushReceiverBeans = pushReceiverBeansNew;
                }
            }
        }
        return pushReceiverBeans;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 250636752)
    public synchronized void resetPushReceiverBeans() {
        pushReceiverBeans = null;
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
    @Generated(hash = 1822969843)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRootIdentityDao() : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */

}
