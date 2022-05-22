package com.mrq.paljobs.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mrq.paljobs.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}