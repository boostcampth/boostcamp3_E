package com.teame.boostcamp.myapplication.ui;

import com.teame.boostcamp.myapplication.adapter.GoodsListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

interface MyListContract {

    interface View extends BaseView<Presenter> {
        void showMyListItems(String headerKey);

        void finishLoad(int size);
    }

    interface Presenter extends BasePresenter {
        void loadMyList(GoodsListHeaderRecyclerAdapter adapter);

        void getMyListUid(int position);

        void deleteMyList(int position);
    }
}
