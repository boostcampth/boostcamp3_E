package com.teame.boostcamp.myapplication.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.base.BaseRecyclerAdatper;
import com.teame.boostcamp.myapplication.adapter.listener.OnItemClickListener;
import com.teame.boostcamp.myapplication.databinding.ItemLocationBaseListGoodsBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class LocationBaseGoodsListRecyclerAdapter extends BaseRecyclerAdatper<Goods, LocationBaseGoodsListRecyclerAdapter.ViewHolder> {

    private OnItemClickListener listener;

    public void setOnClickListener(OnItemClickListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location_base_list_goods, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(v,holder.getAdapterPosition());
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goods item = itemList.get(position);
        holder.binding.setItem(item);

        DLogUtil.d(itemList.get(position).toString());
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

        private ItemLocationBaseListGoodsBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }
}
