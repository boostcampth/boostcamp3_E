package com.teame.boostcamp.myapplication.ui.writereply;

import com.teame.boostcamp.myapplication.model.repository.GoodsDetailRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import io.reactivex.disposables.CompositeDisposable;

public class WriteReplyPresenter implements WriteReplyContract.Presenter {

    CompositeDisposable disposable = new CompositeDisposable();
    GoodsDetailRepository repository;
    WriteReplyContract.View view;

    public WriteReplyPresenter(WriteReplyContract.View view, GoodsDetailRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void writeReply(String itemId, String content, int ratio) {
        // TODO 댓글 쓰기 일정 시간 텀 주기
        disposable.add(repository
                .writeReply(itemId, content, ratio)
                .subscribe(reply -> {
                            view.successWriteItem(reply);
                        },
                        e -> {
                            view.failWriteItem();
                            DLogUtil.e(e.getMessage());
                        }
                ));
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        if (disposable.isDisposed()) {
            disposable.dispose();
        }

    }
}
