package com.teame.boostcamp.myapplication.ui.login;


import android.app.Activity;
import android.app.ProgressDialog;

import com.google.firebase.auth.FirebaseAuth;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View view;
    private FirebaseAuth auth;
    private ProgressDialog loading;

    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void doLogIn(String email, String password) {
        this.loading = view.showLogInLoading();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, task -> {
                    loading.dismiss();
                    if (task.isSuccessful()) {
                        view.succeedLogIn();
                    } else {
                        view.occurLogInError();
                    }
                });
    }

    @Override
    public void checkLogIn() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
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
        this.view = null;
    }
}
