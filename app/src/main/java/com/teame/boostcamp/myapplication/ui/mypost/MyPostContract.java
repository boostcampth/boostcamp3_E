package com.teame.boostcamp.myapplication.ui.mypost;

import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.adapter.SnsSearchRecyclerAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

import java.util.List;

public interface MyPostContract {
    interface View extends BaseView<Presenter> {
        void stopLoading(int size);
        void succeedDelete(int size);
    }

    interface Presenter extends BasePresenter {
        void loadMyPost(PostListAdapter adapter);
        void adjustLike(String key, int position);
        void deletePost(String key, List<String> imagePathList, int position);
    }
}
