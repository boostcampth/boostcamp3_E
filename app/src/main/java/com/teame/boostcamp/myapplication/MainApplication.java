package com.teame.boostcamp.myapplication;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

public class MainApplication extends MultiDexApplication {

    static public boolean DEBUG;

    @Override
    public void onCreate() {
        super.onCreate();

        // 디버그모드에 따라서 로그를 남기거나 남기지 않는다
        Log.d("test", DEBUG + "");
        DEBUG = isDebuggable(this);
    }

    /**
     * * ELog를 위한 Debug 상태 리턴
     */
    private boolean isDebuggable(Context context) {
        boolean debuggable = false;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            /* debuggable variable will remain false */
        }
        return debuggable;
    }
}
