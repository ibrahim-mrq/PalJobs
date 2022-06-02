package com.mrq.paljobs.controller.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrq.paljobs.controller.activities.AddProposalActivity;
import com.mrq.paljobs.controller.activities.MainActivity;
import com.mrq.paljobs.controller.adapters.CompanyAdapter;
import com.mrq.paljobs.databinding.FragmentCompanyBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseFragment;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Proposal;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CompanyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public CompanyFragment() {
        // Required empty public constructor
    }

    public static CompanyFragment newInstance() {
        CompanyFragment fragment = new CompanyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    FragmentCompanyBinding binding;
    CompanyAdapter adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentCompanyBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        binding.fab.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), AddProposalActivity.class)
                    .putExtra(Constants.TYPE_ID, Constants.TYPE_ADD)
            );
        });

        adapter = new CompanyAdapter(getActivity());
        binding.include.swipeToRefresh.setOnRefreshListener(this);
        binding.include.recyclerView.setAdapter(adapter);
        binding.include.recyclerView.setHasFixedSize(true);
        binding.include.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initJobs();

    }

    private void initJobs() {
        binding.include.swipeToRefresh.setRefreshing(false);
        new ApiRequest<Proposal>().getData(
                MainActivity.context,
                "Proposal",
                "companyId",
                Hawk.get(Constants.USER_TOKEN),
                Proposal.class,
                new Results<ArrayList<Proposal>>() {
                    @Override
                    public void onSuccess(ArrayList<Proposal> proposals) {
                        binding.include.statefulLayout.showContent();
                        adapter.setList(proposals);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.include.statefulLayout.showOffline(offline, view -> initJobs());
                    }

                    @Override
                    public void onEmpty() {
                        binding.include.statefulLayout.showEmpty();
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        binding.include.statefulLayout.showError(exception, view -> initJobs());
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
    public void onRefresh() {
        initJobs();
    }
}