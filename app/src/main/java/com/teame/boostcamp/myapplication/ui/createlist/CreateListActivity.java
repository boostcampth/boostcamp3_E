package com.teame.boostcamp.myapplication.ui.createlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivityCreateListBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.repository.GoodsListRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.DLogUtil;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public class CreateListActivity extends BaseMVPActivity<ActivityCreateListBinding, CreateListContract.Presenter> implements CreateListContract.View {

    CompositeDisposable disposable = new CompositeDisposable();
    PublishSubject<RecyclerView> subject = PublishSubject.create();

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_shoppinglist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.btn_decide:
                presenter.decideShoppingList();
                break;
            default:
                break;
        }

        return true;
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CreateListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
    }

    public void initView() {
        setSupportActionBar(binding.toolbarScreen);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //홈 아이콘을 숨김처리합니다.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_all_back);
        GoodsListRecyclerAdapter adapter = new GoodsListRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false);
        binding.rvRecommendList.setLayoutManager(linearLayoutManager);
        binding.rvRecommendList.setAdapter(adapter);
        presenter.loadListData(adapter);

        adapter.setOnItemClickListener((view, position, isCheck) ->
                presenter.selectItem(position, isCheck));
        binding.etAddItem.setOnClickListener(view -> binding.ablTopControl.setExpanded(false));
        binding.ibAddItem.setOnClickListener(v -> {
            String itemName = binding.etAddItem.getText().toString();
            presenter.addItem(itemName);
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
    public void goNextStep(List<Goods> list) {
        // TODO : 저장된 아이템 해쉬테그, 제목 결정하는 곳으로 넘겨주기
        showToast("NextStep");
    }

    @Override
    public void showAddedItem(int position) {

        GoodsListRecyclerAdapter adapter = (GoodsListRecyclerAdapter) binding.rvRecommendList.getAdapter();
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) binding.rvRecommendList.getLayoutManager();

        if (position == -1) {
            // 리스트에 아이템이 없으면
            int lastPosition = adapter.getItemCount();
            DLogUtil.e("lastPositin : " + lastPosition);
            linearLayoutManager.scrollToPosition(lastPosition - 1);
        } else {
            // 아이템이 있으면
            binding.rvRecommendList.smoothScrollToPosition(position);
            // 현재 View에 있는 아이템은 shake Anim
            View viewItem = binding.rvRecommendList.getLayoutManager().findViewByPosition(position);
            if (viewItem != null) {
                View layout = viewItem.findViewById(R.id.cv_item_layout);
                final Animation animShake
                        = AnimationUtils.loadAnimation(layout.getContext(), R.anim.anim_shake);
                layout.startAnimation(animShake);
            } else {
                adapter.setAnimPosition(position);
                linearLayoutManager.scrollToPosition(position);
            }
        }
        DLogUtil.e("position : " + position);
        binding.ablTopControl.setExpanded(false);
    }

}
