package com.alterego.ibeaconapp.app.managers;

import android.app.Fragment;
import android.app.FragmentManager;

import com.alterego.ibeaconapp.app.screens.homewithbridge.FragmentHomeWithBridge;
import com.alterego.ibeaconapp.app.screens.homewithoutbridge.FragmentHomeWithoutBridge;

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
            FragmentManager fragmentManager = mSettingsManager.getParentActivity().getFragmentManager();
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
