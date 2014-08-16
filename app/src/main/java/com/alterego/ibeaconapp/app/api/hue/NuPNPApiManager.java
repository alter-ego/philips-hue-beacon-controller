package com.alterego.ibeaconapp.app.api.hue;

import com.alterego.ibeaconapp.app.R;
import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeInfo;
import com.alterego.ibeaconapp.app.interfaces.IDisposable;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

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
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("NuPNPApiManager"))
                .build();

        mNuPNPApiService = restAdapter.create(INuPNPApi.class);
    }

    /**
     * This is the call to Philips server to find local bridges.
     *
     * @return {@link HueBridgeInfo} It returns a HueBridgeInfo object list with data on bridges on the local network
     */
    public Observable<List<HueBridgeInfo>> findLocalBridges () {

        mSettingsManager.getLogger().debug("NuPNPApiManager findLocalBridges");
        return mNuPNPApiService.getNuPNPBridgeInfo();

    }

    @Override
    public void dispose() {
        //do nothing
    }
}
