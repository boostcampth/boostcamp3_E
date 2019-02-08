package com.teame.boostcamp.myapplication.ui.createlist;

import com.teame.boostcamp.myapplication.adapter.GoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface CreateListContract {

    interface View extends BaseView<Presenter> {
        void goNextStep(List<Goods> list);

        void showAddedItem(int position);
    }

    interface Presenter extends BasePresenter {

        void loadListData(GoodsListRecyclerAdapter adapter);

        void selectItem(int position, boolean isCheck);

        void addItem(String itemName);

        void decideShoppingList();

    }

}
