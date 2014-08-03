package com.alterego.ibeaconapp.app.hue.data;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class HueBridgeConfiguration {
//TODO
    //Editable name given to the bridge
    @Getter @Setter public String name;

    @Getter @Setter @SerializedName("ipaddress") public String IPAddress;

    @Getter @Setter @SerializedName("mac") public String macAddress;

    /****
     * proxyport 	uint16 	Port of the proxy being used by the bridge. If set to 0 then a proxy is not being used.
     UTC 	string 	Current time stored on the bridge.
     name 	string 4..16 	Name of the bridge. This is also its uPnP name, so will reflect the actual uPnP name after any conflicts have been resolved.
     swupdate 	object 	Contains information related to software updates.
     whitelist 	object 	An array of whitelisted user IDs.
     swversion 	string 	Software version of the bridge.
     proxyaddress 	string 0..40 	IP Address of the proxy server being used. A value of “none” indicates no proxy.
     mac 	string 	MAC address of the bridge.
     linkbutton 	bool 	Indicates whether the link button has been pressed within the last 30 seconds.
     ipaddress 	string 	IP address of the bridge.
     netmask 	string 	Network mask of the bridge.
     gateway 	string 	Gateway IP address of the bridge.
     dhcp 	bool 	Whether the IP address of the bridge is obtained with DHCP.
     portalservices 	bool 	This indicates whether the bridge is registered to synchronize data with a portal account.
     */


}
