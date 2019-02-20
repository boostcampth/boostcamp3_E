package com.teame.boostcamp.myapplication.ui.selectedgoods;

import com.teame.boostcamp.myapplication.adapter.GoodsMyListAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.model.repository.local.preference.CheckListPreferences;
import com.teame.boostcamp.myapplication.model.repository.local.preference.CheckListPreferencesHelper;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.DataStringUtil;

import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class SelectedGoodsPresenter implements SelectedGoodsContract.Presenter {

    private SelectedGoodsContract.View view;
    private MyListRepository myListRepository;
    private CompositeDisposable disposable;
    private GoodsMyListAdapter adapter;
    private List<Goods> itemList;
    private CheckListPreferencesHelper preferences;

    SelectedGoodsPresenter(SelectedGoodsContract.View view, MyListRepository myListRepository) {
        this.view = view;
        this.myListRepository = myListRepository;
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
    public void loadListData(GoodsMyListAdapter adapter, String headerUid) {
        this.adapter = adapter;
        disposable.add(myListRepository.getMyListItems(headerUid)
                .subscribe(
                        list -> {
                            HashMap<String, Boolean> checkMap = preferences.getMyListCheck();
                            for (Goods item : list) {
                                if (checkMap.get(item.getKey()) == null) {
                                    item.setCheck(false);
                                } else {
                                    boolean isCheck = checkMap.get(item.getKey());
                                    item.setCheck(isCheck);
                                }

                            }
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

    @Override
    public void calculatorPrice() {
        int resultPrice = 0;
        int remainCount = 0;
        int totalPrice = 0;
        boolean isTotalAlpha = false;
        boolean isAlpha = false;
        boolean buyAlpha = false;

        String formatedResult;

        if (itemList == null) {
            formatedResult = "통신에 장애가 있어요 :<";
            view.setResultPrice(formatedResult, formatedResult, formatedResult);
            return;
        }

        for (Goods item : itemList) {
            totalPrice += item.totalPrice();
            if (item.getLprice() == null) {
                isTotalAlpha = true;
            }

            if (item.isCheck() && item.getLprice() == null) {
                buyAlpha = true;
            }
            if (!item.isCheck()) {
                resultPrice += item.totalPrice();
                remainCount++;
                if (item.getLprice() == null) {
                    isAlpha = true;
                }
            }
        }

        String total = DataStringUtil.makeStringComma(Integer.toString(totalPrice)) + "원";
        if (isTotalAlpha) {
            total += " + α";
        }

        String buy = DataStringUtil.makeStringComma(Integer.toString(totalPrice - resultPrice)) + "원";
        if (buyAlpha) {
            buy += " + α";
        }

        String remain = DataStringUtil.makeStringComma(Integer.toString(resultPrice)) + "원";
        if (isAlpha) {
            remain += " + α";
        }

        view.setResultPrice(total, buy, remain);

        if (itemList.size() == 0 || remainCount == 0) {
            view.completeMyList();
            return;
        }

    }

    @Override
    public void setMyListId(String uid) {
        preferences = new CheckListPreferences(uid);
    }

    @Override
    public void saveCheckStatus(int position) {
        Goods item = itemList.get(position);

        HashMap<String, Boolean> map = preferences.getMyListCheck();
        map.put(item.getKey(), item.isCheck());

        preferences.saveMyListCheck(map);
    }

}
