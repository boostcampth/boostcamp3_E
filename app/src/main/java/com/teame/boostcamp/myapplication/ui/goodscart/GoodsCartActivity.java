package com.teame.boostcamp.myapplication.ui.goodscart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import com.google.android.material.chip.Chip;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsCartAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityGoodsCartBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsCartActivity extends BaseMVPActivity<ActivityGoodsCartBinding, GoodsCartContract.Presenter> implements GoodsCartContract.View {

    private boolean isChange = false;

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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage(R.string.would_you_save)
                            .setPositiveButton(getString(R.string.confirm), (__, ___) -> {
                                presenter.saveCartList();
                                showToast(getString(R.string.success_save));
                                finish();
                            })
                            .setNegativeButton(R.string.cancle, (__, ___) -> finish())
                            .show();
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

    void initView() {
        setSupportActionBar(binding.toolbarScreen);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //홈 아이콘을 숨김처리합니다.

        GoodsCartAdapter adapter = new GoodsCartAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false);

        presenter.loadData(adapter);
        presenter.calculatorPrice();
        presenter.detectIsAllCheck();
        GoodsListHeader header = presenter.getHeaderData();

        binding.tieTitle.setOnFocusChangeListener((__, hasFocus) -> {
            if (hasFocus) {
                binding.tilTitle.setHint("title");
            } else {
                String title = binding.tieTitle.getText().toString();
                if (title.length() <= 0) {
                    binding.tilTitle.setHint(header.getDefaultTitle());
                }
            }
        });

        binding.tieHashtag.setOnEditorActionListener((__, actionId, ___) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                presenter.addHashTag(binding.tieHashtag.getText().toString());
            }
            return false;
        });

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
            presenter.saveMyList();
        });
    }

    @Override
    public void addedHashTag(String tag) {
        Chip chip = new Chip(this);
        chip.setText(tag);
        chip.setCloseIconEnabled(true); // 대체방법을 못찾음
        chip.setClickable(false);
        chip.setCheckable(false);
        binding.cgHashSet.addView(chip);
        chip.setOnCloseIconClickListener(view -> {
            binding.cgHashSet.removeView(chip);
            presenter.removeHashTag(chip.getText().toString());
        });
        binding.tieHashtag.setText("");
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
    }

    @Override
    public void noSelectGoods() {
        showToast(getString(R.string.no_select_item));
    }


    @Override
    public void successSave() {
        showToast("성공");
    }
}
