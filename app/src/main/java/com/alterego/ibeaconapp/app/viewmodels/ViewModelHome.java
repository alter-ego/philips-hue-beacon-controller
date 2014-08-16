package com.alterego.ibeaconapp.app.viewmodels;

import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.alterego.androidbound.ViewModel;
import com.alterego.ibeaconapp.app.fragments.ConnectUsernameFragment;
import com.alterego.ibeaconapp.app.api.hue.HueBridgeApiManager;
import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeInfo;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewModelHome extends ViewModel {

    private final SettingsManager mSettingsManager;
    private List<HueBridgeInfo> mHueBridges;
    private View mView;

    public ViewModelHome (SettingsManager mgr) {
        mSettingsManager = mgr;
        mLogger = mgr.getLogger();
        mHueBridges = mSettingsManager.getHueBridgeManager().getLastHueBridges();
    }


    public boolean canOpenBridge () {
        //TODO canOpenBridge
        return true;
    }

    public void doOpenBridge (HueBridgeInfo bridgeNuPNPInfo) {
        //TODO doOpenBridge
        mSettingsManager.setHueBridgeApiManager(new HueBridgeApiManager(mSettingsManager, bridgeNuPNPInfo.getInternalIPAddress()));
        ConnectUsernameFragment.newInstance(mSettingsManager).show(((ActionBarActivity) mSettingsManager.getParentActivity()).getSupportFragmentManager(), null);
    }

    public List<HueBridgeInfo> getSavedBridges () {
        return mHueBridges;
    }

    public void setSavedBridges (List<HueBridgeInfo> bridges) {
        mHueBridges = bridges;
        raisePropertyChanged("SavedBridges");
        setHasSavedBridges();
    }

    public boolean getHasSavedBridges () {
        return mHueBridges!=null && mHueBridges.size() > 0;
    }

    public void setHasSavedBridges () {
        raisePropertyChanged("HasSavedBridges");
    }

    public boolean canLookForLocalHueBridges () {
        //TODO canLookForLocalHueBridges check if we're connected via wifi!
        return true;
    }

    public void doLookForLocalHueBridges () {
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
                    }

                    @Override
                    public void onNext(List<HueBridgeInfo> hueBridgeNuPNPInfoList) {
                        mLogger.info("doLookForLocalHueBridges onNext hueBridgeNuPNPInfoList = " + hueBridgeNuPNPInfoList.toString());
                        setSavedBridges(hueBridgeNuPNPInfoList);
                        mSettingsManager.getHueBridgeManager().setLastHueBridges(hueBridgeNuPNPInfoList);
                    }
                });
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void setView(View view) {
        mView = view;
    }
}
