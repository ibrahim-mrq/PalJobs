package com.mrq.paljobs.controller.activities;

import android.content.Intent;
import android.os.Bundle;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.ActivitySettingBinding;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;

public class SettingActivity extends BaseActivity {

    ActivitySettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        binding.appbar.tvTool.setText(getString(R.string.login));

        binding.logout.setOnClickListener(view -> {
            Constants.logout(this);
        });

        binding.account.setOnClickListener(view -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}