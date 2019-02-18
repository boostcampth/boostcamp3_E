package com.teame.boostcamp.myapplication.ui;


import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface SNSContract {
    interface View extends BaseView<Presenter> {
        void stopRefreshIcon();
    }

    interface Presenter extends BasePresenter {
        void loadPostData(PostListAdapter adapter);
    }
}
