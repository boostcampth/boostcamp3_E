package com.teame.boostcamp.myapplication.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.adapter.MainViewPagerAdapter;
import com.teame.boostcamp.myapplication.databinding.FragmentSnsBinding;
import com.teame.boostcamp.myapplication.ui.addpost.AddPostActivity;
import com.teame.boostcamp.myapplication.ui.base.BaseFragment;
import com.teame.boostcamp.myapplication.ui.tabmypost.TabMyPostFragment;
import com.teame.boostcamp.myapplication.ui.tabsns.TabSNSFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SNSFragment extends BaseFragment<FragmentSnsBinding, SNSContract.Presenter> {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_sns;
    }

    @Override
    protected SNSContract.Presenter getPresenter() {
        return null;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.fabAddPost.setOnClickListener(__ -> AddPostActivity.startActivity(getContext()));
        binding.tlSns.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.vpSnsFragment.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setupViewPager();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupViewPager() {
        //ViewPager remove swipe
        binding.vpSnsFragment.setOnTouchListener((__, ___) -> true);
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getFragmentManager());
        Fragment fragmentTabSNS = TabSNSFragment.newInstance();
        Fragment fragmentTabMyPost = TabMyPostFragment.newInstance();
        mainViewPagerAdapter.addFragment(fragmentTabSNS);
        mainViewPagerAdapter.addFragment(fragmentTabMyPost);
        binding.vpSnsFragment.setAdapter(mainViewPagerAdapter);
        binding.vpSnsFragment.setOffscreenPageLimit(mainViewPagerAdapter.getCount()-1);
    }

}
