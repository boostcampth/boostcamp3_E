package com.teame.boostcamp.myapplication.ui.example;

import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityMainBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.ResourceProvider;

public class ExampleActivity extends BaseMVPActivity<ActivityMainBinding, ExampleContract.Presenter> implements ExampleContract.View {

    /**
     * 엑티비티에서 사용할 ResourcceId를 리턴해주게 작성 (처리는 BaseActivity)
     *
     * @return int
     */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    /**
     * 등록할 Presenter를 반환해주게 작성 (처리는 BaseActivity)
     *
     * @return T extends BasePresenter
     */
    @Override
    protected ExampleContract.Presenter getPresenter() {
        ResourceProvider resourceProvider = new ResourceProvider(this);
        return new ExamplePresenter(this, resourceProvider);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showTost(String string) {
        showLongToast(string);
    }

}
