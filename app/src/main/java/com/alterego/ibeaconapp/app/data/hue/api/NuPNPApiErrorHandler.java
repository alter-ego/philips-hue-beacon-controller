package com.alterego.ibeaconapp.app.data.hue.api;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;

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
                mLogger.error("NuPNPApiException error = " + cause.getMessage() + ", response = " + cause.getResponse().getBody().in().toString());
            } catch (Exception e) {
                mLogger.error("NuPNPApiException error = " + cause.getMessage() + ", can't read the response!");
            }
        }
    }
}