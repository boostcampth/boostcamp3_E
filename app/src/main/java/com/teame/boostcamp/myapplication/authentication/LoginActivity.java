package com.teame.boostcamp.myapplication.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityLoginBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class LoginActivity extends AppCompatActivity implements LoginContractor.View {
    private ActivityLoginBinding binding;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = new LoginPresenter(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.btnLogin.setOnClickListener(v -> onLogInButtonClicked());
        binding.btnRegister.setOnClickListener(v -> startSingUpActivity());
    }

    public void onLogInButtonClicked() {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            presenter.onLogInButtonClicked(this, email, password);
        } else {
            makeToast("아이디 또는 비밀번호를 입력해 주세요");
        }
    }

    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startSingUpActivity() {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
