package com.teame.boostcamp.myapplication.ui.writereply;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.lottie.LottieDrawable;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityWriteReplyBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.GoodsDetailRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.InputKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

public class WriteReplyActivity extends BaseMVPActivity<ActivityWriteReplyBinding, WriteReplyContract.Presenter> implements WriteReplyContract.View {


    private final static String EXTRA_GOODS = "EXTRA_GOODS";
    private final static String EXTRA_REPLY = "EXTRA_REPLY";
    List<Integer> imageViewList = new ArrayList<>();

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_write_reply;
    }

    @Override
    protected WriteReplyContract.Presenter getPresenter() {

        GoodsDetailRepository repository = GoodsDetailRepository.getInstance();
        return new WriteReplyPresenter(this, repository);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(R.string.cancle_write_reply)
                        .setPositiveButton(getString(R.string.confirm), (__, ___) -> finish())
                        .setCancelable(true)
                        .show();
                break;
            default:
                break;
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }


    public static Intent getIntent(Context context, Goods item) {
        Intent intent = new Intent(context, WriteReplyActivity.class);
        intent.putExtra(EXTRA_GOODS, item);
        return intent;
    }

    @Override
    public void onBackPressed() {
        if (binding.includeLoading.clLoadingBackground.getVisibility() == View.VISIBLE) {
            return;
        } else {
            super.onBackPressed();
        }
    }

    private void initView() {
        Intent intent = getIntent();
        final Goods item;
        item = intent.getParcelableExtra(EXTRA_GOODS);
        setSupportActionBar(binding.toolbarScreen);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //홈 아이콘을 숨김처리합니다.
        if (item == null) {
            return;
        }

        binding.rbReply.setRating(5f);
        binding.rbReply.setOnRatingBarChangeListener((ratingBar, rating, b) -> {
            DLogUtil.d(rating + "");
        });
        binding.includeLoading.clLoadingBackground.setVisibility(View.GONE);
        binding.tvWriteReply.setOnClickListener(view -> {
            if (binding.etReplyContent.getText().toString().length() < 5) {
                showToast(getString(R.string.notice_reply_length));
                return;
            }
            InputKeyboardUtil.hideKeyboard(this);
            binding.includeLoading.clLoadingBackground.setVisibility(View.VISIBLE);
            binding.includeLoading.lavLoading.playAnimation();
            binding.includeLoading.lavLoading.setRepeatCount(LottieDrawable.INFINITE);
            binding.includeLoading.clLoadingBackground.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorBlurGray));
            int ratio = (int) binding.rbReply.getRating();
            String content = binding.etReplyContent.getText().toString();
            presenter.writeReply(item.getKey(), content, ratio);
        });
    }

    @Override
    public void successWriteItem(Reply item) {
        showToast("성공");
        Intent intent = new Intent();
        intent.putExtra(EXTRA_REPLY, item);
        setResult(Activity.RESULT_OK, intent);
        finishLoad(Constant.SUCCESS_LOAD);
        finish();
    }

    @Override
    public void failWriteItem() {
        showToast("실패");
        finishLoad(Constant.FAIL_LOAD);
        finish();
    }

    public void finishLoad(int flag) {
        binding.includeLoading.clLoadingBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
        binding.includeLoading.lavLoading.cancelAnimation();
        binding.includeLoading.lavLoading.setVisibility(View.GONE);
        if (flag == Constant.LOADING_NONE_ITEM) {
            showLongToast(String.format(getString(R.string.none_item), getString(R.string.toast_reply)));
        } else if (flag == Constant.FAIL_LOAD) {
            showLongToast(getString(R.string.fail_load));
        }
    }
}
