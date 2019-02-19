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
import java.util.Locale;

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
                                    item.setCheck(checkMap.get(item.getKey()));
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
    public void deleteList() {

    }

    @Override
    public void detectIsAllCheck() {
        boolean allCheck = true;

        if (itemList == null)
            return;

        for (Goods item : itemList) {
            allCheck = allCheck && item.isCheck();
            if (!allCheck) {
//                view.setAllorNoneCheck(false);
            }
        }
        // 아이템이 모두 체크되면
        // view.setAllorNoneCheck(allCheck);
    }

    @Override
    public void calculatorPrice() {
        int total = 0;
        int remainCount = 0;
        boolean isAlpha = false;

        String formatedResult;

        if (itemList == null) {
            formatedResult = "통신에 장애가 있어요 :<";
            view.setResultPrice(formatedResult);
            return;
        }

        for (Goods item : itemList) {
            if (!item.isCheck()) {
                total += item.totalPrice();
                remainCount++;
                if (item.getLprice() == null) {
                    isAlpha = true;
                }
            }
        }

        String result = DataStringUtil.makeStringComma(Integer.toString(total));

        if (itemList.size() == 0) {
            view.setOfferDelete();
            return;
        } else if (remainCount == 0) {
            formatedResult = String.format(Locale.getDefault(), "모든 물품을 구입했어요!", result, remainCount);
        } else if (isAlpha) {
            formatedResult = String.format(Locale.getDefault(), "예상금액 : %s원 + α / %d개", result, remainCount);
        } else {
            formatedResult = String.format(Locale.getDefault(), "예상금액 : %s원 / %d개", result, remainCount);
        }

        view.setResultPrice(formatedResult);
    }

    @Override
    public void setMyListId(String uid) {
        preferences = new CheckListPreferences(uid);
    }

    @Override
    public void saveCheckStatus(int position) {
        Goods item = itemList.get(position);

        HashMap<String,Boolean> map = preferences.getMyListCheck();
        map.put(item.getKey(),item.isCheck());

        preferences.saveMyListCheck(map);
    }

}
