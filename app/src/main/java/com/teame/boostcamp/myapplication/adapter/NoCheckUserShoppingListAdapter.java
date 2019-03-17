package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemNoCheckShoppinglistBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.ui.goodsdetail.GoodsDetailActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class NoCheckUserShoppingListAdapter extends RecyclerView.Adapter<NoCheckUserShoppingListAdapter.ViewHolder>{
    private List<Goods> list=new ArrayList<>();

    public void setList(List<Goods> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_check_shoppinglist,parent,false);
        ViewHolder holder=new ViewHolder(view);
        holder.binding.ivNoCheckUserItemDetail.setOnClickListener(__ -> {
            GoodsDetailActivity.startActivity(parent.getContext(),list.get(holder.getLayoutPosition()));
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goods goods=list.get(position);
        holder.binding.setItem(goods);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ItemNoCheckShoppinglistBinding binding;
        public ViewHolder(View v){
            super(v);
            binding=DataBindingUtil.bind(v);
        }
    }
}
