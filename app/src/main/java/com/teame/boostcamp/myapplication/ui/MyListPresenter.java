package com.teame.boostcamp.myapplication.ui;


import android.text.TextUtils;

import com.teame.boostcamp.myapplication.adapter.GoodsListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.SearchUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MyListPresenter implements MyListContract.Presenter {

    private MyListRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private GoodsListHeaderRecyclerAdapter adapter;
    private MyListContract.View view;
    private String debounceQuery = null;
    private PublishSubject<String> subject = PublishSubject.create();
    private List<GoodsListHeader> originList = new ArrayList<>();
    private List<GoodsListHeader> itemList = new ArrayList<>();

    public MyListPresenter(MyListContract.View view, MyListRepository repository) {
        this.view = view;
        this.repository = repository;

        disposable.add(subject.debounce(200, TimeUnit.MILLISECONDS, Schedulers.computation())
                .delay(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(query -> (!TextUtils.isEmpty(query) && query.equals(debounceQuery)))
                .subscribe(result -> {
                    ArrayList<GoodsListHeader> goods = new ArrayList<>();
                    for (GoodsListHeader target : originList) {
                        if (SearchUtil.matchString(target.getTitle(), result)) {
                            goods.add(target.clone());
                        }
                    }
                    adapter.setData(goods);
                }));

    }
    /**
     * 선택한 아이템의 체크여부에 따라 HashMap에 추가하거나 제거함
     */
    public void setOriginList() {
        ArrayList<GoodsListHeader> goods = new ArrayList<>();
        for (GoodsListHeader target : originList) {
            goods.add(target.clone());
        }
        adapter.setData(goods);
    }

    @Override
    public void loadMyList(GoodsListHeaderRecyclerAdapter adapter) {
        this.adapter = adapter;

        disposable.add(repository.getMyList()
                .subscribe(list -> {
                            itemList = list;
                            for (GoodsListHeader item : itemList) {
                                originList.add(item.clone());
                            }

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
    public void reLoadMyList() {
        disposable.add(repository.getMyList()
                .subscribe(list -> {
                            itemList = list;
                            for (GoodsListHeader item : itemList) {
                                originList.add(item.clone());
                            }
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

    @Override
    public void diffSerchList(String query) {
        subject.onNext(query);
        debounceQuery = query;

    }


}
