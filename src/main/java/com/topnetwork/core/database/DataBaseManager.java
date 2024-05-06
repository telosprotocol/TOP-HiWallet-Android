package com.topnetwork.core.database;

import android.content.Context;

import com.topnetwork.core.identity.DaoMaster;
import com.topnetwork.core.identity.DaoSession;
import com.topnetwork.core.identity.IdentityDao;
import com.topnetwork.core.identity.RootIdentityDao;
import com.topnetwork.core.model.NftPlatformDao;
import com.topnetwork.core.model.WalletSkinDao;
import com.topnetwork.core.utils.NumericUtil;
import com.topnetwork.core.utils.StringUtils;

import org.bitcoinj.core.Sha256Hash;
import org.greenrobot.greendao.database.Database;

import java.nio.charset.StandardCharsets;

public class DataBaseManager {

    private Database dbRootIdentity;
    private Database dbIdentity;
    private Database dbWalletSkin;
    private Database dbNftPlatform;
    private Context context;

    private DaoSession rootIdentityDaoSession; //Identity database
    private DaoSession identityDaoSession;//database of identity details
    private DaoSession walletSkinDaoSession;//database of wallet skins
    private DaoSession nftPlatformDaoSession;//Custom nft database

    private boolean isOpenIdentityDataBase = false;

    private boolean isClearCurrentIdentityWallet = false; //Whether to clear the Wallet of your current identity

    public boolean isClearCurrentIdentityWallet() {
        return isClearCurrentIdentityWallet;
    }

    public void setClearCurrentIdentityWallet(boolean clearCurrentIdentityWallet) {
        isClearCurrentIdentityWallet = clearCurrentIdentityWallet;
    }

    //这面需要建立数据库
    private static DataBaseManager instance;

    public boolean isOpenIdentityDataBase() {
        return isOpenIdentityDataBase;
    }

    public void setOpenIdentityDataBase(boolean openIdentityDataBase) {
        isOpenIdentityDataBase = openIdentityDataBase;
    }

    public DaoSession getRootIdentityDaoSession() {
        return rootIdentityDaoSession;
    }

    public DaoSession getIdentityDaoSession() {
        return identityDaoSession;
    }

    public DaoSession getWalletSkinDaoSession() {
        return walletSkinDaoSession;
    }

    public void setWalletSkinDaoSession(DaoSession walletSkinDaoSession) {
        this.walletSkinDaoSession = walletSkinDaoSession;
    }

    public DaoSession getNftPlatformDaoSession() {
        return nftPlatformDaoSession;
    }

    public void setNftPlatformDaoSession(DaoSession nftPlatformDaoSession) {
        this.nftPlatformDaoSession = nftPlatformDaoSession;
    }

    public static DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }

    //Open skin collection
    public void openWalletSkinDataBase(Context context) {
        this.context = context;
        MyOpenHelper mHelper = new MyOpenHelper(context, "wallet_skin.db"
                , null, WalletSkinDao.class);
        getInstance().dbWalletSkin = mHelper.getWritableDb();
        DaoMaster mDaoMaster = new DaoMaster(instance.dbWalletSkin);
        getInstance().walletSkinDaoSession = mDaoMaster.newSession();
    }

    //Open custom nft
    public void openCustomNftDataBase(Context context) {
        this.context = context;
        MyOpenHelper mHelper = new MyOpenHelper(context, "custom_nft.db"
                , null, NftPlatformDao.class);
        getInstance().dbNftPlatform = mHelper.getWritableDb();
        DaoMaster mDaoMaster = new DaoMaster(instance.dbNftPlatform);
        getInstance().nftPlatformDaoSession = mDaoMaster.newSession();
    }

    //Open the identity collection database
    public void openRootIdentityDataBase(Context context) {
        this.context = context;
        MyOpenHelper mHelper = new MyOpenHelper(context, "root_identity.db"
                , null, RootIdentityDao.class);
        getInstance().dbRootIdentity = mHelper.getWritableDb();
        DaoMaster mDaoMaster = new DaoMaster(instance.dbRootIdentity);
        getInstance().rootIdentityDaoSession = mDaoMaster.newSession();
    }

    //Open a database for an identity
    public void openIdentityDataBase(String password, String dataBaseName, Context context) {
        this.context = context;
        if (!StringUtils.isEmpty(dataBaseName)) {
            if (!dataBaseName.endsWith(".db")) {
                dataBaseName = dataBaseName + ".db";
            }
            MyOpenHelper mHelper = new MyOpenHelper(context, dataBaseName, null
                    , IdentityDao.class);
            getInstance().dbIdentity = mHelper.getEncryptedWritableDb(getDatabasePassword(password));
            DaoMaster mDaoMaster = new DaoMaster(instance.dbIdentity);
            getInstance().identityDaoSession = mDaoMaster.newSession();
            getInstance().isOpenIdentityDataBase = true;
            openWalletSkinDataBase(context);
            openCustomNftDataBase(context);
            DaoManager.getInstance().initIdentityDataBaseDao();
        }

    }

    /**
     * Get password Utf-8 Hash value (identity database password)
     * Hash once
     */
    public String getDatabasePassword(String password) {
        byte[] bytes = Sha256Hash.hash(password.getBytes(StandardCharsets.UTF_8));
        return NumericUtil.bytesToHex1(bytes);
    }

    private void closeDbRootIdentity() {
        if (dbRootIdentity != null) {
            dbRootIdentity.close();
        }
    }

    public void closeDbIdentity() {
        if (dbIdentity != null) {
            dbIdentity.close();
        }
    }

    public Database getDbIdentity() {
        return dbIdentity;
    }

}
