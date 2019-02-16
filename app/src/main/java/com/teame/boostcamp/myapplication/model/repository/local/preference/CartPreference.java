package com.teame.boostcamp.myapplication.model.repository.local.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teame.boostcamp.myapplication.MainApplication;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartPreference implements CartPreferenceHelper {


    private static final String PREF_GOODS_CART = "PREF_GOODS_CART";
    private static final String PREF_HEADER = "PREF_HEADER";

    private SharedPreferences cartPreferences;

    public CartPreference() {
        setUp();
    }

    private void setUp() {
        cartPreferences = MainApplication.getApplication()
                .getSharedPreferences(PREF_GOODS_CART, Context.MODE_PRIVATE);
    }

    @Override
    public void saveGoodsCartList(List<Goods> list) {
        Gson gson = new Gson();
        String jsonList = gson.toJson(list);
        cartPreferences.edit().putString(PREF_GOODS_CART, jsonList).apply();
    }

    @Override
    public List<Goods> getGoodsCartList() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Goods>>() {
        }.getType();
        String jsonList = cartPreferences.getString(PREF_GOODS_CART, "");

        List<Goods> list = gson.fromJson(jsonList, type);
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    public void clearPreferenceData() {
        cartPreferences.edit().remove(PREF_GOODS_CART).apply();
        cartPreferences.edit().remove(PREF_HEADER).apply();
    }

    @Override
    public void saveListHeader(GoodsListHeader header) {
        Gson gson = new Gson();
        String headerString = gson.toJson(header);
        cartPreferences.edit().putString(PREF_HEADER, headerString).apply();
    }

    @Override
    public GoodsListHeader getListHeader() {
        Gson gson = new Gson();
        String jsonList = cartPreferences.getString(PREF_HEADER, "");

        GoodsListHeader header = gson.fromJson(jsonList, GoodsListHeader.class);
        if (header == null) {
            return new GoodsListHeader();
        }
        return header;
    }
}
