package com.alterego.ibeaconapp.app.api.hue.requests;

import com.alterego.ibeaconapp.app.MainApplication;
import com.alterego.ibeaconapp.app.R;

public class ConnectUserRequest {

    //string 0..40 <application_name>#<devicename>
    //application_name string 0..20, devicename string 0..19
    //(Example: hue#iphone peter )
    String devicetype = MainApplication.instance.getResources().getString(R.string.app_name) + android.os.Build.MODEL;

    //string 10..40 A username. If this is not provided, a random key will be generated and returned in the response.
    //It is recommended that a unique identifier for the device be used as the username
    String username = "";

    public ConnectUserRequest (String uname) {
        username = uname;
    }
}
