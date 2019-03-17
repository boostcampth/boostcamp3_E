package com.teame.boostcamp.myapplication.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.base.BaseRecyclerAdatper;
import com.teame.boostcamp.myapplication.adapter.listener.OnItemClickListener;
import com.teame.boostcamp.myapplication.databinding.ItemListGoodsBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.GoodsDiffUtilCallBack;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsListRecyclerAdapter extends BaseRecyclerAdatper<Goods, GoodsListRecyclerAdapter.ViewHolder> {
    private OnItemClickListener onItemDetailListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_goods, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);


        holder.binding.getRoot().setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            DLogUtil.e("리스너 :" + holder.getAdapterPosition());
            if (onItemDetailListener != null) {
                onItemDetailListener.onItemClick(view, position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goods item = itemList.get(position);
        holder.binding.setItem(item);
        DLogUtil.d(itemList.get(position).toString());
    }

    public void setOnItemDetailListener(OnItemClickListener onItemDetailListener) {
        this.onItemDetailListener = onItemDetailListener;
    }

    /**
     * ItemName을 기준으로 해당 아이템 position 반환
     */
    public int searchItem(Goods target) {
        if (itemList.contains(target)) {
            for (int i = 0; i < itemList.size(); i++) {
                if (TextUtils.equals(itemList.get(i).getName(), target.getName())) {
                    return i;
                }
            }
        }
        // 아이템이 없다면
        return -1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemListGoodsBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void setData(ArrayList<Goods> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new GoodsDiffUtilCallBack(newData, (ArrayList) itemList));
        diffResult.dispatchUpdatesTo(this);
        itemList.clear();
        this.itemList.addAll(newData);
    }
}
