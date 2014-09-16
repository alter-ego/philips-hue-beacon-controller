package com.alterego.ibeaconapp.app.navigation;

import com.alterego.androidbound.ViewModel;
import com.alterego.ibeaconapp.app.R;
import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeConfiguration;
import com.alterego.ibeaconapp.app.managers.BeaconFragmentFactory;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@Accessors(prefix = "m")
public class NavigationDrawerViewModel extends ViewModel {

    private final SettingsManager mSettingsManager;
    private final Subscription mConfigOKSubscription;
    private final Subscription mConfigNullOrEmptyOrErrorSubscription;
    @Getter private int mCurrentSelectedPosition; //TODO do we need mCurrentSelectedPosition here and in fragment?
    private BeaconFragmentFactory mBeaconFragmentFactory;
    private List<String> mMenuItems;

    public NavigationDrawerViewModel(SettingsManager mgr, int currentSelectedPosition) {

        mSettingsManager = mgr;
        mBeaconFragmentFactory = mSettingsManager.getBeaconFragmentFactory();
        setLogger(mgr.getLogger());
        mCurrentSelectedPosition = currentSelectedPosition;

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
            setNavigationList(Arrays.asList(mSettingsManager.getParentApplication().getResources().getStringArray(R.array.full_menu_items)));
            doSelectMenuItem(getNavigationList().get(mCurrentSelectedPosition));
        }
    };

    Observer<HueBridgeConfiguration> hueBridgeConfigurationNullOrErrorOrEmptyObserver = new Observer<HueBridgeConfiguration>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            mLogger.error("ViewModelHomeWithoutBridge hueBridgeConfigurationNullObserver onError = " + e.toString());
            setNavigationList(Arrays.asList(mSettingsManager.getParentApplication().getResources().getStringArray(R.array.short_menu_items)));
            mCurrentSelectedPosition = 0;
            doSelectMenuItem(getNavigationList().get(mCurrentSelectedPosition));
        }

        @Override
        public void onNext(HueBridgeConfiguration hueBridgeConfiguration) {
            setNavigationList(Arrays.asList(mSettingsManager.getParentApplication().getResources().getStringArray(R.array.short_menu_items)));
            mCurrentSelectedPosition = 0;
            doSelectMenuItem(getNavigationList().get(mCurrentSelectedPosition));
        }
    };

    public List<String> getNavigationList() {
        return mMenuItems;
    }

    public void setNavigationList(List<String> items) {
        mMenuItems = items;
        raisePropertyChanged("NavigationList");
    }

    public boolean canSelectMenuItem() {
        return true;
    }

    public void doSelectMenuItem(String menu_title) {
        try {
            mLogger.debug("doSelectMenuItem item = " + menu_title);
            mCurrentSelectedPosition = getNavigationList().indexOf(menu_title);
            mSettingsManager.getNavigationDrawerHandler().selectItem(mCurrentSelectedPosition);
        } catch (Exception e) {
            mSettingsManager.getLogger().warning("NavigationDrawerViewModel doSelectMenuItem exception = " + e.toString());
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if (mConfigOKSubscription != null) {
            mConfigOKSubscription.unsubscribe();
        }
        if (mConfigNullOrEmptyOrErrorSubscription != null) {
            mConfigNullOrEmptyOrErrorSubscription.unsubscribe();
        }
    }
}
