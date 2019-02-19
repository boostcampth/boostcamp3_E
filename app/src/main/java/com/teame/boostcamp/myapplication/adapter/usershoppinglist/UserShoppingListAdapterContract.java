package com.teame.boostcamp.myapplication.adapter.usershoppinglist;

import com.teame.boostcamp.myapplication.adapter.OnItemClickListener;
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
