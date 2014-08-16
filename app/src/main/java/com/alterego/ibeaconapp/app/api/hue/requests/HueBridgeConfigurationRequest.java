package com.alterego.ibeaconapp.app.api.hue.requests;

import com.alterego.ibeaconapp.app.api.hue.data.HueBridgeSoftwareUpdate;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class HueBridgeConfigurationRequest {

    //Editable name given to the bridge. This is also its uPnP name, so will reflect the actual uPnP name after any conflicts have been resolved.
    @Getter @Setter public String name;

    @Getter @Setter @SerializedName("ipaddress") public String IPAddress;

    //Port of the proxy being used by the bridge. If set to 0 then a proxy is not being used.
    @Getter @Setter @SerializedName("proxyport") public int proxyPort;

    //IP Address of the proxy server being used. A value of “none” indicates no proxy.
    @Getter @Setter @SerializedName("proxyaddress") public String proxyAddress;

    //Network mask of the bridge.
    @Getter @Setter @SerializedName("netmask") public String networkMask;

    //Gateway IP address of the bridge.
    @Getter @Setter @SerializedName("gaeway") public String gatewayIP;

    //Whether the IP address of the bridge is obtained with DHCP.
    @Getter @Setter @SerializedName("dhcp") public boolean isAddressObtainedViaDHCP;

    //Indicates whether the link button has been pressed within the last 30 seconds.
    @Getter @Setter @SerializedName("linkbutton") public boolean isLinkButtonPressed;

    //This indicates whether the bridge is registered to synchronize data with a portal account.
    @Getter @Setter @SerializedName("portalservices") public boolean isBridgeRegisteredWithPortal;

    //Contains information related to software updates.
    @Getter @Setter @SerializedName("swupdate") public ArrayList<HueBridgeSoftwareUpdate> bridgeSoftwareUpdates;

}
