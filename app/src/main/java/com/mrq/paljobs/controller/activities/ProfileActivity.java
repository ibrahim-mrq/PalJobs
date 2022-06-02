package com.mrq.paljobs.controller.activities;

import android.os.Bundle;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.ActivityProfileBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.User;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ProfileActivity extends BaseActivity {

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.appbar.tvTool.setText(getString(R.string.profile));
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        loadData();
    }

    private void loadData() {
        new ApiRequest<User>().getData(
                MainActivity.context,
                "User",
                Hawk.get(Constants.USER_TOKEN),
                User.class,
                new Results<User>() {
                    @Override
                    public void onSuccess(User user) {
                        binding.name.setText(user.getFirstName());
                        binding.email.setText(user.getEmail());
                        binding.address.setText(user.getAddress());
                        binding.about.setText(user.getAbout());
                        if (!user.getPhoto().isEmpty())
                            Picasso.get().load(user.getPhoto()).placeholder(R.drawable.shape_accent).into(binding.photo);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(ProfileActivity.this, getString(R.string.no_internet), R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(ProfileActivity.this, getString(R.string.error), R.color.red);
                    }

                    @Override
                    public void onEmpty() {

                    }

                    @Override
                    public void onLoading(boolean loading) {
                        if (loading)
                            showCustomProgress(false);
                        else dismissCustomProgress();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}