package com.mrq.paljobs.controller.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrq.paljobs.controller.activities.AboutActivity;
import com.mrq.paljobs.controller.activities.EditProfileActivity;
import com.mrq.paljobs.databinding.FragmentSettingBinding;
import com.mrq.paljobs.helpers.BaseFragment;
import com.mrq.paljobs.helpers.Constants;

import org.jetbrains.annotations.NotNull;

public class SettingFragment extends BaseFragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    FragmentSettingBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        binding.logout.setOnClickListener(view -> {
            logout();
        });

        binding.privacy.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), AboutActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_PRIVACY)
            );
        });

        binding.help.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), AboutActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_HELP)
            );
        });

        binding.about.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), AboutActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Constants.TYPE_ABOUT)
            );
        });

        binding.account.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), EditProfileActivity.class));
        });

    }

    private void logout() {
        new AlertDialog.Builder(requireActivity())
                .setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    dialog.dismiss();
                    Constants.logout(requireActivity());
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                })
                .create()
                .show();
    }
}