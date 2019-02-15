package com.teame.boostcamp.myapplication.ui.modifypost;


import android.app.ProgressDialog;
import android.net.Uri;

import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.ui.addpost.AddPostContract;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class ModifyPostPresenter implements ModifyPostContract.Presenter {
    private ModifyPostContract.View view;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MyListRepository myListRep;
    private PostListRepository postListRep;
    private GoodsListHeader selectedListHeader = null;
    private ProgressDialog loading;

    public ModifyPostPresenter(ModifyPostContract.View view) {
        this.view = view;
        this.myListRep = MyListRepository.getInstance();
        this.postListRep = PostListRepository.getInstance();
    }

    @Override
    public void loadMyList() {
        disposable.add(myListRep.getMyList()
                .subscribe(list -> {
                            DLogUtil.d(list.toString());
                            view.showListSelection(list);
                        },
                        e -> {
                            DLogUtil.e(e.getMessage());
                            view.occurServerError();
                        })
        );
    }

    @Override
    public void setListSelection(GoodsListHeader selected) {
        this.selectedListHeader = selected;
    }

    @Override
    public void addPost(String content, List<Uri> uriList) {
        if (selectedListHeader == null) {
            view.failAddPost();
            return;
        }
        loading = view.showLoading();
        disposable.add(postListRep.writePost(content, uriList, selectedListHeader)
                .subscribe(list -> {
                            DLogUtil.d(list.toString());
                            loading.dismiss();
                            view.succeedAddPost();
                        },
                        e -> {
                            DLogUtil.e(e.getMessage());
                            view.occurServerError();
                        })
        );

    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        this.view = null;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}

