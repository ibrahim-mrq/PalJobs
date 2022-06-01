package com.mrq.paljobs.controller.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.SkillsAdapter;
import com.mrq.paljobs.databinding.ActivitySubmitBinding;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Proposal;
import com.squareup.picasso.Picasso;

public class SubmitActivity extends BaseActivity {

    ActivitySubmitBinding binding;
    SkillsAdapter adapter;
    Proposal model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubmitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        model = (Proposal) getIntent().getSerializableExtra(Constants.TYPE_MODEL);

        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        binding.appbar.tvTool.setText(getString(R.string.submit_proposal));

        binding.name.setText(model.getCompanyName());
        binding.time.setText(model.getTime());
        binding.title.setText(model.getTitle());
        binding.content.setText(model.getContent());

        if (!model.getCompanyImage().isEmpty())
            Picasso.get().load(model.getCompanyImage()).into(binding.image);

        adapter = new SkillsAdapter(this);
        adapter.setList(model.getSkills());
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}