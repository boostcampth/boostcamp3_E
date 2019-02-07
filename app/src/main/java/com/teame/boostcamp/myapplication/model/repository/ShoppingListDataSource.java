package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Goods;

import java.util.List;

import io.reactivex.Single;

public interface ShoppingListDataSource {

    Single<List<Goods>> getItemList();

    void saveMyChoiceList(List<Goods> list);
}
