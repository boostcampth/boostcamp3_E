package com.teame.boostcamp.myapplication.ui.writereply;

import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface WriteReplyContract {

    interface View extends BaseView<Presenter> {
        void successWriteItem(Reply item);

        void failWriteItem();
    }

    interface Presenter extends BasePresenter {
        void writeReply(String itemId, String content, int ratio);
    }
}
