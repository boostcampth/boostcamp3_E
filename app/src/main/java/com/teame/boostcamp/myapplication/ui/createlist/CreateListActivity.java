package com.teame.boostcamp.myapplication.ui.createlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.airbnb.lottie.LottieDrawable;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityCreateListBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.GoodsListHeader;
import com.teame.boostcamp.myapplication.model.repository.GoodsListRepository;
import com.teame.boostcamp.myapplication.ui.addgoods.AddGoodsActivity;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.goodscart.GoodsCartActivity;
import com.teame.boostcamp.myapplication.ui.goodsdetail.GoodsDetailActivity;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;
import com.teame.boostcamp.myapplication.util.view.ListSpaceItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;

public class CreateListActivity extends BaseMVPActivity<ActivityCreateListBinding, CreateListContract.Presenter> implements CreateListContract.View {

    private final static int REQ_ADD_ITEM = 5324;
    private static final String EXTRA_GOODS_LIST_HDAER = "EXTRA_GOODS_LIST_HDAER";
    private static final String EXTRA_SELECTED_GOODS_LIST = "EXTRA_SELECTED_GOODS_LIST";
    private static final String EXTRA_ADD_GOODS = "EXTRA_ADD_GOODS";
    private static final int SCROLL_DIRECTION_UP = -1;
    private AppCompatTextView tvBadge;
    private AppCompatImageView cartImage;
    private SearchView svGoods;
    private CompositeDisposable disposable = new CompositeDisposable();
    private boolean isFinish = false;

    @Override
    protected CreateListContract.Presenter getPresenter() {
        return new CreateListPresenter(this, GoodsListRepository.getInstance());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_create_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_list, menu);
        final MenuItem showCartItem = menu.findItem(R.id.btn_show_cart);
        final MenuItem goodsSearchItem = menu.findItem(R.id.goods_search);
        View cartItemActionView = showCartItem.getActionView();
        View goodsSearchItemActionView = goodsSearchItem.getActionView();
        tvBadge = cartItemActionView.findViewById(R.id.cart_badge);

        cartImage = cartItemActionView.findViewById(R.id.iv_cart_img);
        tvBadge.setVisibility(View.GONE);
        svGoods = goodsSearchItemActionView.findViewById(R.id.goods_search);
        ImageView icon = svGoods.findViewById(androidx.appcompat.R.id.search_button);
        icon.setColorFilter(Color.BLACK);
        ImageView iconClose = svGoods.findViewById(androidx.appcompat.R.id.search_close_btn);
        iconClose.setColorFilter(Color.BLACK);

        EditText editText = svGoods.findViewById(androidx.appcompat.R.id.search_src_text);
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.colorIphoneBlack));
        editText.setTextColor(Color.BLACK);

        iconClose.setOnClickListener(view -> initToolbar());

        svGoods.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print

                tvBadge.setVisibility(View.VISIBLE);
                cartImage.setVisibility(View.VISIBLE);
                binding.toolbarTitle.setVisibility(View.VISIBLE);
                binding.llcAddGoods.setVisibility(View.VISIBLE);
                presenter.diffSerchList(query);
                if (!svGoods.isIconified()) {
                    svGoods.setIconified(true);
                }
                svGoods.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String typingQuery) {
                if (typingQuery.length() != 0) {
                    binding.llcAddGoods.setVisibility(View.VISIBLE);
                    presenter.diffSerchList(typingQuery);
                }

                return false;
            }
        });


        presenter.getShoppingListCount();

        cartItemActionView.setOnClickListener(v -> onOptionsItemSelected(showCartItem));

        icon.setOnClickListener(v -> {
            tvBadge.setVisibility(View.GONE);
            cartImage.setVisibility(View.GONE);
            binding.toolbarTitle.setVisibility(View.GONE);
            svGoods.onActionViewExpanded();
        });
        return true;
    }

    public void initToolbar() {

        if (TextUtils.isEmpty(tvBadge.getText().toString()) || TextUtils.equals(tvBadge.getText().toString(), "0")) {
            tvBadge.setVisibility(View.GONE);
        } else {
            tvBadge.setVisibility(View.VISIBLE);
        }
        cartImage.setVisibility(View.VISIBLE);
        binding.toolbarTitle.setVisibility(View.VISIBLE);
        binding.llcAddGoods.setVisibility(View.GONE);
        binding.llcNoSearchResult.setVisibility(View.GONE);
        if (!svGoods.isIconified()) {
            svGoods.setIconified(true);
        }
        svGoods.onActionViewCollapsed();
        presenter.backCreateList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_show_cart:
                GoodsCartActivity.startActivity(this);
                break;
            case android.R.id.home:
                initToolbar();
                break;
            default:
                break;
        }
        return true;
    }

    public static void startActivity(Context context, GoodsListHeader header) {
        Intent intent = new Intent(context, CreateListActivity.class);
        intent.putExtra(EXTRA_GOODS_LIST_HDAER, header);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, GoodsListHeader header, ArrayList<Goods> goodslist) {
        Intent intent = new Intent(context, CreateListActivity.class);
        intent.putExtra(EXTRA_GOODS_LIST_HDAER, header);
        intent.putParcelableArrayListExtra(EXTRA_SELECTED_GOODS_LIST, goodslist);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tvBadge != null) {
            presenter.getShoppingListCount();
        }
    }

    public void initView() {
        binding.includeLoading.lavLoading.playAnimation();
        binding.includeLoading.lavLoading.setRepeatCount(LottieDrawable.INFINITE);
        GoodsListHeader header = getIntent().getParcelableExtra(EXTRA_GOODS_LIST_HDAER);
        if (header == null) {
            // 테스트 코드
            header = new GoodsListHeader();
            header.setNation("JP");
            header.setCity("osaka");
            header.setStartDate(Calendar.getInstance().getTime());
            header.setEndDate(Calendar.getInstance().getTime());
            header.setLat(11.1);
            header.setLng(11.2);
        }

        presenter.saveListHeader(header);
        setSupportActionBar(binding.toolbarScreen);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //홈 아이콘을 숨김처리합니다.
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_all_back);
        binding.toolbarTitle.setText(header.getCity());
        GoodsListRecyclerAdapter adapter = new GoodsListRecyclerAdapter();
        binding.rvRecommendList.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rvRecommendList.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemDetailListener((v, position) -> {
            presenter.getDetailItemUid(position);
        });
        binding.rvRecommendList.setAdapter(adapter);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space_line);
        binding.rvRecommendList.addItemDecoration(new ListSpaceItemDecoration(spacingInPixels, 3));
        //TODO API 21 이하 대응 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.rvRecommendList.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (binding.rvRecommendList.canScrollVertically(SCROLL_DIRECTION_UP)) {
                        binding.ablTopControl.setElevation(10);
                    } else {
                        binding.ablTopControl.setElevation(0);
                    }
                }
            });
        }
        presenter.loadListData(adapter, header.getNation(), header.getCity());
        binding.llcAddGoods.setOnClickListener(view -> {
            presenter.addGoods();
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onDetach();
        if (disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void showDetailItem(Goods item) {

        if (item.getUserCustomUri() != null && item.getKey() == null) {
            showToast(getString(R.string.none_click_goods));
            return;
        }
        GoodsDetailActivity.startActivity(this, item);
    }

    @Override
    public void finishLoad(int size) {
        isFinish = true;
        binding.includeLoading.lavLoading.cancelAnimation();
        binding.includeLoading.lavLoading.setVisibility(View.GONE);
        if (size == Constant.LOADING_NONE_ITEM) {
            binding.llcAddGoods.setVisibility(View.VISIBLE);
            binding.llcNoSearchResult.setVisibility(View.VISIBLE);
            showLongToast(String.format(getString(R.string.none_item), getString(R.string.toast_goods)));
        } else if (size == Constant.FAIL_LOAD) {
            showLongToast(getString(R.string.fail_load));
        }
    }

    @Override
    public void setBadge(String count) {
        if (count == null || Integer.valueOf(count) == 0) {
            tvBadge.setVisibility(View.GONE);
            return;
        }
        if (Integer.valueOf(count) >= 99) {
            tvBadge.setText("99+");
            tvBadge.setVisibility(View.VISIBLE);
        }
        tvBadge.setText(count);
        tvBadge.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        initToolbar();
    }


    @Override
    public void backActivity(int size) {
        if (isFinish) {
            if (size == 0) {
                binding.llcAddGoods.setVisibility(View.VISIBLE);
                binding.llcNoSearchResult.setVisibility(View.VISIBLE);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.cancel_create_list))
                .setPositiveButton(getString(R.string.confirm), (__, ___) -> {
                    presenter.removeCart();
                    finish();
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

    }

    @Override
    public void resultSearchScreen(int size) {
        if (size <= 0) {
            binding.llcNoSearchResult.setVisibility(View.VISIBLE);
        } else {
            binding.llcNoSearchResult.setVisibility(View.GONE);
        }
        binding.llcAddGoods.setVisibility(View.VISIBLE);
    }

    @Override
    public void goAddItem(String goodsName) {
        Intent intent = AddGoodsActivity.getIntent(this, goodsName);
        startActivityForResult(intent, REQ_ADD_ITEM);
    }

    @Override
    public void successAddCart() {
        showToast(getString(R.string.goods_add_cart));
        presenter.getShoppingListCount();
        initToolbar();
        int position = binding.rvRecommendList.getAdapter().getItemCount();
        binding.rvRecommendList.smoothScrollToPosition(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD_ITEM) {
            if (resultCode == RESULT_OK) {
                Goods item = data.getParcelableExtra(EXTRA_ADD_GOODS);
                presenter.addCartGoods(item);
                DLogUtil.d(item.toString());
            }
        }
    }
}

