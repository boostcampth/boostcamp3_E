package com.teame.boostcamp.myapplication.adapter;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.base.BaseRecyclerAdatper;
import com.teame.boostcamp.myapplication.adapter.listener.OnItemClickListener;
import com.teame.boostcamp.myapplication.databinding.ItemMyCheckGoodsBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsMyListAdapter extends BaseRecyclerAdatper<Goods, GoodsMyListAdapter.ViewHolder> {

    private OnItemClickListener onItemCheckListener;

    @NonNull
    @Override
    public GoodsMyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_check_goods, parent, false);
        final GoodsMyListAdapter.ViewHolder holder = new GoodsMyListAdapter.ViewHolder(itemView);


        holder.binding.cbSelect.setClickable(false);
        holder.binding.cbSelect.setOnTouchListener(null);

        holder.binding.getRoot().setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            boolean currentCheck = itemList.get(position).isCheck();

            if (currentCheck) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setMessage("이미 구입한 항목입니다. \n취소하시겠습니까?")
                        .setPositiveButton(parent.getContext().getString(R.string.confirm), (__, ___) -> {
                            itemList.get(position).setCheck(false);
                            holder.binding.cbSelect.setChecked(false);
                            if (onItemCheckListener != null) {
                                onItemCheckListener.onItemClick(view, position);
                            }
                        })
                        .setNegativeButton(R.string.cancle, (dialogInterface, i) -> {

                        })
                        .setCancelable(true);

                final AlertDialog dialog = builder.create();
                dialog.setOnShowListener(__ -> {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(parent.getContext(), R.color.colorAccent));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(parent.getContext(), R.color.colorAccent));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.colorClear));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.colorClear));

                });

                dialog.show();
            } else {
                itemList.get(position).setCheck(true);
                holder.binding.cbSelect.setChecked(true);
                if (onItemCheckListener != null) {
                    onItemCheckListener.onItemClick(view, position);
                }
            }


        });
        holder.binding.cbSelect.setOnCheckedChangeListener((buttonView, isCheck) -> {


        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsMyListAdapter.ViewHolder holder, int position) {
        Goods item = itemList.get(position);
        holder.binding.setItem(item);
        holder.binding.cbSelect.setChecked(item.isCheck());
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

    public void allCheck(boolean isCheck) {
        for (Goods item : itemList) {
            item.setCheck(isCheck);
        }
    }
}
