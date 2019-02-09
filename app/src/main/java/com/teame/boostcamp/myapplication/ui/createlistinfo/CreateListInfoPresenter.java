package com.teame.boostcamp.myapplication.ui.createlistinfo;

import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class CreateListInfoPresenter implements CreateListInfoContract.Presenter {
    private CompositeDisposable disposable = new CompositeDisposable();
    private HashSet<String> hashTagSet = new HashSet<>();
    private CreateListInfoContract.View view;
    private MyListRepository repository;

    public CreateListInfoPresenter(CreateListInfoContract.View view, MyListRepository repository) {
        this.view = view;
        this.repository = repository;
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
    public void addHashTag(String tag) {
        if (hashTagSet.contains(tag)) {
            view.duplicationTag();
        } else {
            hashTagSet.add(tag);
            view.addedHashTag(tag);
        }
    }

    @Override
    public void removeHashTag(String tag) {
        hashTagSet.remove(tag);
    }

    @Override
    public void saveMyList(List<Goods> goodsList, GoodsListHeader header) {
        List<String> list = new ArrayList<>(hashTagSet);
        DLogUtil.d(goodsList.toString());
        DLogUtil.d(header.toString());
        DLogUtil.d(list.toString());
        disposable.add(repository.saveMyList(goodsList, list, header)
                .subscribe(b -> view.successSave(),
                        e -> DLogUtil.e(e.getMessage())));
    }
}
