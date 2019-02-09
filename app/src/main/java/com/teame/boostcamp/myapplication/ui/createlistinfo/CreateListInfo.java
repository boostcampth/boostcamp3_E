package com.teame.boostcamp.myapplication.ui.createlistinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityCreateListInfoBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.model.repository.remote.MyListRemoteDataSource;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.createlist.CreateListActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.ArrayList;
import java.util.List;

public class CreateListInfo extends BaseMVPActivity<ActivityCreateListInfoBinding, CreateListInfoContract.Presenter> implements CreateListInfoContract.View {

    private static final String EXTRA_GOODS_LIST_HDAER = "EXTRA_GOODS_LIST_HDAER";
    private static final String EXTRA_GOODS_LIST = "EXTRA_GOODS_LIST";

    public static void startActivity(Context context, GoodsListHeader header, ArrayList<Goods> list) {
        Intent intent = new Intent(context, CreateListInfo.class);
        intent.putExtra(EXTRA_GOODS_LIST_HDAER, header);
        intent.putParcelableArrayListExtra(EXTRA_GOODS_LIST, list);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_shoppinglist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.btn_decide:
                GoodsListHeader header = getIntent().getParcelableExtra(EXTRA_GOODS_LIST_HDAER);
                List<Goods> goodsList = getIntent().getParcelableArrayListExtra(EXTRA_GOODS_LIST);
                header.setTitle(binding.tieTitle.getText().toString());
                presenter.saveMyList(goodsList, header);
                showToast("결정");
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_list_info;
    }

    @Override
    protected CreateListInfoContract.Presenter getPresenter() {
        MyListRepository repository = MyListRepository.getInstance();
        return new CreateListInfoPresenter(this, repository);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_all_back);
        initView();
    }

    private void initView() {
        GoodsListHeader header = getIntent().getParcelableExtra(EXTRA_GOODS_LIST_HDAER);
        List<Goods> list = getIntent().getParcelableArrayListExtra(EXTRA_GOODS_LIST);
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
            if (actionId == EditorInfo.IME_ACTION_GO) {
                presenter.addHashTag(binding.tieHashtag.getText().toString());
            }
            return false;
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
    public void successSave() {
        showToast("성공");
    }
}