package com.alterego.ibeaconapp.app.helpers;

import android.app.Activity;
import android.content.Context;

import com.alterego.advancedandroidlogger.implementations.NullAndroidLogger;
import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.androidbound.ViewBinder;
import com.alterego.androidbound.interfaces.IValueConverter;
import com.alterego.ibeaconapp.app.hue.data.HueBridgeNuPNPInfo;
import com.alterego.ibeaconapp.app.managers.SettingsManager;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Locale;

public class BindingValueConverters {

    private static IAndroidLogger mLogger = NullAndroidLogger.instance;
    private static Context mContext;
    private static SettingsManager mSettingsManager;

    public BindingValueConverters(Activity activity, SettingsManager mgr) {
        mSettingsManager = mgr;
        mLogger = mSettingsManager.getLogger();
        mContext = activity;
        registerDefaultConverters(mSettingsManager.getViewBinder());
    }

    public void setParentActivity(Activity act) {
        mContext = act;
    }

    private void registerDefaultConverters(ViewBinder viewBinder) {
        viewBinder.registerConverter("PrettifyDateTime", BindingValueConverters.PrettifyDateTime);
        viewBinder.registerConverter("BridgeNumberInList", BindingValueConverters.BridgeNumberInList);

    }


    public static IValueConverter PrettifyDateTime = new IValueConverter() {

        @Override
        public Object convert(Object value, Class<?> targetType, Object parameter, Locale culture) {

            DateTimeZone timeZone = DateTimeZone.getDefault();
            String formattingParam = (String) parameter;
            String result = GeneralHelper.convertDateToString(mContext, (DateTime) value, Locale.getDefault(), timeZone, formattingParam);
            return result;
        }

        @Override
        public Object convertBack(Object value, Class<?> targetType, Object parameter, Locale culture) {
            return null;
        }
    };

    public static IValueConverter BridgeNumberInList = new IValueConverter() {

        @Override
        public Object convert(Object value, Class<?> targetType, Object parameter, Locale culture) {
            HueBridgeNuPNPInfo bridge = (HueBridgeNuPNPInfo) value;
            mLogger.debug("BridgeNumberInList bridge = " + bridge.toString());
            return mSettingsManager.getHueBridgeManager().getHueBridgePosition(bridge);
        }

        @Override
        public Object convertBack(Object value, Class<?> targetType, Object parameter, Locale culture) {
            return null;
        }
    };


}
