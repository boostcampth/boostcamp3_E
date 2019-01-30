package com.teame.boostcamp.myapplication.ui;

import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.FragmentSnsBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;

public class SNSFragment extends BaseFragment<FragmentSnsBinding, SNSContract.Presenter> {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_sns;
    }

    @Override
    protected SNSContract.Presenter getPresenter() {
        return null;
    }

    @Override
    protected String getClassName() {
        return "SNSFragment";
    }


    @Deprecated
    public SNSFragment() {
        // 기본 생성자는 쓰지 말것 (new Instance 사용)
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
}
