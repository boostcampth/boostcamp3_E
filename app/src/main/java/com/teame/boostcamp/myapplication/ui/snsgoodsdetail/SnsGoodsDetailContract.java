package com.teame.boostcamp.myapplication.ui.snsgoodsdetail;

import com.teame.boostcamp.myapplication.adapter.GoodsDetailRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface SnsGoodsDetailContract {

    interface View extends BaseView<Presenter> {
        void finishLoad(float totalRatio,int size);

        void completeReloadReply();
    }

    interface Presenter extends BasePresenter {
        void loadReplyList(GoodsDetailRecyclerAdapter adapter, String itemUid);

        void writeReply(Reply item);

        void deleteReply(String itemId,int position);

        Reply getItem(int position);

    }

}

