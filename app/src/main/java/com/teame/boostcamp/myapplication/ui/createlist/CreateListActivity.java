package com.teame.boostcamp.myapplication.ui.createlist;

import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityCreateListBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;

public class CreateListActivity extends BaseMVPActivity<ActivityCreateListBinding,CreateListContract.Presenter> implements CreateListContract.View {

    @Override
    protected CreateListContract.Presenter getPresenter() {
        return new CreateListPresenter();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_list;
    }

    @Override
    protected String getClassName() {
        return "CreateListActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar);
    }

}
