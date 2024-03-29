package com.mrq.paljobs.controller.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.ActivityRegisterBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RegisterActivity extends BaseActivity {

    ActivityRegisterBinding binding;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        type = getIntent().getStringExtra(Constants.USER_TYPE);

        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        binding.appbar.tvTool.setText(getString(R.string.register));
        binding.etType.setText(type);

        binding.checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                binding.checkBox.setError(null);
                binding.checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#2699FB")));
            } else {
                binding.checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#EE3636")));
            }
        });

        binding.btnRegister.setOnClickListener(view -> register());

        if (type.equals(Constants.TYPE_COMPANY)) {
            binding.tvLName.setVisibility(View.GONE);
            binding.tvFName.setHint(getString(R.string.company_name));
        }
    }

    private void register() {
        if (isNotEmpty(binding.etFName, binding.tvFName)
                && isNotEmpty(binding.etEmail, binding.tvEmail)
                && isValidEmail(binding.etEmail, binding.tvEmail)
                && isNotEmpty(binding.etPassword, binding.tvPassword)
                && isPasswordLess(binding.etPassword, binding.tvPassword)
                && isNotEmpty(binding.etConfirmPassword, binding.tvConfirmPassword)
                && isPasswordLess(binding.etConfirmPassword, binding.tvConfirmPassword)
                && isPasswordMatch(binding.etPassword, binding.tvPassword, binding.etConfirmPassword, binding.tvConfirmPassword)
                && isCheckBoxChecked(binding.checkBox)
        ) {
            User user = new User();
            user.setId("");
            user.setFirstName(getText(binding.etFName));
            user.setLastName(getText(binding.etLName) + "");
            user.setEmail(getText(binding.etEmail));
            user.setPassword(getText(binding.etPassword));
            user.setPhone("");
            user.setPhoto("");
            user.setAddress("");
            user.setGender("");
            user.setJobField("");
            user.setAbout("");
            user.setCv("");
            user.setUserType(type);
            user.setSkills(new ArrayList<>());

            new ApiRequest<String>().register(
                    this,
                    user,
                    new Results<String>() {
                        @Override
                        public void onSuccess(String s) {
                            showAlert(RegisterActivity.this, getString(R.string.create_an_account), R.color.green_success);
                            new Handler().postDelayed(() -> {
                                Intent intent = new Intent(RegisterActivity.this, CompleteAccountActivity.class);
                                if (type.equals(Constants.TYPE_COMPANY)) {
                                    intent.putExtra(Constants.USER_TYPE, Constants.TYPE_COMPANY);
                                } else {
                                    intent.putExtra(Constants.USER_TYPE, Constants.TYPE_EMPLOYEE);
                                }
                                startActivity(intent);
                                finish();
                            }, 2000);
                        }

                        @Override
                        public void onFailureInternet(@NotNull String offline) {
                            enableElements(true);
                            showAlert(RegisterActivity.this, offline, R.color.orange);
                        }

                        @Override
                        public void onEmpty() {

                        }

                        @Override
                        public void onException(@NotNull String message) {
                            enableElements(true);
                            showAlert(RegisterActivity.this, message, R.color.red);
                        }

                        @Override
                        public void onLoading(boolean loading) {
                            enableElements(false);
                        }
                    });


        }

    }

    private void enableElements(boolean enable) {
        binding.btnRegister.setEnabled(enable);
        if (!enable) {
            binding.btnRegister.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_gray));
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnRegister.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_accent));
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
        binding.appbar.imgBack.setEnabled(enable);
        binding.etFName.setEnabled(enable);
        binding.etLName.setEnabled(enable);
        binding.etEmail.setEnabled(enable);
        binding.etPassword.setEnabled(enable);
        binding.etConfirmPassword.setEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}