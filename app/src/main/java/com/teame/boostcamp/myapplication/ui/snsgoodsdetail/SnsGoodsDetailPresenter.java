package com.teame.boostcamp.myapplication.ui.snsgoodsdetail;

import com.teame.boostcamp.myapplication.adapter.GoodsDetailRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.GoodsDetailRepository;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class SnsGoodsDetailPresenter implements SnsGoodsDetailContract.Presenter {

    private GoodsDetailRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private GoodsDetailRecyclerAdapter adapter;
    private SnsGoodsDetailContract.View view;

    public SnsGoodsDetailPresenter(SnsGoodsDetailContract.View view) {
        this.repository = GoodsDetailRepository.getInstance();
        this.view = view;
    }

    @Override
    public void loadReplyList(GoodsDetailRecyclerAdapter adapter, String itemUid) {
        this.adapter = adapter;
        disposable.add(repository.getReplyList(itemUid)
                .subscribe(list -> {
                            adapter.initItems(list);
                            float totalRatio = 0;
                            for (Reply reply : list) {
                                totalRatio += reply.getRatio();
                            }
                            view.finishLoad(totalRatio, list.size());
                            DLogUtil.d(list.toString());
                        },
                        e -> {
                            view.finishLoad(0, Constant.FAIL_LOAD);
                            DLogUtil.e(e.getMessage());
                        })
        );
    }

    @Override
    public void writeReply(Reply item) {
        adapter.addItem(0, item);
        view.completeReloadReply();
    }

    @Override
    public void deleteReply(String itemId, int position) {
        Reply item = adapter.getItem(position);
        disposable.add(repository.deleteReply(itemId, item.getKey())
                .subscribe(b -> adapter.removeItem(position),
                        e -> DLogUtil.e(e.getMessage())));
    }

    @Override
    public Reply getItem(int position) {
        return adapter.getItem(position);
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
