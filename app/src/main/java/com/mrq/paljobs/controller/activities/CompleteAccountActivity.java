package com.mrq.paljobs.controller.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.mrq.paljobs.models.Skills;
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
    String file = "";
    String cover = "";
    String photo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompleteAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.appbar.tvTool.setText(getString(R.string.complete_profile));
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());

        genders();
        skills();

        binding.etCv.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, Constants.REQUEST_FILE_CODE);
        });

        binding.uploadCover.setOnClickListener(view -> {
            ImagePicker.Companion.with(this)
                    .crop(1, 1)
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(Constants.REQUEST_COVER_GALLERY_CODE);
        });

        binding.uploadPhoto.setOnClickListener(view -> {
            ImagePicker.Companion.with(this)
                    .crop(1, 1)
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start(Constants.REQUEST_PHOTO_GALLERY_CODE);
        });

        binding.btnConfirm.setOnClickListener(view -> complete());

    }

    private void complete() {
        if (isNotEmpty(binding.etJobTitle, binding.tvJobTitle)
                && isNotEmpty(binding.etGender, binding.tvGender)
                && isNotEmpty(binding.etCv, binding.tvCv)
                && isNotEmpty(binding.etCv, binding.tvCv)
                && isListNotEmpty(this, localSkills, binding.uploadSkills)
                && isListNotEmpty(this, localSkills, binding.uploadSkills)
                && isStringNotEmpty(this, photo)
                && isStringNotEmpty(this, cover)
                && isFileStringNotEmpty(this, file)
        ) {
            enableElements(false);
            DocumentReference docRef = db.collection("User").document(Hawk.get(Constants.USER_TOKEN));
            docRef.update("jobTitle", getText(binding.etJobTitle));
            docRef.update("gender", getText(binding.etGender));
            docRef.update("skills", localSkills);
            docRef.addSnapshotListener((value, error) -> {
                showAlert(CompleteAccountActivity.this,
                        getString(R.string.update_profile_successfully), R.color.green_success);
                enableElements(true);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        }
    }

    private void genders() {
        ArrayList<String> genders = new ArrayList<>();
        genders.add("Male");
        genders.add("Female");
        ArrayAdapter<String> gendersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, genders);
        binding.etGender.setAdapter(gendersAdapter);
        binding.tvGender.setVisibility(View.VISIBLE);
    }

    private void skills() {
        binding.uploadSkills.setVisibility(View.GONE);
        adapter = new SkillsSelectedAdapter(this);
        adapter.removeSkills((model, position) -> {
            skillsString.add(model);
            localSkills.remove(position);
            if (localSkills.isEmpty()) {
                binding.uploadSkills.setVisibility(View.GONE);
            } else {
                binding.uploadSkills.setVisibility(View.VISIBLE);
            }
            adapter.setList(localSkills);
        });
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        loadSkills();
    }

    private void loadSkills() {
        new ApiRequest<Skills>().getData(this, "Skills", Skills.class,
                new Results<ArrayList<Skills>>() {
                    @Override
                    public void onSuccess(ArrayList<Skills> listSkills) {
                        for (int i = 0; i < listSkills.size(); i++) {
                            skillsString.add(listSkills.get(i).getName());
                        }
                        binding.etSkills.setOnClickListener(view -> {
                            dialogSkills(getString(R.string.select_skills), skillsString);
                        });
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.uploadSkills.setVisibility(View.GONE);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        binding.uploadSkills.setVisibility(View.GONE);
                    }

                    @Override
                    public void onEmpty() {

                    }

                    @Override
                    public void onLoading(boolean loading) {

                    }
                });
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
                binding.uploadSkills.setVisibility(View.GONE);
            } else {
                binding.uploadSkills.setVisibility(View.VISIBLE);
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
        binding.appbar.imgBack.setEnabled(enable);
        binding.uploadCover.setEnabled(enable);
        binding.uploadPhoto.setEnabled(enable);
        binding.etJobTitle.setEnabled(enable);
        binding.etGender.setEnabled(enable);
        binding.etSkills.setEnabled(enable);
        binding.etCv.setEnabled(enable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_FILE_CODE) {
                file = data.getData().toString();
                binding.etCv.setText(Constants.getFileName(this, data.getData()));
                uploadFile(data.getData(), Constants.getFileName(this, data.getData()));
            } else if (requestCode == Constants.REQUEST_PHOTO_GALLERY_CODE) {
                photo = data.getData().toString();
                Picasso.get().load(data.getData()).into(binding.photo);
                uploadImage(data.getData(), Constants.getFileName(this, data.getData()), Constants.TYPE_PHOTO);
            } else if (requestCode == Constants.REQUEST_COVER_GALLERY_CODE) {
                cover = data.getData().toString();
                Picasso.get().load(data.getData()).into(binding.cover);
                binding.cover.setScaleType(ImageView.ScaleType.FIT_XY);
                uploadImage(data.getData(), Constants.getFileName(this, data.getData()), Constants.TYPE_PHOTO_COVER);
            }
        }
    }

    private void uploadImage(Uri imagePath, String fileName, String photoType) {
        new ApiRequest<>().uploadImage(this, imagePath, fileName, photoType, new Results<String>() {
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
        new ApiRequest<>().uploadFile(this, filePath, fileName, new Results<String>() {
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