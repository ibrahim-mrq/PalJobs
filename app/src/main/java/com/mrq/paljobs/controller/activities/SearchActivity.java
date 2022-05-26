package com.mrq.paljobs.controller.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mrq.paljobs.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}