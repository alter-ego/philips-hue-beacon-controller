
package com.alterego.ibeaconapp.app.api.hue.data;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public class HueLight {

    //The hardware model of the light. max length = 6
    @Getter @Setter private String modelid;

    //A unique, editable name given to the light. max length = 32
    @Getter @Setter private String name;

    //This parameter is reserved for future functionality.
    @Getter @Setter private Map<String, Object> pointsymbol;

    //Details the state of the light
    @Getter @Setter @SerializedName ("state") private HueLightState state;

    //An identifier for the software version running on the light. ax length = 8
    @Getter @Setter @SerializedName ("swversion") private String softwareVersion;

    //A fixed name describing the type of light e.g. “Extended color light”.
    @Getter @Setter private String type;

    //this checks if we have a light that supports color (Philips Hue) or just a white bulb (Philips Lux)
    //we just check if the HueLightState.getColormode() is not an empty string
    public Boolean hasColor() {
        return state!=null && state.getColormode()!=null && !state.getColormode().equals("");
    }

}
