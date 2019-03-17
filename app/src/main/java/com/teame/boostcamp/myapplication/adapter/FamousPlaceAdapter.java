package com.teame.boostcamp.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.listener.OnItemClickListener;
import com.teame.boostcamp.myapplication.databinding.ItemFamousPlaceBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Banner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

public class FamousPlaceAdapter extends PagerAdapter {
    ItemFamousPlaceBinding binding;
    Context context;
    List<Integer> drawableId = new ArrayList<>();
    List<Banner> bannerlist=new ArrayList<>();
    OnItemClickListener listener;

    public FamousPlaceAdapter(Context context, List<Integer> drawableId, List<Banner> bannerlist) {
        this.context = context;
        this.drawableId = drawableId;
        this.bannerlist=bannerlist;
    }

    public void setOnClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public int getCount() {
        return bannerlist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }
    public Banner getItem(int position){
        return bannerlist.get(position);
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_famous_place, container, false);
        binding.ivCountryImage.setImageResource(drawableId.get(position));
        binding.setBanner(bannerlist.get(position));
        binding.ivCountryImage.setOnClickListener(v -> {
            listener.onItemClick(v,position);
        });
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
