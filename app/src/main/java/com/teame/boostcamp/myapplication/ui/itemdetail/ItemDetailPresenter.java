package com.teame.boostcamp.myapplication.ui.itemdetail;

import com.teame.boostcamp.myapplication.adapter.ItemDetailRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.ItemDetailRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import io.reactivex.disposables.CompositeDisposable;

public class ItemDetailPresenter implements ItemDetailContract.Presenter {

    private ItemDetailRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ItemDetailRecyclerAdapter adapter;
    private ItemDetailContract.View view;

    public ItemDetailPresenter(ItemDetailContract.View view , ItemDetailRepository repository) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void loadReplyList(ItemDetailRecyclerAdapter adapter, String itemUid) {
        this.adapter = adapter;
        disposable.add(repository.getReplyList(itemUid)
                .subscribe(list -> {
                            adapter.initItems(list);
                            DLogUtil.d(list.toString());
                        },
                        e -> DLogUtil.e(e.getMessage()))
        );
    }

    @Override
    public void writeReply(String itemId, String content, int ratio) {
        // TODO 댓글 쓰기 일정 시간 텀 주기
        disposable.add(repository
                .writeReply(itemId, content, ratio)
                .subscribe(reply -> {
                            adapter.addItem(0, reply);
                            view.successWriteItem();
                        },
                        e -> DLogUtil.e(e.getMessage())
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
