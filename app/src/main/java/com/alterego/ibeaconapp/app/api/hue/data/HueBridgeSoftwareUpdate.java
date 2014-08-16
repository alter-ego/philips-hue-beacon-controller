package com.alterego.ibeaconapp.app.api.hue.data;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class HueBridgeSoftwareUpdate {

    @Getter @Setter @SerializedName("updatestate") public int updateState;

    @Getter @Setter @SerializedName("url") public String updateUrl;

    @Getter @Setter @SerializedName("text") public String updateTextDescription;

    @Getter @Setter @SerializedName("notify") public boolean shouldNotify;
}
