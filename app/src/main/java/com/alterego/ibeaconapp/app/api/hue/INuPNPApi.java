package com.alterego.ibeaconapp.app.api.hue;


import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeInfo;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

public interface INuPNPApi {
    /**
     * This is the call to Philips server to find local bridges.
     *
     * @return {@link HueBridgeInfo} It returns a HueBridgeInfo object list with data on bridges on the local network
     */
    @GET("/api/nupnp")
    Observable<List<HueBridgeInfo>> getNuPNPBridgeInfo();

}
