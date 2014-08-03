package com.alterego.ibeaconapp.app.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.alterego.ibeaconapp.app.hue.data.HueBridgeInfo;

import info.metadude.android.typedpreferences.BooleanPreference;
import info.metadude.android.typedpreferences.StringPreference;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class StoredPreferencesHelper {

    private static final String PREF_FIRST_RUN = "FIRST_RUN";

    protected final SharedPreferences mSharedPreferences;

    protected static final String PREF_KEY_LASTHUEBRIDGE = "LASTHUEBRIDGE";
    protected static final String PREF_KEY_LASTBRIDGEUSERNAME = "PREF_KEY_LASTBRIDGEUSERNAME";

    public StoredPreferencesHelper(Context applicationContext) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    public void storeLastAccessedHueBridge(final HueBridgeInfo bridgeInfo) {
        Schedulers.io().schedule(new Action1<Scheduler.Inner>() {
            @Override
            public void call(Scheduler.Inner inner) {
                String last = bridgeInfo.getId();
                StringPreference pref = new StringPreference(mSharedPreferences, PREF_KEY_LASTHUEBRIDGE);
                pref.set(last);
            }
        });
    }

    public String restoreLastAccessedHueBridge() {
        StringPreference pref = new StringPreference(mSharedPreferences, PREF_KEY_LASTHUEBRIDGE);
        return pref.get();
    }

    public boolean storesLastAccessedHueBridge() {
        StringPreference pref = new StringPreference(mSharedPreferences, PREF_KEY_LASTHUEBRIDGE);
        return pref.isSet();
    }

    public void storeLastAccessedBridgeUsername(final String username) {
        Schedulers.io().schedule(new Action1<Scheduler.Inner>() {
            @Override
            public void call(Scheduler.Inner inner) {
                StringPreference pref = new StringPreference(mSharedPreferences, PREF_KEY_LASTBRIDGEUSERNAME);
                pref.set(username);
            }
        });
    }

    public String restoreLastAccessedBridgeUsername() {
        StringPreference pref = new StringPreference(mSharedPreferences, PREF_KEY_LASTBRIDGEUSERNAME);
        return pref.get();
    }

    public boolean storesLastAccessedBridgeUsername() {
        StringPreference pref = new StringPreference(mSharedPreferences, PREF_KEY_LASTBRIDGEUSERNAME);
        return pref.isSet();
    }

//    public void savePlayerState(final String player_state_key, final ISimpleMusicPlayer.PLAYER_STATE player_state) {
//        Schedulers.io().schedule(new Action1<Scheduler.Inner>() {
//            @Override
//            public void call(Scheduler.Inner inner) {
//                int state = player_state.getValue();
//                IntPreference pref = new IntPreference(mSharedPreferences, player_state_key);
//                pref.set(state);
//            }
//        });
//    }
//
//    public Observable<Integer> loadPlayerState(final String player_state_key) {
//        return Observable
//                .create(new Observable.OnSubscribe<Integer>() {
//                    @Override
//                    public void call(Subscriber<? super Integer> subscriber) {
//                        IntPreference pref = new IntPreference(mSharedPreferences, player_state_key);
//                        subscriber.onNext(pref.get());
//                        subscriber.onCompleted();
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public boolean storesPlayerState(String player_state_key) {
//        if (player_state_key != null && !player_state_key.equals("")) {
//            IntPreference pref = new IntPreference(mSharedPreferences, player_state_key);
//            return pref.isSet();
//        } else { return false; }
//    }

    public boolean isFirstRun() {
        return new BooleanPreference(mSharedPreferences, PREF_FIRST_RUN, true).get();
    }

    public void setFirstRun(final boolean firstRun) {
        Schedulers.io().schedule(new Action1<Scheduler.Inner>() {
            @Override
            public void call(Scheduler.Inner inner) {
                new BooleanPreference(mSharedPreferences, PREF_FIRST_RUN).set(firstRun);
            }
        });
    }
}
