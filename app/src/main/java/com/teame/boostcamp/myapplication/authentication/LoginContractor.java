package com.teame.boostcamp.myapplication.authentication;

import android.app.Activity;

public interface LoginContractor {

    interface View {
        void makeToast(String message);

        void startSingUpActivity();
    }

    interface Presenter {
        void onLogInButtonClicked(Activity activity, String email, String password);
    }
}
