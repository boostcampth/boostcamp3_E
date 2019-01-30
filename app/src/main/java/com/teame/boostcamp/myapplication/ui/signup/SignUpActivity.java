package com.teame.boostcamp.myapplication.ui.signup;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.SeekBar;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivitySignupBinding;
import com.teame.boostcamp.myapplication.model.entitiy.User;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;

public class SignUpActivity extends BaseMVPActivity<ActivitySignupBinding, SignUpContractor.Presenter> implements SignUpContractor.View {

    @Override
    protected SignUpContractor.Presenter getPresenter() {
        return new SignUpPresenter(this);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_signup;
    }

    @Override
    protected String getClassName() {
        return "SignUpActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        binding.btnEmailcheck.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            if (!TextUtils.isEmpty(email)) {
                onEmailCheckButtonClicked(email);
            } else {
                showToast("이메일을 입력해 주세요");
            }
        });
        binding.btnRegister.setOnClickListener(v -> onSignUpButtonClicked());
        binding.etPasswordconfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = binding.etPassword.getText().toString();
                String passwordConfirm = binding.etPasswordconfirm.getText().toString();

                if (password.equals(passwordConfirm) && !TextUtils.isEmpty(passwordConfirm)) {
                    binding.tvPasswordCheck.setText("비밀번호가 일치합니다.");
                    binding.tvPasswordCheck.setTextColor(Color.GREEN);
                } else if (TextUtils.isEmpty(password)) {
                    binding.tvPasswordCheck.setText("");
                } else {
                    binding.tvPasswordCheck.setText("비밀번호가 일치하지 않습니다.");
                    binding.tvPasswordCheck.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.sbAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.tvAge.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void onEmailCheckButtonClicked(String email) {
        if (!TextUtils.isEmpty(email)) {
            presenter.onEmailCheckButtonClicked(email);
        } else {
            showToast("이메일을 입력해 주세요");
        }
    }

    private void onSignUpButtonClicked() {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        String passwordConfirm = binding.etPasswordconfirm.toString();
        int age = binding.sbAge.getProgress();
        String sex = null;
        if (binding.rbFemale.isChecked()) {
            sex = "F";
        } else if (binding.rbMale.isChecked()) {
            sex = "M";
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(sex) && age > 1) {
            User userData = new User(email, password, age, sex);
            presenter.onSignUpButtonClicked(this, email, password, userData);


        } else {
            showToast("값을 다 입력해주세요");

        }


    }

    @Override
    public void startMainActivity() {       // 회원가입 완료 후 메인액티비티 이동 메서드
        presenter.onDetach();
        showToast("회원가입 성공"); // 테스트 토스트 - 지워주세요
    }


}
