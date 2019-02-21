package com.teame.boostcamp.myapplication.ui.goodscart;

import android.text.TextUtils;

import com.teame.boostcamp.myapplication.adapter.GoodsCartAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.model.repository.local.preference.CartPreference;
import com.teame.boostcamp.myapplication.model.repository.local.preference.CartPreferenceHelper;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.DataStringUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

public class GoodsCartPresenter implements GoodsCartContract.Presenter, DatePickerDialog.OnDateSetListener {

    private HashSet<String> hashTagSet = new HashSet<>();
    private GoodsCartAdapter adapter;
    private CartPreferenceHelper cartPreferenceHelper;
    private GoodsCartContract.View view;
    private List<Goods> itemList;
    private CompositeDisposable disposable = new CompositeDisposable();
    private MyListRepository repository;
    GoodsListHeader header ;

    public GoodsCartPresenter(GoodsCartContract.View view) {
        this.view = view;
        cartPreferenceHelper = new CartPreference();
        repository = MyListRepository.getInstance();
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        DLogUtil.e(""+year+" "+monthOfYear+" "+dayOfMonth);
    }

    @Override
    public int loadData(GoodsCartAdapter adapter) {
        this.adapter = adapter;
        List<Goods> list = cartPreferenceHelper.getGoodsCartList();
        itemList = list;
        header = cartPreferenceHelper.getListHeader();
        for(String hash : header.getHashTag().keySet()){
            hashTagSet.add(hash);
        }
        adapter.initItems(list);
        view.setLoadData(header);
        return list.size();
    }

    @Override
    public void deleteItem(int position) {
        Goods item = adapter.getItem(position);

        List<Goods> list = cartPreferenceHelper.getGoodsCartList();

        int listPosition = -1;
        if (list.contains(item)) {
            for (int i = 0; i < list.size(); i++) {
                if (TextUtils.equals(list.get(i).getName(), item.getName())) {
                    listPosition = i;
                    break;
                }
            }
        }

        if (listPosition != -1) {
            list.remove(position);
            adapter.removeItem(position);
        }
        cartPreferenceHelper.saveGoodsCartList(list);
    }

    @Override
    public GoodsListHeader getHeaderData() {
        GoodsListHeader header = cartPreferenceHelper.getListHeader();
        return header;
    }

    @Override
    public void saveCartList(String title) {
        List<String> hashList = new ArrayList<>(hashTagSet);
        Map<String, Boolean> hashTag = new HashMap<>();

        for (String tag : hashList) {
            hashTag.put(tag, true);
        }
        header.setTitle(title);
        header.setHashTag(hashTag);
        cartPreferenceHelper.saveListHeader(header);
        cartPreferenceHelper.saveGoodsCartList(itemList);

    }

    @Override
    public void detectIsAllCheck() {
        boolean allCheck = true;
        for (Goods item : itemList) {
            allCheck = allCheck && item.isCheck();
            if (!allCheck) {
                view.setAllorNoneCheck(false);
            }
        }
        view.setAllorNoneCheck(allCheck);
    }

    @Override
    public void calculatorPrice() {
        int total = 0;
        for (Goods item : itemList) {
            if (item.isCheck()) {
                total += item.totalPrice();
            }
        }
        String result = DataStringUtil.makeStringComma(Integer.toString(total));
        String formatedResult = String.format(Locale.getDefault(), "예상금액 : %s원", result);
        view.setResultPrice(formatedResult);
        if(itemList.size() == 0){
            view.emptyList();
        }
    }

    @Override
    public void removeHashTag(String tag) {
        hashTagSet.remove(tag);
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
    public void saveMyList(String title) {

        if (itemList.size() <= 0) {
            view.errorSaveGoods(0);
            return;
        }
        List<String> list = new ArrayList<>(hashTagSet);
        Map<String, Boolean> hashTag = new HashMap<>();

        for (String tag : list) {
            hashTag.put(tag, true);
        }

        header.setHashTag(hashTag);

        if (TextUtils.isEmpty(title)) {
            view.errorSaveGoods(1);
            return;
        }
        header.setTitle(title);

        //TODO: TEST 코드 입니다.
        if(header.getStartDate()==null||header.getEndDate()==null){
            header.setStartDate(Calendar.getInstance().getTime());
            header.setEndDate(Calendar.getInstance().getTime());
        }

        DLogUtil.d(itemList.toString());
        DLogUtil.d(header.toString());
        DLogUtil.d(list.toString());

        disposable.add(repository.saveMyList(itemList, list, header)
                .subscribe(b -> {
                            view.successSave();
                            cartPreferenceHelper.clearPreferenceData();
                        },
                        e -> DLogUtil.e(e.getMessage())));
    }
}
