package com.teame.boostcamp.myapplication.ui.selectedgoods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.airbnb.lottie.LottieDrawable;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsMyListAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivitySelectedGoodsBinding;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;

public class SelectedGoodsActivity extends BaseMVPActivity<ActivitySelectedGoodsBinding, SelectedGoodsContract.Presenter> implements SelectedGoodsContract.View {

    public static final String EXTRA_HEADER_UID = "EXTRA_HEADER_UID";
    private CompositeDisposable disposable = new CompositeDisposable();

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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_back);
        binding.includeLoading.lavLoading.playAnimation();
        binding.includeLoading.lavLoading.setRepeatCount(LottieDrawable.INFINITE);

        GoodsMyListAdapter adapter = new GoodsMyListAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false);
        binding.rvCartList.setLayoutManager(linearLayoutManager);
        binding.rvCartList.setAdapter(adapter);
        binding.rvCartList.setLayoutManager(linearLayoutManager);
        presenter.loadListData(adapter, headerUid);

        adapter.setOnItemCheckListener((v, position) -> {
            presenter.calculatorPrice();
            presenter.saveCheckStatus(position);
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
    public void setResultPrice(String totalPrice, String buyPrice, String resultPrice) {
        binding.tvTotalPrice.setText(totalPrice);
        binding.tvBuyPrice.setText(buyPrice);
        binding.tvResultPrice.setText(resultPrice);
    }

    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                0,
                view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                DLogUtil.d("에니메이션 종료");
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animate);
    }

    @Override
    public void completeMyList() {

        binding.clDone.setVisibility(View.VISIBLE);
        binding.clDone.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                slideDown(view);
                binding.clDone.setOnTouchListener(null);
                view.performClick();
            }
            return true;
        });

        binding.rvCartList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onDetach();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
