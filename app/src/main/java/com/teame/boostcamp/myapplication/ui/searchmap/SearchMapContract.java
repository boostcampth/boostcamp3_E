package com.teame.boostcamp.myapplication.ui.searchmap;

import com.google.android.gms.maps.model.LatLng;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface SearchMapContract {
    interface View extends BaseView<Presenter> {
        void showSearchResult(String place);
        void moveCamera(LatLng latlng);
    }

    interface Presenter extends BasePresenter {
        void setProgressState(boolean state);
        void searchMapFromLocation(LatLng latlng);
        void searchMapFromName(String place);
    }
}
