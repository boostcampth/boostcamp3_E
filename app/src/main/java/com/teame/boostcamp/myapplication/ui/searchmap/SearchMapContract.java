package com.teame.boostcamp.myapplication.ui.searchmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

import io.reactivex.Single;

public interface SearchMapContract {
    interface View extends BaseView<Presenter> {
        void showSearchResult(String place);
        void moveCamera(LatLng latlng);
        void setUserPinMarker(LatLng latlng);
        void userPinMarkerFinish(Marker marker);
        void setUserPinMarkerClick(Marker marker,boolean click);
    }

    interface Presenter extends BasePresenter {
        void searchMapFromLocation(LatLng latlng);
        void userPinMarkerFinish();
        void searchMapFromName(String place);
        void userMarkerClicked(Marker marker);
        Single<GoodsListHeader> getGoodsListHeader(Marker marker);
    }
}
