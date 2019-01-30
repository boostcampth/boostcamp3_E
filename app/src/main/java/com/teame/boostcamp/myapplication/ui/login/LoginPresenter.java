package com.teame.boostcamp.myapplication.ui.login;


import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private FirebaseAuth auth;

    public LoginPresenter(LoginContract.View view, FirebaseAuth auth) {
        this.view = view;
        this.auth = auth;
    }

    @Override
    public void doLogIn(String email, String password) {
        view.showLogInLoading(true);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, task -> {
                    view.showLogInLoading(false);
                    if (task.isSuccessful()) {
                        view.succeedLogIn();
                    } else {
                        view.occurLogInError();
                    }
                });
    }

    @Override
    public void checkLogIn() {
        if (auth.getCurrentUser() != null) {
            view.isLogIn(true);
        } else {
            view.isLogIn(false);
        }
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
