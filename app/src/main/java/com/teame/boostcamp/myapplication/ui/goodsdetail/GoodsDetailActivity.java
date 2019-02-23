package com.teame.boostcamp.myapplication.ui.goodsdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;

import com.airbnb.lottie.LottieDrawable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsDetailRecyclerAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityGoodsDetailBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.entitiy.Reply;
import com.teame.boostcamp.myapplication.model.repository.GoodsDetailRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.goodscart.GoodsCartActivity;
import com.teame.boostcamp.myapplication.ui.writereply.WriteReplyActivity;
import com.teame.boostcamp.myapplication.util.Constant;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GoodsDetailActivity extends BaseMVPActivity<ActivityGoodsDetailBinding, GoodsDetailContract.Presenter> implements GoodsDetailContract.View {

    private final static String EXTRA_GOODS = "EXTRA_GOODS";
    private final static int REQ_WRITE_REPLY = 1000;
    private final static String EXTRA_REPLY = "EXTRA_REPLY";
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private AppCompatTextView tvBadge;

    @Override
    protected GoodsDetailContract.Presenter getPresenter() {
        GoodsDetailRepository repository = GoodsDetailRepository.getInstance();
        return new GoodsDetailPresenter(this, repository);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_goods_detail;
    }

    public static void startActivity(Context context, Goods item) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(EXTRA_GOODS, item);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        DLogUtil.e("onCreateOptionMenu");
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        final MenuItem menuItem = menu.findItem(R.id.btn_show_cart);
        View actionView = menuItem.getActionView();
        tvBadge = actionView.findViewById(R.id.cart_badge);
        tvBadge.setVisibility(View.GONE);
        presenter.getShoppingListCount();

        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_show_cart:
                GoodsCartActivity.startActivity(this);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        final Goods item;
        item = intent.getParcelableExtra(EXTRA_GOODS);
        if (item == null) {
            return;
        }

        presenter.reLoadReplyList(item.getKey());
        if (tvBadge != null) {
            presenter.getShoppingListCount();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onDetach();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_WRITE_REPLY) {
            if (resultCode == Activity.RESULT_OK) {
                // TODO Add Update Reply
                Reply item = data.getParcelableExtra(EXTRA_REPLY);
                DLogUtil.d(item.toString());
                presenter.writeReply(item);
            }
        }
    }

    private void initView() {

        Intent intent = getIntent();
        final Goods item;
        item = intent.getParcelableExtra(EXTRA_GOODS);
        if (item == null) {
            return;
        }
        binding.setItem(item);

        setSupportActionBar(binding.toolbarScreen);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //홈 아이콘을 숨김처리합니다.

        binding.tvWriteReply.setOnClickListener(view -> {
            Intent p = WriteReplyActivity.getIntent(this, item);
            startActivityForResult(p, REQ_WRITE_REPLY);
        });
        binding.includeLoading.lavLoading.playAnimation();
        binding.includeLoading.lavLoading.setRepeatCount(LottieDrawable.INFINITE);
        // 밑줄 넣기
        binding.tvItemMinPrice.setPaintFlags(binding.tvItemMinPrice.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        GoodsDetailRecyclerAdapter adapter = new GoodsDetailRecyclerAdapter();
        adapter.setOnItemDeleteListener((v, position) -> {
            Reply selectedReply = presenter.getItem(position);
            boolean isMine = TextUtils.equals(auth.getUid(), selectedReply.getWriter());
            BottomReplyDialogFragment bottomSheetDialog = BottomReplyDialogFragment.newInstance(isMine);

            bottomSheetDialog.setOnDeleteClickListener(__ -> {
                GoodsDetailRecyclerAdapter.ViewHolder holder =
                        (GoodsDetailRecyclerAdapter.ViewHolder) binding.rvReplyList
                                .findViewHolderForAdapterPosition(position);

                if (holder != null) {
                    holder.itemView.findViewById(R.id.pb_deleting).setVisibility(View.VISIBLE);
                    holder.itemView.findViewById(R.id.iv_delete).setVisibility(View.GONE);
                }

                bottomSheetDialog.dismiss();
                presenter.deleteReply(item.getKey(), position);
            });
            bottomSheetDialog.setOnReportClickListener(__ -> {
                bottomSheetDialog.dismiss();
                showToast("신고완료");
            });
            bottomSheetDialog.show(getSupportFragmentManager(), "Custom Bottom Sheet");
        });


        BottomSheetBehavior behavior = BottomSheetBehavior.from(binding.llcAddBottom);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setPeekHeight(0);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false);
        binding.rvReplyList.setLayoutManager(linearLayoutManager);
        binding.rvReplyList.setAdapter(adapter);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

        binding.tvAddList.setOnClickListener(view -> {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        presenter.loadReplyList(adapter, item.getKey());

        binding.tvItemMinPrice.setOnClickListener(view -> {
            if (item.getLink() != null) {
                Intent LpriceIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                startActivity(LpriceIntent);
            }
        });

        binding.tvCountPlus.setOnClickListener(view -> {
            int count = Integer.valueOf(binding.tvGoodsCount.getText().toString());

            if (count <= 1) {
                binding.tvGoodsCount.setText("1");
                return;
            }
            binding.tvGoodsCount.setText(String.format(Locale.getDefault(), "%d", --count));
            item.setCount(count);
        });

        binding.tvCountMinus.setOnClickListener(view -> {
            int count = Integer.valueOf(binding.tvGoodsCount.getText().toString());
            if (count >= 99) {
                binding.tvGoodsCount.setText("99");
                return;
            }
            binding.tvGoodsCount.setText(String.format(Locale.getDefault(), "%d", ++count));
            item.setCount(count);
        });

        binding.tvBottomCollaps.setOnClickListener(view ->
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED));

        binding.tvSelectGoods.setOnClickListener(__ -> {
            item.setCheck(true);
            presenter.addCartGoods(item);
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        });
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                                            @Override
                                            public void onStateChanged(@NonNull View view, int i) {
                                                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                                                    slideDown(binding.clBottomRoot);
                                                } else if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                                                    slideUp(binding.clBottomRoot);
                                                }
                                            }

                                            @Override
                                            public void onSlide(@NonNull View view, float v) {
                                            }
                                        }
        );

    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                view.getHeight(),
                0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                0,
                view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    @Override
    public void finishLoad(float totalRatio, int size) {
        binding.includeLoading.lavLoading.cancelAnimation();
        binding.includeLoading.lavLoading.setVisibility(View.GONE);
        if (size == Constant.LOADING_NONE_ITEM) {
            binding.tvTotalReplyCount.setText(String.format(getString(R.string.ratio_count), size));
            return;
        } else if (size == Constant.FAIL_LOAD) {
            showLongToast(getString(R.string.fail_load));
            binding.tvTotalReplyCount.setVisibility(View.GONE);
            return;
        }
        binding.tvTotalReplyCount.setText(String.format(getString(R.string.ratio_count), size));
        float percentRatio = totalRatio / size;
        String stringRatio = String.format(Locale.getDefault(), "%.1f", percentRatio);
        binding.tvItemRatio.setText(stringRatio);
        binding.rbReply.setRating(percentRatio);
    }

    @Override
    public void completeReloadReply() {
        binding.rvReplyList.smoothScrollToPosition(0);
    }

    @Override
    public void successAddCart() {
        showToast(getString(R.string.goods_add_cart));
        presenter.getShoppingListCount();
    }

    @Override
    public void duplicationAddCart() {
        showToast(getString(R.string.goods_add_cart));
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
    public boolean dispatchTouchEvent(MotionEvent event) {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(binding.llcAddBottom);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                Rect outRect = new Rect();
                binding.llcAddBottom.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
