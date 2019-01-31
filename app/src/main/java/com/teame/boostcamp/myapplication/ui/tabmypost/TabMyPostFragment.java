package com.teame.boostcamp.myapplication.ui.tabmypost;

import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.FragmentTabMypostBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;

public class TabMyPostFragment extends BaseFragment<FragmentTabMypostBinding, TabMyPostFragmentContract.Presenter> {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_tab_mypost;
    }

    @Override
    protected TabMyPostFragmentContract.Presenter getPresenter() {
        return null;
    }

    @Override
    protected String getClassName() {
        return "TabMyPostFragment";
    }

    @Deprecated
    public TabMyPostFragment() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
    }

    public static TabMyPostFragment newInstance() {
        TabMyPostFragment fragment = new TabMyPostFragment();
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
}
