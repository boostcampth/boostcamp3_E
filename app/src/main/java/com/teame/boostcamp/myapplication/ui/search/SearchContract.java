package com.teame.boostcamp.myapplication.ui.search;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.teame.boostcamp.myapplication.adapter.searchadapter.ExListAdapterContract;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.Date;
import java.util.List;


public interface SearchContract {

    interface View extends BaseView<Presenter> {
        void showPositionInMap(LatLng latlon);
        void showSearchResult(int count,String city);
        void showUserPinPreview(GoodsListHeader header);
        void showUserPin(LatLng latlng);
        void showPeriodSetting();
        void hideUserPin();
        void showFragmentToast(String text);
        void showUserGoodsListActivity(List<Goods> list);
        void showmarkerInfoWindow(Marker marker);
        void redPinShow(Marker marker);
        void bluePinShow(Marker marker);
    }

    interface Presenter extends BasePresenter{
        void showUserPinClicked();
        void mapLongClicked(Marker user);
        void infoWindowClicked();
        void onSearchSubmit(String place);
        void showUserPin();
        boolean markerClicked(Marker marker);
        void userPreviewClicked();
        void floatingButtonClicked(Date start, Date end);
        void setSelectedList(List<Goods> goodslist);
    }
}
