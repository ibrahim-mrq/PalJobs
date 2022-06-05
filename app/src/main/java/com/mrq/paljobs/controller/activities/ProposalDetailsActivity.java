package com.mrq.paljobs.controller.activities;

import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.SkillsAdapter;
import com.mrq.paljobs.databinding.ActivityProposalDetailsBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Favorite;
import com.mrq.paljobs.models.Proposal;
import com.mrq.paljobs.models.Submit;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ProposalDetailsActivity extends BaseActivity {

    ActivityProposalDetailsBinding binding;
    SkillsAdapter adapter;
    String type;
    boolean saved = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProposalDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        type = getIntent().getStringExtra(Constants.TYPE_TITLE);
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        binding.appbar.tvTool.setText(getString(R.string.submit_proposal));

        adapter = new SkillsAdapter(this);
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(adapter);

        if (type.equals(Constants.TYPE_FAVORITE)) {
            Favorite favorite = (Favorite) getIntent().getSerializableExtra(Constants.TYPE_MODEL);

            binding.name.setText(favorite.getCompanyName());
            binding.time.setText(favorite.getTime());
            binding.title.setText(favorite.getTitle());
            binding.content.setText(favorite.getContent());
            binding.requirements.setText(favorite.getRequirement());

            if (!favorite.getCompanyImage().isEmpty())
                Picasso.get().load(favorite.getCompanyImage()).into(binding.image);

            adapter.setList(favorite.getSkills());
            binding.save.setImageResource(R.drawable.ic_save);

            binding.save.setOnClickListener(view -> {
                if (saved) {
                    removeFavorite(favorite.getId());
                } else {
                    showAlert(this, getString(R.string.remove_favorite_success), R.color.green);
                }
                saved = !saved;
            });

            if (favorite.getSubmit()) {
                binding.btnSubmit.setBackgroundResource(R.drawable.shape_gray);
                binding.btnSubmit.setText(R.string.submitted);
                binding.btnSubmit.setTextColor(ContextCompat.getColor(this, R.color.textPrimary));
                binding.btnSubmit.setEnabled(false);
            } else {
                binding.btnSubmit.setBackgroundResource(R.drawable.shape_accent);
                binding.btnSubmit.setText(R.string.submit_your_proposal);
                binding.btnSubmit.setTextColor(ContextCompat.getColor(this, R.color.white));
                binding.btnSubmit.setEnabled(true);
            }

            binding.btnSubmit.setOnClickListener(view -> {
                startActivity(new Intent(this, SubmitActivity.class)
                        .putExtra(Constants.TYPE_TITLE, Constants.TYPE_FAVORITE)
                        .putExtra(Constants.TYPE_MODEL, favorite)
                );
            });

        } else if (type.equals(Constants.TYPE_SUBMIT)) {
            Submit submit = (Submit) getIntent().getSerializableExtra(Constants.TYPE_MODEL);

            binding.name.setText(submit.getCompanyName());
            binding.time.setText(submit.getTime());
            binding.title.setText(submit.getTitle());
            binding.content.setText(submit.getContent());
            binding.requirements.setText(submit.getRequirement());

            if (!submit.getCompanyImage().isEmpty())
                Picasso.get().load(submit.getCompanyImage()).into(binding.image);

            adapter.setList(submit.getSkills());

            if (submit.isSaved()) {
                binding.save.setImageResource(R.drawable.ic_save);
            } else {
                binding.save.setImageResource(R.drawable.ic_unsave);
            }
            binding.save.setOnClickListener(view -> {
                if (submit.isSaved()) {
                    showAlert(this, getString(R.string.proposal_already__saved), R.color.green);
                } else {
                    addFavorite(submit);
                }
            });

            binding.btnSubmit.setBackgroundResource(R.drawable.shape_gray);
            binding.btnSubmit.setText(R.string.submitted);
            binding.btnSubmit.setTextColor(ContextCompat.getColor(this, R.color.textPrimary));
            binding.btnSubmit.setEnabled(false);

        } else {
            Proposal proposal = (Proposal) getIntent().getSerializableExtra(Constants.TYPE_MODEL);

            binding.name.setText(proposal.getCompanyName());
            binding.time.setText(proposal.getTime());
            binding.title.setText(proposal.getTitle());
            binding.content.setText(proposal.getContent());
            binding.requirements.setText(proposal.getRequirement());

            if (!proposal.getCompanyImage().isEmpty())
                Picasso.get().load(proposal.getCompanyImage()).into(binding.image);

            adapter.setList(proposal.getSkills());

            if (proposal.getSaved()) {
                binding.save.setImageResource(R.drawable.ic_save);
            } else {
                binding.save.setImageResource(R.drawable.ic_unsave);
            }
            binding.save.setOnClickListener(view -> {
                if (proposal.getSaved()) {
                    showAlert(this, getString(R.string.proposal_already__saved), R.color.green);
                } else {
                    addFavorite(proposal);
                }
            });

            if (proposal.getSubmit()) {
                binding.btnSubmit.setBackgroundResource(R.drawable.shape_gray);
                binding.btnSubmit.setText(R.string.submitted);
                binding.btnSubmit.setTextColor(ContextCompat.getColor(this, R.color.textPrimary));
                binding.btnSubmit.setEnabled(false);
            } else {
                binding.btnSubmit.setBackgroundResource(R.drawable.shape_accent);
                binding.btnSubmit.setText(R.string.submit_your_proposal);
                binding.btnSubmit.setTextColor(ContextCompat.getColor(this, R.color.white));
                binding.btnSubmit.setEnabled(true);
            }

            binding.btnSubmit.setOnClickListener(view -> {
                startActivity(new Intent(this, SubmitActivity.class)
                        .putExtra(Constants.TYPE_TITLE, Constants.TYPE_PROPOSAL)
                        .putExtra(Constants.TYPE_MODEL, proposal)
                );
            });

        }

    }

    private void addFavorite(Submit model) {

        Favorite favorite = new Favorite();
        favorite.setCompanyId(model.getCompanyId());
        favorite.setCompanyImage(model.getCompanyImage());
        favorite.setCompanyName(model.getCompanyName());
        favorite.setContent(model.getContent());
        favorite.setId("");
        favorite.setProposalId(model.getId());
        favorite.setRequirement(model.getRequirement());

        favorite.setSkills(model.getSkills());
        favorite.setTime(Constants.getCurrentDate());
        favorite.setTitle(model.getTitle());
        favorite.setCustomerId(Hawk.get(Constants.USER_TOKEN));

        new ApiRequest<String>().addFavorite(
                MainActivity.context,
                favorite,
                new Results<String>() {
                    @Override
                    public void onSuccess(String success) {
                        model.setSaved(true);
                        binding.save.setImageResource(R.drawable.ic_save);
                        showAlert(ProposalDetailsActivity.this, success, R.color.green_success);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(ProposalDetailsActivity.this, offline, R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(ProposalDetailsActivity.this, exception, R.color.red);
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

    private void addFavorite(Proposal model) {

        Favorite favorite = new Favorite();
        favorite.setCompanyId(model.getCompanyId());
        favorite.setCompanyImage(model.getCompanyImage());
        favorite.setCompanyName(model.getCompanyName());
        favorite.setContent(model.getContent());
        favorite.setId("");
        favorite.setProposalId(model.getId());
        favorite.setRequirement(model.getRequirement());

        favorite.setSkills(model.getSkills());
        favorite.setTime(Constants.getCurrentDate());
        favorite.setTitle(model.getTitle());
        favorite.setCustomerId(Hawk.get(Constants.USER_TOKEN));

        new ApiRequest<String>().addFavorite(
                MainActivity.context,
                favorite,
                new Results<String>() {
                    @Override
                    public void onSuccess(String success) {
                        model.setSubmit(true);
                        model.setSaved(true);
                        binding.save.setImageResource(R.drawable.ic_save);
                        showAlert(ProposalDetailsActivity.this, success, R.color.green_success);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(ProposalDetailsActivity.this, offline, R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(ProposalDetailsActivity.this, exception, R.color.red);
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

    private void removeFavorite(String id) {
        new ApiRequest<String>().removeFavorite(
                MainActivity.context,
                id,
                new Results<String>() {
                    @Override
                    public void onSuccess(String success) {
                        showAlert(ProposalDetailsActivity.this, success, R.color.orange);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(ProposalDetailsActivity.this, offline, R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(ProposalDetailsActivity.this, exception, R.color.red);
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
}