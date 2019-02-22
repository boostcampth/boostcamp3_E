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

    public PostReplyPresenter(PostReplyContract.View view) {
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
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void writePostReply(String postUid, String content) {
        disposable.add(postListRepository
                .writePostReply(postUid, content)
                .subscribe(reply -> {
                            adapter.addItem(reply);
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
                            view.stopRefreshIcon(list.size());
                            DLogUtil.d(list.toString());
                        },
                        e -> {
                            DLogUtil.e(e.getMessage());
                            view.stopRefreshIcon(0);
                        })
        );
    }

    @Override
    public void deleteReply(String postUid, int position) {
        disposable.add(postListRepository.deleteReply(postUid, adapter.getItem(position).getKey())
                .subscribe(b -> {
                            adapter.removeItem(position);
                            view.controlNo(adapter.getItemCount());
                        },
                        e -> DLogUtil.e(e.getMessage())));
    }
}
