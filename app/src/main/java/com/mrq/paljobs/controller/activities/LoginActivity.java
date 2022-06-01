package com.mrq.paljobs.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.ActivityLoginBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.models.User;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.appbar.tvTool.setText(getString(R.string.login));

        binding.forget.setOnClickListener(view -> {
            startActivity(new Intent(this, ForgetActivity.class));
        });
        binding.register.setOnClickListener(view -> {
            startActivity(new Intent(this, ChooseLoginActivity.class));
        });


        binding.facebook.setOnClickListener(view -> {
            showAlert(this, "Soon", R.color.orange);
        });

        binding.google.setOnClickListener(view -> {
            showAlert(this, "Soon", R.color.orange);
        });

        binding.twitter.setOnClickListener(view -> {
            showAlert(this, "Soon", R.color.orange);
        });

        binding.btnLogin.setOnClickListener(view -> login());
    }

    private void login() {
        if (isNotEmpty(binding.etEmail, binding.tvEmail)
                && isValidEmail(binding.etEmail, binding.tvEmail)
                && isNotEmpty(binding.etPassword, binding.tvPassword)
                && isPasswordLess(binding.etPassword, binding.tvPassword)
        ) {
            new ApiRequest<User>().login(
                    this,
                    getText(binding.etEmail),
                    getText(binding.etPassword),
                    new Results<User>() {
                        @Override
                        public void onSuccess(User user) {
                            showAlert(LoginActivity.this, getString(R.string.login_successfully), R.color.green_success);
                            new Handler().postDelayed(() -> {
                                enableElements(true);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }, 2000);

                        }

                        @Override
                        public void onFailureInternet(@NotNull String offline) {
                            showAlert(LoginActivity.this, offline, R.color.orange);
                        }

                        @Override
                        public void onException(@NotNull String exception) {
                            showAlert(LoginActivity.this, exception, R.color.red);
                        }

                        @Override
                        public void onEmpty() {

                        }

                        @Override
                        public void onLoading(boolean loading) {
                            enableElements(false);
                        }
                    });
        }
    }

    private void enableElements(boolean enable) {
        binding.btnLogin.setEnabled(enable);
        if (!enable) {
            binding.btnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_gray));
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_accent));
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
        binding.etEmail.setEnabled(enable);
        binding.etPassword.setEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}