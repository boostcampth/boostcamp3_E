package com.teame.boostcamp.myapplication.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentSnsBinding;
import com.teame.boostcamp.myapplication.ui.addpost.AddPostActivity;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SNSFragment extends BaseFragment<FragmentSnsBinding, SNSContract.Presenter> implements SNSContract.View {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_sns;
    }

    @Override
    protected SNSContract.Presenter getPresenter() {
        return new SNSPresenter(this);
    }

    @Override
    public void setPresenter(SNSContract.Presenter presenter) {
        super.setPresenter(presenter);
    }

    @Deprecated
    public SNSFragment() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setPresenter(new SNSPresenter(this));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static SNSFragment newInstance() {
        SNSFragment fragment = new SNSFragment();
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
        binding.srlSns.setRefreshing(true);
        initView();
    }

    private void initView() {
        PostListAdapter adapter = new PostListAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,
                false);

        binding.rvSns.setLayoutManager(linearLayoutManager);
        binding.rvSns.setAdapter(adapter);
        binding.fabAddPost.setOnClickListener(__ -> AddPostActivity.startActivity(getContext()));
        presenter.loadPostData(adapter);
        binding.rvSns.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    binding.fabAddPost.hide();
                } else {
                    binding.fabAddPost.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        binding.srlSns.setOnRefreshListener(() -> initView());
    }

    @Override
    public void stopRefreshIcon() {
        binding.srlSns.setRefreshing(false);
    }
}


