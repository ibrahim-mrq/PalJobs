package com.mrq.paljobs.controller.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.DialogAdapter;
import com.mrq.paljobs.controller.adapters.SkillsSelectedAdapter;
import com.mrq.paljobs.databinding.ActivityAddProposalBinding;
import com.mrq.paljobs.databinding.CustomDialogListBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Proposal;
import com.mrq.paljobs.models.Skills;
import com.mrq.paljobs.models.User;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddProposalActivity extends BaseActivity {

    ActivityAddProposalBinding binding;
    ArrayList<String> skillsString = new ArrayList<>();
    ArrayList<String> localSkills = new ArrayList<>();
    SkillsSelectedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProposalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.appbar.tvTool.setText(getString(R.string.add_new_job));
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        binding.btnSave.setOnClickListener(view -> addJob());
        initSkills();
    }

    private void addJob() {
        if (isNotEmpty(binding.jobField)
                && isNotEmpty(binding.description)
                && isNotEmpty(binding.requirements)
                && isListNotEmpty(this, localSkills, binding.linearSkills)
        ) {
            User user = Hawk.get(Constants.USER, null);
            Proposal proposal = new Proposal();
            proposal.setSaved(false);
            proposal.setSubmit(false);
            proposal.setCompanyId(user.getId());
            proposal.setCompanyImage(user.getPhoto());
            proposal.setCompanyName(user.getFirstName() + " " + user.getLastName());
            proposal.setContent(getText(binding.description));
            proposal.setId("");
            proposal.setRequirement(getText(binding.requirements));
            proposal.setSkills(localSkills);
            proposal.setTitle(getText(binding.jobField));
            proposal.setTime(Constants.getCurrentDate());

            new ApiRequest<Proposal>().addToProposal(
                    this,
                    proposal,
                    new Results<String>() {
                        @Override
                        public void onSuccess(String s) {
                            showAlert(AddProposalActivity.this, getString(R.string.add_new_job_successfully), R.color.green_success);
                            new Handler().postDelayed(() -> {
                                enableElements(true);
                                startActivity(new Intent(AddProposalActivity.this, MainActivity.class));
                                finish();
                            }, 2000);
                        }

                        @Override
                        public void onFailureInternet(@NotNull String offline) {
                            showAlert(AddProposalActivity.this, offline, R.color.orange);
                        }

                        @Override
                        public void onException(@NotNull String exception) {
                            showAlert(AddProposalActivity.this, exception, R.color.red);
                        }

                        @Override
                        public void onEmpty() {

                        }

                        @Override
                        public void onLoading(boolean loading) {
                            enableElements(false);
                        }
                    }
            );
        }
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
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(adapter);
        loadSkills();
    }

    private void loadSkills() {
        new ApiRequest<Skills>().getData(
                this,
                "Skills",
                Skills.class,
                new Results<ArrayList<Skills>>() {
                    @Override
                    public void onSuccess(ArrayList<Skills> listSkills) {
                        for (int i = 0; i < listSkills.size(); i++) {
                            skillsString.add(listSkills.get(i).getName());
                        }
                        binding.skills.setOnClickListener(view -> {
                            dialogSkills(getString(R.string.select_skills), skillsString);
                        });
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.linearSkills.setVisibility(View.GONE);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        binding.linearSkills.setVisibility(View.GONE);
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
        binding.btnSave.setEnabled(enable);
        if (!enable) {
            binding.btnSave.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_gray));
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnSave.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_accent));
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
        binding.jobField.setEnabled(enable);
        binding.description.setEnabled(enable);
        binding.recyclerview.setEnabled(enable);
        binding.skills.setEnabled(enable);
        binding.appbar.imgBack.setEnabled(enable);
        binding.linearSkills.setEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}