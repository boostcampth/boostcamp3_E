package com.teame.boostcamp.myapplication.ui.othershoppinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.airbnb.lottie.LottieDrawable;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsMyListAdapter;
import com.teame.boostcamp.myapplication.adapter.GoodsOtherListAdapter;
import com.teame.boostcamp.myapplication.adapter.OnItemClickListener;
import com.teame.boostcamp.myapplication.databinding.ActivityOtherUserShoppingListBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.snsgoodsdetail.SnsGoodsDetailActivity;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OtherShoppingListActivity extends BaseMVPActivity<ActivityOtherUserShoppingListBinding, OtherShoppingListContract.Presenter> implements OtherShoppingListContract.View {

    public static final String EXTRA_HEADER_UID = "EXTRA_HEADER_UID";
    public static final String EXTRA_EMAIL = "EXTRA_EMAIL";

    @Override
    protected OtherShoppingListContract.Presenter getPresenter() {
        return new OtherShoppingListPresenter(this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_other_user_shopping_list;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }


    public static void startActivity(Context context, String headerUid, String userEmail) {
        Intent intent = new Intent(context, OtherShoppingListActivity.class);
        intent.putExtra(EXTRA_HEADER_UID, headerUid);
        intent.putExtra(EXTRA_EMAIL, userEmail);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
    }

    public void initView() {
        Intent intent = getIntent();
        final String headerUid;
        final String userEmail;
        headerUid = intent.getStringExtra(EXTRA_HEADER_UID);
        userEmail = intent.getStringExtra(EXTRA_EMAIL);
        binding.setEmail(userEmail);
        DLogUtil.e("실패???");
        if (headerUid == null) {
            finish();
            return;
        }

        setSupportActionBar(binding.toolbarScreen);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //홈 아이콘을 숨김처리합니다.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_back);
        binding.includeLoading.lavLoading.playAnimation();
        binding.includeLoading.lavLoading.setRepeatCount(LottieDrawable.INFINITE);

        GoodsOtherListAdapter adapter = new GoodsOtherListAdapter();
        adapter.setOnItemClickListener((v, position) -> SnsGoodsDetailActivity.startActivity(getApplicationContext(), adapter.getItem(position)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false);
        binding.rvCartList.setLayoutManager(linearLayoutManager);
        binding.rvCartList.setAdapter(adapter);
        presenter.loadListData(adapter, headerUid);

        binding.rvCartList.setLayoutManager(linearLayoutManager);
        binding.rvCartList.setAdapter(adapter);
    }

    public void finishLoad(int size) {
        binding.includeLoading.lavLoading.cancelAnimation();
        binding.includeLoading.lavLoading.setVisibility(View.GONE);

        if (size == Constant.LOADING_NONE_ITEM) {
            showLongToast(String.format(getString(R.string.none_item), getString(R.string.toast_my_items)));
        } else if (size == Constant.FAIL_LOAD) {
            showLongToast(getString(R.string.fail_load));
        }
        //presenter.calculatorPrice();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onDetach();
    }

}
