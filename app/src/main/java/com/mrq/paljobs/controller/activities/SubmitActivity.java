package com.mrq.paljobs.controller.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.SkillsAdapter;
import com.mrq.paljobs.databinding.ActivitySubmitBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Favorite;
import com.mrq.paljobs.models.Proposal;
import com.mrq.paljobs.models.Submit;
import com.mrq.paljobs.models.User;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class SubmitActivity extends BaseActivity {

    ActivitySubmitBinding binding;
    SkillsAdapter adapter;
    Proposal proposal;
    Favorite favorite;
    String type;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubmitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        type = getIntent().getStringExtra(Constants.TYPE_TITLE);

        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        binding.appbar.tvTool.setText(getString(R.string.submit_proposal));

        if (type.equals(Constants.TYPE_FAVORITE)) {
            favorite = (Favorite) getIntent().getSerializableExtra(Constants.TYPE_MODEL);

            binding.name.setText(favorite.getCompanyName());
            binding.time.setText(favorite.getTime());
            binding.title.setText(favorite.getTitle());
            binding.content.setText(favorite.getContent());

            if (!favorite.getCompanyImage().isEmpty())
                Picasso.get().load(favorite.getCompanyImage()).into(binding.image);

            adapter = new SkillsAdapter(this);
            adapter.setList(favorite.getSkills());
            binding.recyclerview.setHasFixedSize(true);
            binding.recyclerview.setAdapter(adapter);

            binding.btnSubmit.setOnClickListener(view -> {
                addToSubmit(favorite);
            });

        } else {
            proposal = (Proposal) getIntent().getSerializableExtra(Constants.TYPE_MODEL);

            binding.name.setText(proposal.getCompanyName());
            binding.time.setText(proposal.getTime());
            binding.title.setText(proposal.getTitle());
            binding.content.setText(proposal.getContent());

            if (!proposal.getCompanyImage().isEmpty())
                Picasso.get().load(proposal.getCompanyImage()).into(binding.image);

            adapter = new SkillsAdapter(this);
            adapter.setList(proposal.getSkills());
            binding.recyclerview.setHasFixedSize(true);
            binding.recyclerview.setAdapter(adapter);

            binding.btnSubmit.setOnClickListener(view -> {
                addToSubmit(proposal);
            });

            binding.cv.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, Constants.REQUEST_FILE_CODE);

            });
        }

    }

    private void addToSubmit(Favorite model) {
        User user = Hawk.get(Constants.USER, null);
        Submit submit = new Submit();
        submit.setId("");

        submit.setCompanyId(model.getCompanyId());
        submit.setCompanyImage(model.getCompanyImage());
        submit.setCompanyName(model.getCompanyName());

        submit.setCustomerId(user.getId());
        submit.setCustomerName(user.getFirstName() + " " + user.getLastName());
        submit.setCustomerEmail(user.getEmail());
        submit.setCustomerPhone(user.getPhone());
        if (!url.isEmpty()) {
            submit.setCustomerCv(url);
        } else {
            submit.setCustomerCv(user.getCv());
        }
        submit.setCustomerSkills(user.getSkills());

        submit.setProposalId(model.getId());
        submit.setProposal(getText(binding.etProposal) + "");

        submit.setTitle(model.getTitle());
        submit.setContent(model.getContent());
        submit.setSkills(model.getSkills());
        submit.setRequirement(model.getRequirement());

        submit.setTime(Constants.getCurrentDate());

        new ApiRequest<Submit>().addToSubmit(
                MainActivity.context,
                submit,
                new Results<String>() {
                    @Override
                    public void onSuccess(String success) {
                        showAlert(SubmitActivity.this, success, R.color.green_success);
                        new Handler().postDelayed(() -> {
                            onBackPressed();
                        }, 2000);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(SubmitActivity.this, offline, R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(SubmitActivity.this, exception, R.color.red);
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
                }
        );
    }

    private void addToSubmit(Proposal model) {
        User user = Hawk.get(Constants.USER, null);
        Submit submit = new Submit();
        submit.setId("");

        submit.setCompanyId(model.getCompanyId());
        submit.setCompanyImage(model.getCompanyImage());
        submit.setCompanyName(model.getCompanyName());

        submit.setCustomerId(user.getId());
        submit.setCustomerName(user.getFirstName() + " " + user.getLastName());
        submit.setCustomerEmail(user.getEmail());
        submit.setCustomerPhone(user.getPhone());
        if (!url.isEmpty()) {
            submit.setCustomerCv(url);
        } else {
            submit.setCustomerCv(user.getCv());
        }

        Log.e("response", "url = " + url);
        Log.e("response", "user.getCv() = " + user.getCv());

        submit.setCustomerSkills(user.getSkills());

        submit.setProposalId(model.getId());
        submit.setProposal(getText(binding.etProposal) + "");

        submit.setTitle(model.getTitle());
        submit.setContent(model.getContent());
        submit.setSkills(model.getSkills());
        submit.setRequirement(model.getRequirement());

        submit.setTime(Constants.getCurrentDate());

        new ApiRequest<Submit>().addToSubmit(
                MainActivity.context,
                submit,
                new Results<String>() {
                    @Override
                    public void onSuccess(String success) {
                        showAlert(SubmitActivity.this, success, R.color.green_success);
                        new Handler().postDelayed(() -> {
                            onBackPressed();
                        }, 2000);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(SubmitActivity.this, offline, R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(SubmitActivity.this, exception, R.color.red);
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
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void uploadFile(Uri filePath, String fileName) {
        new ApiRequest<>().uploadFile(
                this,
                filePath,
                fileName,
                "add",
                new Results<String>() {
                    @Override
                    public void onSuccess(String s) {
                        url = s;
                        showAlert(SubmitActivity.this,
                                getString(R.string.upload_file_successfully), R.color.green_success);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(SubmitActivity.this, offline, R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(SubmitActivity.this, exception, R.color.red);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_FILE_CODE) {
                binding.cv.setText(Constants.getFileName(this, data.getData()));
                uploadFile(data.getData(), Constants.getFileName(this, data.getData()));
            }
        }
    }

}