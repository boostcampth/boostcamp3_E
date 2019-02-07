package com.teame.boostcamp.myapplication.ui.listitems;

import com.teame.boostcamp.myapplication.adapter.ItemListRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Item;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface ListItemContract {

    interface View extends BaseView<Presenter> {
        void saveCheckedList(List<Item> list);

        void showDetailItem(String itemUid);
    }

    interface Presenter extends BasePresenter {

        void loadListData(ItemListRecyclerAdapter adapter, String headerUid);

        void selectItem(int position, boolean isCheck);

        void getDetailItemUid(int position);

        void getCheckedList();

    }

}
