package com.alterego.ibeaconapp.app;

public class Settings {

    /**
     * The default duration in milliseconds of the bluetooth scan cycle
     */
    public static final long DEFAULT_FOREGROUND_SCAN_PERIOD = 1100;
    /**
     * The default duration in milliseconds spent not scanning between each bluetooth scan cycle
     */
    public static final long DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD = 100;
    /**
     * The default duration in milliseconds of the bluetooth scan cycle when no ranging/monitoring clients are in the foreground
     */
    public static final long DEFAULT_BACKGROUND_SCAN_PERIOD = 2500;
    /**
     * The default duration in milliseconds spent not scanning between each bluetooth scan cycle when no ranging/monitoring clients are in the foreground
     */
    public static final long DEFAULT_BACKGROUND_BETWEEN_SCAN_PERIOD = 1*60*1000;

}
