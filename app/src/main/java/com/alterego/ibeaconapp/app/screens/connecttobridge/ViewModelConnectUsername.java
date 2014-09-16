package com.alterego.ibeaconapp.app.screens.connecttobridge;

import android.view.View;
import android.widget.EditText;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.androidbound.ViewModel;
import com.alterego.ibeaconapp.app.MainApplication;
import com.alterego.ibeaconapp.app.R;
import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeInfo;
import com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import java.util.List;
import java.util.UUID;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ViewModelConnectUsername extends ViewModel {

    private final ConnectUsernameFragment mConnectUsernameFragment;
    private SettingsManager mSettingsManager;
    private List<HueBridgeInfo> mHueBridges;
    private View mView;

    private boolean mConnectingProgressBarVisible = false;
    private boolean mErrorTextVisible = false;
    private String mErrorDescription;
    private Subscription bridgeResponseOkSubscription;
    private Subscription bridgeResponseErrorSubscription;
    private Subscription bridgeResponseEmptyOrNullSubscription;

    public ViewModelConnectUsername(ConnectUsernameFragment fragment) {
        mConnectUsernameFragment = fragment;
    }

    public boolean canConnectToBridgeWithUsername() {
        return true;
    }

    public void doConnectToBridgeWithUsername() {
        String username = UUID.randomUUID().toString();

        setErrorTextVisible(false, "");
        //then in the app to connect, save the username and then load the configuration

        if (getSettingsManager().getHueBridgeApiManager() != null) {
            getLogger().info("doConnectToBridgeWithUsername username = " + username);
            setConnectingProgressBarVisible(true);

            Observable<List<HueBridgeOperationResponse>> userConnectedObservable = getSettingsManager().getHueBridgeApiManager().getUserConnected(username)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            bridgeResponseOkSubscription = userConnectedObservable.filter(new Func1<List<HueBridgeOperationResponse>, Boolean>() {
                @Override
                public Boolean call(List<HueBridgeOperationResponse> responseList) {
                    return (responseList != null && responseList.size() > 0 && responseList.get(0).getSuccess().size() > 0 && responseList.get(0).getSuccess().containsKey("username"));
                }
            }).subscribe(bridgeResponseOkObserver);

            bridgeResponseErrorSubscription = userConnectedObservable.filter(new Func1<List<HueBridgeOperationResponse>, Boolean>() {
                @Override
                public Boolean call(List<HueBridgeOperationResponse> responseList) {
                    return (responseList != null && responseList.size() > 0 && responseList.get(0).getError().size() > 0);
                }
            }).subscribe(bridgeResponseErrorObserver);

            bridgeResponseEmptyOrNullSubscription = userConnectedObservable.filter(new Func1<List<HueBridgeOperationResponse>, Boolean>() {
                @Override
                public Boolean call(List<HueBridgeOperationResponse> responseList) {
                    return (responseList != null && responseList.size() > 0 && responseList.get(0).getSuccess().size() > 0 && responseList.get(0).getSuccess().containsKey("username"));
                }
            }).subscribe(bridgeResponseEmptyOrNullObserver);
        }
    }

    Action1<List<HueBridgeOperationResponse>> bridgeResponseOkObserver = new Action1<List<HueBridgeOperationResponse>>() {
        @Override
        public void call(List<HueBridgeOperationResponse> responseList) {
            setConnectingProgressBarVisible(false);
            String registered_username = responseList.get(0).getSuccess().get("username");
            getLogger().debug("doConnectToBridgeWithUsername onNext registered_username = " + registered_username);
            getSettingsManager().getHueBridgeManager().setLastHueBridgeUsername(registered_username);
            mConnectUsernameFragment.dismiss();
        }
    };

    Action1<List<HueBridgeOperationResponse>> bridgeResponseEmptyOrNullObserver = new Action1<List<HueBridgeOperationResponse>>() {
        @Override
        public void call(List<HueBridgeOperationResponse> responseList) {
            getLogger().debug("doConnectToBridgeWithUsername onNext empty");
            setErrorTextVisible(true, mSettingsManager.getParentApplication().getString(R.string.dialog_usernameconnect_error));
        }
    };

    Observer<List<HueBridgeOperationResponse>> bridgeResponseErrorObserver = new Observer<List<HueBridgeOperationResponse>>() {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            getLogger().warning("doConnectToBridgeWithUsername error = " + e.toString());
            setConnectingProgressBarVisible(false);
        }

        @Override
        public void onNext(List<HueBridgeOperationResponse> responseList) {
            setConnectingProgressBarVisible(false);
            getLogger().debug("doConnectToBridgeWithUsername onNext Error = " + responseList.get(0).getError().get("description"));
            setErrorTextVisible(true, responseList.get(0).getError().get("description"));
        }
    };

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

    public boolean getConnectingProgressBarVisible() {
        return mConnectingProgressBarVisible;
    }

    public void setConnectingProgressBarVisible(boolean visible) {
        mConnectingProgressBarVisible = visible;
        raisePropertyChanged("ConnectingProgressBarVisible");
    }

    @Override
    public void dispose() {
        super.dispose();

        if (bridgeResponseOkSubscription != null) {
            bridgeResponseOkSubscription.unsubscribe();
        }
        if (bridgeResponseErrorSubscription != null) {
            bridgeResponseErrorSubscription.unsubscribe();
        }
        if (bridgeResponseEmptyOrNullSubscription != null) {
            bridgeResponseEmptyOrNullSubscription.unsubscribe();
        }
    }

    public void setView(View view) {
        mView = view;
    }

    private SettingsManager getSettingsManager() {
        if (mSettingsManager == null)
            mSettingsManager = MainApplication.instance.getSettingsManager();

        return mSettingsManager;
    }

    private IAndroidLogger getLogger() {
        if (mLogger == null)
            mLogger = getSettingsManager().getLogger();

        return mLogger;
    }
}
