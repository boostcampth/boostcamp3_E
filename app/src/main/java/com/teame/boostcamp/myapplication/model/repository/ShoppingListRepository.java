package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.repository.remote.ShoppingListRemoteDataSource;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

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
    public Single<List<Goods>> getItemList() {
        return shoppingListRemoteDataSource.getItemList().observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void saveMyChoiceList(List<Goods> list) {
        shoppingListRemoteDataSource.saveMyChoiceList(list);
    }
}
