package com.alterego.ibeaconapp.app.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.graphics.Typeface;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.androidbound.ViewBinder;
import com.alterego.androidbound.zzzztoremove.UiThreadScheduler;
import com.alterego.ibeaconapp.app.R;
import com.alterego.ibeaconapp.app.api.beacon.BeaconConsumer;
import com.alterego.ibeaconapp.app.helpers.BindingValueConverters;
import com.alterego.ibeaconapp.app.helpers.DateTimeSerializer;
import com.alterego.ibeaconapp.app.helpers.StoredPreferencesHelper;
import com.alterego.ibeaconapp.app.api.hue.HueBridgeApiManager;
import com.alterego.ibeaconapp.app.api.hue.NuPNPApiManager;
import com.alterego.ibeaconapp.app.interfaces.IActionBarTitleHandler;
import com.alterego.ibeaconapp.app.interfaces.INavigationDrawerHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.radiusnetworks.ibeacon.IBeaconManager;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix="m")
public class SettingsManager {

    @Getter private final Application mParentApplication;
    @Getter private final ViewBinder mViewBinder;
    @Getter private final IAndroidLogger mLogger;
    private static DateTimeSerializer dateSerializer = new DateTimeSerializer(ISODateTimeFormat.dateTimeParser().withZoneUTC());
    @Getter private final ImageLoaderConfiguration mImageLoaderConfiguration;
    @Getter private final HueBridgeManager mHueBridgeManager;
    @Getter private Gson mGson = new GsonBuilder().registerTypeAdapter(DateTime.class, dateSerializer).create();
    private final BeaconConsumer mBeaconConsumer;
    @Getter private IBeaconManager mBeaconManager;
    public static final String MMBeaconProximityUUID = "e2c56db5-dffb-48d2-b060-d0f5a71096e0";
    public static final int MMBeaconMajor = 0;
    public static final int MMBeaconMinor = 0;
    @Getter private Activity mParentActivity;
    @Getter @Setter private INavigationDrawerHandler mNavigationDrawerHandler;
    @Getter @Setter private IActionBarTitleHandler mActionBarTitleHandler;
    @Getter private BeaconFragmentFactory mBeaconFragmentFactory;
    @Getter private StoredPreferencesHelper mStoredPreferencesHelper;
    @Getter @Setter private HueBridgeApiManager mHueBridgeApiManager;

    //API Managers
    @Getter private NuPNPApiManager mNuPNPApiManager;
    private BindingValueConverters mDefaultValueConverters;

    public SettingsManager (Application app, IAndroidLogger logger, IAndroidLogger viewbinder_logger, ImageLoaderConfiguration imageLoaderConfig) {

        setupBluetooth(app);
        mLogger = logger;
        mParentApplication  = app;
        mImageLoaderConfiguration = imageLoaderConfig;
        mViewBinder = new ViewBinder(app, UiThreadScheduler.instance, viewbinder_logger, getImageLoaderConfiguration());
        mViewBinder.getFontManager().setDefaultFont(Typeface.createFromAsset(mParentApplication.getAssets(), "roboto_thin.ttf"));
        //mViewBinder.getFontManager().registerFont("");
        mBeaconConsumer = new BeaconConsumer(this);
        mHueBridgeManager = new HueBridgeManager(this);
        mStoredPreferencesHelper = new StoredPreferencesHelper(mParentApplication);

        setupManagers ();
    }

    public void setParentActivity (Activity act) {
        mParentActivity = act;


        if (mDefaultValueConverters == null) {
            mDefaultValueConverters = new BindingValueConverters(act, this);
        } else {
            mDefaultValueConverters.setParentActivity(act);
        }
    }

    private void setupManagers () {
        mNuPNPApiManager = new NuPNPApiManager(this);
        mBeaconFragmentFactory = new BeaconFragmentFactory (this, R.id.container);
    }

    public void setupBluetooth(Application app) {
        mBeaconManager = IBeaconManager.getInstanceForApplication(app);
        verifyBluetooth();
        mBeaconManager.setBackgroundBetweenScanPeriod(app.getResources().getInteger(R.integer.default_background_between_scan_period_in_ms));
        mBeaconManager.setBackgroundScanPeriod(app.getResources().getInteger(R.integer.default_background_scan_period_in_ms));
        mBeaconManager.setForegroundBetweenScanPeriod(app.getResources().getInteger(R.integer.default_foreground_between_scan_period_in_ms));
        mBeaconManager.setForegroundScanPeriod(app.getResources().getInteger(R.integer.default_foreground_scan_period_in_ms));
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
        mBeaconManager.bind(mBeaconConsumer);
    }

    public void unBindBluetooth () {
        mBeaconManager.unBind(mBeaconConsumer);
    }

    public void pauseBluetooth() {
        if (mBeaconManager.isBound(mBeaconConsumer)) mBeaconManager.setBackgroundMode(mBeaconConsumer, true);
    }

    public void resumeBluetooth() {
        if (mBeaconManager.isBound(mBeaconConsumer)) mBeaconManager.setBackgroundMode(mBeaconConsumer, false);
    }


}
