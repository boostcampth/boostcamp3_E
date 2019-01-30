package com.teame.boostcamp.myapplication.ui.login;

import com.teame.boostcamp.myapplication.ui.base.BasePresenter;
import com.teame.boostcamp.myapplication.ui.base.BaseView;

public interface LoginContract {

    interface View extends BaseView {
        void succeedLogIn();

        void occurLogInError();

        void isLogIn(boolean logInCheck);

        void showLogInLoading(boolean visibility);
    }

    interface Presenter extends BasePresenter {
        void doLogIn(String email, String password);

        void checkLogIn();
    }
}
