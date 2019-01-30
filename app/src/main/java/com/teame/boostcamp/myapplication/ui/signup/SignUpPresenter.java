package com.teame.boostcamp.myapplication.ui.signup;


import android.app.Activity;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.teame.boostcamp.myapplication.model.entitiy.User;

import java.util.List;

public class SignUpPresenter implements SignUpContract.Presenter {
    private SignUpContract.View view;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public SignUpPresenter(SignUpContract.View view, FirebaseAuth auth) {
        this.view = view;
        this.auth = auth;
    }

    @Override
    public void checkEmailValidation(String email) {
        view.showSignUpLoading(true);
        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    view.showSignUpLoading(false);
                    if (task.isSuccessful()) {
                        SignInMethodQueryResult result = task.getResult();
                        List<String> signInMethods = result.getSignInMethods();
                        if (!signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                            view.succeedEmailValidation();
                        } else {
                            view.occurEmailDuplication();
                        }
                    } else {
                        view.occurEmailFormatError();
                    }
                });
    }

    @Override
    public void doSignUp(String email, String password, User userData) {
        view.showSignUpLoading(true);
        db = FirebaseFirestore.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view, task -> {
                    view.showSignUpLoading(false);
                    if (task.isSuccessful()) {
                        db.collection("users").document(auth.getUid()).set(userData);
                        view.succeedSignUp();
                    } else {
                        view.occurSignUpError();
                    }
                });

    }


    @Override
    public void onAttach() {

    }

    @Override
    public void onDetach() {

    }
}
