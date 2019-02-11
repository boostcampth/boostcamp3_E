package com.teame.boostcamp.myapplication.ui.search;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teame.boostcamp.myapplication.adapter.searchadapter.ExListAdapterContract;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.UserPinPreview;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public interface SearchContract {

    interface View extends BaseView<Presenter> {
        void showPositionInMap(LatLng latlon, String nation, String city);
        void showSearchResult(int count);
        void showUserPinPreview(UserPinPreview preview);
        void showUserPin(LatLng latlng);
        void showPeriodSetting();
        void hideExSearchView();
        void showFragmentToast(String text);
        void showUserGoodsListActivity(List<Goods> list);
    }

    interface Presenter extends BasePresenter{
        void onSearchSubmit(String place);
        void showUserPin();
        void setAdpaterView(ExListAdapterContract.View adapter);
        void setAdpaterModel(ExListAdapterContract.Model adapter);
        void initView();
        void getUserPinPreview(Marker marker);
        void getUserPinGoodsList(Marker currentMarker);
        void onTextChange(String text);
    }
}
