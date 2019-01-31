package com.teame.boostcamp.myapplication.util;

import android.util.Log;
import com.teame.boostcamp.myapplication.MainApplication;

/**
 ** DLogUtil
 ** release 환경에서 로그를 숨기는 Log Util*/
public class DLogUtil {
    static final String TAG = "TeamE";

    /**
     ** Error Log */
    public static void e(String message) {
        if (MainApplication.DEBUG) Log.e(TAG, buildLogMsg(message));
    }

    /**
     ** Warning Log */
    public static void w(String message) {
        if (MainApplication.DEBUG) Log.w(TAG, buildLogMsg(message));
    }

    /**
     ** Information Log */
    public static void i(String message) {
        if (MainApplication.DEBUG) Log.i(TAG, buildLogMsg(message));
    }

    /**
     ** Debug Log */
    public static void d(String message) {
        if (MainApplication.DEBUG) Log.d(TAG, buildLogMsg(message));
    }

    /**
     ** Verbose Log */
    public static void v(String message) {
        if (MainApplication.DEBUG) Log.v(TAG, buildLogMsg(message));
    }

    private static String buildLogMsg(String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ste.getFileName().replace(".java", ""));
        sb.append("::");
        sb.append(ste.getMethodName());
        sb.append("]");
        sb.append(message);
        return sb.toString();
    }
}