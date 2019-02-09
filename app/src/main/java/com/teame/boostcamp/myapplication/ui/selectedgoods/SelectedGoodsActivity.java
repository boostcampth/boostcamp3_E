package com.teame.boostcamp.myapplication.ui.selectedgoods;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsListRecyclerAdapter;
import com.teame.boostcamp.myapplication.databinding.ActivitySelectedGoodsBinding;
import com.teame.boostcamp.myapplication.model.entitiy.Goods;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.goodsdetail.GoodsDetailActivity;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.CompositeDisposable;

public class SelectedGoodsActivity extends BaseMVPActivity<ActivitySelectedGoodsBinding, SelectedGoodsContract.Presenter> implements SelectedGoodsContract.View {

    public static final String EXTRA_HEADER_UID = "EXTRA_HEADER_UID";
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected SelectedGoodsContract.Presenter getPresenter() {
        return new SelectedGoodsPresenter(this, MyListRepository.getInstance());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_selected_goods;
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
                presenter.getCheckedList();
                break;
            default:
                break;
        }

        return true;
    }

    public static void startActivity(Context context, String headerUid) {
        Intent intent = new Intent(context, SelectedGoodsActivity.class);
        intent.putExtra(EXTRA_HEADER_UID, headerUid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView();
    }

    public void initView() {
        Intent intent = getIntent();
        final String headerUid;
        headerUid = intent.getStringExtra(EXTRA_HEADER_UID);

        if(headerUid==null){
            finish();
            return;
        }

        setSupportActionBar(binding.toolbarScreen);
        getSupportActionBar().setDisplayShowHomeEnabled(true); //홈 아이콘을 숨김처리합니다.
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_all_back);
        GoodsListRecyclerAdapter adapter = new GoodsListRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.VERTICAL,
                false);
        binding.rvRecommendList.setLayoutManager(linearLayoutManager);
        binding.rvRecommendList.setAdapter(adapter);
        presenter.loadListData(adapter, headerUid);

        adapter.setOnItemClickListener((__, position, isCheck) ->
                presenter.selectItem(position, isCheck));

        adapter.setOnItemDetailListener((__, position) -> presenter.getDetailItemUid(position));
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
    public void saveCheckedList(List<Goods> list) {
        // TODO : 저장된 아이템 해쉬테그, 제목 결정하는 곳으로 넘겨주기
        showToast("NextStep");
    }

    @Override
    public void showDetailItem(Goods item) {
        GoodsDetailActivity.startActivity(this, item);
    }

}
