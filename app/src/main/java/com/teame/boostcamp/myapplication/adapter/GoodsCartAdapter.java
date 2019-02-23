package com.teame.boostcamp.myapplication.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemCartBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsCartAdapter extends BaseRecyclerAdatper<Goods, GoodsCartAdapter.ViewHolder> {

    private OnItemClickListener onItemCheckListener;
    private OnItemClickListener onItemSpinnerListener;
    private OnItemClickListener onItemDeleteListener;

    @NonNull
    @Override
    public GoodsCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        final GoodsCartAdapter.ViewHolder holder = new GoodsCartAdapter.ViewHolder(itemView);


        holder.binding.cbSelect.setClickable(false);
        holder.binding.cbSelect.setOnTouchListener(null);

        holder.binding.getRoot().setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            boolean currentCheck = itemList.get(position).isCheck();

            if (currentCheck) {
                itemList.get(position).setCheck(false);
                holder.binding.cbSelect.setChecked(false);
                if (onItemCheckListener != null) {
                    onItemCheckListener.onItemClick(view, position);
                }
            } else {
                itemList.get(position).setCheck(true);
                holder.binding.cbSelect.setChecked(true);
                if (onItemCheckListener != null) {
                    onItemCheckListener.onItemClick(view, position);
                }
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
    public void onBindViewHolder(@NonNull GoodsCartAdapter.ViewHolder holder, int position) {
        Goods item = itemList.get(position);
        holder.binding.setItem(item);
        holder.binding.spinner.setSelection(item.getCount() - 1);


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;

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
