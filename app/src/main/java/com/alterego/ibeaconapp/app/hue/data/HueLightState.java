
package com.alterego.ibeaconapp.app.hue.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*NOTES:
A light cannot have its hue, saturation, brightness, effect, ct or xy modified when it is turned off. Doing so will return error 201.

There are 3 methods available to set the color of the light – hue and saturation (hs), xy or color temperature (ct).
If multiple methods are used then a priority is used: xy > ct > hs. All included parameters will be updated but the ‘colormode’ will be set using the priority system.
*/

public class HueLightState {

    /*The alert effect, which is a temporary change to the bulb’s state. This can take one of the following values:
    “none” – The light is not performing an alert effect.
    “select” – The light is performing one breathe cycle.
    “lselect” – The light is performing breathe cycles for 30 seconds or until an "alert": "none" command is received.

    Note that in version 1.0 this contains the last alert sent to the light and not its current state. This will be changed to contain the current state in an upcoming patch.
    */
   	@Getter @Setter private String alert;

    //Brightness of the light. This is a scale from the minimum brightness the light is capable of, 0, to the maximum capable brightness, 255. Note a brightness of 0 is not off.
    @Getter @Setter @SerializedName("bri") private int brightness;

    //Indicates the color mode in which the light is working, this is the last command type it received.
    // Values are “hs” for Hue and Saturation, “xy” for XY and “ct” for Color Temperature. This parameter is only present when the light supports at least one of the values.
    @Getter @Setter private String colormode = "";

    //The Mired Color temperature of the light. 2012 connected lights are capable of 153 (6500K) to 500 (2000K).
    @Getter @Setter @SerializedName("ct") private int colorTemperature;

    //The dynamic effect of the light, can either be “none” or “colorloop”.
    //If set to colorloop, the light will cycle through all hues using the current brightness and saturation settings.
    @Getter @Setter private String effect;

    //Hue of the light. This is a wrapping value between 0 and 65535. Both 0 and 65535 are red, 25500 is green and 46920 is blue.
    @Getter @Setter private int hue;

    //On/Off state of the light. On=true, Off=false
    @Getter @Setter @SerializedName("on") private boolean isLightOn;

    //Indicates if a light can be reached by the bridge.
    @Getter @Setter @SerializedName("reachable") private boolean isLightReachable;

    //Saturation of the light. 255 is the most saturated (colored) and 0 is the least saturated (white).
    @Getter @Setter @SerializedName("sat") private int saturation;

    //The x and y coordinates of a color in CIE color space.
    //The first entry is the x coordinate and the second entry is the y coordinate. Both x and y are between 0 and 1.
    @Getter @Setter @SerializedName("xy") private List<Double> cieColorSpaceCoordinates; //

    /********************* ONLY WHEN SETTING ***********************/

    //The duration of the transition from the light’s current state to the new state.
    // This is given as a multiple of 100ms and defaults to 4 (400ms). For example, setting transistiontime: 10 will make the transition last 1 second.
    @Getter @Setter private int transitiontime;

}
