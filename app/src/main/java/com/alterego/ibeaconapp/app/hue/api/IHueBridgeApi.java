package com.alterego.ibeaconapp.app.hue.api;

import com.alterego.ibeaconapp.app.hue.data.HueBridgeConfiguration;
import com.alterego.ibeaconapp.app.hue.data.requests.ConnectUserRequest;
import com.alterego.ibeaconapp.app.hue.data.requests.HueBridgeConfigurationRequest;
import com.alterego.ibeaconapp.app.hue.data.responses.HueBridgeOperationResponse;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import rx.Observable;

public interface IHueBridgeApi {

    /**
     * This call tries to register the user with the bridge. The call must be executed inside 30 seconds of the pressing
     * the link button on the bridge
     *
     * @param request {@link ConnectUserRequest} ConnectUserRequest with the username to be registered with the bridge
     * @return {@link com.alterego.ibeaconapp.app.hue.data.responses.HueBridgeOperationResponse} Response which can contain an error or a success map with params inside
     */
    @POST("/api")
    Observable<List<HueBridgeOperationResponse>> getUserConnected(@Body ConnectUserRequest request);

    /**
     * This call fetches bridge configuration
     *
     * @param username  Username registered with the bridge
     * @return {@link HueBridgeConfiguration} {@link rx.Observable} Returns a bridge configuration object
     */
    @GET("/api/{username}/config")
    Observable<HueBridgeConfiguration> getBridgeConfig(@Path("username") String username);

    /**
     * This call saves the bridge configuration
     *
     * @param username  Username registered with the bridge
     * @param request {@link HueBridgeConfigurationRequest} HueBridgeConfigurationRequest with the config parameters to be changed
     * @return {@link com.alterego.ibeaconapp.app.hue.data.responses.HueBridgeOperationResponse} Response which can contain an error or a success map with params inside
     */
    @PUT("/api/{username}/config")
    Observable<List<HueBridgeOperationResponse>> putBridgeConfig(@Path("username") String username, @Body HueBridgeConfigurationRequest request);
}
