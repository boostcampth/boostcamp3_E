package com.teame.boostcamp.myapplication.ui.selectedgoods;

import com.teame.boostcamp.myapplication.adapter.GoodsMyListAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface SelectedGoodsContract {

    interface View extends BaseView<Presenter> {
        void finishLoad(int size);

        void setResultPrice(String resultPrice);

        void setAllorNoneCheck(boolean allCheck);

        void setOfferDelete();

        void deleteAdapterItem(int position);
    }

    interface Presenter extends BasePresenter {

        void loadListData(GoodsMyListAdapter adapter, String headerUid);

        void deleteList();

        void deleteItem(int position);

        void detectIsAllCheck();

        void calculatorPrice();

    }

}
