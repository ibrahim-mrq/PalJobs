package com.mrq.paljobs.controller.activities;

import android.os.Bundle;

import com.mrq.paljobs.databinding.ActivityBoardBinding;
import com.mrq.paljobs.helpers.BaseActivity;

public class BoardActivity extends BaseActivity {

    ActivityBoardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.skip.setOnClickListener(view -> {

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}