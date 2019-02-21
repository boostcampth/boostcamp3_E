package com.teame.boostcamp.myapplication.util;

import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;

import java.util.ArrayList;

import androidx.recyclerview.widget.DiffUtil;

public class GoodsListHeaderDiffUtilCallBack extends DiffUtil.Callback {
    ArrayList<GoodsListHeader> newList;
    ArrayList<GoodsListHeader> oldList;

    public GoodsListHeaderDiffUtilCallBack(ArrayList<GoodsListHeader> newList, ArrayList<GoodsListHeader> oldList) {
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
        return newList.get(newItemPosition).getTitle() == oldList.get(oldItemPosition).getTitle();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).equals(oldList.get(oldItemPosition));
    }
}

