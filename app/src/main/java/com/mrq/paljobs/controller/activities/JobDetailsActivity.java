package com.mrq.paljobs.controller.activities;

import android.content.Intent;
import android.os.Bundle;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.SkillsAdapter;
import com.mrq.paljobs.databinding.ActivityJobDetailsBinding;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Proposal;
import com.squareup.picasso.Picasso;

public class JobDetailsActivity extends BaseActivity {

    ActivityJobDetailsBinding binding;
    SkillsAdapter adapter;
    Proposal model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        model = (Proposal) getIntent().getSerializableExtra(Constants.TYPE_MODEL);

        binding.tvTool.setText(getString(R.string.job_details));
        binding.imgBack.setOnClickListener(view -> onBackPressed());

        adapter = new SkillsAdapter(this);
        adapter.setList(model.getSkills());
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(adapter);

        binding.name.setText(model.getCompanyName());
        binding.time.setText(model.getTime());
        binding.title.setText(model.getTitle());
        binding.content.setText(model.getContent());
        binding.requirements.setText(model.getRequirement());

        if (!model.getCompanyImage().isEmpty()) {
            Picasso.get().load(model.getCompanyImage())
                    .placeholder(R.drawable.ic_company_logo)
                    .into(binding.image);
        } else {
            binding.image.setImageResource(R.drawable.ic_company_logo);
        }

        binding.btnShow.setOnClickListener(view -> {
            startActivity(new Intent(this, ProposalActivity.class)
                    .putExtra(Constants.TYPE_MODEL, model)
            );
        });

        binding.edit.setOnClickListener(view -> {
            startActivity(new Intent(this, AddProposalActivity.class)
                    .putExtra(Constants.TYPE_ID, Constants.TYPE_EDIT)
                    .putExtra(Constants.TYPE_MODEL, model)
            );
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}