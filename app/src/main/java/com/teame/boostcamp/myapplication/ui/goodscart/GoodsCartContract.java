package com.teame.boostcamp.myapplication.ui.goodscart;

import com.teame.boostcamp.myapplication.adapter.GoodsCartAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

interface GoodsCartContract {

    interface View extends BaseView<Presenter> {
        void decide(GoodsListHeader header);

        void setResultPrice(String resultPrice);

        void setAllorNoneCheck(boolean allCheck);

        void noSelectGoods();
    }

    interface Presenter extends BasePresenter {
        void loadData(GoodsCartAdapter adapter);

        void deleteItem(int position);

        void getSaveData();

        void saveCartList();

        void detectIsAllCheck();

        void calculatorPrice();
    }
}
