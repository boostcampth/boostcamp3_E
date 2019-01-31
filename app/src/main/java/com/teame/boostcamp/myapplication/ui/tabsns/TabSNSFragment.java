package com.teame.boostcamp.myapplication.ui.tabsns;

import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.FragmentTabSnsBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;

public class TabSNSFragment extends BaseFragment<FragmentTabSnsBinding, TabSNSFragmentContract.Presenter> {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_tab_sns;
    }

    @Override
    protected TabSNSFragmentContract.Presenter getPresenter() {
        return null;
    }

    @Override
    protected String getClassName() {
        return "TabSNSFragment";
    }

    @Deprecated
    public TabSNSFragment() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
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
}

