package com.yichiuan.moedict;

import android.app.Application;

import timber.log.Timber;

public class MoeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
