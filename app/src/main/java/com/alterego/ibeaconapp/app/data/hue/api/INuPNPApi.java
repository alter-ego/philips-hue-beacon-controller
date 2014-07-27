package com.alterego.ibeaconapp.app.data.hue.api;


import com.alterego.ibeaconapp.app.data.hue.data.HueBridgeNuPNPInfo;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

public interface INuPNPApi {

    @GET("/api/nupnp")
    Observable<List<HueBridgeNuPNPInfo>> getNuPNPBridgeInfo();

}
