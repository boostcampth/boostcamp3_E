package com.teame.boostcamp.myapplication.ui.writereply;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;

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
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage(R.string.cancle_write_reply)
                        .setPositiveButton(getString(R.string.confirm), (__, ___) -> finish())
                        .setNegativeButton(getString(R.string.cancle), (dialogInterface, i) -> {
                        })
                        .setCancelable(true);

                final AlertDialog dialog = dialogBuilder.create();
                dialog.setOnShowListener(__ -> {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
                });

                dialog.show();
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
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage(R.string.cancle_write_reply)
                    .setPositiveButton(getString(R.string.confirm), (__, ___) -> finish())
                    .setNegativeButton(getString(R.string.cancle), (dialogInterface, i) -> {
                    })
                    .setCancelable(true);

            final AlertDialog dialog = dialogBuilder.create();
            dialog.setOnShowListener(__ -> {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
            });

            dialog.show();
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

        binding.rbReply.setOnRatingBarChangeListener((ratingBar, rating, b) -> {
            DLogUtil.d(rating + "");
        });
        binding.includeLoading.clLoadingBackground.setVisibility(View.GONE);
        binding.llcSelectScore.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            builder.setTitle("점수를 선택해 주세요!");
            View dialogLayout = inflater.inflate(R.layout.layout_dialog_rating, null);
            final RatingBar ratingBar = dialogLayout.findViewById(R.id.ratingBar);
            builder.setView(dialogLayout)
                    .setPositiveButton(getString(R.string.confirm), (__, ___) -> {
                        binding.rbReply.setRating(ratingBar.getRating());
                        if(ratingBar.getRating() < 0.5f){
                            binding.rbReply.setRating(0.5f);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancle), (dialogInterface, i) -> {

                    })
                    .setCancelable(true);

            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(__ -> {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));

            });

            dialog.show();
        });
        binding.tvWriteReply.setOnClickListener(view -> {
            if (binding.etReplyContent.getText().toString().length() < 5) {
                showToast(getString(R.string.notice_reply_length));
                return;
            }
            if(binding.rbReply.getRating() <0.5f){
                showToast(getString(R.string.wolud_you_select_score));
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
