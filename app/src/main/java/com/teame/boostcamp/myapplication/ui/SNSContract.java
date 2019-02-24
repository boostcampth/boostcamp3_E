package com.teame.boostcamp.myapplication.ui;


import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.adapter.SnsSearchRecyclerAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface SNSContract {
    interface View extends BaseView<Presenter> {
        void stopRefreshIcon(int size);
        void succeedSearch(PostListAdapter searchAdapter);
        void succeedDelete(int size);
        void succeedSearchDelete(int size);
    }

    interface Presenter extends BasePresenter {
        void setAdapter(PostListAdapter adapter);
        void createList(SnsSearchRecyclerAdapter exAdapter);
        void deletePost(String key, List<String> imagePathList, int position);
        void loadPostData(PostListAdapter adapter);
        void loadSearchPost(PostListAdapter searchAdapter, String tag);
        void saveToJson();
        void adjustLike(String key, int position);
        void searchAdjustLike(String key, int position);
        void deleteSearchPost(String key, List<String> imagePathList, int position);
    }
}
