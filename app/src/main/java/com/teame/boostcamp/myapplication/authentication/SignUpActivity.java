package com.teame.boostcamp.myapplication.authentication;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.SeekBar;
import android.widget.Toast;

import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivitySignupBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

public class SignUpActivity extends AppCompatActivity implements SignUpContractor.View {
    private ActivitySignupBinding binding;
    private SignUpPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = new SignUpPresenter(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        initView();
    }

    public void initView(){
        binding.btnEmailcheck.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            if(!TextUtils.isEmpty(email)) {
                onEmailCheckButtonClicked(email);
            }else{
                makeToast("이메일을 입력해 주세요");
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

                if(password.equals(passwordConfirm) && !TextUtils.isEmpty(passwordConfirm)){
                    binding.tvPasswordCheck.setText("비밀번호가 일치합니다.");
                    binding.tvPasswordCheck.setTextColor(Color.GREEN);
                }else if(TextUtils.isEmpty(password)){
                    binding.tvPasswordCheck.setText("");
                }
                else{
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
                binding.tvAge.setText(progress+"");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onEmailCheckButtonClicked(String email){
        if(!TextUtils.isEmpty(email)){
            presenter.onEmailCheckButtonClicked(email);
        }else{
            makeToast("이메일을 입력해 주세요");
        }
    }

    public void onSignUpButtonClicked(){
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();
        String passwordConfirm = binding.etPasswordconfirm.toString();
        int age = binding.sbAge.getProgress();
        String sex = null;
        if(binding.rbFemale.isChecked()){
            sex = "F";
        }
        else if(binding.rbMale.isChecked()){
            sex = "M";
        }
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(sex) && age  > 1 && passwordConfirm.equals(password)){
            User userData = new User(email, password, age, sex);
            presenter.onSignUpButtonClicked(this, email, password, userData);


        }else{
            makeToast("값을 다 입력해주세요");
            return;
        }


    }

    public void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startNextActivity() {       // 로그인 후 다음화면 이동 메서드

    }


}
