package com.alterego.ibeaconapp.app.hue.data.responses;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class ConnectUserResponse {

    @Getter public Map<String, String> success = new HashMap<String, String>();
    @Getter public Map<String, String> error = new HashMap<String, String>();

}
