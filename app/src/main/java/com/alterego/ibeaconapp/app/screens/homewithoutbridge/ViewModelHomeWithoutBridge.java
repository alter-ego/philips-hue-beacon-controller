package com.alterego.ibeaconapp.app.screens.homewithoutbridge;

import android.view.View;

import com.alterego.androidbound.ViewModel;
import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeConfiguration;
import com.alterego.ibeaconapp.app.screens.connecttobridge.ConnectUsernameFragment;
import com.alterego.ibeaconapp.app.api.hue.HueBridgeApiManager;
import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeInfo;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ViewModelHomeWithoutBridge extends ViewModel {

    private final SettingsManager mSettingsManager;
    private Subscription mConfigErrorAndNullSubscription;
    private Subscription mConfigOKSubscription;
    private Subscription mConfigEmptySubscription;
    private List<HueBridgeInfo> mHueBridges;
    private View mView;
    private boolean mConnectingProgressBarVisible = false;
    private boolean mErrorTextVisible = false;
    private String mErrorDescription;

    public ViewModelHomeWithoutBridge(SettingsManager mgr) {
        mSettingsManager = mgr;
        mLogger = mgr.getLogger();
        mHueBridges = mSettingsManager.getHueBridgeManager().getLastHueBridges();

        mConfigOKSubscription = mSettingsManager.getHueBridgeManager().getConfigSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(new Func1<HueBridgeConfiguration, Boolean>() {
                    @Override
                    public Boolean call(HueBridgeConfiguration hueBridgeConfiguration) {
                        return hueBridgeConfiguration != null && !hueBridgeConfiguration.isEmpty();
                    }
                }).subscribe(hueBridgeConfigurationOkObserver);

        mConfigErrorAndNullSubscription = mSettingsManager.getHueBridgeManager().getConfigSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(new Func1<HueBridgeConfiguration, Boolean>() {
                    @Override
                    public Boolean call(HueBridgeConfiguration hueBridgeConfiguration) {
                        return hueBridgeConfiguration == null;
                    }
                }).subscribe(hueBridgeConfigurationErrorAndNullObserver);

        mConfigEmptySubscription = mSettingsManager.getHueBridgeManager().getConfigSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .filter(new Func1<HueBridgeConfiguration, Boolean>() {
                    @Override
                    public Boolean call(HueBridgeConfiguration hueBridgeConfiguration) {
                        return hueBridgeConfiguration.isEmpty();
                    }
                }).subscribe(hueBridgeConfigurationEmptyObserver);
    }

    Observer<HueBridgeConfiguration> hueBridgeConfigurationErrorAndNullObserver = new Observer<HueBridgeConfiguration>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            mLogger.error("ViewModelHomeWithoutBridge hueBridgeConfigurationNullObserver onError = " + e.toString());
            setErrorTextVisible(true, e.toString()); //TODO add error message
            setConnectingProgressBarVisible(false);
        }

        @Override
        public void onNext(HueBridgeConfiguration hueBridgeConfiguration) {
            //setErrorTextVisible(true, mSettingsManager.getParentApplication().getString(R.string.dialog_usernameconnect_error)); //TODO add message
            setErrorTextVisible(true, "error connecting!");
            setConnectingProgressBarVisible(false);
        }
    };

    Action1<HueBridgeConfiguration> hueBridgeConfigurationEmptyObserver = new Action1<HueBridgeConfiguration>() {
        @Override
        public void call(HueBridgeConfiguration hueBridgeConfiguration) {
            setErrorTextVisible(false, "");
            setConnectingProgressBarVisible(false);
        }
    };

    Action1<HueBridgeConfiguration> hueBridgeConfigurationOkObserver = new Action1<HueBridgeConfiguration>() {
        @Override
        public void call(HueBridgeConfiguration hueBridgeConfiguration) {
            setErrorTextVisible(false, "");
            setConnectingProgressBarVisible(false);
            mSettingsManager.getBeaconFragmentFactory().replaceFragmentInMainContainer(0); //TODO this should be in a navigation drawer!
        }
    };

    public boolean canOpenBridge() {
        //TODO canOpenBridge
        return true;
    }

    public void doOpenBridge(HueBridgeInfo bridgeNuPNPInfo) {
        mSettingsManager.setHueBridgeApiManager(new HueBridgeApiManager(mSettingsManager, bridgeNuPNPInfo.getInternalIPAddress()));
        if (mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername() == null)
            ConnectUsernameFragment.newInstance(mSettingsManager).show(mSettingsManager.getParentActivity().getFragmentManager(), null);
        else {
            mLogger.info("doOpenBridge we already have a username = " + mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername());
            setConnectingProgressBarVisible(true);
            mSettingsManager.getHueBridgeManager().loadConfigForLastHueBridgeUsername();
        }
    }

    public List<HueBridgeInfo> getSavedBridges() {
        return mHueBridges;
    }

    public void setSavedBridges(List<HueBridgeInfo> bridges) {
        mHueBridges = bridges;
        raisePropertyChanged("SavedBridges");
        setHasSavedBridges();
    }

    public boolean getHasSavedBridges() {
        return mHueBridges != null && mHueBridges.size() > 0;
    }

    public void setHasSavedBridges() {
        raisePropertyChanged("HasSavedBridges");
    }

    public boolean canLookForLocalHueBridges() {
        //TODO canLookForLocalHueBridges check if we're connected via wifi!
        return true;
    }

    public void doLookForLocalHueBridges() {

        setConnectingProgressBarVisible(true);

        mSettingsManager.getNuPNPApiManager()
                .findLocalBridges()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HueBridgeInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.warning("doLookForLocalHueBridges error = " + e.toString());
                        //TODO error handling?
                    }

                    @Override
                    public void onNext(List<HueBridgeInfo> hueBridgeNuPNPInfoList) {
                        mLogger.info("doLookForLocalHueBridges onNext hueBridgeNuPNPInfoList = " + hueBridgeNuPNPInfoList.toString());
                        setConnectingProgressBarVisible(false);
                        setSavedBridges(hueBridgeNuPNPInfoList);
                        mSettingsManager.getHueBridgeManager().setLastHueBridges(hueBridgeNuPNPInfoList);
                    }
                });
    }

    public boolean getConnectingProgressBarVisible() {
        return mConnectingProgressBarVisible;
    }

    public void setConnectingProgressBarVisible(boolean visible) {
        mConnectingProgressBarVisible = visible;
        raisePropertyChanged("ConnectingProgressBarVisible");
    }

    public String getErrorDescription() {
        return mErrorDescription;
    }

    public boolean getErrorTextVisible() {
        return mErrorTextVisible;
    }

    public void setErrorTextVisible(boolean visible, String desc) {
        mErrorTextVisible = visible;
        mErrorDescription = desc;
        raisePropertyChanged("ErrorDescription");
        raisePropertyChanged("ErrorTextVisible");
    }

    @Override
    public void dispose() {
        super.dispose();

        if (mConfigEmptySubscription != null) {
            mConfigEmptySubscription.unsubscribe();
        }
        if (mConfigErrorAndNullSubscription != null) {
            mConfigErrorAndNullSubscription.unsubscribe();
        }
        if (mConfigOKSubscription != null) {
            mConfigOKSubscription.unsubscribe();
        }
    }

    public void setView(View view) {
        mView = view;
    }
}
