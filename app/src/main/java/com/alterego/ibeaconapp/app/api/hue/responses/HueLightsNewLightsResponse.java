package com.alterego.ibeaconapp.app.api.hue.responses;

import com.alterego.ibeaconapp.app.api.hue.data.HueLightName;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Map;

import lombok.Getter;
import lombok.ToString;

@ToString
public class HueLightsNewLightsResponse {

    @Getter public String lastscan = "";
    @Getter public Map<String, HueLightName> newLights;

    public HueLightsNewLightsResponse(Map<String, Object> rawData) {
        newLights = new LinkedTreeMap<String, HueLightName>();

        if (rawData.containsKey("lastscan")) {
            lastscan = (String) rawData.get("lastscan");
            rawData.remove("lastscan");
        }

        for (Map.Entry<String, Object> entry : rawData.entrySet()) {
            HueLightName value = (HueLightName) entry.getValue();
            newLights.put(entry.getKey(), value);
        }

    }

}
