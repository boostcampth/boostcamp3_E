package com.teame.boostcamp.myapplication.ui;


import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.adapter.SnsSearchRecyclerAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface SNSContract {
    interface View extends BaseView<Presenter> {
        void stopRefreshIcon();
        void succeedSearch(PostListAdapter searchAdapter);
    }

    interface Presenter extends BasePresenter {
        void createList(SnsSearchRecyclerAdapter exAdapter);
        void loadPostData(PostListAdapter adapter);
        void loadSearchPost(PostListAdapter searchAdapter, String tag);
        void saveToJson();
    }
}
