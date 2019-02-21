package com.teame.boostcamp.myapplication.ui.mypost;

import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.adapter.SnsSearchRecyclerAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface MyPostContract {
    interface View extends BaseView<Presenter> {
        void stopLoading(int size);
    }

    interface Presenter extends BasePresenter {
        void loadMyPost(PostListAdapter adapter);
    }
}
