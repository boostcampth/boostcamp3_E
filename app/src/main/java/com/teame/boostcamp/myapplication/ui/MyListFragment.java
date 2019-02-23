package com.teame.boostcamp.myapplication.ui;

import android.app.AlertDialog;
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
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
        adapter.setOnItemDeleteListener((v, position) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getString(R.string.would_you_remove_check_list))
                    .setPositiveButton(getString(R.string.confirm), (__, ___) -> {
                        presenter.deleteMyList(position);
                    })
                    .setNegativeButton(getString(R.string.cancle), (dialogInterface, i) -> {

                    })
                    .setCancelable(true);

            final AlertDialog dialog = builder.create();
            dialog.setOnShowListener(__ -> {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorClear));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorClear));

            });

            dialog.show();

            });
        binding.rvMyList.setLayoutManager(linearLayoutManager);
        binding.rvMyList.setItemAnimator(new DefaultItemAnimator());
        binding.rvMyList.setAdapter(adapter);
        presenter.loadMyList(adapter);

        binding.svMylist.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                presenter.diffSerchList(query);
                binding.svMylist.onActionViewCollapsed();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String typingQuery) {
                if (typingQuery.length() != 0) {
                    presenter.diffSerchList(typingQuery);
                }

                return false;
            }
        });

        binding.srlMyList.setOnRefreshListener(() -> {
            presenter.reLoadMyList();
            binding.srlMyList.setRefreshing(false);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.reLoadMyList();
    }

    @Override
    public void finishLoad(int size) {
        binding.includeLoading.lavLoading.cancelAnimation();
        binding.includeLoading.lavLoading.setVisibility(View.GONE);
        if (size == Constant.LOADING_NONE_ITEM) {
            binding.llcNoSearchResult.setVisibility(View.VISIBLE);
        } else if (size == Constant.FAIL_LOAD) {
            binding.llcNoSearchResult.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMyListItems(String headerKey) {
        SelectedGoodsActivity.startActivity(getContext(), headerKey);
    }
}
