package com.teame.boostcamp.myapplication.ui;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.adapter.FamousPlaceAdapter;
import com.teame.boostcamp.myapplication.adapter.LocationBaseGoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.adapter.MainOtherListViewPagerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface NewMainContract {
    interface View extends BaseView<Presenter> {
        void showGoodsDetail(Goods goods);
        void showCreateListActivity(GoodsListHeader header);
        void showSearchMapActivity(String place);
        void setGoodsMoreView(boolean state);
        void setVisitedMoreView(boolean state);
        void showGoodsEmptyView(boolean state);
        void showVisitedEmptyView(boolean state);
        void bannerClick(String country);
        void showViewPage(int position);
        void showUserShoppingActivity(List<Goods> list, GoodsListHeader header);
        void setCurrentLocation(String nation, String city);
    }

    interface Presenter extends BasePresenter {
        void getCurrentLocation();
        void locationMoreClick();
        void visitedMoreClick();
        void setViewPagerAdapter(FamousPlaceAdapter adapter);
        void setUserViewPagerAdapter(MainOtherListViewPagerAdapter adapter);
        void setLocationAdapter(LocationBaseGoodsListRecyclerAdapter adapter);
        void loadListData(LocationBaseGoodsListRecyclerAdapter goodsAdapter, String nation, String city);
        void loadHeaderKeys(LatLng center);
    }
}
