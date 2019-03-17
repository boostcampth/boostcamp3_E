package com.teame.boostcamp.myapplication.adapter;

import com.teame.boostcamp.myapplication.adapter.listener.OnItemDeleteClickListener;

import java.util.ArrayList;
import java.util.List;

public interface ExListAdapterContract {
    interface View{
        void setOnItemClickListener(OnItemDeleteClickListener listener);
    }
    interface Model{
        void add(String text);
        void setList(ArrayList<String> list);
        List<String> getList();
        void remove(int position);
        boolean searchList(String text);
        void searchText(String text);
    }
}
