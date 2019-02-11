package com.teame.boostcamp.myapplication.model.repository;

import android.util.Pair;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.UserPinPreview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface UserPinDataSource {
    Single<List<Pair<LatLng,String>>> getUserVisitedLocation(LatLng center);
    void setLocation(String Key, LatLng location);
    Single<UserPinPreview> getUserPinPreview(String Key);
    Single<List<Goods>> getUserPinGoodsList(String Key);
}
