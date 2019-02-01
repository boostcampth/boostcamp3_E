package com.teame.boostcamp.myapplication.ui.Search;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface SearchContract {

    interface View extends BaseView<Presenter> {
        public void showPositionInMap(LatLng latlon);
        public void showExSearchList(List<String> exsearch);
        public void showSearchResult();
        public void showUserPin();
        public void userPinClicked();
        public void showPeriodSetting();
    }

    interface Presenter extends BasePresenter{
        public void onSearchSubmit(String place);
    }
}
