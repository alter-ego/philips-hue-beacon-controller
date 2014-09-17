package com.alterego.ibeaconapp.app.api.hue;

import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeConfiguration;
import com.alterego.ibeaconapp.app.api.hue.requests.ConnectUserRequest;
import com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse;
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
        mSettingsManager.setHueLightsApiManager(new HueLightsApiManager(mSettingsManager, bridgeIP));


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(mSettingsManager.getGson()))
                .setEndpoint("http://" + bridgeIP)
                .setErrorHandler(new HueApiErrorHandler(mSettingsManager.getLogger()))
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("HueBridgeApiManager"))
                .build();

        mHueBridgeApiService = restAdapter.create(IHueBridgeApi.class);
    }

    /**
     * This call tries to register the user with the bridge. The call must be executed inside 30 seconds of the pressing
     * the link button on the bridge
     *
     * @param username String of the username to be registered with the bridge
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse} Response which can contain an error or a success map with params inside
     */
    public Observable<List<HueBridgeOperationResponse>> getUserConnected (String username) {

        mSettingsManager.getLogger().debug("HueBridgeApiManager getUserConnected username = " + username);
        return mHueBridgeApiService.getUserConnected(new ConnectUserRequest(username));

    }

    /**
     * This call fetches bridge configuration for the specified username registered with the bridge.
     *
     *  * @param username String of the username registered with the bridge
     * @return {@link com.alterego.ibeaconapp.app.api.hue.data.HueBridgeConfiguration} {@link rx.Observable} Returns a bridge configuration object
     */
    public Observable<HueBridgeConfiguration> getConfig(String username) {
        mSettingsManager.getLogger().debug("HueBridgeApiManager getConfigForUser username = " + username);
        return mHueBridgeApiService.getBridgeConfig(username);
    }

    @Override
    public void dispose() {
        //do nothing
    }
}
