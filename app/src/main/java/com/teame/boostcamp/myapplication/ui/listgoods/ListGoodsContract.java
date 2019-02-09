package com.teame.boostcamp.myapplication.ui.listgoods;

import com.teame.boostcamp.myapplication.adapter.GoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface ListGoodsContract {

    interface View extends BaseView<Presenter> {
        void saveCheckedList(List<Goods> list);

        void showDetailItem(String itemUid);
    }

    interface Presenter extends BasePresenter {

        void loadListData(GoodsListRecyclerAdapter adapter, String headerUid);

        void selectItem(int position, boolean isCheck);

        void getDetailItemUid(int position);

        void getCheckedList();

    }

}
