package com.alterego.ibeaconapp.app.api.hue;

import com.alterego.ibeaconapp.app.api.hue.data.HueLight;
import com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse;

import java.util.List;
import java.util.Map;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

public interface IHueLightsApi {

    /**
     * Gets a list of all lights that have been discovered by the bridge.
     * Returns a list of all lights in the system. As of 1.3 the full light resource is returned from the bridge.
     * Note: For bridge versions < 1.3 only the name and light identifier are returned.
     * If there are no lights in the system then the bridge will return an empty object, {}.
     *
     * @param username Username registered with the bridge
     * @return {@link com.alterego.ibeaconapp.app.api.hue.data.HueLight} {@link rx.Observable} Returns a list of lights
     */
    @GET("/api/{username}/lights")
//    Observable<List<HueLight>> getLights(@Path("username") String username);
    Observable<Map<String, HueLight>> getLights(@Path("username") String username);

    /**
     * Gets a list of lights that were discovered the last time a search for new lights was performed.
     * The list of new lights is always deleted when a new search is started.
     *
     * @param username Username registered with the bridge
     * @return Returns a map that needs to be transformed into the response
     */
    @GET("/api/{username}/lights/new")
    Observable<Map<String, Object>> getNewLights(@Path("username") String username);

    /**
     * Starts a search for new lights. As of 1.3 will also find switches (e.g. "tap")
     * The bridge will search for 1 minute and will add a maximum of 15 new lights. To add further lights,
     * the command needs to be sent again after the search has completed. If a search is already active,
     * it will be aborted and a new search will start.
     * When the search has finished, new lights will be available using the get new lights command.
     * In addition, the new lights will now be available by calling get all lights or by calling get group attributes on group 0.
     * Group 0 is a special group that cannot be deleted and will always contain all lights known by the bridge.
     *
     * For API 1.1 and above.
     *
     * @param username Username registered with the bridge
     * @param devicesToStartSearching List of devices to start search on
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse}
     * The response details whether the search was successfully started.
     * Response can contain an error or a success map with params inside
     */
    @POST("/api/{username}/lights")
    Observable<List<HueBridgeOperationResponse>> searchForNewLights(@Path("username") String username, @Body Map<String, List<String>> devicesToStartSearching);

    /**
     * Starts a search for new lights. As of 1.3 will also find switches (e.g. "tap")
     * The bridge will search for 1 minute and will add a maximum of 15 new lights. To add further lights,
     * the command needs to be sent again after the search has completed. If a search is already active,
     * it will be aborted and a new search will start.
     * When the search has finished, new lights will be available using the get new lights command.
     * In addition, the new lights will now be available by calling get all lights or by calling get group attributes on group 0.
     * Group 0 is a special group that cannot be deleted and will always contain all lights known by the bridge.
     *
     * For API 1.0.
     *
     * @param username Username registered with the bridge
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse}
     * The response details whether the search was successfully started.
     * Response can contain an error or a success map with params inside
     */
    @POST("/api/{username}/lights")
    Observable<List<HueBridgeOperationResponse>> searchForNewLights(@Path("username") String username);

}
