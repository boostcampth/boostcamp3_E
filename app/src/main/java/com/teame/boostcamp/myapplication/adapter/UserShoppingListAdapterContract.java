package com.teame.boostcamp.myapplication.adapter;

import com.teame.boostcamp.myapplication.adapter.listener.OnUserShoppingItemClick;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;

public interface UserShoppingListAdapterContract {
    interface View{
        void setOnClickListener(OnUserShoppingItemClick listener);
        void selectAll(boolean state);
    }
    interface Model{
        Goods getGoods(int position);
    }
}
