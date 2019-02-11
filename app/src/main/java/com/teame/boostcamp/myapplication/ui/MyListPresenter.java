package com.teame.boostcamp.myapplication.ui;


import com.teame.boostcamp.myapplication.adapter.GoodsListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import io.reactivex.disposables.CompositeDisposable;

public class MyListPresenter implements MyListContract.Presenter {

    private MyListRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private GoodsListHeaderRecyclerAdapter adapter;
    private MyListContract.View view;

    public MyListPresenter(MyListContract.View view, MyListRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadMyList(GoodsListHeaderRecyclerAdapter adapter) {
        this.adapter = adapter;
        disposable.add(repository.getMyList()
                .subscribe(list -> {
                            DLogUtil.d(list.toString());
                            view.finishLoad(list.size());
                            adapter.initItems(list);
                        },
                        e -> {
                            view.finishLoad(Constant.FAIL_LOAD);
                            DLogUtil.e(e.getMessage());
                        })
        );
    }

    @Override
    public void getMyListUid(int position) {
        GoodsListHeader header = adapter.getItem(position);

        DLogUtil.d("position : " + position);
        DLogUtil.d(header.toString());

        String headerKey = header.getKey();
        view.showMyListItems(headerKey);
    }

    @Override
    public void deleteMyList(int position) {
        GoodsListHeader header = adapter.getItem(position);
        disposable.add(repository.deleteMyList(header.getKey())
                .subscribe(b -> adapter.removeItem(position)
                        , e -> DLogUtil.e(e.getMessage()))
        );
    }

    @Override
    public void onAttach() {
        DLogUtil.d("onAttach");
    }

    @Override
    public void onDetach() {
        if (disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
