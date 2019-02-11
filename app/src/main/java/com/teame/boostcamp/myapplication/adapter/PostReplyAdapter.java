package com.teame.boostcamp.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemPostReplyBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PostReplyAdapter extends RecyclerView.Adapter<PostReplyAdapter.ItemViewHolder> {
    private List<Reply> replyList = new ArrayList<>();
    private Context context;

    public void add(Reply reply) {
        replyList.add(reply);
        notifyDataSetChanged();
    }

    public PostReplyAdapter(Context context) {
        this.context = context;
    }

    public void initItems(List<Reply> replyList) {
        this.replyList = replyList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_reply, viewGroup, false);
        PostReplyAdapter.ItemViewHolder holder = new PostReplyAdapter.ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int i) {
        holder.binding.setReply(replyList.get(i));
    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemPostReplyBinding binding;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }
}

