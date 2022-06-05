package com.mrq.paljobs.controller.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;

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
            logout();
        });

        binding.account.setOnClickListener(view -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        binding.privacy.setOnClickListener(view -> {
            startActivity(new Intent(this, AboutActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_PRIVACY)
            );
        });

        binding.help.setOnClickListener(view -> {
            startActivity(new Intent(this, AboutActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_HELP)
            );
        });

        binding.about.setOnClickListener(view -> {
            startActivity(new Intent(this, AboutActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_ABOUT)
            );
        });

    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    dialog.dismiss();
                    Constants.logout(this);
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                })
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}