package com.alterego.ibeaconapp.app.api.hue.data;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class HueBridgeWhitelistEntry {

    @Getter @Setter @SerializedName("last use date") public String lastSeenDateTime;

    @Getter @Setter @SerializedName("create date") public String createdDateTime;

    @Getter @Setter @SerializedName("name") public String userName;
}
