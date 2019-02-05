package com.teame.boostcamp.myapplication.ui.tabsns;

import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface TabSNSContract {
    interface View extends BaseView<Presenter> {
        //void addData(Post post);
    }

    interface Presenter extends BasePresenter {
        void loadPostData(PostListAdapter adapter);
    }
}
