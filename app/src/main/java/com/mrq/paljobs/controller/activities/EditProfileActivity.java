package com.mrq.paljobs.controller.activities;

import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.DialogAdapter;
import com.mrq.paljobs.controller.adapters.SkillsAdapter;
import com.mrq.paljobs.controller.services.MyService;
import com.mrq.paljobs.databinding.ActivityEditProfileBinding;
import com.mrq.paljobs.databinding.CustomDialogListBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.User;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EditProfileActivity extends BaseActivity {

    ActivityEditProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SkillsAdapter adapter;

    String file = "";
    String photo = "";
    Uri imagePath;
    Uri filePath;

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
            binding.cv.setVisibility(View.GONE);
            binding.tvLName.setVisibility(View.GONE);
            binding.tvFName.setHint(getString(R.string.company_name));
            binding.tvPhone.setHint(getString(R.string.telephone_fax));
        } else {
            binding.uploadSkills.setVisibility(View.GONE);
            binding.jobField.setVisibility(View.GONE);
        }

        loadData();

        binding.btnUpdate.setOnClickListener(view -> {
            if (Hawk.get(Constants.USER_TYPE).equals(Constants.TYPE_COMPANY)) {
                updateCompany();
            } else {
                updateEmployee();
            }
        });

        binding.cv.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, Constants.REQUEST_FILE_CODE);
        });

        binding.camera.setOnClickListener(view -> {
            ImagePicker.Companion.with(this)
                    .crop(1, 1)
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(Constants.REQUEST_PHOTO_GALLERY_CODE);
        });

        binding.jobField.setOnClickListener(view -> {
            dialogField(getString(R.string.choose_your_field), Constants.field());
        });

    }

    private void updateCompany() {
        if (isNotEmpty(binding.etFName, binding.tvFName)
                && isNotEmpty(binding.etPhone, binding.tvPhone)
                && isNotEmpty(binding.etAddress, binding.tvAddress)
                && isTextViewNotEmpty(this, binding.tvJobField, binding.jobField)
                && isNotEmpty(binding.etGender, binding.tvGender)
                && isNotEmpty(binding.about)
        ) {
            if (!photo.isEmpty()) {
                uploadImage();
            }
            enableElements(false);
            DocumentReference docRef = db.collection("User").document(Hawk.get(Constants.USER_TOKEN));
            docRef.update("firstName", getText(binding.etFName));
            docRef.update("lastName", "");
            docRef.update("phone", getText(binding.etPhone));
            docRef.update("address", getText(binding.etAddress));
            docRef.update("jobField", getText(binding.tvJobField));
            docRef.update("gender", getText(binding.etGender));
            docRef.update("about", getText(binding.about));
            docRef.addSnapshotListener((value, error) -> {
                showAlert(EditProfileActivity.this, getString(R.string.update_profile_successfully), R.color.green_success);
                enableElements(true);
            });
        }
    }

    private void updateEmployee() {
        if (isNotEmpty(binding.etFName, binding.tvFName)
                && isNotEmpty(binding.etLName, binding.tvLName)
                && isNotEmpty(binding.etPhone, binding.tvPhone)
                && isNotEmpty(binding.etAddress, binding.tvAddress)
                && isTextViewNotEmpty(this, binding.tvJobField, binding.jobField)
                && isNotEmpty(binding.etGender, binding.tvGender)
                && isNotEmpty(binding.about)
        ) {
            if (!photo.isEmpty()) {
                uploadImage();
            }
            if (!file.isEmpty()) {
                uploadFile();
            }
            enableElements(false);
            DocumentReference docRef = db.collection("User").document(Hawk.get(Constants.USER_TOKEN));
            docRef.update("firstName", getText(binding.etFName));
            docRef.update("lastName", getText(binding.etLName));
            docRef.update("phone", getText(binding.etPhone));
            docRef.update("address", getText(binding.etAddress));
            docRef.update("jobField", getText(binding.tvJobField));
            docRef.update("gender", getText(binding.etGender));
            docRef.update("about", getText(binding.about));
            docRef.addSnapshotListener((value, error) -> {
                showAlert(EditProfileActivity.this, getString(R.string.update_profile_successfully), R.color.green_success);
                enableElements(true);
            });
        }
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
                        binding.etFName.setText(user.getFirstName());
                        binding.etLName.setText(user.getLastName());
                        binding.etEmail.setText(user.getEmail());
                        binding.etPhone.setText(user.getPhone());
                        binding.etPassword.setText(user.getPassword());
                        binding.etAddress.setText(user.getAddress());
                        binding.tvJobField.setText(user.getJobField());
                        binding.etGender.setText(user.getGender());
                        binding.about.setText(user.getAbout());
                        adapter.setList(user.getSkills());
                        initGenders();
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

    private void initGenders() {
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        ArrayAdapter<String> gendersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, genders);
        binding.etGender.setAdapter(gendersAdapter);
    }

    private void dialogField(String title, ArrayList<String> list) {
        Dialog dialog = new Dialog(this);
        CustomDialogListBinding dialogBinding = CustomDialogListBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.tvTitle.setText(title);
        DialogAdapter dialogAdapter = new DialogAdapter(this);
        dialogAdapter.setAnInterface(model -> {
            binding.tvJobField.setText(model);
            dialog.dismiss();
        });
        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.recyclerView.setHasFixedSize(true);
        dialogBinding.recyclerView.setAdapter(dialogAdapter);
        dialogAdapter.setList(list);
        dialog.show();
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
        binding.about.setEnabled(enable);
        binding.etGender.setEnabled(enable);

        binding.jobField.setEnabled(enable);
        binding.cv.setEnabled(enable);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_FILE_CODE) {
                file = data.getData().toString();
                filePath = data.getData();
                binding.tvCv.setText(Constants.getFileName(this, data.getData()));
            } else if (requestCode == Constants.REQUEST_PHOTO_GALLERY_CODE) {
                photo = data.getData().toString();
                imagePath = data.getData();
                Picasso.get().load(data.getData()).into(binding.photo);
            }
        }
    }

    private void uploadImage() {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("name", Constants.getFileName(this, imagePath));
        bundle.putString("path", photo);
        bundle.putString("type", Constants.TYPE_PHOTO);
        ComponentName componentName = new ComponentName(this, MyService.class);
        JobInfo jobInfo = new JobInfo.Builder(10, componentName)
                .setExtras(bundle)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(jobInfo);
    }

    private void uploadFile() {
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("name", Constants.getFileName(this, filePath));
        bundle.putString("path", file);
        bundle.putString("type", Constants.TYPE_FILE);
        ComponentName componentName = new ComponentName(this, MyService.class);
        JobInfo jobInfo = new JobInfo.Builder(11, componentName)
                .setExtras(bundle)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(jobInfo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}