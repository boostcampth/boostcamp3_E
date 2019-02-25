package com.teame.boostcamp.myapplication.ui.mypost;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

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
        disposable.add(postListRepository.getMyPostList()
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

    @Override
    public void adjustLike(String key, int position) {
        disposable.add(postListRepository.adjustLike(key)
                .subscribe(post -> {
                            adapter.itemList.set(position, post);
                            adapter.notifyItemChanged(position, PostListAdapter.LIKE_UPDATE);
                        },
                        e -> {
                            DLogUtil.e(e.getMessage());
                        })
        );
    }

    @Override
    public void deletePost(String key, List<String> imagePathList, int position) {
        disposable.add(postListRepository.deletePost(key, imagePathList)
                .subscribe(b -> {
                            adapter.removeItem(position);
                            view.succeedDelete(adapter.getItemCount());
                        },
                        e -> DLogUtil.e(e.getMessage())));
    }
}

