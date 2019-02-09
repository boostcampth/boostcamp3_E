package com.teame.boostcamp.myapplication.ui.goodsdetail;

import com.teame.boostcamp.myapplication.adapter.GoodsDetailRecyclerAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface GoodsDetailContract {

    interface View extends BaseView<Presenter> {
        void successWriteItem();
    }

    interface Presenter extends BasePresenter {
        void loadReplyList(GoodsDetailRecyclerAdapter adapter, String itemUid);

        void writeReply(String itemId,String content,int ratio);
    }

}
