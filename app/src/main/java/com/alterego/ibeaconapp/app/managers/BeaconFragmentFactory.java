package com.alterego.ibeaconapp.app.managers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import com.alterego.ibeaconapp.app.fragments.FragmentHome;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import java.util.ArrayList;
import java.util.Arrays;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix="m")
public class BeaconFragmentFactory {

    private final SettingsManager mSettingsManager;
    @Getter private ArrayList<String> mMenuItemTitles;

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
                return_fragment = FragmentHome.newInstance();
                break;
            default:
                return_fragment = FragmentHome.newInstance();
        }

        mSettingsManager.getLogger().debug("ReaderFragmentFactory getFragmentForPosition menu_position = " + menu_position + ", fragment = " + return_fragment.toString());

        return return_fragment;
    }


    public void replaceFragmentInMainContainer (Fragment fragment) {
        if (mSettingsManager.getParentActivity() != null) {
            FragmentManager fragmentManager = ((ActionBarActivity) mSettingsManager.getParentActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(mMainContainerId, fragment)
                    .commit();
        }
    }

    public void replaceFragmentInMainContainer (String menu_title) {
        int menu_position = mMenuItemTitles.indexOf(menu_title);
        Fragment fragment = getFragmentForPosition(menu_position);
        replaceFragmentInMainContainer(fragment);
        mSettingsManager.getActionBarTitleHandler().setActionBarTitle(menu_title);
    }

}
