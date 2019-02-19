package com.teame.boostcamp.myapplication.ui.selectedgoods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.airbnb.lottie.LottieDrawable;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsMyListAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivitySelectedGoodsBinding;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.Constant;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectedGoodsActivity extends BaseMVPActivity<ActivitySelectedGoodsBinding, SelectedGoodsContract.Presenter> implements SelectedGoodsContract.View {

    public static final String EXTRA_HEADER_UID = "EXTRA_HEADER_UID";

    @Override
    protected SelectedGoodsContract.Presenter getPresenter() {
        return new SelectedGoodsPresenter(this, MyListRepository.getInstance());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_selected_goods;
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

    public static void startActivity(Context context, String headerUid) {
        Intent intent = new Intent(context, SelectedGoodsActivity.class);
        intent.putExtra(EXTRA_HEADER_UID, headerUid);
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
        headerUid = intent.getStringExtra(EXTRA_HEADER_UID);

        if (headerUid == null) {
            finish();
            return;
        }
        presenter.setMyListId(headerUid);

        setSupportActionBar(binding.toolbarScreen);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //홈 아이콘을 숨김처리합니다.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_all_back);
        binding.includeLoading.lavLoading.playAnimation();
        binding.includeLoading.lavLoading.setRepeatCount(LottieDrawable.INFINITE);

        GoodsMyListAdapter adapter = new GoodsMyListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false);
        binding.rvCartList.setLayoutManager(linearLayoutManager);
        binding.rvCartList.setAdapter(adapter);
        presenter.loadListData(adapter, headerUid);
        presenter.detectIsAllCheck();
        adapter.setOnItemCheckListener((v, position) -> {
            presenter.detectIsAllCheck();
            presenter.calculatorPrice();
            presenter.saveCheckStatus(position);
        });

        binding.rvCartList.setLayoutManager(linearLayoutManager);
        binding.rvCartList.setAdapter(adapter);

        binding.tvOfferDelete.setOnClickListener(view -> {
            presenter.deleteList();
        });
        binding.tvSaveMyList.setOnClickListener(view -> {
            // TODO: 저장 로직 추가
//            presenter.getSaveData()
        });
    }

    @Override
    public void finishLoad(int size) {
        binding.includeLoading.lavLoading.cancelAnimation();
        binding.includeLoading.lavLoading.setVisibility(View.GONE);

        if (size == Constant.LOADING_NONE_ITEM) {
            showLongToast(String.format(getString(R.string.none_item), getString(R.string.toast_my_items)));
        } else if (size == Constant.FAIL_LOAD) {
            showLongToast(getString(R.string.fail_load));
        }
        presenter.calculatorPrice();
    }

    @Override
    public void setResultPrice(String resultPrice) {
        binding.tvTotalPrice.setText(resultPrice);
    }

    @Override
    public void setOfferDelete() {
        binding.tvOfferDelete.setVisibility(View.VISIBLE);
        binding.tvTotalPrice.setVisibility(View.GONE);
        binding.tvSaveMyList.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onDetach();
    }

}
