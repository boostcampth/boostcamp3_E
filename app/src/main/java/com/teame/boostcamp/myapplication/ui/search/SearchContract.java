package com.teame.boostcamp.myapplication.ui.search;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.adapter.SearchAdapter.ExListAdapterContract;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;


public interface SearchContract {

    interface View extends BaseView<Presenter> {
        void showPositionInMap(LatLng latlon);
        void showSearchResult();
        void showUserPin();
        void userPinClicked();
        void showPeriodSetting();
        void hideExSearchView();
        void showFragmentToast(String text);
    }

    interface Presenter extends BasePresenter{
        void onSearchSubmit(String place);
        void setAdpaterView(ExListAdapterContract.View adapter);
        void setAdpaterModel(ExListAdapterContract.Model adapter);
        void initView();
        void onTextChange(String text);
    }
}
