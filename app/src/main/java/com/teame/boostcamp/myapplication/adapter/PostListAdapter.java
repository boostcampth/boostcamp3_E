package com.teame.boostcamp.myapplication.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemPostBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PostListAdapter extends BaseRecyclerAdatper<Post, PostListAdapter.ViewHolder> {

    public static final String LIKE_UPDATE = "update_like";
    public static final String SHOW_LIKE_LOADING = "show_loading";

    private OnItemClickListener onLikeClickListener;
    private OnItemClickListener onReplyClickListener;
    private OnItemClickListener onShowListClickListener;
    private OnItemClickListener onMenuClickListener;


    public PostListAdapter() {
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);

        holder.binding.ivPostLike.setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            DLogUtil.e("리스너 :" + holder.getAdapterPosition());
            if (onLikeClickListener != null) {
                onLikeClickListener.onItemClick(view, position);
            }
        });
        holder.binding.ivPostReply.setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            DLogUtil.e("리스너 :" + holder.getAdapterPosition());
            if (onReplyClickListener != null) {
                onReplyClickListener.onItemClick(view, position);
            }
        });
        holder.binding.ivShoppingList.setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            DLogUtil.e("리스너 :" + holder.getAdapterPosition());
            if (onShowListClickListener != null) {
                onShowListClickListener.onItemClick(view, position);
            }
        });
        holder.binding.ivPostMenu.setOnClickListener(view -> {
            int position = holder.getLayoutPosition();
            DLogUtil.e("리스너 :" + holder.getAdapterPosition());
            if (onMenuClickListener != null) {
                onMenuClickListener.onItemClick(view, position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        // 해쉬테그가 없으면 GONE
        if (itemList.get(i).getHeader().getHashTag().size() == 0) {
            holder.binding.cgHashTag.setVisibility(View.GONE);
        } else {
            holder.binding.cgHashTag.setVisibility(View.VISIBLE);
            holder.binding.cgHashTag.removeAllViews();
            for (String tag : itemList.get(i).getTags().keySet()) {
                Chip chip = new Chip(holder.itemView.getContext());
                chip.setText("#" + tag);
                chip.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorIphoneBlack));
                chip.setClickable(false);
                chip.setCheckable(false);
                holder.binding.cgHashTag.addView(chip);
            }
        }
        holder.binding.setPost(itemList.get(i));
        holder.binding.setAuth(FirebaseAuth.getInstance());
        holder.binding.vpPostImages.setAdapter(new PostImagePagerAdapter(itemList.get(i).getImagePathList()));
        holder.binding.tlImageIndicator.setupWithViewPager(holder.binding.vpPostImages, true);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    String type = (String) payload;
                    if (TextUtils.equals(type, LIKE_UPDATE) && holder instanceof ViewHolder) {
                        holder.binding.setPost(itemList.get(position));
                        holder.binding.ivPostLike.setVisibility(View.VISIBLE);
                        holder.binding.clpbPostLike.setVisibility(View.INVISIBLE);
                    } else if (TextUtils.equals(type, SHOW_LIKE_LOADING) && holder instanceof ViewHolder) {
                        holder.binding.clpbPostLike.setVisibility(View.VISIBLE);
                        holder.binding.ivPostLike.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public void setOnLikeClickListener(OnItemClickListener onLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener;
    }

    public void setOnReplyClickListener(OnItemClickListener onReplyClickListener) {
        this.onReplyClickListener = onReplyClickListener;
    }

    public void setOnShowListClickListener(OnItemClickListener onShowListClickListener) {
        this.onShowListClickListener = onShowListClickListener;
    }

    public void setOnMenuClickListener(OnItemClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }
}
