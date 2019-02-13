package com.teame.boostcamp.myapplication.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityUsersShoppinglistBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class UserShoppinglistActivity extends BaseMVPActivity<ActivityUsersShoppinglistBinding, UserShoppinglistContract.Presenter> {

    private static final String EXTRA_GOODSLIST="EXTRA_GOODSLIST";
    private static final int EXTRA_REQUEST_CODE=111;
    private ArrayList<Goods> goodsArrayList;
    private ArrayList<Goods> selectedList;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent result=new Intent();
        result.putParcelableArrayListExtra(EXTRA_GOODSLIST,selectedList);
        setResult(EXTRA_REQUEST_CODE,result);
        switch(item.getItemId()){
            case R.id.tb_back_import:
                return true;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent result=new Intent();
        result.putParcelableArrayListExtra(EXTRA_GOODSLIST,selectedList);
        setResult(EXTRA_REQUEST_CODE,result);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.tbBackImport);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        goodsArrayList=getIntent().getParcelableArrayListExtra(EXTRA_GOODSLIST);
        //TODO: 유저 리스트 호출 구현
    }

    public static void startActivity(Context context, ArrayList<Goods> goodslist){
        Intent intent=new Intent(context,UserShoppinglistActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_GOODSLIST,goodslist);
        context.startActivity(intent);
    }

    public static void startActivity(Fragment fragment, ArrayList<Goods> goodslist){
        Intent intent=new Intent(fragment.getContext(),UserShoppinglistActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_GOODSLIST,goodslist);
        fragment.startActivityForResult(intent,EXTRA_REQUEST_CODE);
    }
}
