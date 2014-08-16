package com.alterego.ibeaconapp.app.viewmodels;

import android.view.View;
import android.widget.EditText;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.androidbound.ViewModel;
import com.alterego.ibeaconapp.app.MainApplication;
import com.alterego.ibeaconapp.app.R;
import com.alterego.ibeaconapp.app.fragments.ConnectUsernameFragment;
import com.alterego.ibeaconapp.app.hue.data.HueBridgeInfo;
import com.alterego.ibeaconapp.app.hue.data.responses.HueBridgeOperationResponse;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewModelConnectUsername extends ViewModel {

    private final ConnectUsernameFragment mConnectUsernameFragment;
    private SettingsManager mSettingsManager;
    private List<HueBridgeInfo> mHueBridges;
    private View mView;

    private boolean mConnectingProgressBarVisible = false;
    private boolean mErrorTextVisible;
    private String mErrorDescription;

    public ViewModelConnectUsername(ConnectUsernameFragment fragment) {
        mConnectUsernameFragment = fragment;
    }

    public boolean canConnectToBridgeWithUsername() {
        return true;
    }

    public void doConnectToBridgeWithUsername() {
        String username = "";
        setErrorTextVisible (false, "");

        if (mView != null) {
            EditText edittext_username = (EditText) mView.findViewById(R.id.edittext_username);
            username = edittext_username.getText().toString();
        }

        if (username != null && getSettingsManager().getHueBridgeApiManager() != null) {
            getLogger().info("doConnectToBridgeWithUsername username = " + username);
            setConnectingProgressBarVisible(true);

            getSettingsManager().getHueBridgeApiManager().getUserConnected(username)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<HueBridgeOperationResponse>>() {
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
                            getLogger().debug("doConnectToBridgeWithUsername onNext ");

                            if (responseList != null && responseList.size() > 0 && responseList.get(0).getSuccess().size() > 0 && responseList.get(0).getSuccess().containsKey("username")) {
                                String registered_username = responseList.get(0).getSuccess().get("username");
                                getLogger().debug("doConnectToBridgeWithUsername onNext registered_username = " + registered_username);
                                getSettingsManager().getHueBridgeManager().setLastHueBridgeUsername(registered_username);
                                mConnectUsernameFragment.dismiss();
                            } else if (responseList != null && responseList.size() > 0 && responseList.get(0).getError().size() > 0) {
                                getLogger().debug("doConnectToBridgeWithUsername onNext Error = " + responseList.get(0).getError().get("description"));
                                setErrorTextVisible (true, responseList.get(0).getError().get("description"));
                            } else {
                                getLogger().debug("doConnectToBridgeWithUsername onNext empty");
                                setErrorTextVisible(true, mSettingsManager.getParentApplication().getString(R.string.dialog_usernameconnect_error));
                            }

                        }
                    });
        }
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
