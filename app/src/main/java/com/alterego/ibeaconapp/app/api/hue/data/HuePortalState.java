package com.alterego.ibeaconapp.app.api.hue.data;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

public class HuePortalState {

    @Getter @Setter @SerializedName("signedon") public boolean portalSignedOn;

    @Getter @Setter @SerializedName("incoming") public boolean portalIncoming;

    @Getter @Setter @SerializedName("outgoing") public boolean portalOutgoing;

    @Getter @Setter @SerializedName("communication") public String portalCommunicationState;

}
