package com.teame.boostcamp.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemPostBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.ui.modifypost.ModifyPostActivity;
import com.teame.boostcamp.myapplication.ui.othershoppinglist.OtherShoppingListActivity;
import com.teame.boostcamp.myapplication.ui.postreply.PostReplyActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;

public class PostListAdapter extends BaseRecyclerAdatper<Post, PostListAdapter.ViewHolder> {

    public static final String LIKE_UPDATE = "update_like";
    public static final String SHOW_LIKE_LOADING = "show_loading";

    private OnItemClickListener onLikeClickListener;
    private OnItemClickListener onReplyClickListener;
    private OnItemClickListener onShowListClickListener;
    private OnItemClickListener onMenuClickListener;


    public PostListAdapter() { }


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
        /*
        holder.binding.ivPostReply.setOnClickListener(__ -> onReplyButtonClick(i));
        holder.binding.ivPostLike.setOnClickListener(__ -> onLikeButtonClick(i));
        holder.binding.ivShoppingList.setOnClickListener(__ -> onListButtonClick(i));
        holder.binding.ivPostMenu.setOnClickListener(v -> onMenuButtonClick(v, i));
        */

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

    /*
    public void onReplyButtonClick(int i) {
        PostReplyActivity.startActivity(context, postList.get(i).getKey());
    }

    public void onMenuButtonClick(View view, int i) {
        PopupMenu menu = new PopupMenu(context, view);
        ((Activity) context).getMenuInflater().inflate(R.menu.menu_post, menu.getMenu());
        menu.setOnMenuItemClickListener(item -> {
            if (item.toString().equals("수정")) {
                ModifyPostActivity.startActivity(context, postList.get(i));
            } else {
                disposable.add(rep.deletePost(postList.get(i).getKey(), postList.get(i).getImagePathList())
                        .subscribe(b -> {
                                    postList.remove(i);
                                    notifyDataSetChanged();
                                },
                                e -> DLogUtil.e(e.getMessage())));
            }
            return false;
        });
        menu.show();
    }

    public void onListButtonClick(int i) {
        OtherShoppingListActivity.startActivity(context, postList.get(i).getHeader().getKey(), postList.get(i).getWriter());
    }


    public void onLikeButtonClick(int i) {
        notifyItemChanged(i, SHOW_LIKE_LOADING);
        disposable.add(rep.adjustLike(postList.get(i).getKey())
                .subscribe(post -> {
                            postList.set(i, post);
                            notifyItemChanged(i, LIKE_UPDATE);
                        },
                        e -> {
                            DLogUtil.e(e.getMessage());
                        })
        );
    }
    */







    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;
        private OnPostClickListener mListener;

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
