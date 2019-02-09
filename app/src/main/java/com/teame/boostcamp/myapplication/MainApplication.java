package com.teame.boostcamp.myapplication;

import android.util.Log;

import com.teame.boostcamp.myapplication.util.DLogUtil;

import androidx.multidex.MultiDexApplication;

public class MainApplication extends MultiDexApplication {

    static public boolean DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();
        // 디버그모드에 따라서 로그를 남기거나 남기지 않는다
        Log.d("isDebug? : ", DEBUG + "");
        DEBUG = BuildConfig.DEBUG;
    }

}
