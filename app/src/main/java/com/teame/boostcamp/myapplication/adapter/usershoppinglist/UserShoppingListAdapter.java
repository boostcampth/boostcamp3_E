package com.teame.boostcamp.myapplication.adapter.usershoppinglist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.OnItemClickListener;
import com.teame.boostcamp.myapplication.databinding.ItemUsershoppinglistBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.ui.goodsdetail.GoodsDetailActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class UserShoppingListAdapter extends RecyclerView.Adapter<UserShoppingListAdapter.ViewHolder> implements UserShoppingListAdapterContract.View
        ,UserShoppingListAdapterContract.Model{
    private List<Goods> list=new ArrayList<>();
    private OnUserShoppingItemClick listener;

    public UserShoppingListAdapter(List<Goods> list) {
        this.list = list;
    }

    @Override
    public Goods getGoods(int position) {
        return list.get(position);
    }

    @Override
    public void setOnClickListener(OnUserShoppingItemClick listener) {
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usershoppinglist,parent,false);
        ViewHolder holder=new ViewHolder(view);
        holder.binding.cbUserCheck.setOnClickListener(v -> {
            listener.OnItemClick(holder.binding.cbUserCheck.isChecked(),holder.getLayoutPosition());
        });
        holder.binding.ivUserItemDetail.setOnClickListener(__ -> {
            GoodsDetailActivity.startActivity(parent.getContext(),list.get(holder.getLayoutPosition()));
        });
        return holder;
    }

    @Override
    public void selectAll(boolean state) {
        for(int i=0; i<list.size(); i++){
            if(state)
                list.get(i).setCheck(true);
            else
                list.get(i).setCheck(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goods goods=list.get(position);
        holder.binding.setItem(goods);
        holder.binding.cbUserCheck.setChecked(goods.isCheck());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ItemUsershoppinglistBinding binding;
        public ViewHolder(View v){
            super(v);
            binding=DataBindingUtil.bind(v);
        }
    }
}
