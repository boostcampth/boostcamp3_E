package com.teame.boostcamp.myapplication.authentication;


import android.app.Activity;

public class LoginPresenter implements LoginContractor.Presenter {
    private LoginContractor.View view;

    public LoginPresenter(LoginContractor.View view) {
        this.view = view;
    }

    @Override
    public void onLogInButtonClicked(Activity activity, String email, String password) {
        new AuthenticationUtil().doLogIn(activity, email, password)
                .subscribe(aBoolean -> {
                            view.makeToast("성공");
                        }, throwable -> {
                            view.makeToast("에메일과 비밀번호를 확인해 주세요");
                        }
                );
    }

}
