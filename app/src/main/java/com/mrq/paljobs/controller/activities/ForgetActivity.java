package com.mrq.paljobs.controller.activities;

import android.os.Bundle;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.ActivityForgetBinding;
import com.mrq.paljobs.helpers.BaseActivity;

public class ForgetActivity extends BaseActivity {

    ActivityForgetBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        binding.appbar.tvTool.setText(getString(R.string.forget_password));

        binding.btnRestore.setOnClickListener(view -> restore());
    }

    private void restore() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}