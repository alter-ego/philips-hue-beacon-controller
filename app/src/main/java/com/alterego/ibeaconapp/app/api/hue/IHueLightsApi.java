package com.alterego.ibeaconapp.app.api.hue;

import com.alterego.ibeaconapp.app.api.hue.data.HueLight;

import java.util.List;
import java.util.Map;

import retrofit.http.GET;
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

}
