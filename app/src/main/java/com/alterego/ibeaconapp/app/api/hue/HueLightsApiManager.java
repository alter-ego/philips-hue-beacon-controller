package com.alterego.ibeaconapp.app.api.hue;

import com.alterego.ibeaconapp.app.api.hue.data.HueLight;
import com.alterego.ibeaconapp.app.api.hue.responses.HueLightsNewLightsResponse;
import com.alterego.ibeaconapp.app.interfaces.IDisposable;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import java.util.List;
import java.util.Map;

import lombok.experimental.Accessors;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.functions.Func1;

@Accessors(prefix = "m")
public class HueLightsApiManager implements IDisposable {

    private final SettingsManager mSettingsManager;
    private final IHueLightsApi mHueLightsApiService;

    public HueLightsApiManager(SettingsManager mgr, String bridgeIP) {
        mSettingsManager = mgr;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(mSettingsManager.getGson()))
                .setEndpoint("http://" + bridgeIP)
                .setErrorHandler(new HueApiErrorHandler(mSettingsManager.getLogger()))
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("HueLightsApiManager"))
                .build();

        mHueLightsApiService = restAdapter.create(IHueLightsApi.class);
    }

    /**
     * Gets a list of all lights that have been discovered by the bridge.
     * Returns a list of all lights in the system. As of 1.3 the full light resource is returned from the bridge.
     * Note: For bridge versions < 1.3 only the name and light identifier are returned.
     * If there are no lights in the system then the bridge will return an empty object, {}.
     *
     * @return {@link com.alterego.ibeaconapp.app.api.hue.data.HueLight} {@link rx.Observable} Returns a map with lights and ids
     */
    //public Observable<List<HueLight>> getLights (String username) {
    public Observable<Map<String, HueLight>> getLights () {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        mSettingsManager.getLogger().debug("HueLightsApiManager getLights username = " + username);
        return mHueLightsApiService.getLights(username);
    }

    /**
     * Gets a list of lights that were discovered the last time a search for new lights was performed.
     * The list of new lights is always deleted when a new search is started.
     *
     * @return {@link HueLightsNewLightsResponse} Returns a list of new lights and lastscan time
     */
    public Observable<HueLightsNewLightsResponse> getNewLights () {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        mSettingsManager.getLogger().debug("HueLightsApiManager getNewLights username = " + username);
        return mHueLightsApiService.getNewLights(username).map(new Func1<Map<String, Object>, HueLightsNewLightsResponse>() {
            @Override
            public HueLightsNewLightsResponse call(Map<String, Object> rawData) {
                return new HueLightsNewLightsResponse(rawData);
            }
        });

    }

    @Override
    public void dispose() {
        //do nothing
    }
}
