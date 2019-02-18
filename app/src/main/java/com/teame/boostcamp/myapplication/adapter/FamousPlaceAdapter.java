package com.teame.boostcamp.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemFamousPlaceBinding;
import com.teame.boostcamp.myapplication.databinding.ItemPostImagePagerBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

public class FamousPlaceAdapter extends PagerAdapter {
    ItemFamousPlaceBinding binding;
    Context context;
    List<Integer> drawableId = new ArrayList<>();

    public FamousPlaceAdapter(Context context, List<Integer> drawableId) {
        this.context = context;
        this.drawableId = drawableId;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_famous_place, container, true);
        binding.ivCountryImage.setImageResource(drawableId.get(position));
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
