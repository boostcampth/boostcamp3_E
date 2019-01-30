package com.teame.boostcamp.myapplication.authentication;


import android.app.Activity;

public class SignUpPresenter implements SignUpContractor.Presenter {
    private SignUpContractor.View view;

    public SignUpPresenter(SignUpContractor.View view) {
        this.view = view;
    }

    @Override
    public void onEmailCheckButtonClicked(String email) {
        new AuthenticationUtil().checkEmailAvailable(email)
                .subscribe(aBoolean -> {
                            view.makeToast("사용가능한 이메일 입니다.");
                        }, throwable -> {
                            view.makeToast("중복되었거나 유효하지 않은 이메일 형식입니다");
                        }
                );
    }

    @Override
    public void onSignUpButtonClicked(Activity activity, String email, String password, User userData) {
        new AuthenticationUtil().doSignUp(activity, email, password, userData)
                .subscribe(aBoolean -> {
                            view.startNextActivity();
                        }, throwable -> {
                            view.makeToast("실패");
                        }
                );
    }


}
