package com.teame.boostcamp.myapplication.ui;

import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieDrawable;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentMyListBinding;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.selectedgoods.SelectedGoodsActivity;
import com.teame.boostcamp.myapplication.util.Constant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyListFragment extends BaseFragment<FragmentMyListBinding, MyListContract.Presenter> implements MyListContract.View {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_my_list;
    }

    @Override
    protected MyListContract.Presenter getPresenter() {
        MyListRepository repository = MyListRepository.getInstance();
        return new MyListPresenter(this, repository);
    }

    @Deprecated
    public MyListFragment() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
    }

    public static MyListFragment newInstance() {
        MyListFragment fragment = new MyListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        presenter.onAttach();
    }

    private void initView() {
        binding.includeLoading.lavLoading.playAnimation();
        binding.includeLoading.lavLoading.setRepeatCount(LottieDrawable.INFINITE);
        GoodsListHeaderRecyclerAdapter adapter = new GoodsListHeaderRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,
                false);

        adapter.setOnItemClickListener((v, position) -> presenter.getMyListUid(position));
        adapter.setOnItemAlaramListener((v, position) -> {
            // TODO: 알람 등록 리스너
        });
        adapter.setOnItemDeleteListener((v, position) -> presenter.deleteMyList(position));
        binding.rvMyList.setLayoutManager(linearLayoutManager);
        binding.rvMyList.setAdapter(adapter);
        presenter.loadMyList(adapter);
    }

    @Override
    public void finishLoad(int size) {
        binding.includeLoading.lavLoading.cancelAnimation();
        binding.includeLoading.lavLoading.setVisibility(View.GONE);
        if (size == Constant.LOADING_NONE_ITEM) {
            showLongToast(String.format(getString(R.string.none_item), getString(R.string.toast_mylist)));
        } else if (size == Constant.FAIL_LOAD) {
            showLongToast(getString(R.string.fail_load));
        }
    }

    @Override
    public void showMyListItems(String headerKey) {
        SelectedGoodsActivity.startActivity(getContext(), headerKey);
    }
}
