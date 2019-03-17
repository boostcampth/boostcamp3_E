package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.base.BaseRecyclerAdatper;
import com.teame.boostcamp.myapplication.adapter.listener.OnItemClickListener;
import com.teame.boostcamp.myapplication.databinding.ItemMyListHeaderBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.GoodsListHeaderDiffUtilCallBack;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsListHeaderRecyclerAdapter extends BaseRecyclerAdatper<GoodsListHeader, GoodsListHeaderRecyclerAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private OnItemClickListener onItemDeleteListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_list_header, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);

        holder.binding.getRoot().setOnClickListener(view -> {
            if (onItemClickListener != null) {
                DLogUtil.d("position :" + holder.getLayoutPosition());
                int position = holder.getLayoutPosition();
                onItemClickListener.onItemClick(view, position);
            }
        });

        holder.binding.viewFakeClick.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                if (onItemClickListener != null) {
                    DLogUtil.d("position :" + holder.getLayoutPosition());
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(view, position);
                }
                holder.binding.viewFakeClick.performClick();
            }
            return false;
        });

        holder.binding.ivDelete.setOnClickListener(view -> {
            if (onItemDeleteListener != null) {
                DLogUtil.d("position :" + holder.getLayoutPosition());
                int position = holder.getLayoutPosition();
                onItemDeleteListener.onItemClick(view, position);
            }
        });
        DLogUtil.w("create finish");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GoodsListHeader goodsListHeader = itemList.get(position);
        holder.binding.setItem(goodsListHeader);

        // 해쉬테그가 없으면 GONE
        if (goodsListHeader.getHashTag().size() == 0) {
            holder.binding.cgHashTag.setVisibility(View.GONE);
            holder.binding.cgHashTag.removeAllViews();
        } else {
            holder.binding.cgHashTag.setVisibility(View.VISIBLE);
            holder.binding.cgHashTag.removeAllViews();
            for (String tag : goodsListHeader.getHashTag().keySet()) {
                Chip chip = new Chip(holder.itemView.getContext());
                chip.setText("#" + tag);
                chip.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorIphoneBlack));
                chip.setClickable(false);
                chip.setCheckable(false);
                holder.binding.cgHashTag.addView(chip);
            }
        }
        // 이미지 리스트
        if (itemList.get(position).getImages().size() == 0) {
            holder.binding.rvImages.setVisibility(View.GONE);
        } else {
            holder.binding.rvImages.setVisibility(View.VISIBLE);
            ((GoodsListHeaderImagesAdapter) holder.binding.rvImages.getAdapter()).initItems(itemList.get(position).getImages());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemDeleteListener(OnItemClickListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMyListHeaderBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            binding.rvImages.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            GoodsListHeaderImagesAdapter imagesAdapter = new GoodsListHeaderImagesAdapter();
            imagesAdapter.setItemClickListener(view -> {
                onItemClickListener.onItemClick(view, 0);
            });
            binding.rvImages.setAdapter(imagesAdapter);
        }
    }

    public void setData(ArrayList<GoodsListHeader> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new GoodsListHeaderDiffUtilCallBack(newData, (ArrayList) itemList));
        diffResult.dispatchUpdatesTo(this);
        itemList.clear();
        this.itemList.addAll(newData);
    }
}
