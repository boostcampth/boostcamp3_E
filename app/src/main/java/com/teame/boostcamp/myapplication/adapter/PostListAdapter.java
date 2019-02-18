package com.teame.boostcamp.myapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.teame.boostcamp.myapplication.ui.postreply.PostReplyActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingConversion;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ItemViewHolder> implements OnPostClickListener {
    private List<Post> postList = new ArrayList<>();
    private Context context;
    private CompositeDisposable disposable = new CompositeDisposable();
    private PostListRepository rep = PostListRepository.getInstance();

    private static final String LIKE_UPDATE = "update_like";
    private static final String SHOW_LIKE_LOADING = "show_loading";

    public void add(Post post) {
        postList.add(post);
        notifyDataSetChanged();
    }

    public PostListAdapter(Context context) {
        this.context = context;
    }

    public void initItems(List<Post> postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        PostListAdapter.ItemViewHolder holder = new PostListAdapter.ItemViewHolder(view);
        holder.setPostClickListener(this);
        return holder;
    }
    @BindingConversion
    public static int showIndicator(int size){
        return size > 1 ? View.VISIBLE : View.GONE;
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int i) {
        // 해쉬테그가 없으면 GONE
        if (postList.get(i).getTags().size() == 0) {
            holder.binding.cgHashTag.setVisibility(View.GONE);
        } else {
            holder.binding.cgHashTag.setVisibility(View.VISIBLE);
            for (String tag : postList.get(i).getTags().keySet()) {
                Chip chip = new Chip(holder.itemView.getContext());
                chip.setText("#" + tag);
                chip.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorIphoneBlack));
                chip.setClickable(false);
                chip.setCheckable(false);
                holder.binding.cgHashTag.addView(chip);
            }
        }
        holder.binding.setPost(postList.get(i));
        holder.binding.setAuth(FirebaseAuth.getInstance());
        holder.binding.ivPostReply.setOnClickListener(__ -> onReplyButtonClick(i));
        holder.binding.ivPostLike.setOnClickListener(__ -> onLikeButtonClick(i));
        holder.binding.ivShoppingList.setOnClickListener(__ -> onListButtonClick(i));
        holder.binding.vpPostImages.setAdapter(new PostImagePagerAdapter(context, postList.get(i).getImagePathList()));
        holder.binding.tlImageIndicator.setupWithViewPager(holder.binding.vpPostImages, true);
        holder.binding.ivPostMenu.setOnClickListener(v -> onMenuButtonClick(v, i));
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads);
        }else{
            for(Object payload : payloads){
                if (payload instanceof String){
                    String type = (String) payload;
                    if(TextUtils.equals(type, LIKE_UPDATE) && holder instanceof ItemViewHolder){
                        holder.binding.setPost(postList.get(position));
                        holder.binding.ivPostLike.setVisibility(View.VISIBLE);
                        holder.binding.clpbPostLike.setVisibility(View.INVISIBLE);
                    }else if(TextUtils.equals(type, SHOW_LIKE_LOADING) && holder instanceof ItemViewHolder){
                        holder.binding.clpbPostLike.setVisibility(View.VISIBLE);
                        holder.binding.ivPostLike.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public void onReplyButtonClick(int i) {
        PostReplyActivity.startActivity(context, postList.get(i).getKey());
    }

    @Override
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

    @Override
    public void onListButtonClick(int i) {

    }

    @Override
    public void onMenuButtonClick(View view, int i) {
        PopupMenu menu = new PopupMenu(context, view);
        ((Activity) context).getMenuInflater().inflate(R.menu.menu_post, menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
            }
        });
        menu.show();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;
        private OnPostClickListener mListener;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setPostClickListener(OnPostClickListener onClick) {
            mListener = onClick;
        }
    }
}
