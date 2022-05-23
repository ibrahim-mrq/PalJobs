package com.mrq.paljobs.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrq.paljobs.controller.adapters.ProposalAdapter;
import com.mrq.paljobs.databinding.FragmentHomeBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseFragment;
import com.mrq.paljobs.models.Proposal;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    FragmentHomeBinding binding;
    ProposalAdapter adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        adapter = new ProposalAdapter(getActivity());
        binding.include.swipeToRefresh.setOnRefreshListener(this);
        binding.include.recyclerView.setAdapter(adapter);
        binding.include.recyclerView.setHasFixedSize(true);
        binding.include.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData();
    }

    private void loadData() {
//        binding.include.swipeToRefresh.setRefreshing(false);
//        new ApiRequest<Proposal>().getDataOrderBy(
//                getActivity(),
//                "Proposal",
//                "date",
//                Proposal.class,
//                new Results<ArrayList<Proposal>>() {
//                    @Override
//                    public void onSuccess(ArrayList<Proposal> proposals) {
//                        binding.include.statefulLayout.showContent();
//                        adapter.setList(proposals);
//                    }
//
//                    @Override
//                    public void onFailureInternet(@NotNull String offline) {
//                        binding.include.statefulLayout.showOffline(offline, view -> loadData());
//                    }
//
//                    @Override
//                    public void onEmpty() {
//                        binding.include.statefulLayout.showEmpty();
//                    }
//
//                    @Override
//                    public void onLoading(boolean loading) {
//                        if (loading) {
//                            binding.include.statefulLayout.showLoading();
//                        }
//                    }
//                }
//        );



        binding.include.swipeToRefresh.setRefreshing(false);
        new ApiRequest<Proposal>().getData(
                getActivity(),
                "Proposal",
                Proposal.class,
                new Results<ArrayList<Proposal>>() {
                    @Override
                    public void onSuccess(ArrayList<Proposal> proposals) {
                        binding.include.statefulLayout.showContent();
                        adapter.setList(proposals);
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
        loadData();
    }
}