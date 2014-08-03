package com.alterego.ibeaconapp.app.hue.data.requests;

import com.alterego.ibeaconapp.app.MainApplication;
import com.alterego.ibeaconapp.app.R;

public class ConnectUserRequest {

    //devicetype should be the app name
    String devicetype = MainApplication.instance.getResources().getString(R.string.app_name);

    //username should be UUID but we don't want to use that
    String username = "";

    public ConnectUserRequest (String uname) {
        username = uname;
    }
}
