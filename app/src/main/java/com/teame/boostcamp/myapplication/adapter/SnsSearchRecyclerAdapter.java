package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemSnsSearchBinding;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SnsSearchRecyclerAdapter extends BaseRecyclerAdatper<String, SnsSearchRecyclerAdapter.ViewHolder> {
    private OnItemClickListener onItemClickListener;
    private OnItemClickListener onDeleteListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sns_search, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);

        holder.binding.getRoot().setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            DLogUtil.e("리스너 :" + holder.getAdapterPosition());
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, position);
            }
        });

        holder.binding.ivRemove.setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            DLogUtil.e("리스너 :" + holder.getAdapterPosition());
            if (onDeleteListener != null) {
                onDeleteListener.onItemClick(view, position);
            }
        });
        return holder;
    }

    public void setOnDeleteListener(OnItemClickListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String search = itemList.get(position);
        holder.binding.setSearch(search);

        DLogUtil.d(itemList.get(position).toString());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemSnsSearchBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }
    public List<String> getItemList(){
        return this.itemList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
