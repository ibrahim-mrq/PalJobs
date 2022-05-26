package com.mrq.paljobs.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.Query;
import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.ProposalAdapter;
import com.mrq.paljobs.databinding.FragmentHomeBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseFragment;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Favorite;
import com.mrq.paljobs.models.Proposal;
import com.orhanobut.hawk.Hawk;

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
        adapter.setSaveInterface((model, imageView) -> {
            model.setSaved(!model.getSaved());
            if (model.getSaved()) {
                imageView.setImageResource(R.drawable.ic_save);
            } else {
                imageView.setImageResource(R.drawable.ic_unsave);
            }

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
                    getActivity(),
                    "FavoriteProposal",
                    favorite,
                    new Results<String>() {
                        @Override
                        public void onSuccess(String proposals) {
                            Log.e("response", "proposals = " + proposals);

                        }

                        @Override
                        public void onFailureInternet(@NotNull String offline) {
                            Log.e("response", "offline = " + offline);

                        }

                        @Override
                        public void onException(@NotNull String exception) {
                            Log.e("response", "exception = " + exception);

                        }

                        @Override
                        public void onEmpty() {

                        }

                        @Override
                        public void onLoading(boolean loading) {

                        }
                    }
            );
        });
        binding.include.swipeToRefresh.setOnRefreshListener(this);
        binding.include.recyclerView.setAdapter(adapter);
        binding.include.recyclerView.setHasFixedSize(true);
        binding.include.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadData(new ArrayList<>());
    }

    private void loadFavorite() {
        new ApiRequest<Favorite>().getData(
                getActivity(),
                "FavoriteProposal",
                "customerId",
                Hawk.get(Constants.USER_TOKEN),
                Favorite.class,
                new Results<ArrayList<Favorite>>() {
                    @Override
                    public void onSuccess(ArrayList<Favorite> favorites) {
                        loadData(favorites);
                        Log.e("response", "favorites = " + favorites);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.include.statefulLayout.showOffline(offline, view -> loadFavorite());
                    }

                    @Override
                    public void onEmpty() {
                        loadData(new ArrayList<>());
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        binding.include.statefulLayout.showError(exception, view -> loadFavorite());
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

    private void loadData(ArrayList<Favorite> favorites) {
        binding.include.swipeToRefresh.setRefreshing(false);
        new ApiRequest<Proposal>().getDataOrderBy(
                getActivity(),
                "Proposal",
                "time",
                Query.Direction.DESCENDING,
                Proposal.class,
                new Results<ArrayList<Proposal>>() {
                    @Override
                    public void onSuccess(ArrayList<Proposal> proposals) {
                        binding.include.statefulLayout.showContent();
//                        for (int i = 0; i < favorites.size() - 1; i++) {
//                            for (int k = i + 1; k < proposals.size(); k++) {
//                                if (proposals.get(i).getId().equals(favorites.get(k).getProposalId())) {
//                                    Log.e("response", i + " and " + k + " are pairs");
//                                    proposals.get(k).setSaved(true);
//                                } else {
//                                    Log.e("response", i + " and " + k + " are not pairs");
//                                    proposals.get(k).setSaved(false);
//                                }
//                            }
//                        }
                        adapter.setList(proposals);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.include.statefulLayout.showOffline(offline, view -> loadData(favorites));
                    }

                    @Override
                    public void onEmpty() {
                        binding.include.statefulLayout.showEmpty();
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        binding.include.statefulLayout.showError(exception, view -> loadData(favorites));
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
//        loadFavorite();
    }
}