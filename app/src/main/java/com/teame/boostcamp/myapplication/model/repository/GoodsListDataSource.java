package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Goods;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface GoodsListDataSource {
    Observable<Goods> getItemListToObservable(String nation, String city);
    Single<List<Goods>> getItemList(String nation, String city);
}
