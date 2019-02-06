package com.teame.boostcamp.myapplication.ui.createlist;

import com.teame.boostcamp.myapplication.adapter.ItemListRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Item;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface CreateListContract {

    interface View extends BaseView<Presenter> {
        void goNextStep(List<Item> list);

        void showAddedItem(int position);
    }

    interface Presenter extends BasePresenter {

        void loadListData(ItemListRecyclerAdapter adapter);

        void selectItem(int position, boolean isCheck);

        void addItem(String itemName);

        void decideShoppingList();

    }

}
