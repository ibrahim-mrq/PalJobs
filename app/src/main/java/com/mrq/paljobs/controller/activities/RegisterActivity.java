package com.mrq.paljobs.controller.activities;

import android.os.Bundle;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.ActivityRegisterBinding;
import com.mrq.paljobs.helpers.BaseActivity;

public class RegisterActivity extends BaseActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        binding.appbar.tvTool.setText(getString(R.string.register));

        binding.btnRegister.setOnClickListener(view -> register());
    }

    private void register() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}