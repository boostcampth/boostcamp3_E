package com.teame.boostcamp.myapplication.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemPostBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Post;
import com.teame.boostcamp.myapplication.ui.postreply.PostReplyActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.BindingConversion;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ItemViewHolder> implements OnPostClickListener {
    private List<Post> postList = new ArrayList<>();
    private Context context;
    private static final String LIKE_UPDATE = "update Like";

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
        holder.binding.setPost(postList.get(i));
        holder.binding.setAuth(FirebaseAuth.getInstance());
        holder.binding.ivPostReply.setOnClickListener(__ -> onReplyButtonClick(i));
        holder.binding.ivPostLike.setOnClickListener(__ -> {
            onLikeButtonClick(i);
        });
        holder.binding.vpPostImages.setAdapter(new PostImagePagerAdapter(context, postList.get(i).getImagePathList()));
        holder.binding.tlImageIndicator.setupWithViewPager(holder.binding.vpPostImages, true);

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
        PostReplyActivity.startActivity(context, FirebaseAuth.getInstance().getUid()+postList.get(i).getCreatedDate());
    }

    @Override
    public void onLikeButtonClick(int i) {
        Post post = postList.get(i);
        String uid = FirebaseAuth.getInstance().getUid();
        if(post.getLikedUidList().contains(uid)){
            post.decreaseLike();
            post.getLikedUidList().remove(uid);
            FirebaseFirestore.getInstance().collection("posts")
                    .document(uid+post.getCreatedDate())
                    .set(post);
            notifyItemChanged(i, LIKE_UPDATE);
        }else{
            post.increaseLike();
            post.getLikedUidList().add(uid);
            FirebaseFirestore.getInstance().collection("posts")
                    .document(uid+post.getCreatedDate())
                    .set(post);
            notifyItemChanged(i, LIKE_UPDATE);
        }
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
