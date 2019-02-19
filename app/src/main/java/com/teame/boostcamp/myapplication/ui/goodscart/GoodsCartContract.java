package com.teame.boostcamp.myapplication.ui.goodscart;

import com.teame.boostcamp.myapplication.adapter.GoodsCartAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

interface GoodsCartContract {

    interface View extends BaseView<Presenter> {
        void addedHashTag(String tag);

        void setResultPrice(String resultPrice);

        void setAllorNoneCheck(boolean allCheck);

        void duplicationTag();

        void noSelectGoods();

        void successSave();

        void emptyList();
    }

    interface Presenter extends BasePresenter {
        void addHashTag(String tag);

        int loadData(GoodsCartAdapter adapter);

        void deleteItem(int position);

        GoodsListHeader getHeaderData();

        void removeHashTag(String tag);
        void saveCartList();

        void detectIsAllCheck();

        void calculatorPrice();

        void saveMyList();
    }
}
