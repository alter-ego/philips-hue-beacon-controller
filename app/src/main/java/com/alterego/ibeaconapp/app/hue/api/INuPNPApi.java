package com.alterego.ibeaconapp.app.hue.api;


import com.alterego.ibeaconapp.app.hue.data.HueBridgeInfo;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

public interface INuPNPApi {

    @GET("/api/nupnp")
    Observable<List<HueBridgeInfo>> getNuPNPBridgeInfo();

}
