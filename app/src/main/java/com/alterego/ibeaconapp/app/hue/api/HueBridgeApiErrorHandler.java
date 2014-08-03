package com.alterego.ibeaconapp.app.hue.api;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;
import com.alterego.ibeaconapp.app.helpers.GeneralHelper;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

class HueBridgeApiErrorHandler implements ErrorHandler {

    private final IAndroidLogger mLogger;

    HueBridgeApiErrorHandler(IAndroidLogger logger) {
        mLogger = logger;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        try {
            return new HueBridgeApiException(cause);
        } catch (Exception e) {
            mLogger.error("HueBridgeApiErrorHandler cannot read body, error = " + cause);
        }
        return cause;
    }



    private class HueBridgeApiException extends Throwable {

        public HueBridgeApiException(RetrofitError cause) {
            try {
                mLogger.error("HueBridgeApiException error = " + cause.getMessage() + ", response = " + GeneralHelper.slurp(cause.getResponse().getBody().in()));
            } catch (Exception e) {
                mLogger.error("HueBridgeApiException error = " + cause + ", can't read the response!");
            }
        }
    }
}