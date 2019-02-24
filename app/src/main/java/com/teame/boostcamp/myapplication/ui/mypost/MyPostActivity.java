package com.teame.boostcamp.myapplication.ui.mypost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityMyPostBinding;
import com.teame.boostcamp.myapplication.ui.addpost.AddPostActivity;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.modifypost.ModifyPostActivity;
import com.teame.boostcamp.myapplication.ui.othershoppinglist.OtherShoppingListActivity;
import com.teame.boostcamp.myapplication.ui.postreply.PostReplyActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyPostActivity extends BaseMVPActivity<ActivityMyPostBinding, MyPostContract.Presenter> implements MyPostContract.View {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_post;
    }

    @Override
    protected MyPostContract.Presenter getPresenter() {
        return new MyPostPresenter(this);
    }

    @Override
    public void setPresenter(MyPostContract.Presenter presenter) {
        super.setPresenter(presenter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        binding.srlPost.setRefreshing(true);
        binding.setUserEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        binding.rvMyPost.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false));
        PostListAdapter adapter = new PostListAdapter();
        adapter.setOnReplyClickListener((__, position) -> PostReplyActivity.startActivity(this, adapter.getItem(position).getKey()));
        adapter.setOnShowListClickListener((__, position) -> OtherShoppingListActivity.startActivity(this, adapter.getItem(position).getUid(), adapter.getItem(position).getHeader().getKey(), adapter.getItem(position).getWriter()));
        adapter.setOnMenuClickListener((v, position) -> {
            PopupMenu menu = new PopupMenu(this, v);
            this.getMenuInflater().inflate(R.menu.menu_post, menu.getMenu());
            menu.setOnMenuItemClickListener(item -> {
                if (item.toString().equals("수정")) {
                    ModifyPostActivity.startActivity(this, adapter.getItem(position));
                } else {
                    presenter.deletePost(adapter.getItem(position).getKey(), adapter.getItem(position).getImagePathList(), position);
                }
                return false;
            });
            menu.show();
        });
        adapter.setOnLikeClickListener((v, position) -> {
            presenter.adjustLike(adapter.getItem(position).getKey(), position);
        });
        binding.rvMyPost.setAdapter(adapter);
        binding.ivMyAddPost.setOnClickListener(__ -> AddPostActivity.startActivity(this));
        binding.ivMyPostBack.setOnClickListener(__ -> finish());
        binding.srlPost.setOnRefreshListener(() -> initView());
        binding.clNoPost.setOnClickListener(__ -> AddPostActivity.startActivity(this));
        presenter.loadMyPost(adapter);

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyPostActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }

    @Override
    public void stopLoading(int size) {
        binding.srlPost.setRefreshing(false);
        if (size == 0) {
            binding.clNoPost.setVisibility(View.VISIBLE);
        } else {
            binding.clNoPost.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void succeedDelete(int size) {
        if (size == 0) {
            binding.clNoPost.setVisibility(View.VISIBLE);
        } else {
            binding.clNoPost.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
    }
}

