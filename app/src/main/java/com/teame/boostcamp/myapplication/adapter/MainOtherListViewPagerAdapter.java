package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemMainOtherListBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

public class MainOtherListViewPagerAdapter extends PagerAdapter {

    private OnItemClickListener onShowDetailClickListener;
    private List<GoodsListHeader> headerlist = new ArrayList<>();

    public MainOtherListViewPagerAdapter() {

    }

    public void setHeaderlist(List<GoodsListHeader> list){
        headerlist.clear();
        headerlist.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void setOnClickListener(OnItemClickListener listener){
        onShowDetailClickListener=listener;
    }

    public GoodsListHeader getItem(int position) {
        return headerlist.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ItemMainOtherListBinding binding;
        binding=DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_main_other_list,container,false);
        binding.setItem(headerlist.get(position));
        binding.mcvUserHeader.setOnClickListener(v->{
            onShowDetailClickListener.onItemClick(v,position);
        });
        // 해쉬테그가 없으면 GONE
        if (headerlist.get(position).getHashTag().size() > 0) {
            binding.llEmptyView.setVisibility(View.GONE);
            binding.cgHashTag.setVisibility(View.VISIBLE);
            for (String tag : headerlist.get(position).getHashTag().keySet()) {
                Chip chip = new Chip(container.getContext());
                chip.setText("#" + tag);
                chip.setTextColor(ContextCompat.getColor(container.getContext(), R.color.colorIphoneBlack));
                chip.setClickable(false);
                chip.setCheckable(false);
                binding.cgHashTag.addView(chip);
            }
        }
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return headerlist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }
}
