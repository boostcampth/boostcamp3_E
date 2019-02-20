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
        void setGoodsMoreView(boolean state);
        void setVisitedMoreView(boolean state);
        void showGoodsEmptyView();
        void showVisitedEmptyView();
        void bannerClick(String country);
        void showViewPage(int position);
        void showUserShoppingActivity(List<Goods> list, GoodsListHeader header);
    }

    interface Presenter extends BasePresenter {
        void setViewPagerAdapter(FamousPlaceAdapter adapter);
        void setUserViewPagerAdapter(MainOtherListViewPagerAdapter adapter);
        void setLocationAdapter(LocationBaseGoodsListRecyclerAdapter adapter);
        void loadListData(LocationBaseGoodsListRecyclerAdapter goodsAdapter, String nation, String city);
        void loadHeaderKeys(LatLng center);
    }
}
