package com.teame.boostcamp.myapplication.ui.postreply;

import com.teame.boostcamp.myapplication.adapter.PostReplyAdapter;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import io.reactivex.disposables.CompositeDisposable;

public class PostReplyPresenter implements PostReplyContract.Presenter {
    private PostListRepository postListRepository;
    private CompositeDisposable disposable;
    private PostReplyContract.View view;
    private PostReplyAdapter adapter;

    public PostReplyPresenter(PostReplyContract.View view, PostListRepository postListRepository) {
        this.view = view;
        this.postListRepository = postListRepository;
        disposable = new CompositeDisposable();
    }
    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void writePostReply(String postUid, String content) {
        disposable.add(postListRepository
                .writePostReply(postUid, content)
                .subscribe(reply -> {
                            adapter.add(reply);
                            view.successWriteReply();
                        },
                        e -> DLogUtil.e(e.getMessage())
                ));
    }

    @Override
    public void loadReply(String postUid, PostReplyAdapter adapter) {
        this.adapter = adapter;
        disposable.add(postListRepository.loadPostReplyList(postUid)
                .subscribe(list -> {
                            adapter.initItems(list);
                            DLogUtil.d(list.toString());
                        },
                        e -> DLogUtil.e(e.getMessage()))
        );
    }
}
