package com.alterego.ibeaconapp.app.screens.homewithoutbridge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alterego.ibeaconapp.app.MainApplication;
import com.alterego.ibeaconapp.app.R;
import com.alterego.ibeaconapp.app.managers.SettingsManager;


public class FragmentHomeWithoutBridge extends Fragment {

    private SettingsManager mSettingsManager;
    private ViewModelHomeWithoutBridge mViewModel;
    private View mView;

    public static FragmentHomeWithoutBridge newInstance() {
        FragmentHomeWithoutBridge fragment = new FragmentHomeWithoutBridge();
        return fragment;
    }
    public FragmentHomeWithoutBridge() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingsManager = MainApplication.instance.getSettingsManager();
        mSettingsManager.getLogger().info("FragmentHomeWithoutBridge onCreate");

        if (mViewModel == null) {
            mViewModel = new ViewModelHomeWithoutBridge(mSettingsManager);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = mSettingsManager.getViewBinder().inflate(getActivity(), mViewModel, R.layout.fragment_homewithoutbridge, null);
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
