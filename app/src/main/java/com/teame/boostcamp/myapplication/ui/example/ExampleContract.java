package com.teame.boostcamp.myapplication.ui.example;

import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface ExampleContract {

    /**
     * showToast라는 메소드명은 신경쓰지 말자, 예시를 위한 Contract */
    interface View extends BaseView<Presenter> {
        void showTost(String resource);
    }
    interface Presenter extends BasePresenter {
        void showTost();
    }
}
