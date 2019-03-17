package com.teame.boostcamp.myapplication.ui.othershoppinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsOtherListAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityOtherUserShoppingListBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.snsgoodsdetail.SnsGoodsDetailActivity;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OtherShoppingListActivity extends BaseMVPActivity<ActivityOtherUserShoppingListBinding, OtherShoppingListContract.Presenter> implements OtherShoppingListContract.View {

    public static final String EXTRA_USER_UID = "EXTRA_USER_UID";
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


    public static void startActivity(Context context, String uid, String headerUid, String userEmail) {
        Intent intent = new Intent(context, OtherShoppingListActivity.class);
        intent.putExtra(EXTRA_USER_UID, uid);
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
        final String uid;
        final String headerUid;
        final String userEmail;
        uid = intent.getStringExtra(EXTRA_USER_UID);
        headerUid = intent.getStringExtra(EXTRA_HEADER_UID);
        userEmail = intent.getStringExtra(EXTRA_EMAIL);
        binding.setEmail(userEmail);
        binding.ivOtherListBack.setOnClickListener(__ -> finish());
        DLogUtil.e("실패???");
        if (headerUid == null) {
            finish();
            return;
        }



        GoodsOtherListAdapter adapter = new GoodsOtherListAdapter();
        adapter.setOnItemClickListener((v, position) -> SnsGoodsDetailActivity.startActivity(this, adapter.getItem(position)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false);
        binding.rvCartList.setLayoutManager(linearLayoutManager);
        binding.rvCartList.setAdapter(adapter);
        presenter.loadListData(adapter, uid, headerUid);

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
