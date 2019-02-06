package com.teame.boostcamp.myapplication.adapter.SearchAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemExSearchBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ExListAdapter extends RecyclerView.Adapter<ExListAdapter.ExListView> implements ExListAdapterContract.Model, ExListAdapterContract.View {
    private ArrayList<String> list=new ArrayList<>();
    private OnItemClickListener clickListener;
    private EndlessScrollListener endlessListener;

    public ExListAdapter(){
        clickListener= __ -> {};
        endlessListener= () -> {};
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener=listener;
    }

    public void setEndlessScrollListener(EndlessScrollListener listener){
        endlessListener=listener;
    }

    @Override
    public void add(String text) {
        notifyDataSetChanged();
    }

    @Override
    public void setList(ArrayList<String> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public List<String> getList() {
        return list;
    }

    @NonNull
    @Override
    public ExListView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExSearchBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_ex_search,parent,false);
        return new ExListView(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExListView holder, int position) {
        holder.setText(list.get(position));
        holder.itemView.setOnClickListener(v -> {
            clickListener.onItemClick(list.get(position));
        });
        if(position==getItemCount()-1){
            endlessListener.onLastPosition();
        }
    }

    @Override
    public boolean searchList(String text) {
        //배열 내에 특정 문자가 있는지 검사
        for(String str:list){
            if(str.equals(text))
                return false;
        }
        return true;
    }

    @Override
    public void searchText(String text) {
        //특정 문자열이 포함되어 있는지 검사
    }

    @Override
    public int getItemCount() {
        if(list==null)
            return 0;
        else
            return list.size();
    }


    public static class ExListView extends RecyclerView.ViewHolder{
        private ItemExSearchBinding binding;
        public ExListView(View v, ItemExSearchBinding binding){
            super(v);
            this.binding=binding;
        }
        public void setText(String text){
            binding.tvExPlace.setText(text);
            long now=System.currentTimeMillis();
            Date date=new Date(now);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            binding.tvSearchDate.setText(sdf.format(date));
        }
    }
    public interface EndlessScrollListener{
        void onLastPosition();
    }
}
