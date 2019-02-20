package com.teame.boostcamp.myapplication.ui.goodsdetail;

import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.adapter.GoodsDetailRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.GoodsDetailRepository;
import com.teame.boostcamp.myapplication.model.repository.local.preference.CartPreference;
import com.teame.boostcamp.myapplication.model.repository.local.preference.CartPreferenceHelper;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class GoodsDetailPresenter implements GoodsDetailContract.Presenter {

    private GoodsDetailRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private GoodsDetailRecyclerAdapter adapter;
    private GoodsDetailContract.View view;
    private CartPreferenceHelper cartPreferenceHelper;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public GoodsDetailPresenter(GoodsDetailContract.View view, GoodsDetailRepository repository) {
        this.repository = repository;
        this.view = view;
        cartPreferenceHelper = new CartPreference();
    }

    @Override
    public void loadReplyList(GoodsDetailRecyclerAdapter adapter, String itemUid) {
        this.adapter = adapter;
        disposable.add(repository.getReplyList(itemUid)
                .subscribe(list -> {
                            for (int i = 0; i < list.size(); i++) {
                                if (TextUtils.equals(list.get(i).getWriter(), auth.getUid())) {
                                    list.add(0, list.remove(i));
                                }
                            }
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
    public void reLoadReplyList(String itemUid) {
        disposable.add(repository.getReplyList(itemUid)
                .subscribe(list -> {
                            for (int i = 0; i < list.size(); i++) {
                                if (TextUtils.equals(list.get(i).getWriter(), auth.getUid())) {
                                    list.add(0, list.remove(i));
                                }
                            }
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
                        }));
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
    public void addCartGoods(Goods item) {
        List<Goods> list = cartPreferenceHelper.getGoodsCartList();
        int postion = -1;
        if (list.contains(item)) {
            for (int i = 0; i < list.size(); i++) {
                if (TextUtils.equals(list.get(i).getName(), item.getName())) {
                    postion = i;
                    break;
                }
            }
        }

        if (postion != -1) {
            list.remove(postion);
        }
        list.add(item);

        cartPreferenceHelper.saveGoodsCartList(list);

        view.successAddCart();
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

    @Override
    public void getShoppingListCount() {
        List<Goods> list = cartPreferenceHelper.getGoodsCartList();
        if (list == null) {
            view.setBadge(String.valueOf(0));
        } else {
            view.setBadge(String.valueOf(list.size()));
        }
    }

}
