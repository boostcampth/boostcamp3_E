package com.teame.boostcamp.myapplication.ui.goodsdetail;

import com.teame.boostcamp.myapplication.adapter.GoodsDetailRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.GoodsDetailRepository;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import io.reactivex.disposables.CompositeDisposable;

public class GoodsDetailPresenter implements GoodsDetailContract.Presenter {

    private GoodsDetailRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private GoodsDetailRecyclerAdapter adapter;
    private GoodsDetailContract.View view;

    public GoodsDetailPresenter(GoodsDetailContract.View view, GoodsDetailRepository repository) {
        this.repository = repository;
        this.view = view;
    }

    @Override
    public void loadReplyList(GoodsDetailRecyclerAdapter adapter, String itemUid) {
        this.adapter = adapter;
        disposable.add(repository.getReplyList(itemUid)
                .subscribe(list -> {
                            adapter.initItems(list);
                            view.finishLoad(list.size());
                            DLogUtil.d(list.toString());
                        },
                        e -> {
                            view.finishLoad(Constant.FAIL_LOAD);
                            DLogUtil.e(e.getMessage());
                        })
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
    public void deleteReply(String itemId, int position) {
        Reply item = adapter.getItem(position);
        disposable.add(repository.deleteReply(itemId, item.getKey())
                .subscribe(b -> adapter.removeItem(position),
                        e -> DLogUtil.e(e.getMessage())));
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
