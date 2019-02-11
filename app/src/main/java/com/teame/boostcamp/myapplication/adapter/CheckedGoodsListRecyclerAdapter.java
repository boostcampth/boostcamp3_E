package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemCheckedGoodsBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CheckedGoodsListRecyclerAdapter extends BaseRecyclerAdatper<Goods, CheckedGoodsListRecyclerAdapter.ViewHolder> {

    private OnItemClickListener onItemDeleteListener;

    @NonNull
    @Override
    public CheckedGoodsListRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checked_goods, parent, false);
        final CheckedGoodsListRecyclerAdapter.ViewHolder holder = new CheckedGoodsListRecyclerAdapter.ViewHolder(itemView);

        holder.binding.ivDeleteItem.setOnClickListener(view -> {
           if(onItemDeleteListener!=null){
               int position = holder.getLayoutPosition();
               onItemDeleteListener.onItemClick(view,position);
           }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckedGoodsListRecyclerAdapter.ViewHolder holder, int position) {
        holder.binding.setItem(itemList.get(position));

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCheckedGoodsBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void setOnItemDeleteListener(OnItemClickListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    /**
     * 최초 아이템 init
     */
    public void initItems(List<Goods> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }
}
