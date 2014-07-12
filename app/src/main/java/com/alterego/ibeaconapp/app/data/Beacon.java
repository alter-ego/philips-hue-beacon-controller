package com.alterego.ibeaconapp.app.data;

import com.radiusnetworks.ibeacon.IBeacon;

import lombok.ToString;

@ToString
public class Beacon extends IBeacon {

    protected String proximityUuid;
    /**
     * A 16 bit integer typically used to represent a group of iBeacons
     */
    protected int major;
    /**
     * A 16 bit integer that identifies a specific iBeacon within a group
     */
    protected int minor;
    /**
     * An integer with four possible values representing a general idea of how far the iBeacon is away
     * @see #PROXIMITY_IMMEDIATE
     * @see #PROXIMITY_NEAR
     * @see #PROXIMITY_FAR
     * @see #PROXIMITY_UNKNOWN
     */
    protected Integer proximity;
    /**
     * A double that is an estimate of how far the iBeacon is away in meters. This name is confusing, but is copied from
     * the iOS7 SDK terminology. Note that this number fluctuates quite a bit with RSSI, so despite the name, it is not
     * super accurate. It is recommended to instead use the proximity field, or your own bucketization of this value.
     */
    protected Double accuracy;
    /**
     * The measured signal strength of the Bluetooth packet that led do this iBeacon detection.
     */
    protected int rssi;
    /**
     * The calibrated measured Tx power of the iBeacon in RSSI
     * This value is baked into an iBeacon when it is manufactured, and
     * it is transmitted with each packet to aid in the distance estimate
     */
    protected int txPower;

    /**
     * The bluetooth mac address
     */
    protected String bluetoothAddress;

    public Beacon(IBeacon otherIBeacon) {
        this.major = otherIBeacon.getMajor();
        this.minor = otherIBeacon.getMinor();
        this.accuracy = otherIBeacon.getAccuracy();
        this.proximity = otherIBeacon.getProximity();
        this.rssi = otherIBeacon.getRssi();
        this.proximityUuid = otherIBeacon.getProximityUuid();
        this.txPower = otherIBeacon.getTxPower();
        this.bluetoothAddress = otherIBeacon.getBluetoothAddress();
    }

}
