package com.teame.boostcamp.myapplication.ui.usershoppinglist;


import com.teame.boostcamp.myapplication.adapter.usershoppinglist.OnUserShoppingItemClick;
import com.teame.boostcamp.myapplication.adapter.usershoppinglist.UserShoppingListAdapterContract;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;

public class UserShoppinglistPresenter implements UserShoppinglistContract.Presenter, OnUserShoppingItemClick {

    private UserShoppinglistContract.View view;
    private boolean canExit=true;
    private GoodsListHeader header;
    private ArrayList<Goods> goodslist;
    private ArrayList<Goods> selectedList=new ArrayList<>();
    private UserShoppingListAdapterContract.View adapterView;
    private UserShoppingListAdapterContract.Model adapterModel;
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
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
