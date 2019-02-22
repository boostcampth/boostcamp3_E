package com.teame.boostcamp.myapplication.ui.postreply;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.OnItemClickListener;
import com.teame.boostcamp.myapplication.adapter.PostReplyAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityPostReplyBinding;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.DividerItemDecorator;
import com.teame.boostcamp.myapplication.util.InputKeyboardUtil;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostReplyActivity extends BaseMVPActivity<ActivityPostReplyBinding, PostReplyContract.Presenter> implements PostReplyContract.View {

    public static final String EXTRA_POST_UID = "postUid";

    @Override
    protected PostReplyContract.Presenter getPresenter() {
        return new PostReplyPresenter(this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_post_reply;
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    public static void startActivity(Context context, String postUid) {
        Intent intent = new Intent(context, PostReplyActivity.class);
        intent.putExtra(EXTRA_POST_UID, postUid);
        context.startActivity(intent);
    }

    private void initView() {
        String postUid = getIntent().getStringExtra(EXTRA_POST_UID);
        binding.srlPostReply.setRefreshing(true);
        PostReplyAdapter adapter = new PostReplyAdapter();
        adapter.setOnDeleteListener((v, position) -> {presenter.deleteReply(postUid, position);});
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false);

        binding.rvPostReply.setLayoutManager(linearLayoutManager);
        binding.rvPostReply.setAdapter(adapter);
        presenter.loadReply(postUid, adapter);
        binding.tvPostReplyInput.setOnClickListener(__ -> onInputClicked(postUid));
        binding.ivReplyBack.setOnClickListener(__ -> finish());
        binding.srlPostReply.setOnRefreshListener(() -> initView());
    }

    private void onInputClicked(String postUid){
        if(binding.tietPostReplyInput.getText().toString().length()>=5){
            presenter.writePostReply(postUid, binding.tietPostReplyInput.getText().toString());
            binding.clpbRegister.setVisibility(View.VISIBLE);
            binding.tvPostReplyInput.setVisibility(View.INVISIBLE);
        }
        else{
            showToast(getString(R.string.notice_reply_length));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }


    @Override
    public void successWriteReply() {
        binding.clNoReply.setVisibility(View.INVISIBLE);
        binding.tietPostReplyInput.setText(null);
        InputKeyboardUtil.hideKeyboard(this);
        binding.tvPostReplyInput.setVisibility(View.VISIBLE);
        binding.clpbRegister.setVisibility(View.INVISIBLE);
    }

    @Override
    public void stopRefreshIcon(int size) {
        binding.srlPostReply.setRefreshing(false);
        if(size==0){
            binding.clNoReply.setVisibility(View.VISIBLE);
        }else{
            binding.clNoReply.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void controlNo(int size) {
        if(size==0){
            binding.clNoReply.setVisibility(View.VISIBLE);
        }else{
            binding.clNoReply.setVisibility(View.INVISIBLE);
        }
    }
}
