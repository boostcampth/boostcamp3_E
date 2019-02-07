package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Item;
import com.teame.boostcamp.myapplication.model.entitiy.ItemListHeader;

import java.util.List;

import io.reactivex.Single;

public interface MyListDataSoruce {
    Single<List<ItemListHeader>> getMyList();

    Single<List<Item>> getMyListItems(String headerUid);
}
