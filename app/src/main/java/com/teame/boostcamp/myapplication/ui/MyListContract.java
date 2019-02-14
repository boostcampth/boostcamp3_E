package com.teame.boostcamp.myapplication.ui;

import com.teame.boostcamp.myapplication.adapter.GoodsListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

interface MyListContract {

    interface View extends BaseView<Presenter> {
        void observeWorkManager(String Tag);

        void adapterImageChange(int position, boolean change);

        void showMyListItems(String headerKey);

        void finishLoad(int size);

        void showDialog(int position);
    }

    interface Presenter extends BasePresenter {
        void alarmButtonPosivive(int position);

        void alarmButtonClick(int position);

        void loadMyList(GoodsListHeaderRecyclerAdapter adapter);

        void getMyListUid(int position);

        void deleteMyList(int position);
    }
}
