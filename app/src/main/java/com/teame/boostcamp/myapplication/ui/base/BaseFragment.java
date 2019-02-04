package com.teame.boostcamp.myapplication.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teame.boostcamp.myapplication.util.DLogUtil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<V extends ViewDataBinding, P extends BasePresenter> extends Fragment implements BaseView<P> {
    // Databinding을 BaseActivity에서 처리하기 위한 추상클래스
    protected abstract int getLayoutResourceId();
    // View에 바인드될 Presenter를 받아오기 위한 추상클레스
    protected abstract P getPresenter();

    // BaseActivity를 상속하면 binding과 presenter는 상단의 추상클레스만 구현하면 사용 가능.
    protected V binding;
    protected P presenter;


    @Override
    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        DLogUtil.i(getClass().getSimpleName()+"::onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DLogUtil.i(getClass().getSimpleName()+"::onCreateView");
        binding = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false);
        View view = binding.getRoot();
        presenter = getPresenter();
        return view;
    }


    @Override
    public void onStart() {
        DLogUtil.i(getClass().getSimpleName()+"::onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        DLogUtil.i(getClass().getSimpleName()+"::onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        DLogUtil.i(getClass().getSimpleName()+"::onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        DLogUtil.i(getClass().getSimpleName()+"::onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        DLogUtil.i(getClass().getSimpleName()+"::onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        DLogUtil.i(getClass().getSimpleName()+"::onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        DLogUtil.i(getClass().getSimpleName()+"::onDetach");
        super.onDetach();
    }

    // 짧은 토스트
    public void showToast(String text){
        Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    // 긴 토스트
    public void showLongToast(String text){
        Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT).show();
    }

}
