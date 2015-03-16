package com.alterego.ibeaconapp.app.api.hue;

import com.alterego.ibeaconapp.app.api.hue.data.HueLight;
import com.alterego.ibeaconapp.app.api.hue.data.HueLightState;
import com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse;
import com.alterego.ibeaconapp.app.api.hue.responses.HueLightsNewLightsResponse;
import com.alterego.ibeaconapp.app.interfaces.IDisposable;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.experimental.Accessors;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.functions.Func1;

@Accessors(prefix = "m")
public class HueLightsApiManager implements IDisposable {

    private static final int PARAMNOTUSED = -1;
    private final SettingsManager mSettingsManager;
    private final IHueLightsApi mHueLightsApiService;

    public HueLightsApiManager(SettingsManager mgr, String bridgeIP) {
        mSettingsManager = mgr;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(mSettingsManager.getGson()))
                .setEndpoint("http://" + bridgeIP)
                .setErrorHandler(new HueApiErrorHandler(mSettingsManager.getLogger()))
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("HueLightsApiManager"))
                .build();

        mHueLightsApiService = restAdapter.create(IHueLightsApi.class);
    }

    /**
     * Gets a list of all lights that have been discovered by the bridge.
     * Returns a list of all lights in the system. As of 1.3 the full light resource is returned from the bridge.
     * Note: For bridge versions < 1.3 only the name and light identifier are returned.
     * If there are no lights in the system then the bridge will return an empty object, {}.
     *
     * @return {@link com.alterego.ibeaconapp.app.api.hue.data.HueLight} {@link rx.Observable} Returns a map with lights and ids
     */
    public Observable<Map<String, HueLight>> getLights() {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        mSettingsManager.getLogger().debug("HueLightsApiManager getLights username = " + username);
        return mHueLightsApiService.getLights(username);
    }

    /**
     * Gets a list of lights that were discovered the last time a search for new lights was performed.
     * The list of new lights is always deleted when a new search is started.
     *
     * @return {@link HueLightsNewLightsResponse} Returns a list of new lights and lastscan time
     */
    public Observable<HueLightsNewLightsResponse> getNewLights() {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        mSettingsManager.getLogger().debug("HueLightsApiManager getNewLights username = " + username);
        return mHueLightsApiService.getNewLights(username).map(new Func1<Map<String, Object>, HueLightsNewLightsResponse>() {
            @Override
            public HueLightsNewLightsResponse call(Map<String, Object> rawData) {
                return new HueLightsNewLightsResponse(rawData);
            }
        });

    }

    //TODO add internal lights subject so that we can refresh the list of lights from the manager
    //TODO add filter for success, add 60+ seconds delay and get a list of lights again
    public Observable<List<HueBridgeOperationResponse>> searchForNewLights(List<String> devicesToStartSearchOn) {
        HashMap<String, List<String>> devices = new HashMap<String, List<String>>();
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        if (devicesToStartSearchOn != null && devicesToStartSearchOn.size() > 0 && mSettingsManager.getHueBridgeManager().getLastHueBridgeConfiguration().isCurrentApiAbove("1.1")) {
            devices.put("deviceid", devicesToStartSearchOn);
            mSettingsManager.getLogger().debug("HueLightsApiManager searchForNewLights username = " + username + ", devices = " + devicesToStartSearchOn.toString());
            return mHueLightsApiService.searchForNewLights(username, devices);
        } else {
            mSettingsManager.getLogger().debug("HueLightsApiManager searchForNewLights username = " + username);
            return mHueLightsApiService.searchForNewLights(username);
        }
    }

    /**
     * Gets the attributes and state of a given light.
     *
     * @param light_id Id of the light
     * @return {@link com.alterego.ibeaconapp.app.api.hue.data.HueLight} {@link rx.Observable} Returns the light info and state
     */
    public Observable<HueLight> getLight(String light_id) {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        mSettingsManager.getLogger().debug("HueLightsApiManager getLight light id = " + light_id + ", username = " + username);
        return mHueLightsApiService.getLight(username, light_id);
    }

    /**
     * Used to rename lights. A light can have its name changed when in any state, including when it is unreachable or off.
     *
     * @param light_id       Id of the light
     * @param new_light_name The new name for the light. If the name is already taken a space and number will be appended
     *                       by the bridge e.g. “Bedroom Light 1”. [ max 32 chars ]
     * @return String {@link rx.Observable} Returns the set light name or null if unsuccessful.
     */
    public Observable<String> renameLight(String light_id, final String new_light_name) {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        HashMap<String, String> lightName = new HashMap<String, String>();
        lightName.put("name", new_light_name);
        mSettingsManager.getLogger().debug("HueLightsApiManager renameLight light id = " + light_id + " , new light name = " + new_light_name + ", username = " + username);

        //here we check the response and if it contains "Updated" returned when the name is too long, we return the requested name.
        return mHueLightsApiService.renameLight(username, light_id, lightName).
                map(new Func1<HueBridgeOperationResponse, String>() {
                    @Override
                    public String call(HueBridgeOperationResponse hueBridgeOperationResponse) {
                        if (hueBridgeOperationResponse.isOperationSuccessful()) {
                            String returned_name = hueBridgeOperationResponse.success.values().iterator().next();
                            if (returned_name.contains("Updated"))
                                return new_light_name;
                            else
                                return returned_name;
                        } else
                            return null;
                    }
                });
    }

    /**
     * Allows the user to turn the light on and off, modify the hue and effects.
     * A light cannot have its hue, saturation, brightness, effect, ct or xy modified when it is turned off. Doing so will return error 201.
     * There are 3 methods available to set the color of the light – hue and saturation (hs), xy or color temperature (ct).
     * If multiple methods are used then a priority is used: xy > ct > hs. All included parameters will be updated but the ‘colormode’ will be set using the priority system.
     *
     * @param light_id    Id of the light
     * @param light_state {@link com.alterego.ibeaconapp.app.api.hue.data.HueLightState} light state with all the changes we want to make
     * @return {@link HueLightsNewLightsResponse} Returns a list of set parameters for the light
     */

    public Observable<HueBridgeOperationResponse> setLightState(String light_id, HueLightState light_state) {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        HashMap<String, Object> lightState = new HashMap<String, Object>();
        lightState.put("on", light_state.isLightOn());
        if (light_state.isLightOn()) {
            if (light_state.getBrightness() != PARAMNOTUSED)
                lightState.put("bri", light_state.getBrightness());
            if (light_state.getHue() != PARAMNOTUSED) lightState.put("hue", light_state.getHue());
            if (light_state.getSaturation() != PARAMNOTUSED)
                lightState.put("sat", light_state.getSaturation());
            if (light_state.getCieColorSpaceCoordinates() != null)
                lightState.put("xy", light_state.getCieColorSpaceCoordinates());
            if (light_state.getColorTemperature() != PARAMNOTUSED)
                lightState.put("ct", light_state.getColorTemperature());
            lightState.put("effect", light_state.getEffect().getValue());
        }

        lightState.put("alert", light_state.getAlert().getValue());

        if (light_state.getTransitiontime() != PARAMNOTUSED)
            lightState.put("transitiontime", light_state.getTransitiontime());

        mSettingsManager.getLogger().debug("HueLightsApiManager renameLight light id = " + light_id + " , new light state = " + lightState.toString() + ", username = " + username);

        return mHueLightsApiService.setLightState(username, light_id, lightState);
    }

    /**
     * Allows the user to turn the light on and off.
     *
     * @param light_id Id of the light
     * @param light_on New light state (boolean)
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse}
     * The response details whether the light state was successfully changed.
     * Response can contain an error or a success map with params inside
     */

    public Observable<HueBridgeOperationResponse> turnLightOnOrOff(String light_id, boolean light_on) {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        HashMap<String, Object> lightState = new HashMap<String, Object>();
        lightState.put("on", light_on);
        mSettingsManager.getLogger().debug("HueLightsApiManager turnLightOnOrOff light id = " + light_id + " , new light state = " + light_on + ", username = " + username);

        return mHueLightsApiService.setLightState(username, light_id, lightState);
    }

    /**
     * Allows the user to set the light brightness.
     *
     * @param light_id   Id of the light
     * @param brightness The brightness value to set the light toBrightness is a scale from 0
     *                   (the minimum the light is capable of) to 255 (the maximum). Note: a brightness of 0 is not off.
     *                   Set it to -1 if don't want to change the parameter.
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse}
     * The response details whether the light state was successfully changed.
     * Response can contain an error or a success map with params inside.
     */

    public Observable<HueBridgeOperationResponse> setLightBrightness(String light_id, int brightness) {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        HashMap<String, Object> lightState = new HashMap<String, Object>();
        if (brightness != PARAMNOTUSED) lightState.put("bri", brightness);
        mSettingsManager.getLogger().debug("HueLightsApiManager turnLightOnOrOff light id = " + light_id + ", brightness = " + brightness + ", username = " + username);

        return mHueLightsApiService.setLightState(username, light_id, lightState);
    }

    /**
     * Allows the user to set the light color using Hue/Saturation pair.
     *
     * @param light_id   Id of the light
     * @param hue        The hue value to set light to.
     *                   The hue value is a wrapping value between 0 and 65535. Both 0 and 65535 are red, 25500 is green and 46920 is blue.
     *                   Set it to -1 if don't want to change the parameter.
     * @param saturation Saturation of the light. 255 is the most saturated (colored) and 0 is the least saturated (white).
     *                   Set it to -1 if don't want to change the parameter.
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse}
     * The response details whether the light state was successfully changed.
     * Response can contain an error or a success map with params inside.
     */

    public Observable<HueBridgeOperationResponse> setLightColor(String light_id, int hue, int saturation) {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        HashMap<String, Object> lightState = new HashMap<String, Object>();
        if (hue != PARAMNOTUSED) lightState.put("hue", hue);
        if (saturation != PARAMNOTUSED) lightState.put("sat", saturation);
        mSettingsManager.getLogger().debug("HueLightsApiManager turnLightOnOrOff light id = " + light_id + ", hue = " + hue + ", saturation = " + saturation + ", username = " + username);

        return mHueLightsApiService.setLightState(username, light_id, lightState);
    }

    /**
     * Allows the user to set the light color using XY coordinates in the CIE color space.
     * If the specified coordinates are not in the CIE color space, the closest color to the coordinates will be chosen.
     *
     * @param light_id          Id of the light
     * @param CIE_color_space_x Must be between 0 and 1.
     * @param CIE_color_space_y Must be between 0 and 1.
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse}
     * The response details whether the light state was successfully changed.
     * Response can contain an error or a success map with params inside.
     */

    public Observable<HueBridgeOperationResponse> setLightColor(String light_id, double CIE_color_space_x, double CIE_color_space_y) {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        HashMap<String, Object> lightState = new HashMap<String, Object>();
        lightState.put("xy", new ArrayList<Double>(Arrays.asList(CIE_color_space_x, CIE_color_space_y)));
        mSettingsManager.getLogger().debug("HueLightsApiManager turnLightOnOrOff light id = " + light_id
                + ", CIE_color_space_x = " + CIE_color_space_x + ", CIE_color_space_y = " + CIE_color_space_y + ", username = " + username);

        return mHueLightsApiService.setLightState(username, light_id, lightState);
    }

    /**
     * Allows the user to set the light color using Mired Color temperature. 2012 connected lights are capable of 153 (6500K) to 500 (2000K)
     *
     * @param light_id          Id of the light
     * @param color_temperature Mired Color temperature. 2012 connected lights are capable of 153 (6500K) to 500 (2000K)
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse}
     * The response details whether the light state was successfully changed.
     * Response can contain an error or a success map with params inside.
     */

    public Observable<HueBridgeOperationResponse> setLightColor(String light_id, int color_temperature) {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        HashMap<String, Object> lightState = new HashMap<String, Object>();
        lightState.put("ct", color_temperature);
        mSettingsManager.getLogger().debug("HueLightsApiManager turnLightOnOrOff light id = " + light_id + ", color_temperature = " + color_temperature + ", username = " + username);

        return mHueLightsApiService.setLightState(username, light_id, lightState);
    }

    /**
     * Allows the user to set the light alert state. The alert effect, is a temporary change to the bulb’s state, and has one of the following values of
     * {@link com.alterego.ibeaconapp.app.api.hue.data.HueLightState.ALERT_STATE}:
     * NONE – The light is not performing an alert effect.
     * SINGLE – The light is performing one breathe cycle.
     * MULTIPLE – The light is performing breathe cycles for 30 seconds or until an "alert": "none" command is received.
     *
     * @param alert_state {@link com.alterego.ibeaconapp.app.api.hue.data.HueLightState.ALERT_STATE}  Alert state of the light
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse}
     * The response details whether the light state was successfully changed.
     * Response can contain an error or a success map with params inside.
     */

    public Observable<HueBridgeOperationResponse> setLightAlert(String light_id, HueLightState.ALERT_STATE alert_state) {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        HashMap<String, Object> lightState = new HashMap<String, Object>();
        lightState.put("alert", alert_state.getValue());
        mSettingsManager.getLogger().debug("HueLightsApiManager turnLightOnOrOff light id = " + light_id + ", alert = " + alert_state.getValue() + ", username = " + username);

        return mHueLightsApiService.setLightState(username, light_id, lightState);
    }

    /**
     * Allows the user to set the The dynamic effect of the light.
     * Setting the effect to colorloop will cycle through all hues using the current brightness and saturation settings.
     * Effect has one of the following values of {@link com.alterego.ibeaconapp.app.api.hue.data.HueLightState.LIGHT_EFFECT}:
     * NONE – The light is not performing any effects.
     * COLORLOOP – The light will cycle through all hues using the current brightness and saturation settings.
     *
     * @param light_effect {@link com.alterego.ibeaconapp.app.api.hue.data.HueLightState.LIGHT_EFFECT}  Light effect
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse}
     * The response details whether the light state was successfully changed.
     * Response can contain an error or a success map with params inside.
     */

    public Observable<HueBridgeOperationResponse> setLightEffect(String light_id, HueLightState.LIGHT_EFFECT light_effect) {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        HashMap<String, Object> lightState = new HashMap<String, Object>();
        lightState.put("effect", light_effect.getValue());
        mSettingsManager.getLogger().debug("HueLightsApiManager turnLightOnOrOff light id = " + light_id + ", effect = " + light_effect.getValue() + ", username = " + username);

        return mHueLightsApiService.setLightState(username, light_id, lightState);
    }

    /**
     * The duration of the transition from the light’s current state to the new state.
     * This is given as a multiple of 100ms and defaults to 4 (400ms).
     *
     * @param light_id        Id of the light
     * @param transition_time multiple of 100ms value for the transition time
     * @return {@link com.alterego.ibeaconapp.app.api.hue.responses.HueBridgeOperationResponse}
     * The response details whether the light state was successfully changed.
     * Response can contain an error or a success map with params inside.
     */

    public Observable<HueBridgeOperationResponse> setLightTransitionTime(String light_id, int transition_time) {
        String username = mSettingsManager.getHueBridgeManager().getLastHueBridgeUsername();
        HashMap<String, Object> lightState = new HashMap<String, Object>();
        lightState.put("transitiontime", transition_time);
        mSettingsManager.getLogger().debug("HueLightsApiManager turnLightOnOrOff light id = " + light_id + ", transition_time (ms) = " + transition_time * 100 + ", username = " + username);

        return mHueLightsApiService.setLightState(username, light_id, lightState);
    }

    @Override
    public void dispose() {
        //do nothing
    }
}
