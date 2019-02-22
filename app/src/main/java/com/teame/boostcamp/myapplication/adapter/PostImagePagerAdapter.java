package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemPostImagePagerBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

public class PostImagePagerAdapter extends PagerAdapter {
    private List<String> imagePathList;
    ItemPostImagePagerBinding binding;

    public PostImagePagerAdapter(ArrayList<String> imagePathList) {
        this.imagePathList = imagePathList;
    }

    @Override
    public int getCount() {
        return imagePathList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        StorageReference mStorageRef;
        binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.item_post_image_pager, container, false);
        mStorageRef = FirebaseStorage.getInstance().getReference(imagePathList.get(position));
        Glide.with(container.getContext()).load(mStorageRef).into(binding.ivPostImage);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
