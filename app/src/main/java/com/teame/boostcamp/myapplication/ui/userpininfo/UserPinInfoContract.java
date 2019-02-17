package com.teame.boostcamp.myapplication.ui.userpininfo;

import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface UserPinInfoContract {
    interface View extends BaseView<Presenter> {
        void showUserShoppinglistActivitiy(List<Goods> list);
    }

    interface Presenter extends BasePresenter {
        void getGoodsList();
    }
}
