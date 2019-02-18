package com.teame.boostcamp.myapplication.util;

import com.teame.boostcamp.myapplication.model.entitiy.Goods;

import java.util.ArrayList;

import androidx.recyclerview.widget.DiffUtil;

public class GoodsDiffUtilCallBack extends DiffUtil.Callback {
    ArrayList<Goods> newList;
    ArrayList<Goods> oldList;

    public GoodsDiffUtilCallBack(ArrayList<Goods> newList, ArrayList<Goods> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).getName() == oldList.get(oldItemPosition).getName();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).equals(oldList.get(oldItemPosition));
    }
}

