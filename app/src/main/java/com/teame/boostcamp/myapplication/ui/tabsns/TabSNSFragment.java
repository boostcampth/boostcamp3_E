package com.teame.boostcamp.myapplication.ui.tabsns;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.PostListAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentTabSnsBinding;
import com.teame.boostcamp.myapplication.model.repository.PostListRepository;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TabSNSFragment extends BaseFragment<FragmentTabSnsBinding, TabSNSContract.Presenter> implements TabSNSContract.View {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_tab_sns;
    }

    @Override
    protected TabSNSContract.Presenter getPresenter() {
        return new TabSNSPresenter(this, PostListRepository.getInstance());
    }

    @Override
    public void setPresenter(TabSNSContract.Presenter presenter) {
        super.setPresenter(presenter);
    }

    @Deprecated
    public TabSNSFragment() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setPresenter(new TabSNSPresenter(this, PostListRepository.getInstance()));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static TabSNSFragment newInstance() {
        TabSNSFragment fragment = new TabSNSFragment();
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
    }

    private void initView(){

        PostListAdapter adapter = new PostListAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL,
                false);

        binding.rvSns.setLayoutManager(linearLayoutManager);
        binding.rvSns.setAdapter(adapter);
        presenter.loadPostData(adapter);
    }
    /*
    @Override
    public void addData(Post post) {
        adapter.add(post);
    }
    */
}

