package com.teame.boostcamp.myapplication.ui.postreply;

import com.teame.boostcamp.myapplication.adapter.PostReplyAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface PostReplyContract {

    interface View extends BaseView {
        void successWriteReply();
        void stopRefreshIcon(int size);
        void controlNo(int size);
    }

    interface Presenter extends BasePresenter {
        void writePostReply(String postUid, String content);
        void loadReply(String postUid, PostReplyAdapter adapter);
        void deleteReply(String postUid, int position);
    }
}
