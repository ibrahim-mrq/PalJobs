package com.mrq.paljobs.controller.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.SkillsAdapter;
import com.mrq.paljobs.databinding.ActivityJobDetailsBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Favorite;
import com.mrq.paljobs.models.Proposal;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class JobDetailsActivity extends BaseActivity {

    ActivityJobDetailsBinding binding;
    SkillsAdapter adapter;
    Proposal proposal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        proposal = (Proposal) getIntent().getSerializableExtra(Constants.TYPE_MODEL);

        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        binding.appbar.tvTool.setText(getString(R.string.submit_proposal));

        binding.name.setText(proposal.getCompanyName());
        binding.time.setText(proposal.getTime());
        binding.title.setText(proposal.getTitle());
        binding.content.setText(proposal.getContent());
        binding.requirements.setText(proposal.getRequirement());

        if (!proposal.getCompanyImage().isEmpty())
            Picasso.get().load(proposal.getCompanyImage()).into(binding.image);

        adapter = new SkillsAdapter(this);
        adapter.setList(proposal.getSkills());
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(adapter);

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

        binding.btnSubmit.setOnClickListener(view -> {
            startActivity(new Intent(this, SubmitActivity.class)
                    .putExtra(Constants.TYPE_MODEL, proposal));
        });
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
                        proposal.setSubmit(true);
                        proposal.setSaved(true);
                        binding.save.setImageResource(R.drawable.ic_save);
                        showAlert(JobDetailsActivity.this, success, R.color.green_success);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(JobDetailsActivity.this, offline, R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(JobDetailsActivity.this, exception, R.color.red);
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