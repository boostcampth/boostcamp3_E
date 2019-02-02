package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Item;

import java.util.List;

import io.reactivex.Single;

public interface ShoppingListDataSource {

    Single<List<Item>> getItemList();

    void saveMyChoiceList(List<Item> list);
}
