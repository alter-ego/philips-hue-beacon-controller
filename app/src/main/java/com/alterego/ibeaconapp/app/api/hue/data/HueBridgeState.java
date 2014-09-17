package com.alterego.ibeaconapp.app.api.hue.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class HueBridgeState {

    //All configuration settings.
    @Getter @Setter @SerializedName("config") public HueBridgeConfiguration bridgeConfiguration;

    //A collection of all lights and their attributes.
    @Getter @Setter @SerializedName("lights") public ArrayList<HashMap<String,HueLight>> bridgeLights;

    //A collection of all groups and their attributes.
    @Getter @Setter @SerializedName("groups") public ArrayList<HashMap<String,Object>> bridgeGroups; //TODO bridgeGroups

    //A collection of all schedules and their attributes.
    @Getter @Setter @SerializedName("schedules") public ArrayList<HashMap<String,Object>> bridgeSchedules; //TODO bridgeSchedules

}
