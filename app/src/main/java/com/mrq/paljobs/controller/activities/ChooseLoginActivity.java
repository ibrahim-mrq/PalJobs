package com.mrq.paljobs.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.ActivityChooseLoginBinding;
import com.mrq.paljobs.helpers.Constants;

public class ChooseLoginActivity extends AppCompatActivity {

    ActivityChooseLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        Intent intent = new Intent(this, RegisterActivity.class);
        binding.appbar.tvTool.setText(getString(R.string.login));
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());

        binding.employee.setOnClickListener(view -> {
            intent.putExtra(Constants.USER_TYPE, Constants.TYPE_EMPLOYEE);
            startActivity(intent);
        });
        binding.company.setOnClickListener(view -> {
            intent.putExtra(Constants.USER_TYPE, Constants.TYPE_COMPANY);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}