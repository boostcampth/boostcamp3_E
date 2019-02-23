package com.teame.boostcamp.myapplication.ui.othershoppinglist;

import com.teame.boostcamp.myapplication.adapter.GoodsOtherListAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class OtherShoppingListPresenter implements OtherShoppingListContract.Presenter {

    private OtherShoppingListContract.View view;
    private MyListRepository myListRepository;
    private CompositeDisposable disposable;
    private GoodsOtherListAdapter adapter;
    private List<Goods> itemList;

    public OtherShoppingListPresenter(OtherShoppingListContract.View view) {
        this.view = view;
        this.myListRepository = MyListRepository.getInstance();
        disposable = new CompositeDisposable();
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

    /**
     * 쇼핑리스트를 고르는데 필요한 정보를 가져옴
     */
    @Override
    public void loadListData(GoodsOtherListAdapter adapter, String uid, String headerUid) {
        this.adapter = adapter;
        disposable.add(myListRepository.getOtherListItems(uid, headerUid)
                .subscribe(
                        list -> {
                            DLogUtil.e(headerUid);
                            DLogUtil.e(list.toString());
                            adapter.initItems(list);
                            itemList = list;
                            view.finishLoad(list.size());
                            DLogUtil.d(list.toString());
                        },
                        e -> {
                            view.finishLoad(Constant.FAIL_LOAD);
                            DLogUtil.d(e.getMessage());
                        }
                )
        );
    }
}
