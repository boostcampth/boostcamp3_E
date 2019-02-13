package com.teame.boostcamp.myapplication.ui.search;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.teame.boostcamp.myapplication.adapter.searchadapter.ExListAdapterContract;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;


public interface SearchContract {

    interface View extends BaseView<Presenter> {
        void showPositionInMap(LatLng latlon);
        void showSearchResult(int count, String nation, String city);
        void showUserPinPreview(GoodsListHeader header);
        void showUserPin(LatLng latlng);
        void showPeriodSetting();
        void hideUserPin();
        void showFragmentToast(String text);
        void showUserGoodsListActivity(List<Goods> list);
    }

    interface Presenter extends BasePresenter{
        void onSearchSubmit(String place);
        void showUserPin();
        void getUserPinPreview(Marker marker);
        void getUserPinGoodsList(Marker currentMarker);
    }
}
