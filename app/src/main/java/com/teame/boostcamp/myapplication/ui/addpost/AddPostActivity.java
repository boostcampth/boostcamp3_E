package com.teame.boostcamp.myapplication.ui.addpost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityAddPostBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;


public class AddPostActivity extends BaseMVPActivity<ActivityAddPostBinding, AddPostContract.Presenter> implements AddPostContract.View {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_post;
    }

    @Override
    protected String getClassName() {
        return "AddPostActivity";
    }

    @Override
    protected AddPostContract.Presenter getPresenter() {
        return new AddPostPresenter(this);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }


    public static void startActivity(Context context){
        Intent intent = new Intent(context, AddPostActivity.class);
        context.startActivity(intent);
    }

    private void initView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}

