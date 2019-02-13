package com.teame.boostcamp.myapplication.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtil {
    public static void putString(Context context, String Key, String data){
        SharedPreferences shared=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=shared.edit();
        editor.putString(Key,data);
        editor.apply();
    }
    public static String getString(Context context, String Key){
        SharedPreferences shared=PreferenceManager.getDefaultSharedPreferences(context);
        return shared.getString(Key,null);
    }
}
