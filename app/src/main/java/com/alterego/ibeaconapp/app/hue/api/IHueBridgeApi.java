package com.alterego.ibeaconapp.app.hue.api;

import com.alterego.ibeaconapp.app.hue.data.requests.ConnectUserRequest;
import com.alterego.ibeaconapp.app.hue.data.responses.ConnectUserResponse;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.POST;
import rx.Observable;

public interface IHueBridgeApi {

    @POST("/api")
    Observable<List<ConnectUserResponse>> getUserConnected(@Body ConnectUserRequest request);
}
