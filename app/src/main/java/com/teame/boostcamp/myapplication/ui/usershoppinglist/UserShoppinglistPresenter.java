package com.teame.boostcamp.myapplication.ui.usershoppinglist;


import android.text.TextUtils;

import com.teame.boostcamp.myapplication.adapter.listener.OnUserShoppingItemClick;
import com.teame.boostcamp.myapplication.adapter.UserShoppingListAdapterContract;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.local.preference.CartPreference;
import com.teame.boostcamp.myapplication.model.repository.local.preference.CartPreferenceHelper;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.List;

public class UserShoppinglistPresenter implements UserShoppinglistContract.Presenter, OnUserShoppingItemClick {

    private UserShoppinglistContract.View view;
    private boolean canExit=true;
    private GoodsListHeader header;
    private ArrayList<Goods> goodslist;
    private ArrayList<Goods> selectedList=new ArrayList<>();
    private UserShoppingListAdapterContract.View adapterView;
    private UserShoppingListAdapterContract.Model adapterModel;
    private CartPreferenceHelper cartPreferenceHelper;
    @Override
    public void addCartGoods(List<Goods> goodsList) {
        List<Goods> list = cartPreferenceHelper.getGoodsCartList();

        for(Goods item : goodsList){
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
        }
        cartPreferenceHelper.saveGoodsCartList(list);
    }

    @Override
    public void OnItemClick(boolean isCheck, int position) {
        if(isCheck)
            selectedList.add(adapterModel.getGoods(position));
        else {
            int removeIdx=selectedList.indexOf(adapterModel.getGoods(position));
            selectedList.remove(removeIdx);
        }
        if(selectedList.size()==goodslist.size())
            view.setSelectAllState(true);
        else
            view.setSelectAllState(false);
        if(canExit)
            canExit=false;
        DLogUtil.e(selectedList.toString());
    }

    @Override
    public boolean getExit() {
        return canExit;
    }

    @Override
    public void setExit(boolean state) {
        canExit=state;
    }

    @Override
    public void selectAll(boolean state) {
        if(state) {
            adapterView.selectAll(state);
            selectedList.clear();
            selectedList.addAll(goodslist);
            if(canExit)
                canExit=false;
        }
        else{
            adapterView.selectAll(state);
            selectedList.clear();
        }
    }

    @Override
    public void setAdapter(UserShoppingListAdapterContract.View adapterView, UserShoppingListAdapterContract.Model adapterModel) {
        this.adapterModel=adapterModel;
        this.adapterView=adapterView;
        adapterView.setOnClickListener(this);
    }

    @Override
    public ArrayList<Goods> getSelectedList() {
        return selectedList;
    }

    public UserShoppinglistPresenter(UserShoppinglistContract.View view, GoodsListHeader header, ArrayList<Goods> goodslist){
        this.view=view;
        this.header=header;
        this.goodslist=goodslist;
        cartPreferenceHelper = new CartPreference();
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
