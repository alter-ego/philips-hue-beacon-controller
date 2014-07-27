package com.alterego.ibeaconapp.app.hue.api;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.ibeaconapp.app.helpers.GeneralHelper;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

class NuPNPApiErrorHandler implements ErrorHandler {

    private final IAndroidLogger mLogger;

    NuPNPApiErrorHandler(IAndroidLogger logger) {
        mLogger = logger;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        try {
            return new NuPNPApiException(cause);
        } catch (Exception e) {
            mLogger.error("NuPNPApiException cannot read body, error = " + cause);
        }
        return cause;
    }



    private class NuPNPApiException extends Throwable {

        public NuPNPApiException(RetrofitError cause) {
            try {
                mLogger.error("NuPNPApiException error = " + cause.getMessage() + ", response = " + GeneralHelper.slurp(cause.getResponse().getBody().in()));
            } catch (Exception e) {
                mLogger.error("NuPNPApiException error = " + cause + ", can't read the response!");
            }
        }
    }
}