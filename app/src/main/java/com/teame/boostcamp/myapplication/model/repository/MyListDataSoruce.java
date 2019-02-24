package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;

import java.util.List;

import io.reactivex.Single;

public interface MyListDataSoruce {
    Single<List<GoodsListHeader>> getMyList();

    Single<List<Goods>> getMyListItems(String headerUid);

    Single<Boolean> saveMyList(List<Goods> goodsList,List<String> hashTag, GoodsListHeader header);

    Single<Boolean> deleteMyList(String headerUid);

    Single<List<Goods>> getOtherListItems(String uid, String headerUid);
}
