package com.alterego.ibeaconapp.app.screens.homewithbridge;

import android.view.View;

import com.alterego.androidbound.ViewModel;
import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeConfiguration;
import com.alterego.ibeaconapp.app.api.hue.data.HueLight;
import com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

public class ViewModelHomeWithBridge extends ViewModel {

    private final SettingsManager mSettingsManager;
    private View mView;
    private Subscription mConfigSubscription;
    private boolean mErrorTextVisible = false;
    private String mErrorDescription;
    private boolean mConnectingProgressBarVisible = false;
    private boolean mSearchLightsVisible = false;

    public ViewModelHomeWithBridge(SettingsManager mgr) {
        mSettingsManager = mgr;
        mLogger = mgr.getLogger();
        setConnectingProgressBarVisible(true);

        //TODO use mgr internal lights subject so that we can refresh the list of lights from the manager
        mConfigSubscription = mSettingsManager.getHueBridgeManager().getConfigSubject()
                .flatMap(new Func1<HueBridgeConfiguration, Observable<Map<String, HueLight>>>() {
                    @Override
                    public Observable<Map<String, HueLight>> call(HueBridgeConfiguration hueBridgeConfiguration) {
                        return mSettingsManager.getHueLightsApiManager().getLights();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Map<String, HueLight>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        setErrorTextVisible(true, e.toString());
                        setConnectingProgressBarVisible(false);
                        setSearchLightsVisible(false);
                    }

                    @Override
                    public void onNext(Map<String, HueLight> hueLights) {
                        setConnectingProgressBarVisible(false);
                        if (hueLights == null || hueLights.isEmpty())
                            setErrorTextVisible(true, "no lights found"); //TODO empty lights message
                        else {
                            setErrorTextVisible(false, ""); //set lights list!
                            mLogger.debug("lights = " + hueLights.toString());
                        }
                        setSearchLightsVisible(true);
                    }
                });

    }

    public boolean getSearchLightsVisible () {
        return mSearchLightsVisible;
    }

    public void setSearchLightsVisible (boolean visible) {
        mSearchLightsVisible = visible;
        raisePropertyChanged("SearchLightsVisible");
    }

    public boolean canSearchForNewLights () {
        return true;
    }

    public void doSearchForNewLights () {
        //TODO use mgr internal lights subject so that we can refresh the list of lights from the manager
        mSettingsManager.getHueLightsApiManager().searchForNewLights(null).subscribe(new Action1<List<HueBridgeOperationResponse>>() {
            @Override
            public void call(List<HueBridgeOperationResponse> hueBridgeOperationResponse) {
                mLogger.debug("doSearchForNewLights hueBridgeOperationResponse = " + hueBridgeOperationResponse.toString());
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

    public String getErrorDescription () {
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

        if (mConfigSubscription != null) {
            mConfigSubscription.unsubscribe();
        }
    }

    public void setView(View view) {
        mView = view;
    }
}
