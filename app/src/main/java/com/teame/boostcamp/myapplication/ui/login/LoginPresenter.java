package com.teame.boostcamp.myapplication.ui.login;


import android.app.Activity;

import com.teame.boostcamp.myapplication.util.AuthenticationUtil;

import io.reactivex.disposables.CompositeDisposable;

public class LoginPresenter implements LoginContractor.Presenter {
    private LoginContractor.View view;
    private CompositeDisposable logInDisposable = new CompositeDisposable();

    public LoginPresenter(LoginContractor.View view) {
        this.view = view;
    }

    @Override
    public void onLogInButtonClicked(Activity activity, String email, String password) {
        logInDisposable.add(new AuthenticationUtil().doLogIn(activity, email, password)
                .subscribe(aBoolean -> {
                            view.startMainActivity();
                        }, throwable -> {
                            view.showToast("에메일과 비밀번호를 확인해 주세요");
                        }
                ));
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        if (logInDisposable.isDisposed()) {
            logInDisposable.clear();
        }
    }
}
