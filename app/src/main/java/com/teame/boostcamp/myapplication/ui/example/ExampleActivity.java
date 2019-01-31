package com.teame.boostcamp.myapplication.ui.example;

import android.os.Bundle;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityMainBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.util.ResourceProviderUtil;

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
        ResourceProviderUtil resourceProviderUtil = new ResourceProviderUtil(this);
        return new ExamplePresenter(this, resourceProviderUtil);
    }

    /**
     * 생명주기 로그를 위해 BaseActivity에 현재 Activity이름을 알리기위해 구현 (필요없다면 return null)
     *
     * @return String
     */
    @Override
    protected String getClassName() {
        return "ExampleActivity";
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
