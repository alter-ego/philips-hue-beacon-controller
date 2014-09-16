package com.alterego.ibeaconapp.app.managers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.alterego.ibeaconapp.app.screens.homewithbridge.FragmentHomeWithBridge;
import com.alterego.ibeaconapp.app.screens.homewithoutbridge.FragmentHomeWithoutBridge;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class BeaconFragmentFactory {

    private final SettingsManager mSettingsManager;
    private int mMainContainerId;

    public BeaconFragmentFactory(SettingsManager mgr, int main_container_id) {
        mSettingsManager = mgr;
        mMainContainerId = main_container_id;
    }

    public Fragment getFragmentForPosition(int menu_position) {
        Fragment return_fragment;

        if (!mSettingsManager.getHueBridgeManager().isHueBridgeConnected())
            return_fragment = FragmentHomeWithoutBridge.newInstance();
        else {
            switch (menu_position) {
                case 0:
                    return_fragment = FragmentHomeWithBridge.newInstance();
                    break;
                default:
                    return_fragment = FragmentHomeWithBridge.newInstance();
            }
        }

        mSettingsManager.getLogger().debug("ReaderFragmentFactory getFragmentForPosition menu_position = " + menu_position + ", fragment = " + return_fragment.toString());
        return return_fragment;
    }


    private void replaceFragmentInMainContainer(Fragment fragment) {
        if (mSettingsManager.getParentActivity() != null) {
            FragmentManager fragmentManager = ((ActionBarActivity) mSettingsManager.getParentActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(mMainContainerId, fragment)
                    .commit();
        }
    }

    public void replaceFragmentInMainContainer(int menu_position) {
        Fragment fragment = getFragmentForPosition(menu_position);
        replaceFragmentInMainContainer(fragment);
    }

}
