package com.teame.boostcamp.myapplication.ui.itemdetail;

import android.os.Bundle;
import android.app.Activity;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityItemDetailBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;

public class ItemDetailActivity extends BaseMVPActivity<ActivityItemDetailBinding,ItemDetailContract.Presenter> implements ItemDetailContract.View {

    @Override
    protected ItemDetailContract.Presenter getPresenter() {
        return new ItemDetailPresenter();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_item_detail;
    }

    @Override
    protected String getClassName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
    }

}