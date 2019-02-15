package com.teame.boostcamp.myapplication.ui.login;

import android.app.ProgressDialog;

import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface LoginContract {

    interface View extends BaseView {
        void succeedLogIn();

        void occurLogInError();

        void isLogIn(boolean logInCheck);

        ProgressDialog showLogInLoading();


    }

    interface Presenter extends BasePresenter {
        void doLogIn(String email, String password);

        void checkLogIn();
    }
}
