package com.alterego.ibeaconapp.app.managers;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeInfo;

import java.util.ArrayList;
import java.util.List;

public class HueBridgeManager {

    private final SettingsManager mSettingsManager;
    private final IAndroidLogger mLogger;

    private List<HueBridgeInfo> mLastHueBridges = new ArrayList<HueBridgeInfo>();
    private HueBridgeInfo mLastAccessedHueBridge;
    private String mLastHueBridgeUsername;

    public HueBridgeManager (SettingsManager mgr) {
        mSettingsManager = mgr;
        mLogger = mSettingsManager.getLogger();
    }

    public List<HueBridgeInfo> getLastHueBridges () {
        if (mLastHueBridges.size() == 0) {
            //TODO reads from the database;
        }

        mLogger.debug("getLastHueBridges mLastHueBridges size = " + mLastHueBridges.size());

        return mLastHueBridges;
    }

    public boolean setLastHueBridges (List<HueBridgeInfo> bridges) {
        mLogger.debug("setLastHueBridges bridges = " + bridges.toString());
        mLastHueBridges = bridges;
        //TODO saves to the database and returns true if successful
        return false;
    }

    public HueBridgeInfo getLastAccessedHueBridge () {
        if (mLastAccessedHueBridge == null && mSettingsManager.getStoredPreferencesHelper().storesLastAccessedHueBridge()) {
            //TODO try to read mLastAccessedHueBridge from the preferences/db
            String last_accessed_bridge_id = mSettingsManager.getStoredPreferencesHelper().restoreLastAccessedHueBridge();
            //try to find it in the DB
        }
        mLogger.debug("getLastAccessedHueBridge mLastAccessedHueBridge = " + mLastAccessedHueBridge.toString());
        return mLastAccessedHueBridge;
    }

    public void setLastAccessedHueBridge (HueBridgeInfo bridge) {
        mLogger.debug("setLastAccessedHueBridge bridge = " + bridge.toString());
        mLastAccessedHueBridge = bridge;
        mSettingsManager.getStoredPreferencesHelper().storeLastAccessedHueBridge(bridge);
    }

    public String getLastHueBridgeUsername () {
        if (mLastHueBridgeUsername == null && mSettingsManager.getStoredPreferencesHelper().storesLastAccessedBridgeUsername()) {
            mLastHueBridgeUsername = mSettingsManager.getStoredPreferencesHelper().restoreLastAccessedBridgeUsername();
        }
        mLogger.debug("getLastHueBridgeUsername mLastHueBridgeUsername = " + mLastHueBridgeUsername);
        return mLastHueBridgeUsername;
    }

    public void setLastHueBridgeUsername (String username) {
        mLogger.debug("setLastHueBridgeUsername username = " + username);
        mLastHueBridgeUsername = username;
        mSettingsManager.getStoredPreferencesHelper().storeLastAccessedBridgeUsername(mLastHueBridgeUsername);
    }

    public int getHueBridgePosition (HueBridgeInfo bridge) {
        mLogger.debug("getHueBridgePosition bridge = " + bridge.toString());

        if (mLastHueBridges!=null && mLastHueBridges.contains(bridge)) {
            return mLastHueBridges.indexOf(bridge);
        } else
            return 0;

    }

}
