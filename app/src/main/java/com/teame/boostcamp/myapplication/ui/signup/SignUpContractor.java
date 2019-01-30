package com.teame.boostcamp.myapplication.ui.signup;

import android.app.Activity;

import com.teame.boostcamp.myapplication.model.entitiy.User;
import com.teame.boostcamp.myapplication.ui.base.BasePresenter;

public interface SignUpContractor {

    interface View {
        void showToast(String message);

        void startMainActivity();       // 로그인 후 나오는 메인페이지 이동 액티비티
    }

    interface Presenter extends BasePresenter {
        void onEmailCheckButtonClicked(String email);

        void onSignUpButtonClicked(Activity activity, String email, String password, User userData);
    }
}
