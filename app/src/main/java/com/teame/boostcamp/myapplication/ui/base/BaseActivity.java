package com.teame.boostcamp.myapplication.ui.base;

import android.os.Bundle;
import android.widget.Toast;

import com.teame.boostcamp.myapplication.util.DLog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity {

    // Databinding을 BaseActivity에서 처리하기 위한 추상클래스
    protected abstract int getLayoutResourceId();

    protected abstract String getClassName();
    // BaseActivity를 상속하면 binding과 presenter는 상단의 추상클레스만 구현하면 사용 가능.
    protected V binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DLog.i(getClassName()+"::onCreate");
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutResourceId());
    }

    @Override
    protected void onStart() {
        DLog.i(getClassName()+"::onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        DLog.i(getClassName()+"::onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        DLog.i(getClassName()+"::onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        DLog.i(getClassName()+"::onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        DLog.i(getClassName()+"::onRestart");
        super.onRestart();
    }

    // 짧은 토스트
    public void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    // 긴 토스트
    public void showLongToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
