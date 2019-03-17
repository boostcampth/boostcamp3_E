package com.teame.boostcamp.myapplication.ui.usershoppinglist;

import com.teame.boostcamp.myapplication.adapter.UserShoppingListAdapterContract;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.ArrayList;
import java.util.List;

public interface UserShoppinglistContract {

    interface View extends BaseView<Presenter> {
        void setSelectAllState(boolean state);
    }

    interface Presenter extends BasePresenter {
        boolean getExit();
        void addCartGoods(List<Goods> item);
        void setExit(boolean state);
        void selectAll(boolean state);
        ArrayList<Goods> getSelectedList();
        void setAdapter(UserShoppingListAdapterContract.View adpaterView, UserShoppingListAdapterContract.Model adapterModel);
    }
}
