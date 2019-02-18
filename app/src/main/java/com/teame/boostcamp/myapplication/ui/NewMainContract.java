package com.teame.boostcamp.myapplication.ui;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.adapter.LocationBaseGoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.adapter.MainOtherListRecyclerAdapter;
import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface NewMainContract {
    interface View extends BaseView<Presenter> {
        void finishLoad();

    }

    interface Presenter extends BasePresenter {
        void loadListData(LocationBaseGoodsListRecyclerAdapter goodsAdapter, String nation, String city);
        void loadHeaderKeys(LatLng center, MainOtherListRecyclerAdapter listAdapter);
        void loadHeaders(List<String> keyList, MainOtherListRecyclerAdapter listAdapter);
    }
}
