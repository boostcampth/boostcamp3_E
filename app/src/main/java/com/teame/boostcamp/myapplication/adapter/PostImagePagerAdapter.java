package com.teame.boostcamp.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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
    private LayoutInflater inflater;
    private Context context;
    ItemPostImagePagerBinding binding;

    public PostImagePagerAdapter(Context context, ArrayList<String> imagePathList) {
        this.context = context;
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
        RequestOptions options = new RequestOptions();
        options.dontAnimate().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(new ColorDrawable(Color.WHITE))
        .autoClone();

        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_post_image_pager, container, false);
        //ImageView imv = (ImageView) v.findViewById(R.id.iv_post_image);
        mStorageRef = FirebaseStorage.getInstance().getReference(imagePathList.get(position));
        //mStorageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context).load(uri.toString()).apply(options).into(binding.ivPostImage));
        Glide.with(context).load(mStorageRef).into(binding.ivPostImage);
        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //container.removeView((View)object);
}
}
