package com.teame.boostcamp.myapplication.ui.postreply;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.PostReplyAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityPostReplyBinding;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostReplyActivity extends BaseMVPActivity<ActivityPostReplyBinding, PostReplyContract.Presenter> implements PostReplyContract.View {

    public static final String EXTRA_POST_UID = "postUid";
    private String postUid;

    @Override
    protected PostReplyContract.Presenter getPresenter() {
        return new PostReplyPresenter(this, PostListRepository.getInstance());
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
        postUid = getIntent().getStringExtra(EXTRA_POST_UID);
        initView();
    }


    public static void startActivity(Context context, String postUid) {
        Intent intent = new Intent(context, PostReplyActivity.class);
        intent.putExtra(EXTRA_POST_UID, postUid);
        context.startActivity(intent);
    }

    private void initView() {
        PostReplyAdapter adapter = new PostReplyAdapter(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.VERTICAL,
                false);

        binding.rvPostReply.setLayoutManager(linearLayoutManager);
        binding.rvPostReply.setAdapter(adapter);
        presenter.loadReply(postUid, adapter);
        binding.tvPostReplyInput.setOnClickListener(__ -> onInputClicked());
    }

    private void onInputClicked(){
        if(binding.tietPostReplyInput.getText().toString().length()>=5){
            presenter.writePostReply(postUid, binding.tietPostReplyInput.getText().toString());
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

    }
}
