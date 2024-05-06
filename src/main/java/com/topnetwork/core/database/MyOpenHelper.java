package com.topnetwork.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.topnetwork.core.identity.DaoMaster;
import com.topnetwork.core.identity.IdentityDao;
import com.topnetwork.core.model.WalletDao;

import org.greenrobot.greendao.database.Database;

public class MyOpenHelper extends DaoMaster.OpenHelper {
    private Class aClass;
    private Context context;

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory
            , Class aClass) {
        super(context, name, factory);
        this.aClass = aClass;
        this.context = context;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion && oldVersion <= 3) {
            DataBaseManager.getInstance().setClearCurrentIdentityWallet(true);
        }
        if (aClass.getName().equals(IdentityDao.class.getName())) {
            //database table DAO to be managed is passed into the method as the last parameter
            MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

                @Override
                public void onCreateAllTables(Database db, boolean ifNotExists) {
                    DaoMaster.createAllTables(db, ifNotExists);
                }

                @Override
                public void onDropAllTables(Database db, boolean ifExists) {
                    DaoMaster.dropAllTables(db, ifExists);
                }
            }, aClass, WalletDao.class);
        }
        else {
            MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

                @Override
                public void onCreateAllTables(Database db, boolean ifNotExists) {
                    DaoMaster.createAllTables(db, ifNotExists);
                }

                @Override
                public void onDropAllTables(Database db, boolean ifExists) {
                    DaoMaster.dropAllTables(db, ifExists);
                }
            }, aClass);
        }
    }

    /**
     * Update the newly added field in the database
     *
     * @param db
     */
    private void updateParams(Database db) {
        Log.e("MigrationHelper", "updateParams");
        try {
            db.execSQL("UPDATE WALLET SET IS_ACTIVE = true WHERE CHAIN_TYPE != 'EOS'");
        } catch (Exception e) {
            Log.e("SQL", e.getMessage());
        }
    }

}