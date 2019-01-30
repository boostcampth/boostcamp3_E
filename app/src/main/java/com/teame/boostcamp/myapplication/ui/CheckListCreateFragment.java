package com.teame.boostcamp.myapplication.ui;

import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.FragmentCheckListCreateBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;

public class CheckListCreateFragment extends BaseFragment<FragmentCheckListCreateBinding, CheckListCreateContract.Presenter> {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_check_list_create;
    }

    @Override
    protected CheckListCreateContract.Presenter getPresenter() {
        return null;
    }

    @Override
    protected String getClassName() {
        return "CheckListCreateFragment";
    }

    @Deprecated
    public CheckListCreateFragment() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
    }

    public static CheckListCreateFragment newInstance() {
        CheckListCreateFragment fragment = new CheckListCreateFragment();
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
