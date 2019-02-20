package com.teame.boostcamp.myapplication.model.repository.local.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teame.boostcamp.myapplication.MainApplication;

import java.util.HashMap;

public class CheckListPreferences implements CheckListPreferencesHelper {

    private static final String PREF_CHECK_LIST = "PREF_CHECK_LIST";
    private Gson gson = new Gson();
    private SharedPreferences checkListPreferences;

    public CheckListPreferences(String myListKey) {
        setUp(myListKey);
    }

    private void setUp(String myListKey) {
        checkListPreferences = MainApplication.getApplication()
                .getSharedPreferences(PREF_CHECK_LIST, Context.MODE_PRIVATE);
    }


    @Override
    public void saveMyListCheck(HashMap<String, Boolean> map) {

        String jsonHashMap = gson.toJson(map);
        checkListPreferences.edit().putString(PREF_CHECK_LIST, jsonHashMap).apply();
    }

    @Override
    public HashMap<String, Boolean> getMyListCheck() {

        String storedHashMapString = checkListPreferences.getString(PREF_CHECK_LIST,null);
        if (storedHashMapString == null){
            return new HashMap<>();
        }
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Boolean>>() {
        }.getType();
        HashMap<String, Boolean> map = gson.fromJson(storedHashMapString, type);
        return map;
    }

}
