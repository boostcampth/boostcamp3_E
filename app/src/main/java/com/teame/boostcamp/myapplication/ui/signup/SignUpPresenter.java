package com.teame.boostcamp.myapplication.ui.signup;


import android.app.Activity;

import com.teame.boostcamp.myapplication.model.entitiy.User;
import com.teame.boostcamp.myapplication.util.AuthenticationUtil;

import io.reactivex.disposables.CompositeDisposable;

public class SignUpPresenter implements SignUpContractor.Presenter {
    private SignUpContractor.View view;
    private CompositeDisposable signUpDisposable = new CompositeDisposable();

    public SignUpPresenter(SignUpContractor.View view) {
        this.view = view;
    }

    @Override
    public void onEmailCheckButtonClicked(String email) {
        signUpDisposable.add(new AuthenticationUtil().checkEmailAvailable(email)
                .subscribe(aBoolean -> {
                            view.showToast("사용가능한 이메일 입니다.");
                        }, throwable -> {
                            view.showToast("중복되었거나 유효하지 않은 이메일 형식입니다");
                        }
                ));
    }

    @Override
    public void onSignUpButtonClicked(Activity activity, String email, String password, User userData) {
        signUpDisposable.add(new AuthenticationUtil().doSignUp(activity, email, password, userData)
                .subscribe(aBoolean -> {
                            view.startMainActivity();
                        }, throwable -> {
                            view.showToast("실패");
                        }
                ));
    }


    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {
        if (signUpDisposable.isDisposed()) {
            signUpDisposable.clear();
        }
    }
}
