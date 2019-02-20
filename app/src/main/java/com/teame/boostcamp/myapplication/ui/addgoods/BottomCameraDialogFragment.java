package com.teame.boostcamp.myapplication.ui.addgoods;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.LayoutAddGoodsBottomSheetBinding;
import com.teame.boostcamp.myapplication.ui.goodsdetail.BottomReplyDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class BottomCameraDialogFragment extends BottomSheetDialogFragment {

    private LayoutAddGoodsBottomSheetBinding binding;

    private View.OnClickListener onGalleryClickListener;
    private View.OnClickListener onCameraClickListener;

    public static BottomCameraDialogFragment newInstance() {
        BottomCameraDialogFragment bottomCameraDialogFragment = new BottomCameraDialogFragment();
        Bundle bundle = new Bundle();
        bottomCameraDialogFragment.setArguments(bundle);
        return bottomCameraDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_add_goods_bottom_sheet, container, false);

        binding.ivGalleryPick.setOnClickListener(v -> onGalleryClickListener.onClick(v));
        binding.ivTakePicture.setOnClickListener(v -> onCameraClickListener.onClick(v));
        return binding.getRoot();
    }

    public void setOnGalleryClickListener(View.OnClickListener onGalleryClickListener) {
        this.onGalleryClickListener = onGalleryClickListener;
    }

    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        this.onCameraClickListener = onCameraClickListener;
    }

}

