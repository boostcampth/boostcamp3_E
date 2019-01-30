package com.teame.boostcamp.myapplication.authentication;

import android.app.Activity;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.Single;

public class AuthenticationUtil {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public AuthenticationUtil(){
        this.mAuth = FirebaseAuth.getInstance();

    }

    public Single<Boolean> doLogIn(Activity activity, String email, String password){

        return Single.create(emitter -> {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(true);
                        } else {
                            emitter.onError(new Exception("Login Failed"));
                        }
                    });
        });

    }

    public Single<Boolean> checkEmailAvailable(String email){
        return Single.create(emitter -> {
            mAuth.fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInMethods = result.getSignInMethods();
                            if (!signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                emitter.onSuccess(true);
                            }else{
                                emitter.onError(new Exception("Duplicated Email"));
                            }
                        }else {
                            emitter.onError(new Exception("This email pattern is not available"));
                        }
                    });
        });
    }

    public Single<Boolean> doSignUp(Activity activity, String email, String password, User userData){
        db = FirebaseFirestore.getInstance();
        return Single.create(emitter -> {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(true);
                            db.collection("users").document(mAuth.getUid()).set(userData);
                        } else {
                            emitter.onError(new Exception("Sign Up Failed"));
                        }
                    });
        });

    }

}
