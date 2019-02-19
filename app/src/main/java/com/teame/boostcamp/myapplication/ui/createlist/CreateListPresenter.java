package com.teame.boostcamp.myapplication.ui.createlist;

import android.text.TextUtils;

import com.teame.boostcamp.myapplication.adapter.GoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.GoodsListRepository;
import com.teame.boostcamp.myapplication.model.repository.local.preference.CartPreference;
import com.teame.boostcamp.myapplication.model.repository.local.preference.CartPreferenceHelper;
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


public class CreateListPresenter implements CreateListContract.Presenter {

    private CreateListContract.View view;
    private GoodsListRepository shoppingListRepository;
    private CompositeDisposable disposable;
    private GoodsListRecyclerAdapter adapter;
    private List<Goods> originList = new ArrayList<>();
    private List<Goods> itemList = new ArrayList<>();
    private PublishSubject<String> subject = PublishSubject.create();
    private String debounceQuery = null;
    private CartPreferenceHelper cartPreferenceHelper;

    CreateListPresenter(CreateListContract.View view, GoodsListRepository shoppingListRepository) {
        this.view = view;
        this.shoppingListRepository = shoppingListRepository;
        cartPreferenceHelper = new CartPreference();
        disposable = new CompositeDisposable();

        disposable.add(subject.debounce(200, TimeUnit.MILLISECONDS, Schedulers.computation())
                .delay(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(text -> {
                    if (TextUtils.isEmpty(debounceQuery)) {
                        ArrayList<Goods> goods = new ArrayList<>();
                        for (Goods target : originList) {
                            goods.add(target.clone());
                        }
                        adapter.setData(goods);
                        return text;
                    } else {
                        return text;
                    }
                })
                .filter(query -> (!TextUtils.isEmpty(query) && query.equals(debounceQuery)))
                .subscribe(result -> {
                    ArrayList<Goods> goods = new ArrayList<>();
                    for (Goods target : originList) {
                        if (SearchUtil.matchString(target.getName(), result)) {
                            goods.add(target.clone());
                        }
                    }
                    view.resultSearchScreen(goods.size());
                    adapter.setData(goods);
                }));

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
    public void addGoods() {
        view.goAddItem(debounceQuery);
    }

    @Override
    public void diffSerchList(String query) {
        subject.onNext(query);
        debounceQuery = query;

    }

    @Override
    public void removeCart() {
        cartPreferenceHelper.clearPreferenceData();
    }

    @Override
    public void getDetailItemUid(int position) {
        Goods item = adapter.getItem(position);
        view.showDetailItem(item);
    }

    /**
     * 쇼핑리스트를 고르는데 필요한 정보를 가져옴
     */
    @Override
    public void loadListData(GoodsListRecyclerAdapter adapter, String nation, String city) {
        this.adapter = adapter;
        disposable.add(shoppingListRepository.getItemList(nation, city)
                .subscribe(
                        list -> {
                            itemList = list;
                            for (Goods item : itemList) {
                                originList.add(item.clone());
                            }
                            adapter.initItems(list);
                            view.finishLoad(list.size());
                            DLogUtil.d(list.toString());
                        },
                        e -> {
                            DLogUtil.d(e.getMessage());
                            view.finishLoad(Constant.FAIL_LOAD);
                        }
                )
        );
    }

    /**
     * 선택한 아이템의 체크여부에 따라 HashMap에 추가하거나 제거함
     */
    @Override
    public void setOriginList() {
        ArrayList<Goods> goods = new ArrayList<>();
        for (Goods target : originList) {
            goods.add(target.clone());
        }
        adapter.setData(goods);
    }

    @Override
    public void saveListHeader(GoodsListHeader header) {
        cartPreferenceHelper.saveListHeader(header);
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

    @Override
    public void backCreateList() {
        if (debounceQuery != null) {
            debounceQuery = null;
            setOriginList();
        } else {
            view.backActivity();
        }
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
        originList.add(item);
        itemList.add(item);
        view.successAddCart();
    }


}
