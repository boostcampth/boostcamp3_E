package com.teame.boostcamp.myapplication.ui.base;

import android.os.Bundle;

import com.teame.boostcamp.myapplication.util.DLog;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

public abstract class BaseMVPActivity <V extends ViewDataBinding,P extends BasePresenter> extends BaseActivity<V> {

    // View에 바인드될 Presenter를 받아오기 위한 추상클레스
    protected abstract P getPresenter();

    protected P presenter;

    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DLog.i(getClassName()+"::onCreate");
        super.onCreate(savedInstanceState);
        presenter = getPresenter();
    }

}
