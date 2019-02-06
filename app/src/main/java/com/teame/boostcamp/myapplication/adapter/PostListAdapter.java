package com.teame.boostcamp.myapplication.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemPostBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Post;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ItemViewHolder> implements OnPostClickListener {
    private List<Post> postList = new ArrayList<>();
    private Context context;


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

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int i) {
        StorageReference mStorageRef;
        holder.binding.tvPostTitle.setText(postList.get(i).getTitle());
        holder.binding.tvPostContent.setText(postList.get(i).getContent());
        holder.binding.tvPostLikeCount.setText(postList.get(i).getLikeString());
        if(postList.get(i).getLikedUidList().contains(FirebaseAuth.getInstance().getUid())){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.binding.ibPostLike.setImageDrawable(context.getDrawable(R.drawable.ic_star_black_24dp));
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.binding.ibPostLike.setImageDrawable(context.getDrawable(R.drawable.ic_star_border_black_24dp));
            }
        }

        holder.binding.ibPostLike.setOnClickListener(__ -> onLikeButtonClick(i));
        for(String path: postList.get(i).getImagePathList()){
            mStorageRef = FirebaseStorage.getInstance().getReference(path);
            mStorageRef.getDownloadUrl().addOnSuccessListener(uri -> Glide.with(context).load(uri.toString()).into(holder.binding.ivPostImages));
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    @Override
    public void onPostClick(int i) {

    }

    @Override
    public void onLikeButtonClick(int i) {
        if(postList.get(i).getLikedUidList().contains(FirebaseAuth.getInstance().getUid())){
            postList.get(i).decreaseLike();
            postList.get(i).getLikedUidList().remove(FirebaseAuth.getInstance().getUid());
            notifyDataSetChanged();
        }else{
            postList.get(i).increaseLike();
            postList.get(i).getLikedUidList().add(FirebaseAuth.getInstance().getUid());
            notifyDataSetChanged();
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
