package com.teame.boostcamp.myapplication.ui.login;

import android.app.Activity;

import com.teame.boostcamp.myapplication.ui.base.BasePresenter;

public interface LoginContractor {

    interface View {
        void showToast(String resource);

        void startSingUpActivity();

        void startMainActivity();
    }

    interface Presenter extends BasePresenter {
        void onLogInButtonClicked(Activity activity, String email, String password);
    }
}
