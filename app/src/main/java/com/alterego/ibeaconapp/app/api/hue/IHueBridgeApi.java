package com.alterego.ibeaconapp.app.api.hue;

import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeConfiguration;
import com.alterego.ibeaconapp.app.api.hue.requests.ConnectUserRequest;
import com.alterego.ibeaconapp.app.api.hue.requests.HueBridgeConfigurationRequest;
import com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import rx.Observable;

public interface IHueBridgeApi {

    /**
     * This call tries to register the user with the bridge. Creates a new user. The link button on the bridge must be pressed and this command executed within 30 seconds.
     * Once a new user has been created, the user key is added to a ‘whitelist’, allowing access to API commands that require a whitelisted user.
     * At present, all other API commands require a whitelisted user.
     * We ask that published apps use the name of their app as the devicetype.
     *
     * @param request {@link ConnectUserRequest} ConnectUserRequest with the username to be registered with the bridge
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse} Contains a list with a single item that details whether the user was added
     * successfully along with the username parameter. Response can contain an error or a success map with params inside.
     * Note: If the requested username already exists then the response will report a success.
     */
    @POST("/api")
    Observable<List<HueBridgeOperationResponse>> getUserConnected(@Body ConnectUserRequest request);

    /**
     * This call unregisters the user with the bridge.
     *
     * @param username  Username registered with the bridge
     * @param usernameToRemove  Username to be unregistered with the bridge
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse} The response details whether the user was successfully removed from the whitelist.
     * Response can contain an error or a success map with params inside
     */
    @DELETE(" /api/{username}/config/whitelist/{usernameToRemove}")
    Observable<List<HueBridgeOperationResponse>> deleteUser(@Path("username") String username, @Path("usernameToRemove") String usernameToRemove);

    /**
     * This call fetches bridge configuration. Returns list of all configuration elements in the bridge. Note all times are stored in UTC.
     *
     * @param username  Username registered with the bridge
     * @return {@link HueBridgeConfiguration} {@link rx.Observable} Returns a bridge configuration object
     */
    @GET("/api/{username}/config")
    Observable<HueBridgeConfiguration> getBridgeConfig(@Path("username") String username);

    /**
     * This call allows the user to set some configuration values.
     *
     * @param username  Username registered with the bridge
     * @param request {@link HueBridgeConfigurationRequest} HueBridgeConfigurationRequest with the config parameters to be changed
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse} Response which can contain an error or a success map with params inside
     */
    @PUT("/api/{username}/config")
    Observable<List<HueBridgeOperationResponse>> putBridgeConfig(@Path("username") String username, @Body HueBridgeConfigurationRequest request);
}
