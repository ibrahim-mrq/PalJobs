package com.mrq.paljobs.controller.activities;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.SkillsAdapter;
import com.mrq.paljobs.databinding.ActivityEditProfileBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.User;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class EditProfileActivity extends BaseActivity {

    ActivityEditProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SkillsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.appbar.tvTool.setText(getString(R.string.edit_profile));
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());

        adapter = new SkillsAdapter(EditProfileActivity.this);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);

        if (Hawk.get(Constants.USER_TYPE).equals(Constants.TYPE_COMPANY)) {
            binding.tvSkills.setVisibility(View.GONE);
            binding.uploadSkills.setVisibility(View.GONE);
            binding.tvGender.setVisibility(View.GONE);
            binding.tvCv.setVisibility(View.GONE);
            binding.tvLName.setVisibility(View.GONE);
            binding.tvFName.setHint(getString(R.string.company_name));
            binding.tvPhone.setHint(getString(R.string.telephone_fax));
        }
        loadData();
        binding.btnUpdate.setOnClickListener(view -> {
            if (Hawk.get(Constants.USER_TYPE).equals(Constants.TYPE_COMPANY)) {
                updateCompany();
            } else {
                updateEmployee();
            }
        });

    }

    private void updateCompany() {
        enableElements(false);
        DocumentReference docRef = db.collection("User").document(Hawk.get(Constants.USER_TOKEN));
        docRef.update("firstName", getText(binding.etFName));
        docRef.update("lastName", "");
        docRef.update("phone", getText(binding.etPhone));
        docRef.update("address", getText(binding.etAddress));
        docRef.update("jobField", getText(binding.etJobField));
        docRef.addSnapshotListener((value, error) -> {
            showAlert(EditProfileActivity.this, getString(R.string.update_profile_successfully), R.color.green_success);
            enableElements(true);
        });
    }

    private void updateEmployee() {
        enableElements(false);
        DocumentReference docRef = db.collection("User").document(Hawk.get(Constants.USER_TOKEN));
        docRef.update("firstName", getText(binding.etFName));
        docRef.update("lastName", getText(binding.etLName));
        docRef.update("phone", getText(binding.etPhone));
        docRef.update("address", getText(binding.etAddress));
        docRef.update("jobField", getText(binding.etJobField));
        docRef.addSnapshotListener((value, error) -> {
            showAlert(EditProfileActivity.this, getString(R.string.update_profile_successfully), R.color.green_success);
            enableElements(true);
        });
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
                        binding.etFName.setText(user.getFirstName());
                        binding.etLName.setText(user.getLastName());
                        binding.etEmail.setText(user.getEmail());
                        binding.etPhone.setText(user.getPhone());
                        binding.etPassword.setText(user.getPassword());
                        binding.etAddress.setText(user.getAddress());
                        binding.etJobField.setText(user.getJobField());
                        binding.etGender.setText(user.getGender());
                        adapter.setList(user.getSkills());

                        if (user.getSkills().isEmpty()) {
                            binding.tvSkills.setVisibility(View.GONE);
                            binding.uploadSkills.setVisibility(View.GONE);
                        }

                        if (!user.getPhoto().isEmpty())
                            Picasso.get().load(user.getPhoto()).placeholder(R.drawable.shape_accent).into(binding.photo);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(EditProfileActivity.this, getString(R.string.no_internet), R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(EditProfileActivity.this, getString(R.string.error), R.color.red);
                    }

                    @Override
                    public void onEmpty() {

                    }

                    @Override
                    public void onLoading(boolean loading) {
                        enableElements(!loading);
                    }
                });
    }

    private void enableElements(boolean enable) {
        binding.btnUpdate.setEnabled(enable);
        if (!enable) {
            binding.btnUpdate.setBackgroundResource(R.drawable.shape_gray);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnUpdate.setBackgroundResource(R.drawable.shape_accent);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
        binding.etFName.setEnabled(enable);
        binding.etLName.setEnabled(enable);
        binding.etPhone.setEnabled(enable);
        binding.etAddress.setEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}