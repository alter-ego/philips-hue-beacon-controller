package com.alterego.ibeaconapp.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.androidbound.ViewBinder;
import com.alterego.androidbound.zzzztoremove.UiThreadScheduler;
import com.radiusnetworks.ibeacon.IBeaconManager;

import lombok.Getter;

public class SettingsManager {

    @Getter private final Application mParentApplication;
    private final ViewBinder mViewBinder;
    @Getter private final IAndroidLogger mLogger;
    private final BeaconConsumer mBeaconConsumer;
    @Getter private IBeaconManager iBeaconManager;
    public static final String MMBeaconProximityUUID = "e2c56db5-dffb-48d2-b060-d0f5a71096e0";
    public static final int MMBeaconMajor = 0;
    public static final int MMBeaconMinor = 0;
    private Activity mActivity;

    public SettingsManager (Application app, IAndroidLogger logger, IAndroidLogger viewbinder_logger) {
        setupBluetooth(app);
        mLogger = logger;
        mParentApplication  = app;
        mViewBinder = new ViewBinder(app, UiThreadScheduler.instance, mLogger);
        mBeaconConsumer = new BeaconConsumer(this);
//        mReaderServerService = createReaderServerService(app, getGson());
//        mReaderServerApiManager = new ReaderServerApiManager(this);
//        mCurrentLanguage = checkLanguage();
//        mCurrentConfig = new ReaderConfig(); //TODO add reading this from the server, asynchronously; also add default config to assets
//        mReaderFragmentFactory = new ReaderFragmentFactory(this, R.id.container);
    }

    public void setActivity (Activity act) {
        mActivity = act;
        //iBeaconManager.bind(mBeaconConsumer);
    }

    public void setupBluetooth(Application app) {
        iBeaconManager = IBeaconManager.getInstanceForApplication(app);
        verifyBluetooth();
        iBeaconManager.setBackgroundBetweenScanPeriod(Settings.DEFAULT_BACKGROUND_BETWEEN_SCAN_PERIOD);
        iBeaconManager.setBackgroundScanPeriod(Settings.DEFAULT_BACKGROUND_SCAN_PERIOD);
        iBeaconManager.setForegroundBetweenScanPeriod(Settings.DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD);
        iBeaconManager.setForegroundScanPeriod(Settings.DEFAULT_FOREGROUND_SCAN_PERIOD);
    }

    private void verifyBluetooth() {

        try {
            if (!IBeaconManager.getInstanceForApplication(mParentApplication).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mParentApplication);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mParentApplication);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            builder.show();

        }
    }


    public void bindBluetooth () {
        iBeaconManager.bind(mBeaconConsumer);
    }

    public void unBindBluetooth () {
        iBeaconManager.unBind(mBeaconConsumer);
    }

    protected void pauseBluetooth() {
        if (iBeaconManager.isBound(mBeaconConsumer)) iBeaconManager.setBackgroundMode(mBeaconConsumer, true);
    }

    protected void resumeBluetooth() {
        if (iBeaconManager.isBound(mBeaconConsumer)) iBeaconManager.setBackgroundMode(mBeaconConsumer, false);
    }


}
