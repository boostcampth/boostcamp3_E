package com.teame.boostcamp.myapplication.ui.createlistinfo;

import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

interface CreateListInfoContract {

    interface View extends BaseView<Presenter> {
        void addedHashTag(String tag);
        void duplicationTag();
        void successSave();
    }

    interface Presenter extends BasePresenter {
        void addHashTag(String tag);
        void removeHashTag(String tag);
        void saveMyList(List<Goods> goodsList, GoodsListHeader header);
    }

}
