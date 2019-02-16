package com.teame.boostcamp.myapplication;

import android.util.Log;

import androidx.multidex.MultiDexApplication;

public class MainApplication extends MultiDexApplication {

    static public boolean DEBUG;
    private static volatile MainApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 디버그모드에 따라서 로그를 남기거나 남기지 않는다
        Log.d("isDebug? : ", DEBUG + "");
        DEBUG = BuildConfig.DEBUG;
    }

    public static MainApplication getApplication() {
        if (instance == null) {
            throw new IllegalStateException("this application does not extends Application.class");
        }

        return instance;
    }
}
