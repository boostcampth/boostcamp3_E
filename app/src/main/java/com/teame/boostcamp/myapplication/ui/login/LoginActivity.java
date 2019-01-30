package com.teame.boostcamp.myapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivityLoginBinding;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;
import com.teame.boostcamp.myapplication.ui.signup.SignUpActivity;

public class LoginActivity extends BaseMVPActivity<ActivityLoginBinding, LoginContractor.Presenter> implements LoginContractor.View {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected String getClassName() {
        return "LoginActivity";
    }

    @Override
    protected LoginContractor.Presenter getPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding.btnLogin.setOnClickListener(v -> onLogInButtonClicked());
        binding.btnRegister.setOnClickListener(v -> startSingUpActivity());
    }

    private void onLogInButtonClicked() {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            presenter.onLogInButtonClicked(this, email, password);
        } else {
            showToast("아이디 또는 비밀번호를 입력해 주세요");
        }
    }

    @Override
    public void startSingUpActivity() {
        presenter.onDetach();
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override   // 로그인 성공 후 메인액티비티 이동 메서드
    public void startMainActivity() {
        presenter.onDetach();
        showToast("성공");    // 테스트토스트
    }
}
