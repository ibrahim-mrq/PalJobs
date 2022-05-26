package com.mrq.paljobs.controller.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.Query;
import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.activities.MainActivity;
import com.mrq.paljobs.controller.adapters.ProposalAdapter;
import com.mrq.paljobs.databinding.FragmentHomeBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseFragment;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.helpers.NetworkHelper;
import com.mrq.paljobs.models.Favorite;
import com.mrq.paljobs.models.Proposal;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        adapter = new ProposalAdapter(getActivity());
        adapter.setSaveInterface((model, imageView) -> {
            if (!model.getSaved()) {
                imageView.setImageResource(R.drawable.ic_save);
                addFavorite(model);
            } else {
                imageView.setImageResource(R.drawable.ic_unsave);
                removeFavorite(model.getId());
            }
            model.setSaved(!model.getSaved());
        });
        binding.include.swipeToRefresh.setOnRefreshListener(this);
        binding.include.recyclerView.setAdapter(adapter);
        binding.include.recyclerView.setHasFixedSize(true);
        binding.include.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initFavorite();

    }

    private void initFavorite() {
        binding.include.swipeToRefresh.setRefreshing(false);
        new ApiRequest<Favorite>().getData(
                getActivity(),
                "FavoriteProposal",
                "customerId",
                Hawk.get(Constants.USER_TOKEN),
                Favorite.class,
                new Results<ArrayList<Favorite>>() {
                    @Override
                    public void onSuccess(ArrayList<Favorite> favorites) {
                        initProposal(favorites);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.include.statefulLayout.showOffline(offline, view -> initFavorite());
                    }

                    @Override
                    public void onEmpty() {
                        initProposal(new ArrayList<>());
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        initProposal(new ArrayList<>());
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

    private void initProposal(ArrayList<Favorite> favorites) {
        new ApiRequest<Proposal>().getDataOrderBy(
                MainActivity.context,
                "Proposal",
                "time",
                Query.Direction.DESCENDING,
                Proposal.class,
                new Results<ArrayList<Proposal>>() {
                    @Override
                    public void onSuccess(ArrayList<Proposal> proposals) {
                        binding.include.statefulLayout.showContent();
                        adapter.setList(proposals, favorites);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.include.statefulLayout.showOffline(offline, view -> initProposal(favorites));
                    }

                    @Override
                    public void onEmpty() {
                        binding.include.statefulLayout.showEmpty();
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        binding.include.statefulLayout.showError(exception, view -> initProposal(favorites));
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
                requireContext(),
                favorite,
                new Results<String>() {
                    @Override
                    public void onSuccess(String success) {
                        showAlert((Activity) requireContext(), success, R.color.green_success , R.drawable.ic_save);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert((Activity) requireContext(), offline, R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert((Activity) requireContext(), exception, R.color.red);
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
                getActivity(),
                id,
                new Results<String>() {
                    @Override
                    public void onSuccess(String success) {
                        showAlert(requireActivity(), success, R.color.orange, R.drawable.ic_unsave);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(requireActivity(), offline, R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(requireActivity(), exception, R.color.red);
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
    public void onRefresh() {
        initFavorite();
    }

}