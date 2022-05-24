package com.mrq.paljobs.controller.activities;

import android.content.Intent;
import android.os.Bundle;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.ActivityLoginBinding;
import com.mrq.paljobs.helpers.BaseActivity;

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
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
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

        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}