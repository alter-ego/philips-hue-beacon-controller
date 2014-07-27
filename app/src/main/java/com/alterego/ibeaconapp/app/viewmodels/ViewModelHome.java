package com.alterego.ibeaconapp.app.viewmodels;

import android.view.View;

import com.alterego.androidbound.ViewModel;
import com.alterego.ibeaconapp.app.managers.SettingsManager;
import com.alterego.ibeaconapp.app.hue.data.HueBridgeNuPNPInfo;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewModelHome extends ViewModel {

    private final SettingsManager mSettingsManager;
    private List<HueBridgeNuPNPInfo> mHueBridges;
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

    public void doOpenBridge (HueBridgeNuPNPInfo bridgeNuPNPInfo) {
        //TODO doOpenBridge
    }

    public List<HueBridgeNuPNPInfo> getSavedBridges () {
        return mHueBridges;
    }

    public void setSavedBridges (List<HueBridgeNuPNPInfo> bridges) {
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
                .subscribe(new Observer<List<HueBridgeNuPNPInfo>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLogger.warning("doLookForLocalHueBridges error = " + e.toString());
                    }

                    @Override
                    public void onNext(List<HueBridgeNuPNPInfo> hueBridgeNuPNPInfoList) {
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
