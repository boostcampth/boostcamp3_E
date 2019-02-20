package com.teame.boostcamp.myapplication.ui;

import com.teame.boostcamp.myapplication.adapter.GoodsListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;
import java.util.Map;

interface MyListContract {

    interface View extends BaseView<Presenter> {
        void showMyListItems(String headerKey);

        void finishLoad(int size);
    }

    interface Presenter extends BasePresenter {
        void loadMyList(GoodsListHeaderRecyclerAdapter adapter);

        void reLoadMyList();
        void getMyListUid(int position);

        void deleteMyList(int position);
    }
}
