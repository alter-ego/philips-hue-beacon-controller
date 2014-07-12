package com.alterego.ibeaconapp.app;

import android.app.Application;
import com.alterego.advancedandroidlogger.implementations.DetailedAndroidLogger;
import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;

public class MainApplication extends Application {

    public static final String LOGGING_TAG = "BEACON_APP";
    private static final IAndroidLogger.LoggingLevel LOGGING_LEVEL = IAndroidLogger.LoggingLevel.VERBOSE;
    public static MainApplication instance;
    private SettingsManager mSettingsManager;

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        setupSettingsManager();
        //TODO registerAnalytics();
        //TODO setupAds();

    }

    private void setupSettingsManager() {
        IAndroidLogger logger = new DetailedAndroidLogger(LOGGING_TAG, LOGGING_LEVEL);
        mSettingsManager = new SettingsManager(this, logger, logger);
        ;
    }

    public SettingsManager getSettingsManager() {

        if (mSettingsManager == null) {
            setupSettingsManager();
        }

        return mSettingsManager;
    }

}
