package com.alterego.ibeaconapp.app.screens.connecttobridge;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alterego.androidbound.ViewBinder;
import com.alterego.ibeaconapp.app.R;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

public class ConnectUsernameFragment extends DialogFragment {

    private static SettingsManager mSettingsManager;

    private static ViewBinder mViewBinder;
    private ViewModelConnectUsername mViewModel;
    private View mView;

    public static ConnectUsernameFragment newInstance(SettingsManager mgr) {
        ConnectUsernameFragment fragment = new ConnectUsernameFragment();
        mSettingsManager = mgr;
        mViewBinder = mSettingsManager.getViewBinder();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int theme;

        int style = DialogFragment.STYLE_NORMAL;
        if (Build.VERSION.SDK_INT >= 14)
            theme = android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar;
        else
            theme = android.R.style.Theme_Dialog;

        this.setStyle(style, theme);
        mViewModel = new ViewModelConnectUsername(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = mSettingsManager.getParentActivity().getResources().getDimensionPixelSize(R.dimen.dialog_usernameconnect_width);
        int height = mSettingsManager.getParentActivity().getResources().getDimensionPixelSize(R.dimen.dialog_usernameconnect_height);
        getDialog().getWindow().setLayout(width, height);

        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.dimAmount=0.0f;
        getDialog().getWindow().setAttributes(lp);
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setCancelable(true);
        mView = mViewBinder.inflate(mSettingsManager.getParentActivity(), mViewModel, R.layout.dialog_usernameconnect, null);
        mViewModel.setView(mView);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mViewModel != null) {
            mViewModel.dispose();
        }
    }
}
