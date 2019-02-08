package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemMyListHeaderBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsListHeaderRecyclerAdapter extends BaseRecyclerAdatper<GoodsListHeader, GoodsListHeaderRecyclerAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private OnItemClickListener onItemAlaramListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_list_header, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);

        holder.binding.getRoot().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                DLogUtil.d("position :" + holder.getLayoutPosition());
                int position = holder.getLayoutPosition();
                onItemClickListener.onItemClick(view, position);
            }
        });

        holder.binding.ivAlarm.setOnClickListener(view -> {
            if (onItemAlaramListener != null) {
                DLogUtil.d("position :" + holder.getLayoutPosition());
                int position = holder.getLayoutPosition();
                onItemAlaramListener.onItemClick(view, position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setItem(itemList.get(position));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemAlaramListener(OnItemClickListener onItemAlaramListener){
        this.onItemAlaramListener = onItemAlaramListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ItemMyListHeaderBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
