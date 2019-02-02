package com.teame.boostcamp.myapplication.ui.createlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityCreateListBinding;
import com.teame.boostcamp.myapplication.model.repository.ShoppingListRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;

public class CreateListActivity extends BaseMVPActivity<ActivityCreateListBinding, CreateListContract.Presenter> implements CreateListContract.View {

    @Override
    protected CreateListContract.Presenter getPresenter() {
        return new CreateListPresenter(this,ShoppingListRepository.getInstance());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_list;
    }

    @Override
    protected String getClassName() {
        return "CreateListActivity";
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CreateListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbarScreen);
    }

}
