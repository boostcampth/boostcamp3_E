package com.teame.boostcamp.myapplication.ui.selectedgoods;

import com.teame.boostcamp.myapplication.adapter.SelectedGoodsRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface SelectedGoodsContract {

    interface View extends BaseView<Presenter> {
        void saveCheckedList(List<Goods> list);

        void showDetailItem(Goods item);
    }

    interface Presenter extends BasePresenter {

        void loadListData(SelectedGoodsRecyclerAdapter adapter, String headerUid);

        void selectItem(int position, boolean isCheck);

        void getDetailItemUid(int position);

        void getCheckedList();

    }

}
