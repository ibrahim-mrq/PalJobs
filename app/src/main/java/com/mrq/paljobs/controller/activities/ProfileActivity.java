package com.mrq.paljobs.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        type = getIntent().getStringExtra(Constants.TYPE_TITLE);


        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        loadData();

        if (type.equals(Constants.TYPE_EDIT)) {
            binding.edit.setVisibility(View.GONE);
            binding.appbar.tvTool.setText(getString(R.string.company_details));
        } else {
            binding.appbar.tvTool.setText(getString(R.string.profile));
        }
        binding.edit.setOnClickListener(view -> {
            startActivity(new Intent(this, EditProfileActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Hawk.get(Constants.USER_TYPE, ""))
            );
        });
    }

    private void loadData() {
        new ApiRequest<User>().getData(
                this,
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
                        binding.jobField.setText(user.getJobField());
                        binding.phone.setText(user.getPhone());
                        if (!user.getPhoto().isEmpty())
                            Picasso.get().load(user.getPhoto())
                                    .placeholder(R.drawable.ic_company_logo).into(binding.photo);
                        else {
                            binding.photo.setImageResource(R.drawable.ic_company_logo);
                        }
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