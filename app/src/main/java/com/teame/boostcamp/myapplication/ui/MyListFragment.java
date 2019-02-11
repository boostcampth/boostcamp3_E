package com.teame.boostcamp.myapplication.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.GoodsListHeaderRecyclerAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentMyListBinding;
import com.teame.boostcamp.myapplication.model.repository.MyListRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.selectedgoods.SelectedGoodsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyListFragment extends BaseFragment<FragmentMyListBinding, MyListContract.Presenter> implements MyListContract.View{

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_my_list;
    }

    @Override
    protected MyListContract.Presenter getPresenter() {
        MyListRepository repository = MyListRepository.getInstance();
        return new MyListPresenter(this,repository);
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

        GoodsListHeaderRecyclerAdapter adapter = new GoodsListHeaderRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,
                false);

        presenter.loadMyList(adapter);
        adapter.setOnItemClickListener((v, position) -> presenter.getMyListUid(position));
        adapter.setOnItemAlaramListener((v, position) -> {
            // TODO: 알람 등록 리스너
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage(position+"번째 리스트 알림설정")
                    .show();
        });
        adapter.setOnItemDeleteListener((v, position) -> presenter.deleteMyList(position));
        binding.rvMyList.setLayoutManager(linearLayoutManager);
        binding.rvMyList.setAdapter(adapter);
    }

    @Override
    public void showMyListItems(String headerKey) {
        SelectedGoodsActivity.startActivity(getContext(), headerKey);
    }
}
