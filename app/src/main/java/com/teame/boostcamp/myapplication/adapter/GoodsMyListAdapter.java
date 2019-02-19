package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemCartBinding;
import com.teame.boostcamp.myapplication.databinding.ItemMyCheckGoodsBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsMyListAdapter extends BaseRecyclerAdatper<Goods, GoodsMyListAdapter.ViewHolder> {

    private OnItemClickListener onItemCheckListener;

    @NonNull
    @Override
    public GoodsMyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_check_goods, parent, false);
        final GoodsMyListAdapter.ViewHolder holder = new GoodsMyListAdapter.ViewHolder(itemView);


        holder.binding.cbSelect.setOnCheckedChangeListener((buttonView, isCheck) -> {

            int position = holder.getLayoutPosition();
            itemList.get(position).setCheck(isCheck);
            if (onItemCheckListener != null) {
                onItemCheckListener.onItemClick(buttonView, position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsMyListAdapter.ViewHolder holder, int position) {
        Goods item = itemList.get(position);
        holder.binding.setItem(item);
        holder.binding.cbSelect.setChecked(item.isCheck());    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMyCheckGoodsBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void setOnItemCheckListener(OnItemClickListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    public void allCheck(boolean isCheck) {
        for (Goods item : itemList) {
            item.setCheck(isCheck);
        }
    }
}
