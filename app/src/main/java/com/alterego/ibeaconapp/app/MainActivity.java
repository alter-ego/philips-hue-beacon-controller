package com.alterego.ibeaconapp.app;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeConfiguration;
import com.alterego.ibeaconapp.app.navigation.NavigationDrawerFragment;
import com.alterego.ibeaconapp.app.interfaces.IActionBarTitleHandler;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import java.util.Arrays;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity
        implements IActionBarTitleHandler {

    public static final String SAVED_TITLE = "saved_title";

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private SettingsManager mSettingsManager;
    private ActionBar mActionBar;
    private IAndroidLogger mLogger;
    private Subscription mConfigOKSubscription;
    private Subscription mConfigNullOrEmptyOrErrorSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.setProperty("org.joda.time.DateTimeZone.Provider", "com.alterego.ibeaconapp.app.helpers.FastDateTimeZoneProvider");

        mSettingsManager = MainApplication.instance.getSettingsManager();
        mSettingsManager.setParentActivity(this);
        mSettingsManager.setActionBarTitleHandler(this);

        mActionBar = getSupportActionBar();
        mLogger = mSettingsManager.getLogger();

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(SAVED_TITLE);
        } else
            mTitle = getTitle();

        mSettingsManager.bindBluetooth();

        // Set up the drawer.
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        mConfigOKSubscription = mSettingsManager.getHueBridgeManager().getConfigSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(new Func1<HueBridgeConfiguration, Boolean>() {
                    @Override
                    public Boolean call(HueBridgeConfiguration hueBridgeConfiguration) {
                        return hueBridgeConfiguration != null && !hueBridgeConfiguration.isEmpty();
                    }
                }).subscribe(hueBridgeConfigurationOkObserver);

        mConfigNullOrEmptyOrErrorSubscription = mSettingsManager.getHueBridgeManager().getConfigSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(new Func1<HueBridgeConfiguration, Boolean>() {
                    @Override
                    public Boolean call(HueBridgeConfiguration hueBridgeConfiguration) {
                        return hueBridgeConfiguration == null || hueBridgeConfiguration.isEmpty();
                    }
                }).subscribe(hueBridgeConfigurationNullOrErrorOrEmptyObserver);
    }

    Action1<HueBridgeConfiguration> hueBridgeConfigurationOkObserver = new Action1<HueBridgeConfiguration>() {
        @Override
        public void call(HueBridgeConfiguration hueBridgeConfiguration) {
            mLogger.info("MainActivity resuming bluetooth");
            mSettingsManager.resumeBluetooth();
        }
    };

    Observer<HueBridgeConfiguration> hueBridgeConfigurationNullOrErrorOrEmptyObserver = new Observer<HueBridgeConfiguration>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            mLogger.info("MainActivity pausing bluetooth");
            mSettingsManager.pauseBluetooth();
        }

        @Override
        public void onNext(HueBridgeConfiguration hueBridgeConfiguration) {
            mLogger.info("MainActivity pausing bluetooth");
            mSettingsManager.pauseBluetooth();
        }
    };

     public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        setActionBarTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSettingsManager.unBindBluetooth();
        if (mConfigOKSubscription != null) {
            mConfigOKSubscription.unsubscribe();
        }
        if (mConfigNullOrEmptyOrErrorSubscription != null) {
            mConfigNullOrEmptyOrErrorSubscription.unsubscribe();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        mLogger.info("MainActivity pausing bluetooth");
        mSettingsManager.pauseBluetooth();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSettingsManager.getNavigationDrawerHandler().openLastOpenedItem();

        if (mSettingsManager.getHueBridgeManager().isHueBridgeConnected()) {
            mLogger.info("MainActivity resuming bluetooth");
            mSettingsManager.resumeBluetooth();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_TITLE, mTitle.toString());
    }

    @Override
    public void setActionBarTitle(CharSequence title) {
        mLogger.verbose("setActionBarTitle title = " + title);
        if (title != null)
            mTitle = title;

        if (mActionBar != null)
            mActionBar.setTitle(mTitle);
    }
}
