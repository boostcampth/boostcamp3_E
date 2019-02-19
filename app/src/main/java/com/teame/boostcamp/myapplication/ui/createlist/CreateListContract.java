package com.teame.boostcamp.myapplication.ui.createlist;

import com.teame.boostcamp.myapplication.adapter.GoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface CreateListContract {

    interface View extends BaseView<Presenter> {

        void showDetailItem(Goods item);

        void finishLoad(int size);

        void setBadge(String count);

        void backActivity(int size);

        void resultSearchScreen(int size);

        void goAddItem(String goodsName);

        void successAddCart();
    }

    interface Presenter extends BasePresenter {

        void addGoods();

        void diffSerchList(String query);

        void removeCart();

        void getDetailItemUid(int position);

        void loadListData(GoodsListRecyclerAdapter adapter, String nation, String city);

        void saveListHeader(GoodsListHeader header);

        void getShoppingListCount();

        void backCreateList();

        void addCartGoods(Goods item);
    }

}
