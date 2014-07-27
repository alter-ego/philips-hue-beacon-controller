package com.alterego.ibeaconapp.app.viewmodels;

import com.alterego.androidbound.ViewModel;
import com.alterego.ibeaconapp.app.BeaconFragmentFactory;
import com.alterego.ibeaconapp.app.SettingsManager;

import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class NavigationDrawerViewModel extends ViewModel {

    private final SettingsManager mSettingsManager;
    @Getter
    private int mCurrentSelectedPosition;
    private BeaconFragmentFactory mBeaconFragmentFactory;

    public NavigationDrawerViewModel(SettingsManager mgr, int currentSelectedPosition) {

        mSettingsManager = mgr;
        mBeaconFragmentFactory = mSettingsManager.getBeaconFragmentFactory();
        setLogger(mgr.getLogger());
        mCurrentSelectedPosition = currentSelectedPosition;

        setNavigationList();
        doSelectMenuItem(getNavigationList().get(mCurrentSelectedPosition));
    }

    public List<String> getNavigationList() {
        return mSettingsManager.getBeaconFragmentFactory().getMenuItemTitles();
    }

    public void setNavigationList() {
        raisePropertyChanged("NavigationList");
    }

    public boolean canSelectMenuItem() {
        return true;
    }

    public void doSelectMenuItem(String menu_title) {
        try {
            mLogger.debug("doSelectMenuItem item = " + menu_title);
            mSettingsManager.getNavigationDrawerHandler().selectItem(getNavigationList().indexOf(menu_title));
        } catch (Exception e) {
            mSettingsManager.getLogger().warning("NavigationDrawerViewModel doSelectMenuItem exception = " + e.toString());
        }
    }

    public void openMenuItem(String menu_title) {
        try {
            //TODO doSelectMenuItem add checked item!
            mLogger.info("openMenuItem item = " + menu_title);
            if (mSettingsManager.getParentActivity() != null) {
                mBeaconFragmentFactory.replaceFragmentInMainContainer(menu_title);
                mCurrentSelectedPosition = getNavigationList().indexOf(menu_title);
            }
        } catch (Exception e) {
            mSettingsManager.getLogger().warning("NavigationDrawerViewModel openMenuItem exception = " + e.toString());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
