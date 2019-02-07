package com.teame.boostcamp.myapplication.model.repository;

import com.teame.boostcamp.myapplication.model.entitiy.Item;
import com.teame.boostcamp.myapplication.model.entitiy.ItemListHeader;
import com.teame.boostcamp.myapplication.model.repository.remote.MyListRemoteDataSource;

import java.util.List;

import io.reactivex.Scheduler;
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
    public Single<List<ItemListHeader>> getMyList() {
        return myListRemoteDataSource.getMyList();
    }

    @Override
    public Single<List<Item>> getMyListItems(String headerUid) {
        return myListRemoteDataSource.getMyListItems(headerUid)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
