package com.alterego.ibeaconapp.app.screens.homewithbridge;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alterego.ibeaconapp.app.MainApplication;
import com.alterego.ibeaconapp.app.R;
import com.alterego.ibeaconapp.app.managers.SettingsManager;
import com.alterego.ibeaconapp.app.screens.homewithoutbridge.ViewModelHomeWithoutBridge;


public class FragmentHomeWithBridge extends Fragment {

    private SettingsManager mSettingsManager;
    private ViewModelHomeWithBridge mViewModel;
    private View mView;

    public static FragmentHomeWithBridge newInstance() {
        FragmentHomeWithBridge fragment = new FragmentHomeWithBridge();
        return fragment;
    }
    public FragmentHomeWithBridge() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingsManager = MainApplication.instance.getSettingsManager();
        mSettingsManager.getLogger().info("FragmentHomeWithBridge onCreate");
        mSettingsManager.getActionBarTitleHandler().setActionBarTitle(MainApplication.instance.getString(R.string.fragment_home_title));

        if (mViewModel == null) {
            mViewModel = new ViewModelHomeWithBridge(mSettingsManager);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = mSettingsManager.getViewBinder().inflate(getActivity(), mViewModel, R.layout.fragment_homewithbridge, null);
        mViewModel.setView(mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewModel != null) {
            mViewModel.dispose();
            mViewModel = null;
        }
    }

}
