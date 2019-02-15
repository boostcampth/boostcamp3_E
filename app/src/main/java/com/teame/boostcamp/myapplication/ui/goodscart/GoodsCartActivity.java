package com.teame.boostcamp.myapplication.ui.goodscart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsCartAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityGoodsCartBinding;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.createlistinfo.CreateListInfo;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsCartActivity extends BaseMVPActivity<ActivityGoodsCartBinding, GoodsCartContract.Presenter> implements GoodsCartContract.View {

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
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage(R.string.would_you_save)
                        .setPositiveButton(getString(R.string.confirm), (__, ___) -> {
                            presenter.saveCartList();
                            showToast(getString(R.string.success_save));
                            finish();
                        })
                        .setNegativeButton(R.string.cancle, (__, ___) -> finish())
                        .show();
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
        presenter.detectIsAllCheck();
        adapter.setOnItemDeleteListener((v, position) -> {
            presenter.deleteItem(position);
            presenter.calculatorPrice();
        });
        adapter.setOnItemSpinnerListener((v, position) -> presenter.calculatorPrice());
        adapter.setOnItemCheckListener((v, position) -> {
            presenter.calculatorPrice();
            presenter.detectIsAllCheck();
        });
        binding.rvCartList.setLayoutManager(linearLayoutManager);
        binding.rvCartList.setAdapter(adapter);

        binding.cbAll.setOnClickListener(view -> {
            boolean check = binding.cbAll.isChecked();
            adapter.allCheck(check);
        });
        binding.tvDicideCart.setOnClickListener(view -> presenter.getThrowData());
    }

    @Override
    public void decide(GoodsListHeader header) {
        CreateListInfo.startActivity(this, header);
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
}
