package com.mrq.paljobs.controller.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.DialogAdapter;
import com.mrq.paljobs.controller.adapters.SkillsSelectedAdapter;
import com.mrq.paljobs.databinding.ActivityCompleteAccountBinding;
import com.mrq.paljobs.databinding.CustomDialogListBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CompleteAccountActivity extends BaseActivity {

    ActivityCompleteAccountBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> skillsString = new ArrayList<>();
    ArrayList<String> localSkills = new ArrayList<>();
    SkillsSelectedAdapter adapter;
    String type = "";
    String file = "";
    String photo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        type = getIntent().getStringExtra(Constants.USER_TYPE);
        binding.appbar.tvTool.setText(getString(R.string.complete_profile));

        switch (type) {
            case Constants.TYPE_COMPANY:
                binding.employee.setVisibility(View.GONE);
                binding.company.setVisibility(View.VISIBLE);
                binding.tvGender.setVisibility(View.GONE);
                binding.tvPhone.setHint(getString(R.string.telephone_fax));

                binding.tvSkills.setVisibility(View.GONE);
                binding.jobSkills.setVisibility(View.GONE);
                binding.ttvCv.setVisibility(View.GONE);
                binding.cv.setVisibility(View.GONE);

                binding.tvAbout.setText(getString(R.string.type_about_your_company));

                break;
            case Constants.TYPE_EMPLOYEE:
                binding.employee.setVisibility(View.VISIBLE);
                binding.company.setVisibility(View.GONE);
                binding.tvGender.setVisibility(View.VISIBLE);
                binding.tvPhone.setHint(getString(R.string.phone));

                binding.tvSkills.setVisibility(View.VISIBLE);
                binding.jobSkills.setVisibility(View.VISIBLE);
                binding.ttvCv.setVisibility(View.VISIBLE);
                binding.cv.setVisibility(View.VISIBLE);

                binding.tvAbout.setText(getString(R.string.type_about_yourself));

                break;
        }

        initGenders();
        initSkills();

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

        binding.closes.setOnClickListener(view -> {
            binding.closes.setVisibility(View.GONE);
            binding.photo.setVisibility(View.GONE);
            photo = "";
        });


        binding.btnConfirm.setOnClickListener(view -> {
            if (type.equals(Constants.TYPE_COMPANY)) {
                completeCompany();
            } else {
                completeEmployee();
            }
        });

    }

    private void completeEmployee() {
        if (isNotEmpty(binding.etPhone, binding.tvPhone)
                && isNotEmpty(binding.etAddress, binding.tvAddress)
                && isNotEmpty(binding.etGender, binding.tvGender)
                && isStringNotEmpty(this, photo)
                && isTextViewNotEmpty(this, binding.tvJobField, binding.jobField)
                && isListNotEmpty(this, localSkills, binding.jobSkills)
                && isNotEmpty(binding.tvCv, binding.cv)
                && isNotEmpty(binding.about)
        ) {
            enableElements(false);
            DocumentReference docRef = db.collection("User").document(Hawk.get(Constants.USER_TOKEN));
            docRef.update("phone", getText(binding.etPhone));
            docRef.update("address", getText(binding.etAddress));
            docRef.update("gender", getText(binding.etGender));

            docRef.update("jobField", getText(binding.tvJobField));
            docRef.update("skills", localSkills);
            docRef.update("about", getText(binding.about));

            docRef.addSnapshotListener((value, error) -> {
                showAlert(CompleteAccountActivity.this,
                        getString(R.string.update_profile_successfully), R.color.green_success);
                enableElements(true);
                startActivity(new Intent(this, MainActivity.class));
                finish();

            });
        }
    }

    private void completeCompany() {
        if (isNotEmpty(binding.etPhone, binding.tvPhone)
                && isNotEmpty(binding.etAddress, binding.tvAddress)
                && isStringNotEmpty(this, photo)
                && isTextViewNotEmpty(this, binding.tvJobField, binding.jobField)
                && isNotEmpty(binding.about)
        ) {
            enableElements(false);
            DocumentReference docRef = db.collection("User").document(Hawk.get(Constants.USER_TOKEN));
            docRef.update("phone", getText(binding.etPhone));
            docRef.update("address", getText(binding.etAddress));
            docRef.update("gender", "");

            docRef.update("jobField", getText(binding.tvJobField));
            docRef.update("skills", new ArrayList<>());
            docRef.update("about", getText(binding.about));

            docRef.addSnapshotListener((value, error) -> {
                showAlert(CompleteAccountActivity.this,
                        getString(R.string.update_profile_successfully), R.color.green_success);
                enableElements(true);
                startActivity(new Intent(this, MainActivity.class));
                finish();

            });
        }
    }

    private void initGenders() {
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        ArrayAdapter<String> gendersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, genders);
        binding.etGender.setAdapter(gendersAdapter);
    }

    private void initSkills() {
        binding.linearSkills.setVisibility(View.GONE);
        adapter = new SkillsSelectedAdapter(this);
        adapter.removeSkills((model, position) -> {
            skillsString.add(model);
            localSkills.remove(position);
            if (localSkills.isEmpty()) {
                binding.linearSkills.setVisibility(View.GONE);
            } else {
                binding.linearSkills.setVisibility(View.VISIBLE);
            }
            adapter.setList(localSkills);
        });
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);

        binding.jobField.setOnClickListener(view -> {
            dialogField(getString(R.string.choose_your_field), Constants.field());
        });

        binding.jobSkills.setOnClickListener(view -> {
            if (!binding.tvJobField.getText().toString().isEmpty()) {
                dialogSkills(getString(R.string.choose_your_skills), skillsString);
            } else {
                showAlert(CompleteAccountActivity.this, getString(R.string.must_select_jobField), R.color.orange);
            }
        });

    }

    private void dialogField(String title, ArrayList<String> list) {
        Dialog dialog = new Dialog(this);
        CustomDialogListBinding dialogBinding = CustomDialogListBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.tvTitle.setText(title);
        DialogAdapter dialogAdapter = new DialogAdapter(this);
        dialogAdapter.setAnInterface(model -> {
            binding.tvJobField.setText(model);
            skillsString = Constants.fieldSkills(model);

            localSkills = new ArrayList<>();
            adapter.setList(localSkills);
            binding.linearSkills.setVisibility(View.GONE);

            dialog.dismiss();
        });
        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.recyclerView.setHasFixedSize(true);
        dialogBinding.recyclerView.setAdapter(dialogAdapter);
        dialogAdapter.setList(list);
        dialog.show();
    }

    private void dialogSkills(String title, ArrayList<String> list) {
        Dialog dialog = new Dialog(this);
        CustomDialogListBinding dialogBinding = CustomDialogListBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.tvTitle.setText(title);
        DialogAdapter dialogAdapter = new DialogAdapter(this);
        dialogAdapter.setAnInterface(model -> {
            localSkills.add(model);
            dialogAdapter.remove(model);
            if (localSkills.isEmpty()) {
                binding.linearSkills.setVisibility(View.GONE);
            } else {
                binding.linearSkills.setVisibility(View.VISIBLE);
            }
            adapter.setList(localSkills);
            dialog.dismiss();
        });
        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.recyclerView.setHasFixedSize(true);
        dialogBinding.recyclerView.setAdapter(dialogAdapter);
        dialogAdapter.setList(list);

        if (list.isEmpty()) {
            dialogBinding.statefulLayout.showEmpty();
        } else {
            dialogBinding.statefulLayout.showContent();
        }
        dialog.show();
    }

    private void enableElements(boolean enable) {
        binding.btnConfirm.setEnabled(enable);
        if (!enable) {
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_gray));
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnConfirm.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_accent));
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
        binding.photo.setEnabled(enable);
        binding.closes.setEnabled(enable);
        binding.camera.setEnabled(enable);

        binding.etPhone.setEnabled(enable);
        binding.etAddress.setEnabled(enable);
        binding.etGender.setEnabled(enable);

        binding.jobField.setEnabled(enable);
        binding.jobSkills.setEnabled(enable);
        binding.linearSkills.setEnabled(enable);
        binding.cv.setEnabled(enable);
        binding.about.setEnabled(enable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_FILE_CODE) {
                file = data.getData().toString();
                binding.tvCv.setText(Constants.getFileName(this, data.getData()));
//                uploadFile(data.getData(), Constants.getFileName(this, data.getData()));
            } else if (requestCode == Constants.REQUEST_PHOTO_GALLERY_CODE) {
                binding.closes.setVisibility(View.VISIBLE);
                binding.photo.setVisibility(View.VISIBLE);
                photo = data.getData().toString();
                Picasso.get().load(data.getData()).into(binding.photo);
//                uploadImage(data.getData(), Constants.getFileName(this, data.getData()));
            }
        }
    }

    private void uploadImage(Uri imagePath, String fileName) {
        new ApiRequest<>().uploadImage(
                this,
                imagePath,
                fileName,
                Constants.TYPE_PHOTO,
                new Results<String>() {
                    @Override
                    public void onSuccess(String s) {
                        showAlert(CompleteAccountActivity.this,
                                getString(R.string.upload_picture_successfully), R.color.green_success);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(CompleteAccountActivity.this, offline, R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(CompleteAccountActivity.this, exception, R.color.red);
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

    private void uploadFile(Uri filePath, String fileName) {
        new ApiRequest<>().uploadFile(
                this,
                filePath,
                fileName,
                new Results<String>() {
                    @Override
                    public void onSuccess(String s) {
                        showAlert(CompleteAccountActivity.this,
                                getString(R.string.upload_file_successfully), R.color.green_success);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(CompleteAccountActivity.this, offline, R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(CompleteAccountActivity.this, exception, R.color.red);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}