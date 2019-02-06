package com.teame.boostcamp.myapplication.adapter.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

public interface ExListAdapterContract {
    interface View{
        void setOnItemClickListener(OnItemClickListener listener);
    }
    interface Model{
        void add(String text);
        void setList(ArrayList<String> list);
        List<String> getList();
        boolean searchList(String text);
        void searchText(String text);
    }
}
