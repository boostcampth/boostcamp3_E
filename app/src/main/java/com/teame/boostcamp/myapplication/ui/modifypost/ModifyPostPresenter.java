package com.teame.boostcamp.myapplication.ui.modifypost;

import android.app.ProgressDialog;
import android.net.Uri;

import com.teame.boostcamp.myapplication.adapter.PreviewImageAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.ui.addpost.AddPostContract;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class ModifyPostPresenter implements ModifyPostContract.Presenter {
    private ModifyPostContract.View view;
    private CompositeDisposable disposable = new CompositeDisposable();
    private PostListRepository postListRep;
    private GoodsListHeader selectedListHeader = null;
    private ProgressDialog loading;
    private PreviewImageAdapter adapter;

    public ModifyPostPresenter(ModifyPostContract.View view) {
        this.view = view;
        this.postListRep = PostListRepository.getInstance();
    }

    @Override
    public void loadModifyImage(List<String> imagePath, PreviewImageAdapter adapter) {
        loading = view.showLoading();
        this.adapter = adapter;
        disposable.add(postListRep.loadModifyImages(imagePath).subscribe(list ->{
            DLogUtil.e(list.toString());
            for(Uri uri : list){
                loading.dismiss();
                adapter.add(uri);
            }
        }, e -> view.occurServerError()));
    }

    @Override
    public void modifyPost(Post oldPost, String content) {
        loading = view.showLoading();
        disposable.add(postListRep.modifyPost(oldPost, content, adapter.getUriList())
                .subscribe(list -> {
                            DLogUtil.d(list.toString());
                            loading.dismiss();
                            view.succeedModifyPost();
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

