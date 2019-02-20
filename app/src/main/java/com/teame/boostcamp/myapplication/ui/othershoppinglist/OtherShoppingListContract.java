package com.teame.boostcamp.myapplication.ui.othershoppinglist;

import com.teame.boostcamp.myapplication.adapter.GoodsMyListAdapter;
import com.teame.boostcamp.myapplication.adapter.GoodsOtherListAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface OtherShoppingListContract {

    interface View extends BaseView<Presenter>{
        void finishLoad(int size);

    }

    interface Presenter extends BasePresenter {
        void loadListData(GoodsOtherListAdapter adapter, String headerUid);
        //void calculatorPrice();
    }

}
