package com.alterego.ibeaconapp.app.hue;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.ibeaconapp.app.managers.SettingsManager;
import com.alterego.ibeaconapp.app.hue.data.HueBridgeNuPNPInfo;

import java.util.ArrayList;
import java.util.List;

public class HueBridgeManager {

    private final SettingsManager mSettingsManager;
    private final IAndroidLogger mLogger;

    private List<HueBridgeNuPNPInfo> mLastHueBridges = new ArrayList<HueBridgeNuPNPInfo>();
    private HueBridgeNuPNPInfo mLastAccessedHueBridge;

    public HueBridgeManager (SettingsManager mgr) {
        mSettingsManager = mgr;
        mLogger = mSettingsManager.getLogger();
    }

    public List<HueBridgeNuPNPInfo> getLastHueBridges () {
        if (mLastHueBridges.size() == 0) {
            //TODO reads from the database;
        }

        mLogger.debug("getLastHueBridges mLastHueBridges size = " + mLastHueBridges.size());

        return mLastHueBridges;
    }

    public boolean setLastHueBridges (List<HueBridgeNuPNPInfo> bridges) {
        mLogger.debug("setLastHueBridges bridges = " + bridges.toString());
        mLastHueBridges = bridges;
        //TODO saves to the database and returns true if successful
        return false;
    }

    public HueBridgeNuPNPInfo getLastAccessedHueBridge () {
        if (mLastAccessedHueBridge == null) {
            //TODO try to read mLastAccessedHueBridge from the preferences/db
        }
        mLogger.debug("getLastAccessedHueBridge mLastAccessedHueBridge = " + mLastAccessedHueBridge.toString());
        return mLastAccessedHueBridge;
    }

    public void setLastAccessedHueBridge (HueBridgeNuPNPInfo bridge) {
        mLogger.debug("setLastAccessedHueBridge bridge = " + bridge.toString());
        mLastAccessedHueBridge = bridge;
    }

    public int getHueBridgePosition (HueBridgeNuPNPInfo bridge) {
        mLogger.debug("getHueBridgePosition bridge = " + bridge.toString());

        if (mLastHueBridges!=null && mLastHueBridges.contains(bridge)) {
            return mLastHueBridges.indexOf(bridge);
        } else
            return 0;

    }

}
