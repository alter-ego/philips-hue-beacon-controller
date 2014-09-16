package com.alterego.ibeaconapp.app.api.hue.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class HueBridgeConfiguration {

    //string 4..16, Editable name given to the bridge. This is also its uPnP name, so will reflect the actual uPnP name after any conflicts have been resolved.
    @Getter @Setter public String name;

    //Contains information related to software updates.
    @Getter @Setter @SerializedName("swupdate") public HueBridgeSoftwareUpdate bridgeSoftwareUpdate;

    //An array of whitelisted user IDs.
    @Getter @Setter @SerializedName("whitelist") public HashMap<String, HueBridgeWhitelistEntry> userWhitelistEntries;

    //The version of the hue API in the format <major>.<minor>.<patch>, for example 1.2.1
    @Getter @Setter @SerializedName("apiversion") public String currentApiVersion;

    //Software version of the bridge.
    @Getter @Setter @SerializedName("swversion") public String currentSoftwareVersion;

    //IP Address of the proxy server being used. A value of “none” indicates no proxy.
    @Getter @Setter @SerializedName("proxyaddress") public String proxyAddress;

    //Port of the proxy being used by the bridge. If set to 0 then a proxy is not being used.
    @Getter @Setter @SerializedName("proxyport") public short proxyPort;

    //Indicates whether the link button has been pressed within the last 30 seconds.
    @Getter @Setter @SerializedName("linkbutton") public boolean isLinkButtonPressed;

    @Getter @Setter @SerializedName("ipaddress") public String IPAddress;

    @Getter @Setter @SerializedName("mac") public String macAddress;

    //Network mask of the bridge.
    @Getter @Setter @SerializedName("netmask") public String networkMask;

    //Gateway IP address of the bridge.
    @Getter @Setter @SerializedName("gateway") public String gatewayIP;

    //Whether the IP address of the bridge is obtained with DHCP.
    @Getter @Setter @SerializedName("dhcp") public boolean isAddressObtainedViaDHCP;

    //This indicates whether the bridge is registered to synchronize data with a portal account.
    @Getter @Setter @SerializedName("portalservices") public boolean isBridgeRegisteredWithPortal;

    //This indicates whether the bridge is currently connected to the portal
    @Getter @Setter @SerializedName("portalconnection") public String bridgePortalConnectionState;

    //This gives us the portal state
    @Getter @Setter @SerializedName("portalstate") public HuePortalState bridgePortalState;

    //Current time stored on the bridge.
    @Getter @Setter @SerializedName("UTC") public String currentBridgeUTCTime;

    //The local time of the bridge. "none" if not available.
    @Getter @Setter @SerializedName("localtime") public String currentBridgeLocalTime;

    //Timezone of the bridge as OlsenIDs, like "Europe/Amsterdam" or "none" when not configured.
    @Getter @Setter @SerializedName("timezone") public String currentBridgeLocalTimezone;

    //The current wireless frequency channel used by the bridge. It can take values of 11, 15, 20,25 or 0 if undefined (factory new).
    @Getter @Setter @SerializedName("zigbeechannel") public String zigbeeChannel;

    public static final HueBridgeConfiguration Empty = new HueBridgeConfiguration();

    public boolean isEmpty () {
        return Empty.equals(this);
    }

}
