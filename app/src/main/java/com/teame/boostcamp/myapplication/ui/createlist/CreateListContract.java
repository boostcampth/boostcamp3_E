package com.teame.boostcamp.myapplication.ui.createlist;

import com.teame.boostcamp.myapplication.adapter.CheckedGoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.adapter.GoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.adapter.SelectedGoodsRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface CreateListContract {

    interface View extends BaseView<Presenter> {
        void goNextStep(List<Goods> list);

        void showAddedGoods(int position);

        void emptyCheckGoods();

        void showDetailItem(Goods item);

        void notifyDeleteItem(int position,int oldPosition);
    }

    interface Presenter extends BasePresenter {

        void getDetailItemUid(int position);

        void loadListData(GoodsListRecyclerAdapter adapter, CheckedGoodsListRecyclerAdapter checkedAdapter, String nation, String city);

        void checkedItem(int position, boolean isCheck);

        void addItem(String itemName);

        void decideShoppingList();

        void deleteItem(int position);

        void minusCount();

    }

}
