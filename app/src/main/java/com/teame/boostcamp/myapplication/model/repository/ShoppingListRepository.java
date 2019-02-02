package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Item;
import com.teame.boostcamp.myapplication.model.repository.remote.ShoppingListRemoteDataSource;

import java.util.List;

import io.reactivex.Single;

public class ShoppingListRepository implements ShoppingListDataSource {

    private static ShoppingListRepository INSTANCE;
    private ShoppingListRemoteDataSource shoppingListRemoteDataSource;

    public static ShoppingListRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShoppingListRepository();
        }
        return INSTANCE;
    }

    private ShoppingListRepository() {
        this.shoppingListRemoteDataSource = ShoppingListRemoteDataSource.getInstance();
    }


    @Override
    public Single<List<Item>> getItemList() {
        return shoppingListRemoteDataSource.getItemList();
    }

    @Override
    public void saveMyChoiceList(List<Item> list) {
        shoppingListRemoteDataSource.saveMyChoiceList(list);
    }
}
