package com.teame.boostcamp.myapplication.ui.goodscart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.airbnb.lottie.LottieDrawable;
import com.google.android.material.chip.Chip;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsCartAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityGoodsCartBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.MainActivity;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.period.PeriodActivity;
import com.teame.boostcamp.myapplication.util.CalendarUtil;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.ResourceProvider;
import com.teame.boostcamp.myapplication.util.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsCartActivity extends BaseMVPActivity<ActivityGoodsCartBinding, GoodsCartContract.Presenter> implements GoodsCartContract.View {

    private boolean isChange = false;
    private static final int REQUEST_CODE = 1;


    @Override
    protected GoodsCartContract.Presenter getPresenter() {
        return new GoodsCartPresenter(this, new ResourceProvider(getApplicationContext()));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_goods_cart;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isChange) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.would_you_save)
                            .setPositiveButton(getString(R.string.confirm), (__, ___) -> {
                                presenter.saveCartList(binding.etTitle.getText().toString());
                                showToast(getString(R.string.success_save));
                                finish();
                            })
                            .setNegativeButton(R.string.cancle, (__, ___) -> finish());


                    final AlertDialog dialog = builder.create();
                    dialog.setOnShowListener(__ -> {
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));

                    });

                    dialog.show();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }

        return true;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GoodsCartActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
            String firstString = SharedPreferenceUtil.getString(this, "PREF_ACTIVITY_FIRST_DATE");
            String lastString = SharedPreferenceUtil.getString(this, "PREF_ACTIVITY_LAST_DATE");

            Calendar firstDay = Calendar.getInstance();
            Calendar lastDay = Calendar.getInstance();
            try {
                firstDay.setTime(sdf.parse(firstString));
                lastDay.setTime(sdf.parse(lastString));
            } catch (Exception e) {
                DLogUtil.e(e.toString());
            }

            int between = CalendarUtil.daysBetween(firstDay, lastDay);

            String str = "";
            str += firstString + " ~ " + lastString + ", " + between + "박";

            binding.tvTotalDate.setText(str);
        }
    }

    void initView() {
        setSupportActionBar(binding.toolbarScreen);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //홈 아이콘을 숨김처리합니다.

        GoodsCartAdapter adapter = new GoodsCartAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false);

        int size = presenter.loadData(adapter);

        binding.rvCartList.setLayoutManager(linearLayoutManager);
        binding.rvCartList.setAdapter(adapter);
        presenter.calculatorPrice();
        presenter.detectIsAllCheck();
        binding.includeLoading.clLoadingBackground.setVisibility(View.GONE);
        GoodsListHeader header = presenter.getHeaderData();

        binding.etTag.setOnEditorActionListener((__, actionId, ___) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                presenter.addHashTag(binding.etTag.getText().toString());
            }
            return false;
        });

        binding.tvTotalDate.setOnClickListener(__ -> PeriodActivity.startActivity(this));

        String str = "";
        Calendar today = Calendar.getInstance();
        Calendar tomarrow = (Calendar) today.clone();
        tomarrow.add(Calendar.DATE, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        String todayString = sdf.format(today.getTime());
        String tomarrowString = sdf.format(tomarrow.getTime());

        SharedPreferenceUtil.putString(getApplicationContext(), "PREF_ACTIVITY_FIRST_DATE", todayString);
        SharedPreferenceUtil.putString(getApplicationContext(), "PREF_ACTIVITY_LAST_DATE", tomarrowString);

        str += today.get(Calendar.YEAR) + "년 " + Integer.toString(today.get(Calendar.MONTH) + 1) + "월 " + today.get(Calendar.DATE) + "일 ~ ";
        str += tomarrow.get(Calendar.YEAR) + "년 " + Integer.toString(tomarrow.get(Calendar.MONTH) + 1) + "월 " + tomarrow.get(Calendar.DATE) + "일, 1박";
        binding.tvTotalDate.setText(str);

        adapter.setOnItemDeleteListener((v, position) -> {
            isChange = true;
            presenter.deleteItem(position);
            presenter.calculatorPrice();
        });
        adapter.setOnItemSpinnerListener((v, position) -> {
            isChange = true;
            presenter.calculatorPrice();
        });
        adapter.setOnItemCheckListener((v, position) -> {
            isChange = true;
            presenter.detectIsAllCheck();
            presenter.calculatorPrice();
        });
        binding.cbAll.setOnClickListener(view -> {
            isChange = true;
            boolean check = binding.cbAll.isChecked();
            adapter.allCheck(check);
            presenter.detectIsAllCheck();
            presenter.calculatorPrice();
        });
        binding.tvDicideCart.setOnClickListener(view -> {
            binding.includeLoading.clLoadingBackground.setVisibility(View.VISIBLE);
            binding.includeLoading.lavLoading.playAnimation();
            binding.includeLoading.lavLoading.setRepeatCount(LottieDrawable.INFINITE);
            binding.includeLoading.clLoadingBackground.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorBlurGray));
            presenter.saveMyList(binding.etTitle.getText().toString());
        });

        if (size <= 0) {
            emptyList();
        }
    }

    @Override
    public void addedHashTag(String tag) {
        Chip chip = new Chip(this);
        chip.setText(tag);
        chip.setCloseIconEnabled(true); // 대체방법을 못찾음
        chip.setClickable(false);
        chip.setCheckable(false);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(4, 4, 4, 4);
        chip.setLayoutParams(params);

        binding.cgHashSet.addView(chip, binding.cgHashSet.getChildCount() - 1);
        chip.setOnCloseIconClickListener(view -> {
            binding.cgHashSet.removeView(chip);
            presenter.removeHashTag(chip.getText().toString());
        });
        binding.etTag.setText("");
    }


    @Override
    public void duplicationTag() {
        showToast(getString(R.string.already_tag));
    }

    @Override
    public void setResultPrice(String resultPrice) {
        binding.tvTotalPrice.setText(resultPrice);
    }

    @Override
    public void setAllorNoneCheck(int isAllUnCheck) {


        if (isAllUnCheck == binding.rvCartList.getAdapter().getItemCount()) {
            // 모두 언책
            binding.tvDicideCart.setBackgroundColor(ContextCompat.getColor(this, R.color.colorIphoneBlack));
            binding.tvDicideCart.setClickable(false);
            binding.tvTotalPrice.setVisibility(View.GONE);
            binding.cbAll.setChecked(false);
        } else if (isAllUnCheck > 0) {
            // 하나라도 unCheck이 있는것
            binding.tvDicideCart.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            binding.tvDicideCart.setClickable(true);
            binding.tvTotalPrice.setVisibility(View.VISIBLE);
            binding.cbAll.setChecked(false);
        } else {
            // 모두 트루
            binding.tvDicideCart.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            binding.tvDicideCart.setClickable(true);
            binding.tvTotalPrice.setVisibility(View.VISIBLE);
            binding.cbAll.setChecked(true);
        }
    }

    @Override
    public void errorSaveGoods(int flag) {
        if (flag == 0) {
            showToast(getString(R.string.no_select_item));
        } else if (flag == 1) {
            showToast(getString(R.string.would_you_set_title));
        }


        binding.includeLoading.clLoadingBackground.setVisibility(View.GONE);
        binding.includeLoading.clLoadingBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
        binding.includeLoading.lavLoading.cancelAnimation();
        binding.includeLoading.lavLoading.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (binding.includeLoading.clLoadingBackground.getVisibility() == View.VISIBLE) {
            return;
        }

        if (isChange) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.would_you_save)
                    .setPositiveButton(getString(R.string.confirm), (__, ___) -> {
                        presenter.saveCartList(binding.etTitle.getText().toString());
                        showToast(getString(R.string.success_save));
                        finish();
                    })
                    .setNegativeButton(R.string.cancle, (__, ___) -> finish());

            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(__ -> {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));

            });
            dialog.show();
        } else {
            finish();
        }
    }

    @Override
    public void successSave() {
        showToast("성공");
        binding.includeLoading.clLoadingBackground.setVisibility(View.GONE);
        binding.includeLoading.clLoadingBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.colorClear));
        binding.includeLoading.lavLoading.cancelAnimation();
        binding.includeLoading.lavLoading.setVisibility(View.GONE);
        MainActivity.startActivity(this);
        finish();
    }

    @Override
    public void emptyList() {
        binding.llcNoAddItem.setVisibility(View.VISIBLE);
        binding.llcAllCheckRoot.setVisibility(View.GONE);
        binding.tvDicideCart.setBackgroundColor(ContextCompat.getColor(this, R.color.colorIphoneBlack));
        binding.tvDicideCart.setOnClickListener(null);
        binding.tvTotalPrice.setVisibility(View.GONE);
    }

    @Override
    public void setLoadData(GoodsListHeader header) {
        binding.etTitle.setText(header.getTitle());
        Map<String, Boolean> hashTag = header.getHashTag();

        for (String tag : hashTag.keySet()) {

            Chip chip = new Chip(this);
            chip.setText(tag);
            chip.setCloseIconEnabled(true); // 대체방법을 못찾음
            chip.setClickable(false);
            chip.setCheckable(false);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 4, 4, 4);
            chip.setLayoutParams(params);

            binding.cgHashSet.addView(chip, binding.cgHashSet.getChildCount() - 1);
            chip.setOnCloseIconClickListener(view -> {
                binding.cgHashSet.removeView(chip);
                presenter.removeHashTag(chip.getText().toString());
            });
            binding.etTag.setText("");
        }
    }

}
