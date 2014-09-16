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
    @Getter
    private ArrayList<String> mMenuItemTitles;

    private int mMainContainerId;

    public BeaconFragmentFactory(SettingsManager mgr, int main_container_id) {
        mSettingsManager = mgr;
        mMainContainerId = main_container_id;
        mMenuItemTitles = new ArrayList<String>(Arrays.asList("Home"));
    }

    public Fragment getFragmentForPosition(int menu_position) {

        Fragment return_fragment;

        switch (menu_position) {
            case 0:
                if (mSettingsManager.getHueBridgeManager().isHueBridgeConnected())
                    return_fragment = FragmentHomeWithBridge.newInstance();
                else
                    return_fragment = FragmentHomeWithoutBridge.newInstance();
                break;
            default:
                return_fragment = FragmentHomeWithoutBridge.newInstance();
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

    public void replaceFragmentInMainContainer(String menu_title) {
        int menu_position = mMenuItemTitles.indexOf(menu_title);
        Fragment fragment = getFragmentForPosition(menu_position);
        replaceFragmentInMainContainer(fragment);
        //TODO should be in a fragment!
        mSettingsManager.getActionBarTitleHandler().setActionBarTitle(menu_title);
    }

    public void replaceFragmentInMainContainer(int menu_position) {
        Fragment fragment = getFragmentForPosition(menu_position);
        replaceFragmentInMainContainer(fragment);
        //TODO should be in a fragment!
        mSettingsManager.getActionBarTitleHandler().setActionBarTitle(mMenuItemTitles.get(menu_position));
    }

}
