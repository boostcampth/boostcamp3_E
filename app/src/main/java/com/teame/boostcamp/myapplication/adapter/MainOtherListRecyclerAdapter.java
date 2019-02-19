package com.teame.boostcamp.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemMainOtherListBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

public class MainOtherListRecyclerAdapter extends PagerAdapter {

    private OnItemClickListener onShowDetailClickListener;
    private List<GoodsListHeader> headerlist=new ArrayList<>();

    public MainOtherListRecyclerAdapter(List<GoodsListHeader> headerlist) {
        this.headerlist = headerlist;
    }

    public void setOnClickListener(OnItemClickListener listener){
        onShowDetailClickListener=listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ItemMainOtherListBinding binding;
        binding=DataBindingUtil.inflate(LayoutInflater.from(container.getContext()),R.layout.item_main_other_list,container,false);
        binding.setItem(headerlist.get(position));
        binding.
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoodsListHeader goodsListHeader = itemList.get(position);
        holder.binding.setItem(goodsListHeader);

        // 해쉬테그가 없으면 GONE
        if (goodsListHeader.getHashTag().size() == 0) {
            holder.binding.cgHashTag.setVisibility(View.GONE);
        } else {
            holder.binding.cgHashTag.setVisibility(View.VISIBLE);
            for (String tag : goodsListHeader.getHashTag().keySet()) {
                Chip chip = new Chip(holder.itemView.getContext());
                chip.setText("#" + tag);
                chip.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorIphoneBlack));
                chip.setClickable(false);
                chip.setCheckable(false);
                holder.binding.cgHashTag.addView(chip);
            }
        }

    }

    public void setOnShowDetailClickListener(OnItemClickListener onItemClickListener) {
        this.onShowDetailClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMainOtherListBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }
    }
}
