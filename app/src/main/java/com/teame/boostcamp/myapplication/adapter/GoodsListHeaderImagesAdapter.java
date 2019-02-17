package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemMyListImageBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsListHeaderImagesAdapter extends BaseRecyclerAdatper<String, GoodsListHeaderImagesAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_list_image, parent, false);
        final GoodsListHeaderImagesAdapter.ViewHolder holder = new GoodsListHeaderImagesAdapter.ViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = itemList.get(position);
        holder.binding.setImg(url);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMyListImageBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}