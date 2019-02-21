package com.teame.boostcamp.myapplication.ui.mypost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import io.reactivex.disposables.CompositeDisposable;

public class MyPostPresenter implements MyPostContract.Presenter {
    private MyPostContract.View view;
    private PostListRepository postListRepository;
    private CompositeDisposable disposable;
    private PostListAdapter adapter;
    public MyPostPresenter(MyPostContract.View view) {
        this.view = view;
        postListRepository = PostListRepository.getInstance();
        disposable = new CompositeDisposable();
    }


    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        this.view=null;
    }

    @Override
    public void loadMyPost(PostListAdapter adapter) {
        this.adapter = adapter;
        disposable.add(postListRepository.getPostList()
                .subscribe(
                        list -> {
                            adapter.initItems(list);
                            DLogUtil.d(list.toString());
                            view.stopLoading(list.size());
                        },
                        e -> DLogUtil.d(e.getMessage())
                )
        );
    }
}

