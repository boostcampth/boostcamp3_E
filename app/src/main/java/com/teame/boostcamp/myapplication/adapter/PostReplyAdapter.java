package com.teame.boostcamp.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ItemPostReplyBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.BindingConversion;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;

public class PostReplyAdapter extends RecyclerView.Adapter<PostReplyAdapter.ItemViewHolder> implements OnReplyClickListener {
    private List<Reply> replyList = new ArrayList<>();
    private String postUid;
    private Context context;
    private PostListRepository rep;
    private CompositeDisposable disposable = new CompositeDisposable();


    public void add(Reply reply) {
        replyList.add(reply);
        notifyDataSetChanged();
    }

    public PostReplyAdapter(Context context, String postUid) {
        this.context = context;
        this.postUid = postUid;
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
        holder.binding.setWriter(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        holder.binding.ibDeleteReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteButtonClick(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    @Override
    public void onDeleteButtonClick(int i) {
        rep = PostListRepository.getInstance();
        Reply reply = replyList.get(i);
        disposable.add(rep.deleteReply(postUid, reply.getKey())
                .subscribe(b -> {
                            replyList.remove(i);
                            notifyDataSetChanged();
                        },
                        e -> DLogUtil.e(e.getMessage())));
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemPostReplyBinding binding;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    @BindingConversion
    public static int showDeleteButton(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

    public void disposableDispose(){
        if(disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}

