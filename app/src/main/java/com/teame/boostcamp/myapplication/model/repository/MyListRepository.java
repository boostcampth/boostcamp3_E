package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.remote.MyListRemoteDataSource;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MyListRepository implements MyListDataSoruce {


    private static MyListRepository INSTANCE;
    private MyListRemoteDataSource myListRemoteDataSource;

    private MyListRepository() {
        this.myListRemoteDataSource = MyListRemoteDataSource.getInstance();
    }

    public static MyListRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MyListRepository();
        }
        return INSTANCE;
    }


    @Override
    public Single<List<GoodsListHeader>> getMyList() {
        return myListRemoteDataSource.getMyList();
    }

    @Override
    public Single<List<Goods>> getMyListItems(String headerUid) {
        return myListRemoteDataSource.getMyListItems(headerUid)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Single<Boolean> saveMyList(List<Goods> goodsList, List<String> hashTag, GoodsListHeader header) {
        return myListRemoteDataSource.saveMyList(goodsList, hashTag, header).observeOn(AndroidSchedulers.mainThread());
    }
}
