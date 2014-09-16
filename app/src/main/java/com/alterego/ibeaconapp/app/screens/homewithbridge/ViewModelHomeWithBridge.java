package com.alterego.ibeaconapp.app.screens.homewithbridge;

import android.view.View;

import com.alterego.androidbound.ViewModel;
import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeConfiguration;
import com.alterego.ibeaconapp.app.managers.SettingsManager;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class ViewModelHomeWithBridge extends ViewModel {

    private final SettingsManager mSettingsManager;
    private View mView;
    private Subscription mConfigSubscription;
    private boolean mErrorTextVisible;
    private String mErrorDescription;

    public ViewModelHomeWithBridge(SettingsManager mgr) {
        mSettingsManager = mgr;
        mLogger = mgr.getLogger();
        mConfigSubscription = mSettingsManager.getHueBridgeManager().getConfigSubject().subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<HueBridgeConfiguration>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) {
                setErrorTextVisible(true, e.toString());
            }

            @Override
            public void onNext(HueBridgeConfiguration hueBridgeConfiguration) {
                if (hueBridgeConfiguration == null)
                    //setErrorTextVisible(true, mSettingsManager.getParentApplication().getString(R.string.dialog_usernameconnect_error));
                    setErrorTextVisible(true, "error connecting!");
                else {
                    //TODO should show lights list and start connecting to bluetooth!
                    setErrorTextVisible(false, "");
                }
            }
        });

        //TODO add lights subscription
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
