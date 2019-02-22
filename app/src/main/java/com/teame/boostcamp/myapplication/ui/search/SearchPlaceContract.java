package com.teame.boostcamp.myapplication.ui.search;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.teame.boostcamp.myapplication.adapter.searchadapter.ExListAdapterContract;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface SearchPlaceContract {

    interface View extends BaseView<SearchPlaceContract.Presenter> {
        void showCreateList(GoodsListHeader header);
        void showMapActivity(String city);
    }

    interface Presenter extends BasePresenter {
        void saveToJson();
        void onStop();
        void currentButtonClick();
        void createList();
        void setAdapterView(ExListAdapterContract.View view);
        void setAdapterModel(ExListAdapterContract.Model model);
        void search(String text);
        void goMapButtonClick();
    }
}
