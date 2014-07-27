
package com.alterego.ibeaconapp.app.interfaces;

public interface INavigationDrawerHandler {

    public void closeDrawer();

    public void openDrawer();

    public boolean isDrawerOpen();

    public void selectItem(int position);

    public void openLastOpenedItem ();

}
