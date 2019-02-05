package com.teame.boostcamp.myapplication.ui.itemdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.ItemDetailRecyclerAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityItemDetailBinding;
import com.teame.boostcamp.myapplication.model.repository.ItemDetailRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.DividerItemDecorator;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDetailActivity extends BaseMVPActivity<ActivityItemDetailBinding, ItemDetailContract.Presenter> implements ItemDetailContract.View, View.OnClickListener {

    @Override
    protected ItemDetailContract.Presenter getPresenter() {
        ItemDetailRepository repository = ItemDetailRepository.getInstance();
        return new ItemDetailPresenter(this, repository);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_item_detail;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ItemDetailActivity.class);
        context.startActivity(intent);
    }
}
