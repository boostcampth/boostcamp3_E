package com.teame.boostcamp.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemPostReplyBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import androidx.annotation.NonNull;
import androidx.databinding.BindingConversion;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PostReplyAdapter extends BaseRecyclerAdatper<Reply, PostReplyAdapter.ViewHolder> {

    private OnItemClickListener onItemDeleteListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_reply, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);

        holder.binding.ibDeleteReply.setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            DLogUtil.e("리스너 :" + holder.getAdapterPosition());
            if (onItemDeleteListener != null) {
                onItemDeleteListener.onItemClick(view, position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reply item = itemList.get(position);
        holder.binding.setReply(item);
        holder.binding.setWriter(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        DLogUtil.d(itemList.get(position).toString());
    }

    public void setOnDeleteListener(OnItemClickListener onItemDetailListener) {
        this.onItemDeleteListener = onItemDetailListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemPostReplyBinding binding;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }

    @BindingConversion
    public static int showDeleteButton(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }
}
