package com.teame.boostcamp.myapplication.ui.usershoppinglist;

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
import com.teame.boostcamp.myapplication.adapter.usershoppinglist.UserShoppingListAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityUsersShoppinglistBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class UserShoppinglistActivity extends BaseMVPActivity<ActivityUsersShoppinglistBinding, UserShoppinglistContract.Presenter> implements  UserShoppinglistContract.View{

    private static final String EXTRA_GOODSLIST="EXTRA_GOODSLIST";
    private static final String EXTRA_GOODSLISTHEADER="EXTRA_GOODSLISTHEADER";
    private static final int EXTRA_REQUEST_CODE=111;

    @Override
    protected UserShoppinglistContract.Presenter getPresenter() {
        return null;
    }

    @Override
    public void setPresenter(UserShoppinglistContract.Presenter presenter) {
        super.setPresenter(presenter);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_users_shoppinglist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_users_shoppinglist,menu);
        return true;
    }
    private void setIntent(boolean isok){
        ArrayList<Goods> list=presenter.getSelectedList();
        presenter.addCartGoods(list);
        Intent result = new Intent();
        if(isok)
            result.putParcelableArrayListExtra(EXTRA_GOODSLIST, list);
        else
            result.putParcelableArrayListExtra(EXTRA_GOODSLIST, new ArrayList<>());
        setResult(EXTRA_REQUEST_CODE, result);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ArrayList<Goods> list=presenter.getSelectedList();
        Intent result = new Intent();
        switch(item.getItemId()){
            case R.id.backbutton:
                if(list.size()>0) {
                    setIntent(true);
                    presenter.setExit(true);
                    showToast("저장되었습니다.");
                }
                else{
                    showToast("선택된 아이템이 없습니다.");
                }
                return true;
            case android.R.id.home:
                if(list.size()!=0&&!presenter.getExit()){
                    AlertDialog.Builder builder=new AlertDialog.Builder(this);
                    builder.setMessage("체크한 아이템이 있습니다 정말 나가시겠습니까?")
                            .setPositiveButton("확인", (__, ___) -> {
                                setIntent(false);
                                finish();
                            })
                            .setCancelable(true)
                            .setNegativeButton("취소", (__, ___) -> {
                                return;
                            });
                    AlertDialog dialog=builder.create();
                    dialog.setOnShowListener(__ -> {
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));

                    });
                    dialog.show();
                }
                else{
                    setIntent(false);
                    finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        ArrayList<Goods> list=presenter.getSelectedList();
        if(list.size()!=0&&!presenter.getExit()){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("체크한 아이템이 있습니다 정말 나가시겠습니까?")
                    .setPositiveButton("확인", (dialog1, which) -> {
                        setIntent(false);
                        finish();
                    })
                    .setCancelable(true)
                    .setNegativeButton("취소", (__, ___) -> {
                        return;
                    });
            AlertDialog dialog=builder.create();
            dialog.setOnShowListener(__ -> {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
            });
            dialog.show();
        }
        else{
            setIntent(false);
            super.onBackPressed();
        }
    }

    @Override
    public void setSelectAllState(boolean state) {
        binding.cbSelectAll.setChecked(state);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoodsListHeader header=getIntent().getParcelableExtra(EXTRA_GOODSLISTHEADER);
        if(header.getHashTag().size()>0){
            for(String str:header.getHashTag().keySet()){
                binding.cgUserHashtag.setVisibility(View.VISIBLE);
                Chip chip=new Chip(this);
                chip.setText("#"+str);
                chip.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorIphoneBlack));
                chip.setClickable(false);
                chip.setCheckable(false);
                binding.cgUserHashtag.addView(chip);
            }
        }
        binding.cbSelectAll.setOnClickListener(__->{
            if(binding.cbSelectAll.isChecked())
                presenter.selectAll(true);
            else
                presenter.selectAll(false);
        });
        ArrayList<Goods> goodsArrayList =getIntent().getParcelableArrayListExtra(EXTRA_GOODSLIST);
        setPresenter(new UserShoppinglistPresenter(this,header,goodsArrayList));
        setSupportActionBar(binding.toolbarBackImport);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        binding.tvUserlistTitle.setText(header.getTitle());
        UserShoppingListAdapter adapter=new UserShoppingListAdapter(goodsArrayList);
        presenter.setAdapter(adapter,adapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        binding.rvUserShoppinglist.setLayoutManager(layoutManager);
        binding.rvUserShoppinglist.setAdapter(adapter);
    }

    public static void startActivity(Context context, GoodsListHeader header, ArrayList<Goods> goodslist){
        Intent intent=new Intent(context,UserShoppinglistActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_GOODSLIST,goodslist);
        intent.putExtra(EXTRA_GOODSLISTHEADER,header);
        ((Activity)context).startActivityForResult(intent,EXTRA_REQUEST_CODE);
    }

    public static void startActivity(Fragment fragment, GoodsListHeader header, ArrayList<Goods> goodslist){
        Intent intent=new Intent(fragment.getContext(),UserShoppinglistActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_GOODSLIST,goodslist);
        intent.putExtra(EXTRA_GOODSLISTHEADER,header);
        fragment.startActivityForResult(intent,EXTRA_REQUEST_CODE);
    }
}
