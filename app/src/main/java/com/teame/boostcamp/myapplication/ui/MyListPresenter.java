package com.teame.boostcamp.myapplication.ui;


import com.teame.boostcamp.myapplication.adapter.ItemListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.ItemListHeader;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import io.reactivex.disposables.CompositeDisposable;

public class MyListPresenter implements MyListContract.Presenter {

    private MyListRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ItemListHeaderRecyclerAdapter adapter;
    private MyListContract.View view;

    public MyListPresenter(MyListContract.View view, MyListRepository repository) {
        this.view = view;
        this.repository = repository;
    }
    @Override
    public void loadMyList(ItemListHeaderRecyclerAdapter adapter) {
        this.adapter = adapter;
        disposable.add(repository.getMyList()
                .subscribe(list -> {
                            DLogUtil.d(list.toString());
                            adapter.initItems(list);
                        },
                        e -> DLogUtil.e(e.getMessage()))
        );
    }

    @Override
    public void getMyListUid(int position) {
        ItemListHeader header = adapter.getItem(position);

        DLogUtil.d("position : " +position);
        DLogUtil.d(header.toString());

        String headerKey = header.getKey();
        view.showMyListItems(headerKey);
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
