package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemMainOtherListBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MainOtherListRecyclerAdapter extends BaseRecyclerAdatper<GoodsListHeader, MainOtherListRecyclerAdapter.ViewHolder> {

    private OnItemClickListener onShowDetailClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_other_list, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);

        holder.binding.buttonDetail.setOnClickListener(view -> {
            if (onShowDetailClickListener != null) {
                DLogUtil.d("position :" + holder.getLayoutPosition());
                int position = holder.getLayoutPosition();
                onShowDetailClickListener.onItemClick(view, position);
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
