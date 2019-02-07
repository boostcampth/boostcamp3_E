package com.teame.boostcamp.myapplication.ui.itemdetail;

import com.teame.boostcamp.myapplication.adapter.ItemDetailRecyclerAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface ItemDetailContract {

    interface View extends BaseView<Presenter> {
        void successWriteItem();
    }

    interface Presenter extends BasePresenter {
        void loadReplyList(ItemDetailRecyclerAdapter adapter, String itemUid);

        void writeReply(String itemId,String content,int ratio);
    }

}
