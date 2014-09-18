package com.alterego.ibeaconapp.app.api.hue.responses;

import java.util.Map;

import lombok.Getter;
import lombok.ToString;

@ToString
public class HueBridgeOperationResponse {

    @Getter public Map<String, String> success;
    @Getter public Map<String, String> error;

    public boolean isOperationSuccessful () {
        return success != null;
    }

}
