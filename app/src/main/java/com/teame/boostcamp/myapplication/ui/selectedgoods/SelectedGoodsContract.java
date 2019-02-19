package com.teame.boostcamp.myapplication.ui.selectedgoods;

import com.teame.boostcamp.myapplication.adapter.GoodsMyListAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface SelectedGoodsContract {

    interface View extends BaseView<Presenter> {
        void finishLoad(int size);

        void setResultPrice(String resultPrice);

        void setOfferDelete();
    }

    interface Presenter extends BasePresenter {

        void loadListData(GoodsMyListAdapter adapter, String headerUid);

        void deleteList();

        void detectIsAllCheck();

        void calculatorPrice();

        void setMyListId(String uid);

        void saveCheckStatus(int position);
    }

}
