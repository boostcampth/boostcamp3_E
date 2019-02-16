package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemCartBinding;
import com.teame.boostcamp.myapplication.databinding.ItemMyCheckGoodsBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsMyListAdapter extends BaseRecyclerAdatper<Goods, GoodsMyListAdapter.ViewHolder> {

    private OnItemClickListener onItemCheckListener;
    private OnItemClickListener onItemSpinnerListener;
    private OnItemClickListener onItemDeleteListener;

    @NonNull
    @Override
    public GoodsMyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_check_goods, parent, false);
        final GoodsMyListAdapter.ViewHolder holder = new GoodsMyListAdapter.ViewHolder(itemView);


        holder.binding.cbSelect.setOnCheckedChangeListener((buttonView, isCheck) -> {

            int position = holder.getLayoutPosition();
            itemList.get(position).setCheck(isCheck);
            if (onItemCheckListener != null) {
                onItemCheckListener.onItemClick(buttonView, position);
            }
        });


        holder.binding.tvDelete.setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            if (onItemDeleteListener != null) {
                onItemDeleteListener.onItemClick(view, position);
            }
        });

        holder.binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int holderPosition = holder.getLayoutPosition();
                String selected = adapterView.getItemAtPosition(position).toString();
                itemList.get(holderPosition).setCount(Integer.valueOf(selected));

                if (onItemSpinnerListener != null) {
                    onItemSpinnerListener.onItemClick(view, holderPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsMyListAdapter.ViewHolder holder, int position) {
        Goods item = itemList.get(position);
        holder.binding.setItem(item);
        holder.binding.spinner.setSelection(item.getCount() - 1);
        holder.itemView.findViewById(R.id.pb_deleting).setVisibility(View.GONE);
        holder.itemView.findViewById(R.id.tv_delete).setVisibility(View.VISIBLE);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMyCheckGoodsBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void setOnItemCheckListener(OnItemClickListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    public void setOnItemSpinnerListener(OnItemClickListener onItemSpinnerListener) {
        this.onItemSpinnerListener = onItemSpinnerListener;
    }

    public void setOnItemDeleteListener(OnItemClickListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    public void allCheck(boolean isCheck) {
        for (Goods item : itemList) {
            item.setCheck(isCheck);
        }
    }
}
