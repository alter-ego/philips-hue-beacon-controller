package com.alterego.ibeaconapp;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.alterego.ibeaconapp.HueBridgeNUPNPInfo;

import com.alterego.ibeaconapp.HueBridgeNUPNPInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig hueBridgeNUPNPInfoDaoConfig;

    private final HueBridgeNUPNPInfoDao hueBridgeNUPNPInfoDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        hueBridgeNUPNPInfoDaoConfig = daoConfigMap.get(HueBridgeNUPNPInfoDao.class).clone();
        hueBridgeNUPNPInfoDaoConfig.initIdentityScope(type);

        hueBridgeNUPNPInfoDao = new HueBridgeNUPNPInfoDao(hueBridgeNUPNPInfoDaoConfig, this);

        registerDao(HueBridgeNUPNPInfo.class, hueBridgeNUPNPInfoDao);
    }
    
    public void clear() {
        hueBridgeNUPNPInfoDaoConfig.getIdentityScope().clear();
    }

    public HueBridgeNUPNPInfoDao getHueBridgeNUPNPInfoDao() {
        return hueBridgeNUPNPInfoDao;
    }

}