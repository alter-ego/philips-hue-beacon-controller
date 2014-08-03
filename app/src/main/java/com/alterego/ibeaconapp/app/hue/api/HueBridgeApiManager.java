package com.alterego.ibeaconapp.app.hue.api;

import com.alterego.ibeaconapp.app.hue.data.requests.ConnectUserRequest;
import com.alterego.ibeaconapp.app.hue.data.responses.ConnectUserResponse;
import com.alterego.ibeaconapp.app.interfaces.IDisposable;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import java.util.List;

import lombok.experimental.Accessors;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observable;

@Accessors(prefix = "m")
public class HueBridgeApiManager implements IDisposable {

    private final SettingsManager mSettingsManager;
    private final IHueBridgeApi mHueBridgeApiService;

    public HueBridgeApiManager(SettingsManager mgr, String bridgeIP) {
        mSettingsManager = mgr;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(mSettingsManager.getGson()))
                .setEndpoint("http://" + bridgeIP)
                .setErrorHandler(new HueBridgeApiErrorHandler(mSettingsManager.getLogger()))
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("HueBridgeApiManager"))
                .build();

        mHueBridgeApiService = restAdapter.create(IHueBridgeApi.class);
    }

    public Observable<List<ConnectUserResponse>> getUserConnected (String username) {

        mSettingsManager.getLogger().debug("HueBridgeApiManager getUserConnected username = " + username);
        return mHueBridgeApiService.getUserConnected(new ConnectUserRequest(username));

    }

    @Override
    public void dispose() {
        //do nothing
    }
}
