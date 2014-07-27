package com.alterego.ibeaconapp.app.data.hue.api;

import com.alterego.ibeaconapp.app.R;
import com.alterego.ibeaconapp.app.SettingsManager;
import com.alterego.ibeaconapp.app.data.hue.data.HueBridgeNuPNPInfo;
import com.alterego.ibeaconapp.app.interfaces.IDisposable;

import java.util.List;

import lombok.experimental.Accessors;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observable;

@Accessors(prefix = "m")
public class NuPNPApiManager implements IDisposable {

    private final SettingsManager mSettingsManager;
    private final INuPNPApi mNuPNPApiService;

    public NuPNPApiManager(SettingsManager mgr) {
        mSettingsManager = mgr;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(mSettingsManager.getGson()))
                .setEndpoint(mSettingsManager.getParentApplication().getResources().getString(R.string.nupnp_server_url))
                .setErrorHandler(new NuPNPApiErrorHandler(mSettingsManager.getLogger()))
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("LifesumApiManager"))
                .build();

        mNuPNPApiService = restAdapter.create(INuPNPApi.class);
    }

    public Observable<List<HueBridgeNuPNPInfo>> findLocalBridges () {

        mSettingsManager.getLogger().debug("LifesumApiManager findLocalBridges");
        return mNuPNPApiService.getNuPNPBridgeInfo();

    }

    @Override
    public void dispose() {
        //do nothing
    }
}