package com.topnetwork.core.database;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.topnetwork.core.identity.Identity;
import com.topnetwork.core.identity.IdentityDao;
import com.topnetwork.core.identity.RootIdentity;
import com.topnetwork.core.identity.RootIdentityDao;
import com.topnetwork.core.model.Address;
import com.topnetwork.core.model.AddressDao;
import com.topnetwork.core.model.NftPlatform;
import com.topnetwork.core.model.NftPlatformAssets;
import com.topnetwork.core.model.NftPlatformAssetsDao;
import com.topnetwork.core.model.NftPlatformDao;
import com.topnetwork.core.model.PushReceiverBean;
import com.topnetwork.core.model.PushReceiverBeanDao;
import com.topnetwork.core.model.Wallet;
import com.topnetwork.core.model.WalletDao;
import com.topnetwork.core.model.WalletSkin;
import com.topnetwork.core.model.WalletSkinDao;

import org.greenrobot.greendao.query.QueryBuilder;
import org.web3j.protocol.core.filters.Callback;

import java.util.ArrayList;
import java.util.List;

public class DaoManager {

    private String TAG = "DaoManager";
    private static DaoManager instance;
    private Identity mCurrentIdentity;

    private IdentityDao identityDao;
    private WalletDao walletDao;
    private WalletSkinDao walletSkinDao;
    private NftPlatformDao platformDataDao;
    private NftPlatformAssetsDao platformAssetsDao;
    private AddressDao addressDao;
    private PushReceiverBeanDao pushReceiverBeanDao;
    private RootIdentityDao rootIdentityDao;

    private Identity getCurrentIdentity() {
        return mCurrentIdentity;
    }

    public void setCurrentIdentity(Identity mCurrentIdentity) {
        this.mCurrentIdentity = mCurrentIdentity;
    }

    public static DaoManager getInstance() {
        if (instance == null) {
            synchronized (DaoManager.class) {
                if (instance == null) {
                    instance = new DaoManager();
                }
            }
        }
        return instance;
    }

    private DaoManager() {
        initIdentityDataBaseDao();
        /**
         * 初始化RootIdentityDao
         */
        if (DataBaseManager.getInstance().getRootIdentityDaoSession() != null) {
            rootIdentityDao = DataBaseManager.getInstance().getRootIdentityDaoSession().getRootIdentityDao();
            pushReceiverBeanDao = DataBaseManager.getInstance().getRootIdentityDaoSession().getPushReceiverBeanDao();
        } else {
            rootIdentityDao = null;
        }
    }

    /**
     * 初始化IdentityDao
     */
    public void initIdentityDataBaseDao() {
        if (DataBaseManager.getInstance().getIdentityDaoSession() != null) {
            identityDao = DataBaseManager.getInstance().getIdentityDaoSession().getIdentityDao();
            walletDao = DataBaseManager.getInstance().getIdentityDaoSession().getWalletDao();
            addressDao = DataBaseManager.getInstance().getIdentityDaoSession().getAddressDao();
            if(DataBaseManager.getInstance().getWalletSkinDaoSession() != null){
                walletSkinDao = DataBaseManager.getInstance().getWalletSkinDaoSession().getWalletSkinDao();
            }
            if(DataBaseManager.getInstance().getNftPlatformDaoSession()!= null){
                platformDataDao = DataBaseManager.getInstance().getNftPlatformDaoSession().getNftPlatformDao();
                platformAssetsDao = DataBaseManager.getInstance().getNftPlatformDaoSession().getNftPlatformAssetsDao();
            }
        }
    }

    public void setIdentityDao() {
        identityDao = DataBaseManager.getInstance().getIdentityDaoSession().getIdentityDao();
        walletDao = DataBaseManager.getInstance().getIdentityDaoSession().getWalletDao();
        addressDao = DataBaseManager.getInstance().getIdentityDaoSession().getAddressDao();
        pushReceiverBeanDao = DataBaseManager.getInstance().getRootIdentityDaoSession().getPushReceiverBeanDao();
        walletSkinDao = DataBaseManager.getInstance().getWalletSkinDaoSession().getWalletSkinDao();
        platformDataDao = DataBaseManager.getInstance().getNftPlatformDaoSession().getNftPlatformDao();
        platformAssetsDao = DataBaseManager.getInstance().getNftPlatformDaoSession().getNftPlatformAssetsDao();
    }


    /**
     * Insert NftPlatform data
     */
    public void insertNftPlatform(NftPlatform nftPlatform) {
        if (platformDataDao != null) {
            long l = platformDataDao.insert(nftPlatform);
        }

        for(int i = 0; i < nftPlatform.getAssetList().size(); i++){
            nftPlatform.getAssetList().get(i).setPid(nftPlatform.id);
            insertNftPlatformAssets(nftPlatform.getAssetList().get(i));
        }
    }

    /**
     * Insert NftPlatform data
     */
    public void insertNftPlatformAssets(NftPlatformAssets nftPlatformAssets) {
        if (platformAssetsDao != null) {
            long l = platformAssetsDao.insert(nftPlatformAssets);
        }
    }

    /**
     * Update NftPlatform data
     */
    public void upDataNftPlatform(NftPlatform nftPlatform) {
        if (platformDataDao != null) {
            platformDataDao.update(nftPlatform);
        }

    }

    /**
     * Remove NftPlatform data
     */
    public void removeNftPlatform(NftPlatform nftPlatform) {
        if (platformDataDao != null) {
            platformDataDao.deleteByKey(nftPlatform.id);
        }
    }

    /**
     * Example Query NftPlatform data
     */
    public List<NftPlatform> queryNftPlatform() {
        if (platformDataDao == null) return new ArrayList<>();
        return platformDataDao.queryBuilder().list();
    }

    /**
     * Query the NftPlatform data by using the nft address
     *
     */
    public NftPlatform queryNftPlatformByPlatformAddress(String address) {
        if (platformDataDao == null || address == null) return null;
        try {
            return platformDataDao.queryBuilder().where(NftPlatformDao.Properties.Platform.eq(address)).unique();
        }catch (Exception e){
            return null;
        }

    }

    /**
     * id of the NftPlatform
     */
    public NftPlatform queryNftPlatformById(Long id) {
        if (platformDataDao == null || id == null) return null;
        try {
            return platformDataDao.queryBuilder().where(NftPlatformDao.Properties.Id.eq(id)).unique();
        }catch (Exception e){
            return null;
        }

    }

    /**
     * Query all nft assets
     *
     * @return
     */
    public List<NftPlatformAssets> queryNftPlatformAssets() {
        if (platformAssetsDao == null) return new ArrayList<>();
        return platformAssetsDao.queryBuilder().list();
    }

    /**
     * Individual nft platform assets
     *
     * @return
     */
    public NftPlatformAssets queryNftPlatformAssets(String assetContract, String tokenId) {
        if (platformAssetsDao == null) return null;
        return platformAssetsDao.queryBuilder().where(NftPlatformAssetsDao.Properties.AssetContract.eq(assetContract),
                NftPlatformAssetsDao.Properties.TokenId.eq(tokenId)).unique();
    }


    /**
     * Insert walletSkin data
     */
    public void insertWalletSkin(WalletSkin walletSkin) {
        if (walletSkinDao != null) {
            long l = walletSkinDao.insert(walletSkin);
        }
    }

    /**
     * Update walletSkin data
     */
    public void upDataWalletSkin(WalletSkin walletSkin) {
        if (walletSkinDao != null) {
            walletSkinDao.update(walletSkin);
        }

    }

    /**
     * Remove walletSkin data
     */
    public void removeWalletSkin(WalletSkin walletSkin) {
        if (walletSkinDao != null) {
            walletSkinDao.deleteByKey(walletSkin.getId());
        }
    }

    /**
     * Example Query walletSkin data
     */
    public List<WalletSkin> queryWalletSkin() {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (walletSkinDao == null) return new ArrayList<>();
        return walletSkinDao.queryBuilder().list();
    }

    /**
     * single
     *
     * @return
     */
    public WalletSkin queryWalletSkin(String address) {
        if (walletSkinDao == null) return null;
        return walletSkinDao.queryBuilder().where(WalletSkinDao.Properties.WalletAddress.eq(address)).unique();
    }

    /**
     * Insert rootIdentity data
     */
    public void insertRootIdentity(RootIdentity rootIdentity) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (rootIdentityDao != null) {
            long l = rootIdentityDao.insert(rootIdentity);
            //LogUtils.dTag(TAG, "insertRoot: " + l);
        }
    }


    /**
     * Update rootIdentity data
     */
    public void upDataRootIdentity(RootIdentity rootIdentity) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (rootIdentityDao != null) {
            rootIdentityDao.update(rootIdentity);
        }

    }

    /**
     * Example Query rootIdentity data
     */
    public List<RootIdentity> queryRootIdentityData() {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (rootIdentityDao == null) return new ArrayList<>();
        return rootIdentityDao.queryBuilder().list();
    }

    /**
     * Remove rootIdentity data
     */
    public void removeRootIdentity(RootIdentity rootIdentity) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (rootIdentityDao != null) {
            rootIdentityDao.deleteByKey(rootIdentity.getId());
            pushReceiverBeanDao.deleteAll();
        }
    }

    /**
     * Inserting identity data
     */
    public void insertIdentity(Identity identity) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (identityDao != null) {
            long l = identityDao.insert(identity);
            //LogUtils.dTag(TAG, "insert: " + l);
        }

    }

    /**
     * Querying identity all data
     */
    public List<Identity> queryAllIdentityData() {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (identityDao.queryBuilder().list().size() <= 0) return new ArrayList<>();
        mCurrentIdentity = identityDao.queryBuilder().list().get(0);
        return identityDao.queryBuilder().list();
    }

    /**
     * Remove identity
     */
    public void removeIdentity(Identity identity) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (identityDao == null) return;
        identityDao.deleteByKey(identity.getId());
    }

    /**
     * 插入Wallet数据
     */
    public void insertWalletData(Wallet wallet) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        setWalletId(wallet);
        if (walletDao == null) return;
        walletDao.insert(wallet);
    }

    /**
     * Delete Wallet data
     */
    public void removeWalletData(Wallet coin) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao == null) return;
        walletDao.deleteByKey(coin.getId());
    }

    /**
     * Update Wallet data
     */
    public void updateWalletData(Wallet wallet) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao != null && DataBaseManager.getInstance().isOpenIdentityDataBase()) {
            walletDao.update(wallet);
        }
    }

//    /**
//     * Update Wallet data
//     */
//    public void updateWalletSkipData(Wallet wallet) {
//        if (walletDao != null) {
//            Wallet wallet1 = querySimpleWallet(wallet.getId());
//            wallet1.setSkinBase64(wallet.skinBase64);
//            wallet1.setSkinUrl(wallet.skinUrl);
//            walletDao.update(wallet1);
//        }
//    }

    /**
     * @param wallet
     */
    public void updateWalletDataById(Wallet wallet) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao != null) {
            Wallet wallet1 = querySimpleWallet(wallet.getId());
            wallet1.setTransactionRecord(wallet.getTransactionRecord());
            wallet1.setBalance(wallet.getBalance());
            walletDao.update(wallet1);
        }
    }

    /**
     * Query all data displayed in Wallet
     */
    public List<Wallet> queryAllShowWalletData() {
//        DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao == null) return new ArrayList<>();
        List<Wallet> walletList = walletDao.queryBuilder().where(WalletDao.Properties.IsDefaultSetCoinForToken.eq(false),
                WalletDao.Properties.IsHide.eq(false)).orderAsc(WalletDao.Properties.Id).list();
        return walletList;
    }

    /**
     * Query a single wallet
     */
    public Wallet querySimpleWallet(Long id) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        return walletDao.queryBuilder().where(WalletDao.Properties.Id.eq(id)).unique();
    }
    /**
     * Insert Address data
     *
     * @return
     */
    public void addAddress(Address address) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        setAddressId(address);
        if (addressDao != null) {
            addressDao.insert(address);
        }
    }

    /**
     * Update Address data
     */
    public void upDataAddress(Address address) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (addressDao != null) {
            addressDao.update(address);
        }

    }

    /**
     * Remove Address data
     */
    public void removeAddress(Address address) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (addressDao != null) {
            addressDao.deleteByKey(address.getId());
        }

    }

    /**
     * Querying Address Data
     */
    public List<Address> queryAddress() {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (addressDao == null) return new ArrayList<>();
        return addressDao.queryBuilder().list();
    }

    /**
     * Querying Address Data
     */
    public List<Address> queryAddressByAddressType(String type) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (addressDao == null) return new ArrayList<>();
        return addressDao.queryBuilder().where(AddressDao.Properties.Type.eq(type)).list();
    }

    /**
     * Insert push data
     *
     * @param
     * @return
     */
    public void insertPushReceiverBean(PushReceiverBean pushReceiverBean) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (pushReceiverBeanDao == null) return;
        pushReceiverBeanDao.insertOrReplace(pushReceiverBean);
    }

    /**
     * Update push data
     *
     * @param
     * @return
     */
    public void upDataPushReceiverBean(PushReceiverBean pushReceiverBean) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (pushReceiverBeanDao == null) return;
        pushReceiverBeanDao.update(pushReceiverBean);
    }

    /**
     * One-click read
     *
     * @param
     * @return
     */
    public void upDataAllPushReceiverBean() {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (pushReceiverBeanDao == null) return;
        List<PushReceiverBean> list = pushReceiverBeanDao.queryBuilder().where(PushReceiverBeanDao.Properties.IsRead.eq(false)).list();
        for (PushReceiverBean bean : list) {
            bean.setRead(true);
            pushReceiverBeanDao.update(bean);
        }
    }

    /**
     * Query push data
     *
     * @return
     */
    public PushReceiverBean queryPushReceiverBeanById(String id) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (addressDao == null) return null;
        return pushReceiverBeanDao.queryBuilder().where(PushReceiverBeanDao.Properties.Id.eq(id)).unique();
    }


    public List<Wallet> queryBySymbol(String symbol) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao == null) return new ArrayList<>();
        return walletDao.queryBuilder().where(WalletDao.Properties.Symbol.eq(symbol), WalletDao.Properties.MainChain.eq(true)
        ).list();
    }

    public List<Wallet> queryMainWalletBySymbol(String symbol) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao == null) return new ArrayList<>();
        return walletDao.queryBuilder().where(WalletDao.Properties.Symbol.eq(symbol), WalletDao.Properties.MainChain.eq(true),
                WalletDao.Properties.IsDefaultSetCoinForToken.eq(false)).list();
    }

    public List<Wallet> queryShowMainWalletBySymbol(String symbol) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao == null) return new ArrayList<>();
        return walletDao.queryBuilder().where(WalletDao.Properties.Symbol.eq(symbol), WalletDao.Properties.MainChain.eq(true),
                WalletDao.Properties.IsDefaultSetCoinForToken.eq(false),WalletDao.Properties.IsHide.eq(false)).list();
    }

    public List<Wallet> queryBySymbolForToken(String symbol, String chainType) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao == null) return new ArrayList<>();
        return walletDao.queryBuilder().where(WalletDao.Properties.ChainType.eq(chainType)
                , WalletDao.Properties.Symbol.eq(symbol)
                , WalletDao.Properties.MainChain.eq(false)
        ).list();
    }

    public List<Wallet> queryTokenBySymbolAndContractAddressForToken(String chainType, String contractAddress, String address) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao == null) return new ArrayList<>();
        return walletDao.queryBuilder().where(WalletDao.Properties.ChainType.eq(chainType), WalletDao.Properties.MainChain.eq(false)
                , WalletDao.Properties.ContractAddress.eq(contractAddress), WalletDao.Properties.Address.eq(address)
        ).list();
    }

    public List<Wallet> queryTokenBySymbolAndAddressForCoin(String symbol, String address) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao == null) return new ArrayList<>();
        return walletDao.queryBuilder().where(WalletDao.Properties.Symbol.eq(symbol), WalletDao.Properties.MainChain.eq(true)
                , WalletDao.Properties.Address.eq(address)
        ).list();
    }

    /**
     * Set the foreign key of the wallet
     *
     * @param wallet
     */
    private void setWalletId(Wallet wallet) {
        long id = getCurrentIdentity().getId();
        wallet.setWid(id);
    }

    /**
     * Set the foreign key of the address
     */
    private void setAddressId(Address address) {
        long id = getCurrentIdentity().getId();
        address.setAid(id);
    }

    /**
     * Clear Wallet data for your current identity
     */
    public void clearIdentityWallet(Identity identity) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao != null) {
            walletDao.deleteAll();
        }
    }

//    public List<Wallet> queryTokenBySymbolAndCoinChainTypeForTokenIsShow(String symbol, String chainType) {
//        if (walletDao == null) return new ArrayList<>();
//        return walletDao.queryBuilder().where(WalletDao.Properties.ChainType.eq(chainType)
//                , WalletDao.Properties.Symbol.eq(symbol),WalletDao.Properties.IsHide.eq(false),WalletDao.Properties.MainChain.eq(false)).list();
//    }

    /**
     * Query all hidden data in Wallet
     */
    public List<Wallet> queryAllHideWalletData() {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao == null) return new ArrayList<>();
        List<Wallet> walletList = walletDao.queryBuilder().where(WalletDao.Properties.IsDefaultSetCoinForToken.eq(false),
                WalletDao.Properties.IsHide.eq(true)).list();
        return walletList;
    }

    /**
     * Query for hidden main coins
     *
     * @param chainType
     * @return
     */
    public List<Wallet> queryHideWalletByChainType(String chainType) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (walletDao == null) return new ArrayList<>();
        return walletDao.queryBuilder().where(WalletDao.Properties.ChainType.eq(chainType)
                , WalletDao.Properties.MainChain.eq(true), WalletDao.Properties.IsDefaultSetCoinForToken.eq(true)
        ).list();
    }

    // push--------------------------------------------------------------------------------------------------------------------------------

    /**
     * Example Query the number of unread PushMsg messages
     */
    public Long queryPushReceiverBeanUnRead() {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (pushReceiverBeanDao == null) return 0l;
        return pushReceiverBeanDao.queryBuilder().where(PushReceiverBeanDao.Properties.IsRead.eq(false)).count();

    }

    /**
     * Paging queries the number of PushMsg
     */
    public List<PushReceiverBean> queryPushReceiverBeanLimit(int offset, int limit) {
        //DataBaseManager.getInstance().resetOpenDataBase();
        if (pushReceiverBeanDao == null) return new ArrayList<>();
        QueryBuilder<PushReceiverBean> builder = pushReceiverBeanDao.queryBuilder()
                .offset(offset * limit)
                .orderDesc(PushReceiverBeanDao.Properties.Time)
                .limit(limit);
        return builder.list();

    }

    /**
     * Query the latest message
     *
     * @param callback
     */
    public void getLastPushMsg(String id, Callback<PushReceiverBean> callback) {
        // DataBaseManager.getInstance().resetOpenDataBase();
        if (pushReceiverBeanDao == null) {
            callback.onEvent(null);
        }
        List<PushReceiverBean> beans = pushReceiverBeanDao.queryBuilder().where(PushReceiverBeanDao.Properties.Id.eq(id)).list();
        if (beans.size() > 0) {
            callback.onEvent(beans.get(0));
        } else {
            callback.onEvent(null);
        }
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

}
