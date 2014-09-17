package com.alterego.ibeaconapp.app.api.hue;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.ibeaconapp.app.helpers.GeneralHelper;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

class HueApiErrorHandler implements ErrorHandler {

    private final IAndroidLogger mLogger;

    HueApiErrorHandler(IAndroidLogger logger) {
        mLogger = logger;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        try {
            return new HueApiException(cause);
        } catch (Exception e) {
            mLogger.error("HueApiErrorHandler cannot read body, error = " + cause);
        }
        return cause;
    }



    private class HueApiException extends Throwable {

        public HueApiException(RetrofitError cause) {
            try {
                mLogger.error("HueApiException error = " + cause.getMessage() + ", response = " + GeneralHelper.slurp(cause.getResponse().getBody().in()));
            } catch (Exception e) {
                mLogger.error("HueApiException error = " + cause + ", can't read the response!");
            }
        }
    }
}