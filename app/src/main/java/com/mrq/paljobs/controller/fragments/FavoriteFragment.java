package com.mrq.paljobs.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.FavoriteAdapter;
import com.mrq.paljobs.databinding.FragmentFavoriteBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseFragment;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Favorite;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FavoriteFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    FragmentFavoriteBinding binding;
    FavoriteAdapter adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        adapter = new FavoriteAdapter(getActivity());
        adapter.setRemoveListener(this::removeFavorite);
        binding.include.swipeToRefresh.setOnRefreshListener(this);
        binding.include.recyclerView.setAdapter(adapter);
        binding.include.recyclerView.setHasFixedSize(true);
        binding.include.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initFavorite();
    }

    private void initFavorite() {
        binding.include.swipeToRefresh.setRefreshing(false);
        new ApiRequest<Favorite>().getData(
                requireActivity(),
                "FavoriteProposal",
                "customerId",
                Hawk.get(Constants.USER_TOKEN),
                Favorite.class,
                new Results<ArrayList<Favorite>>() {
                    @Override
                    public void onSuccess(ArrayList<Favorite> favorites) {
                        binding.include.statefulLayout.showContent();
                        adapter.setList(favorites);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.include.statefulLayout.showOffline(offline, view -> initFavorite());
                    }

                    @Override
                    public void onEmpty() {
                        binding.include.statefulLayout.showEmpty();
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        binding.include.statefulLayout.showError(exception, view -> initFavorite());
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

    private void removeFavorite(String id) {
        new ApiRequest<String>().removeFavorite(
                requireActivity(),
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