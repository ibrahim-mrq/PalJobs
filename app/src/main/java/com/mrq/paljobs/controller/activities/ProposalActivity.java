package com.mrq.paljobs.controller.activities;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.SubmitAdapter;
import com.mrq.paljobs.databinding.ActivityProposalBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Proposal;
import com.mrq.paljobs.models.Submit;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProposalActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    ActivityProposalBinding binding;
    SubmitAdapter adapter;
    Proposal model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProposalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        model = (Proposal) getIntent().getSerializableExtra(Constants.TYPE_MODEL);

        binding.appbar.tvTool.setText(getString(R.string.profile));
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());

        binding.include.swipeToRefresh.setOnRefreshListener(this);
        adapter = new SubmitAdapter(this);
        binding.include.recyclerView.setAdapter(adapter);
        binding.include.recyclerView.setHasFixedSize(true);
        binding.include.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void loadData() {
        binding.include.swipeToRefresh.setRefreshing(false);
        new ApiRequest<Submit>().getData(
                MainActivity.context,
                "Submit",
                "companyId",
                Hawk.get(Constants.USER_TOKEN),
                "proposalId",
                model.getId(),
                Submit.class,
                new Results<ArrayList<Submit>>() {
                    @Override
                    public void onSuccess(ArrayList<Submit> submits) {
                        binding.include.statefulLayout.showContent();
                        adapter.setList(submits);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.include.statefulLayout.showOffline(offline, view -> loadData());
                    }

                    @Override
                    public void onEmpty() {
                        binding.include.statefulLayout.showEmpty();
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        binding.include.statefulLayout.showError(exception, view -> loadData());
                    }

                    @Override
                    public void onLoading(boolean loading) {
                        if (loading) {
                            binding.include.statefulLayout.showLoading();
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}