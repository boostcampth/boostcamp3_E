package com.teame.boostcamp.myapplication.ui;

import com.teame.boostcamp.myapplication.adapter.ItemListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

interface MyListContract {

    interface View extends BaseView<Presenter> {
        void showMyListItems(String headerKey);
    }

    interface Presenter extends BasePresenter {
        void loadMyList(ItemListHeaderRecyclerAdapter adapter);

        void getMyListUid(int position);
    }
}
