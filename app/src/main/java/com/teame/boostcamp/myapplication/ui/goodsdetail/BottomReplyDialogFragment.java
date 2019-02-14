package com.teame.boostcamp.myapplication.ui.goodsdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.LayoutReplyBottomSheetBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class BottomReplyDialogFragment extends BottomSheetDialogFragment {

    private static String EXTRA_IS_MINE = "EXTRA_IS_MINE";
    private LayoutReplyBottomSheetBinding binding;

    private View.OnClickListener onDeleteClickListener;
    private View.OnClickListener onReportClickListener;

    public static BottomReplyDialogFragment newInstance(boolean isMine) {
        BottomReplyDialogFragment bottomReplyDialogFragment = new BottomReplyDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(EXTRA_IS_MINE, isMine);
        bottomReplyDialogFragment.setArguments(bundle);
        return bottomReplyDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_reply_bottom_sheet, container, false);
        boolean isMine = false;
        if (getArguments() == null) {

        }
        isMine = getArguments().getBoolean(EXTRA_IS_MINE);
        View view = binding.getRoot();
        if(isMine){
            binding.tvDelete.setVisibility(View.VISIBLE);
            binding.tvReport.setVisibility(View.GONE);
        }else{
            binding.tvReport.setVisibility(View.VISIBLE);
            binding.tvDelete.setVisibility(View.GONE);
        }
        binding.tvDelete.setOnClickListener(v -> onDeleteClickListener.onClick(v));
        binding.tvReport.setOnClickListener(v -> onReportClickListener.onClick(v));
        return view;
    }

    public void setOnDeleteClickListener(View.OnClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public void setOnReportClickListener(View.OnClickListener onReportClickListener) {
        this.onReportClickListener = onReportClickListener;
    }

}