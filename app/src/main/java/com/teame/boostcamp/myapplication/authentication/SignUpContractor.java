package com.teame.boostcamp.myapplication.authentication;

import android.app.Activity;

public interface SignUpContractor {

    interface View {
        void makeToast(String message);

        void startNextActivity();       // 로그인 후 나오는 메인페이지 이동 액티비티
    }

    interface Presenter {
        void onEmailCheckButtonClicked(String email);

        void onSignUpButtonClicked(Activity activity, String email, String password, User userData);
    }
}
