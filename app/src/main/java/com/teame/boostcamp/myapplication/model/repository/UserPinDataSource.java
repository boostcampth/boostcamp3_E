package com.teame.boostcamp.myapplication.model.repository;

import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;

import java.util.List;

import io.reactivex.Single;

public interface UserPinDataSource {
    Single<List<Pair<LatLng,String>>> getUserVisitedLocation(LatLng center);
    Single<GoodsListHeader> getUserPinPreview(String Key);
    Single<List<Goods>> getUserPinGoodsList(String Key);
    Single<List<GoodsListHeader>> getUserHeaderList(List<String> keyList);
}
