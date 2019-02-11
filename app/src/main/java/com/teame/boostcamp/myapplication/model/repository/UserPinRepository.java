package com.teame.boostcamp.myapplication.model.repository;

import android.util.Pair;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.UserPinPreview;
import com.teame.boostcamp.myapplication.model.repository.remote.UserPinRemoteDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

public class UserPinRepository implements UserPinDataSource {
    private static UserPinRepository INSTANCE;
    private static UserPinRemoteDataSource shoppingListRemoteDataSource;

    private UserPinRepository(){
        shoppingListRemoteDataSource= UserPinRemoteDataSource.getInstance();
    }

    public static UserPinRepository getInstance(){
        if(INSTANCE==null){
            INSTANCE=new UserPinRepository();
        }
        return INSTANCE;
    }

    @Override
    public Single<List<Pair<LatLng,String>>> getUserVisitedLocation(LatLng center) {
        return shoppingListRemoteDataSource.getUserVisitedLocation(center);
    }

    @Override
    public Single<List<Goods>> getUserPinGoodsList(String Key) {
        return shoppingListRemoteDataSource.getUserPinGoodsList(Key);
    }

    @Override
    public Single<UserPinPreview> getUserPinPreview(String Key) {
        return shoppingListRemoteDataSource.getUserPinPreview(Key);
    }

    @Override
    public void setLocation(String Key, LatLng location) {
        shoppingListRemoteDataSource.setLocation(Key, location);
    }
}
