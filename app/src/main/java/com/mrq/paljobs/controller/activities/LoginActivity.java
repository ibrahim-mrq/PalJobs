package com.mrq.paljobs.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

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
            startActivity(new Intent(this, RegisterActivity.class));
        });

        binding.btnLogin.setOnClickListener(view -> login());
    }

    private void login() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}