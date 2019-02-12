package com.teame.boostcamp.myapplication.ui;

import android.app.Activity;
import android.content.Context;

import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.ui.addpost.AddPostActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import io.reactivex.disposables.CompositeDisposable;

public class SNSPresenter implements SNSContract.Presenter {
    private SNSContract.View view;
    private PostListRepository postListRepository;
    private CompositeDisposable disposable;
    private PostListAdapter adapter;

    public SNSPresenter(SNSContract.View view) {
        this.view = view;
        this.postListRepository = PostListRepository.getInstance();
        disposable = new CompositeDisposable();
    }

    @Override
    public void onAttach() {

    }
    @Override
    public void onDetach() {
        this.view = null;
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    @Override
    public void loadPostData(PostListAdapter adapter) {
        this.adapter = adapter;
        disposable.add(postListRepository.getPostList()
                .subscribe(
                        list -> {
                            adapter.initItems(list);
                            DLogUtil.d(list.toString());
                        },
                        e -> DLogUtil.d(e.getMessage())
                )
        );
    }

}
