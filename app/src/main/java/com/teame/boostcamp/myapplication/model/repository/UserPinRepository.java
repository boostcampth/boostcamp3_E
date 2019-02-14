package com.teame.boostcamp.myapplication.model.repository;

import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.remote.UserPinRemoteDataSource;

import java.util.List;

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
    public Single<GoodsListHeader> getUserPinPreview(String Key) {
        return shoppingListRemoteDataSource.getUserPinPreview(Key);
    }
}
