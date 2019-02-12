package com.teame.boostcamp.myapplication.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemDetailReplyBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsDetailRecyclerAdapter extends BaseRecyclerAdatper<Reply, GoodsDetailRecyclerAdapter.ViewHolder> {


    private OnItemClickListener onItemDeleteListener;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_reply, parent, false);
        final GoodsDetailRecyclerAdapter.ViewHolder holder = new GoodsDetailRecyclerAdapter.ViewHolder(itemView);

        holder.binding.ivDelete.setOnClickListener(view -> {
            if (onItemDeleteListener != null) {
                DLogUtil.d("position :" + holder.getLayoutPosition());
                int position = holder.getLayoutPosition();
                onItemDeleteListener.onItemClick(view, position);
            }
        });


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reply reply = itemList.get(position);
        int ratio = reply.getRatio().intValue();
        holder.binding.setReply(reply);
        DLogUtil.e("reply : " + reply.getRatio().intValue());
        if (TextUtils.equals(auth.getUid(), reply.getWriter())) {
            holder.binding.ivDelete.setVisibility(View.VISIBLE);
        } else {
            holder.binding.ivDelete.setVisibility(View.GONE);
        }
        holder.binding.tvItemRatio.setText(String.valueOf(ratio));
    }


    public void setOnItemDeleteListener(OnItemClickListener onItemDeleteListener) {
        this.onItemDeleteListener = onItemDeleteListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemDetailReplyBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
