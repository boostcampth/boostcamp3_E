package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.repository.remote.GoodsListRemoteDataSource;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class GoodsListRepository implements GoodsListDataSource {

    private static GoodsListRepository INSTANCE;
    private GoodsListRemoteDataSource shoppingListRemoteDataSource;

    public static GoodsListRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GoodsListRepository();
        }
        return INSTANCE;
    }

    private GoodsListRepository() {
        this.shoppingListRemoteDataSource = GoodsListRemoteDataSource.getInstance();
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
