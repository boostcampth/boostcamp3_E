package com.teame.boostcamp.myapplication.ui.nocheckusershoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.nocheckusershoppinglist.NoCheckUserShoppingListAdapter;
import com.teame.boostcamp.myapplication.adapter.usershoppinglist.UserShoppingListAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityNoCheckUsershoppinglistBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BaseActivity;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.usershoppinglist.UserShoppinglistActivity;
import com.teame.boostcamp.myapplication.ui.usershoppinglist.UserShoppinglistPresenter;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NoCheckUserShoppinglistActivity extends BaseActivity<ActivityNoCheckUsershoppinglistBinding> {

    private static final String EXTRA_GOODSLIST="EXTRA_GOODSLIST";
    private static final String EXTRA_GOODSLISTHEADER="EXTRA_GOODSLISTHEADER";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void startActivity(Context context, GoodsListHeader header, ArrayList<Goods> goodslist){
        Intent intent=new Intent(context, NoCheckUserShoppinglistActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_GOODSLIST,goodslist);
        intent.putExtra(EXTRA_GOODSLISTHEADER,header);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoodsListHeader header=getIntent().getParcelableExtra(EXTRA_GOODSLISTHEADER);
        ArrayList<Goods> goodsArrayList =getIntent().getParcelableArrayListExtra(EXTRA_GOODSLIST);
        if(header.getHashTag().size()>0){
            for(String str:header.getHashTag().keySet()){
                binding.cgNoCheckUserHashtag.setVisibility(View.VISIBLE);
                Chip chip=new Chip(this);
                chip.setText("#"+str);
                chip.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorIphoneBlack));
                chip.setClickable(false);
                chip.setCheckable(false);
                binding.cgNoCheckUserHashtag.addView(chip);
            }
        }
        setSupportActionBar(binding.toolbarNoCheckBackImport);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        binding.tvNoCheckUserlistTitle.setText(header.getTitle());
        NoCheckUserShoppingListAdapter adapter=new NoCheckUserShoppingListAdapter();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        binding.rvNoCheckUserShoppinglist.setLayoutManager(layoutManager);
        binding.rvNoCheckUserShoppinglist.setAdapter(adapter);
        adapter.setList(goodsArrayList);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_no_check_usershoppinglist;
    }
}
