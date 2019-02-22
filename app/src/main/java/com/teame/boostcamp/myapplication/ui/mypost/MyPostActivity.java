package com.teame.boostcamp.myapplication.ui.mypost;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityLoginBinding;
import com.teame.boostcamp.myapplication.databinding.ActivityMyPostBinding;
import com.teame.boostcamp.myapplication.ui.MainActivity;
import com.teame.boostcamp.myapplication.ui.addpost.AddPostActivity;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.signup.SignUpActivity;

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
        PostListAdapter adapter = new PostListAdapter(this);
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
        if(size == 0 ){
            binding.clNoPost.setVisibility(View.VISIBLE);
        }else{
            binding.clNoPost.setVisibility(View.INVISIBLE);
        }
    }
}

