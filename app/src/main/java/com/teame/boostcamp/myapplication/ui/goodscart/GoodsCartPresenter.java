package com.teame.boostcamp.myapplication.ui.goodscart;

import android.text.TextUtils;

import com.teame.boostcamp.myapplication.adapter.GoodsCartAdapter;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.preference.CartPreference;
import com.teame.boostcamp.myapplication.model.repository.preference.CartPreferenceHelper;
import com.teame.boostcamp.myapplication.util.DataStringUtil;

import java.util.List;
import java.util.Locale;

public class GoodsCartPresenter implements GoodsCartContract.Presenter {

    private GoodsCartAdapter adapter;
    private CartPreferenceHelper cartPreferenceHelper;
    private GoodsCartContract.View view;
    private List<Goods> itemList;

    public GoodsCartPresenter(GoodsCartContract.View view) {
        this.view = view;
        cartPreferenceHelper = new CartPreference();
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void loadData(GoodsCartAdapter adapter) {
        this.adapter = adapter;
        List<Goods> list = cartPreferenceHelper.getGoodsCartList();
        itemList = list;
        adapter.initItems(list);
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
    public void getSaveData() {
        GoodsListHeader header = cartPreferenceHelper.getListHeader();
        cartPreferenceHelper.saveGoodsCartList(itemList);
        if (itemList.size() <= 0) {
            view.noSelectGoods();
        } else {
            view.decide(header);
        }

    }

    @Override
    public void saveCartList() {
        cartPreferenceHelper.saveGoodsCartList(itemList);

    }

    @Override
    public void detectIsAllCheck() {
        boolean allCheck = true;
        for (Goods item : itemList) {
            allCheck = allCheck && item.isCheck();
            if (!allCheck) {
                view.setAllorNoneCheck(allCheck);
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
    }

}
