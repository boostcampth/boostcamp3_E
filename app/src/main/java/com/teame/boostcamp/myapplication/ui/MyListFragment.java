package com.teame.boostcamp.myapplication.ui;

import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.FragmentMyListBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;

public class MyListFragment extends BaseFragment<FragmentMyListBinding, MyListContract.Presenter> {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_my_list;
    }

    @Override
    protected MyListContract.Presenter getPresenter() {
        return null;
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
}
