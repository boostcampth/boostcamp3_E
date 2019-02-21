package com.teame.boostcamp.myapplication.ui.goodscart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.airbnb.lottie.LottieDrawable;
import com.google.android.material.chip.Chip;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsCartAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityGoodsCartBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.MainActivity;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.period.PeriodActivity;
import com.teame.boostcamp.myapplication.util.CalendarUtil;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DateRangeLimiter;

import java.util.Calendar;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsCartActivity extends BaseMVPActivity<ActivityGoodsCartBinding, GoodsCartContract.Presenter> implements GoodsCartContract.View {

    private boolean isChange = false;
    private static final int REQUEST_CODE=1;
    private static final String EXTRA_FIRSTDAY="EXTRA_FIRSTDAY";
    private static final String EXTRA_LASTDAY="EXTRA_LASTDAY";

    @Override
    protected GoodsCartContract.Presenter getPresenter() {
        return new GoodsCartPresenter(this);
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
        if(requestCode==REQUEST_CODE){
            CalendarDay first=data.getParcelableExtra(EXTRA_FIRSTDAY);
            CalendarDay last=data.getParcelableExtra(EXTRA_LASTDAY);
            Calendar firstDay=Calendar.getInstance();
            firstDay.set(first.getYear(),first.getMonth(),first.getDay());
            Calendar lastDay=Calendar.getInstance();
            lastDay.set(last.getYear(),last.getMonth(),last.getDay());
            String str="";
            str+=first.getYear()+"년 "+first.getMonth()+"월 "+first.getDay()+"일 ~ ";
            str+=last.getYear()+"년 "+last.getMonth()+"월 "+last.getDay()+"일, "+ CalendarUtil.daysBetween(firstDay,lastDay);
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

        binding.tvTotalDate.setOnClickListener(__-> PeriodActivity.startActivity(this));
        String str="";
        Calendar today=Calendar.getInstance();
        str+=today.get(Calendar.YEAR)+"년 "+Integer.toString(today.get(Calendar.MONTH)+1)+"월 "+today.get(Calendar.DATE)+"일 ~ ";
        today.add(Calendar.DATE,1);
        str+=today.get(Calendar.YEAR)+"년 "+Integer.toString(today.get(Calendar.MONTH)+1)+"월 "+today.get(Calendar.DATE)+"일, 1박";
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
        });
        binding.rvCartList.setLayoutManager(linearLayoutManager);
        binding.rvCartList.setAdapter(adapter);
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

        binding.cgHashSet.addView(chip,binding.cgHashSet.getChildCount() - 1);
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
    public void setAllorNoneCheck(boolean allCheck) {
        binding.cbAll.setChecked(allCheck);
        if (allCheck) {
            binding.tvDicideCart.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            binding.tvDicideCart.setClickable(true);
            binding.tvTotalPrice.setVisibility(View.VISIBLE);
        } else {
            binding.tvDicideCart.setBackgroundColor(ContextCompat.getColor(this, R.color.colorIphoneBlack));
            binding.tvDicideCart.setClickable(false);
            binding.tvTotalPrice.setVisibility(View.GONE);
        }
    }

    @Override
    public void errorSaveGoods(int flag) {
        if(flag == 0 ){
            showToast(getString(R.string.no_select_item));
        }else if(flag == 1){
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
        Map<String,Boolean> hashTag = header.getHashTag();

        for(String tag : hashTag.keySet()){

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

            binding.cgHashSet.addView(chip,binding.cgHashSet.getChildCount() - 1);
            chip.setOnCloseIconClickListener(view -> {
                binding.cgHashSet.removeView(chip);
                presenter.removeHashTag(chip.getText().toString());
            });
            binding.etTag.setText("");
        }
    }

}
