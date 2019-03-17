package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.base.BaseRecyclerAdatper;
import com.teame.boostcamp.myapplication.adapter.listener.OnItemClickListener;
import com.teame.boostcamp.myapplication.databinding.ItemOtherUserGoodsBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsOtherListAdapter extends BaseRecyclerAdatper<Goods, GoodsOtherListAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public GoodsOtherListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_other_user_goods, parent, false);
        final GoodsOtherListAdapter.ViewHolder holder = new GoodsOtherListAdapter.ViewHolder(itemView);


        holder.binding.getRoot().setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsOtherListAdapter.ViewHolder holder, int position) {
        Goods item = itemList.get(position);
        holder.binding.setItem(item);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOtherUserGoodsBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void allCheck(boolean isCheck) {
        for (Goods item : itemList) {
            item.setCheck(isCheck);
        }
    }
}
