package com.teame.boostcamp.myapplication.ui.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SeekBar;

import com.google.firebase.auth.FirebaseAuth;
import com.teame.boostcamp.myapplication.R;
import com.teame.boostcamp.myapplication.databinding.ActivitySignupBinding;
import com.teame.boostcamp.myapplication.model.entitiy.User;
import com.teame.boostcamp.myapplication.ui.base.BaseMVPActivity;

public class SignUpActivity extends BaseMVPActivity<ActivitySignupBinding, SignUpContract.Presenter> implements SignUpContract.View {

    @Override
    protected SignUpContract.Presenter getPresenter() {
        return new SignUpPresenter(this, FirebaseAuth.getInstance());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_signup;
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }


    private void initView() {
        binding.buttonEmailcheck.setOnClickListener(__ -> {
            String email = binding.tietEmail.getText().toString();
            if (!TextUtils.isEmpty(email)) {
                presenter.checkEmailValidation(email);
            } else {
                showToast(getString(R.string.empty_email));
            }
        });
        binding.ivSignUpBack.setOnClickListener(v -> finish());
        binding.buttonRegister.setOnClickListener(__ -> onSignUpButtonClicked());
        binding.tietPasswordconfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = binding.tietPassword.getText().toString();
                String passwordConfirm = binding.tietPasswordconfirm.getText().toString();

                if (password.equals(passwordConfirm) && !TextUtils.isEmpty(passwordConfirm)) {
                    binding.tvPasswordConfirm.setText(getString(R.string.password_confirm_true));
                    binding.tvPasswordConfirm.setTextColor(Color.GREEN);
                } else if (TextUtils.isEmpty(password)) {
                    binding.tvPasswordConfirm.setText("");
                } else {
                    binding.tvPasswordConfirm.setText(R.string.password_confirm_false);
                    binding.tvPasswordConfirm.setTextColor(Color.RED);
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

    private void onSignUpButtonClicked() {
        String email = binding.tietEmail.getText().toString();
        String password = binding.tietPassword.getText().toString();
        String passwordConfirm = binding.tietPasswordconfirm.toString();
        int age = binding.sbAge.getProgress();
        String sex = null;
        if (binding.rbFemale.isChecked()) {
            sex = "F";
        } else if (binding.rbMale.isChecked()) {
            sex = "M";
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(sex) && age > 1) {
            User userData = new User(email, age, sex);
            presenter.doSignUp(email, password, userData);


        } else {
            showToast(getString(R.string.signup_empty_input_error));

        }


    }

    @Override
    public void succeedEmailValidation() {
        showToast(getString(R.string.email_validation));
    }

    @Override
    public void occurEmailDuplication() {
        showToast(getString(R.string.email_duplication));
    }

    @Override
    public void occurEmailFormatError() {
        showToast(getString(R.string.email_format_error));
    }

    @Override
    public void succeedSignUp() {
        showToast(getString(R.string.signup_success));
        finish();
    }

    @Override
    public void occurSignUpError() {
        showToast(getString(R.string.signup_error));
    }

    @Override
    public ProgressDialog showSignUpLoading() {
        ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("잠시만 기다려 주세요");
        loading.show();
        return loading;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }


}
