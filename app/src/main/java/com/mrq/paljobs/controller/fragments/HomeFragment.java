package com.mrq.paljobs.controller.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.firestore.Query;
import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.activities.MainActivity;
import com.mrq.paljobs.controller.adapters.DialogAdapter;
import com.mrq.paljobs.controller.adapters.ProposalAdapter;
import com.mrq.paljobs.databinding.CustomDialogListBinding;
import com.mrq.paljobs.databinding.DialogFilterBinding;
import com.mrq.paljobs.databinding.FragmentHomeBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseFragment;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Favorite;
import com.mrq.paljobs.models.Proposal;
import com.mrq.paljobs.models.Submit;
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
    ArrayList<Submit> submitsList = new ArrayList<>();
    ArrayList<Favorite> favoritesList = new ArrayList<>();

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
                model.setSaved(!model.getSaved());
                imageView.setImageResource(R.drawable.ic_save);
                addFavorite(model);
            } else {
                showAlert(requireActivity(), getString(R.string.proposal_already__saved), R.color.green);
            }
        });
        binding.include.swipeToRefresh.setOnRefreshListener(this);
        binding.include.recyclerView.setAdapter(adapter);
        binding.include.recyclerView.setHasFixedSize(true);
        binding.include.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initFavorite();

        binding.filter.setOnClickListener(view -> {
            showFilterDialog();
        });
    }

    private void initFavorite() {
        binding.include.swipeToRefresh.setRefreshing(false);
        new ApiRequest<Favorite>().getData(
                MainActivity.context,
                "Favorite",
                "customerId",
                Hawk.get(Constants.USER_TOKEN),
                Favorite.class,
                new Results<ArrayList<Favorite>>() {
                    @Override
                    public void onSuccess(ArrayList<Favorite> favorites) {
                        favoritesList = favorites;
                        initSubmit(favoritesList);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.include.statefulLayout.showOffline(offline, view -> initFavorite());
                    }

                    @Override
                    public void onEmpty() {
                        initSubmit(new ArrayList<>());
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        initSubmit(new ArrayList<>());
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

    private void initSubmit(ArrayList<Favorite> favorites) {
        new ApiRequest<Submit>().getData(
                MainActivity.context,
                "Submit",
                "customerId",
                Hawk.get(Constants.USER_TOKEN),
                Submit.class,
                new Results<ArrayList<Submit>>() {
                    @Override
                    public void onSuccess(ArrayList<Submit> submit) {
                        submitsList = submit;
                        initProposal(1, favoritesList, submit);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.include.statefulLayout.showOffline(offline, view -> initFavorite());
                    }

                    @Override
                    public void onEmpty() {
                        initProposal(1, favorites, new ArrayList<>());
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        initProposal(1, favorites, new ArrayList<>());
                    }

                    @Override
                    public void onLoading(boolean loading) {

                    }
                }
        );
    }

    private void initProposal(int type, ArrayList<Favorite> favorites, ArrayList<Submit> submit) {
        new ApiRequest<Proposal>().getDataOrderBy(
                MainActivity.context,
                "Proposal",
                "time",
                Query.Direction.DESCENDING,
                Proposal.class,
                new Results<ArrayList<Proposal>>() {
                    @Override
                    public void onSuccess(ArrayList<Proposal> proposals) {
                        if (type == 1) {
                            binding.include.statefulLayout.showContent();
                            adapter.setList(proposals, favorites, submit);
                        } else {
                            adapter.setList(new ArrayList<>(), favorites, submit);
                            for (int i = 0; i < proposals.size(); i++) {
                                for (int j = 0; j < proposals.get(i).getSkills().size(); j++) {
                                    if (proposals.get(i).getSkills().get(j).equals(skills)) {
                                        adapter.addItem(proposals.get(i), favorites, submit);
                                    }
                                }
                            }
                            if (adapter.getList().isEmpty()) {
                                binding.include.statefulLayout.showEmpty();
                            } else {
                                binding.include.statefulLayout.showContent();
                            }
                        }
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.include.statefulLayout.showOffline(offline, view -> initProposal(type, favorites, submit));
                    }

                    @Override
                    public void onEmpty() {
                        binding.include.statefulLayout.showEmpty();
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        binding.include.statefulLayout.showError(exception, view -> initProposal(type, favorites, submit));
                    }

                    @Override
                    public void onLoading(boolean loading) {

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

        new ApiRequest<Favorite>().addFavorite(
                MainActivity.context,
                favorite,
                new Results<String>() {
                    @Override
                    public void onSuccess(String success) {
                        showAlert((Activity) requireContext(), success, R.color.green_success, R.drawable.ic_save);
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
                MainActivity.context,
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

    ArrayList<String> skillsString = new ArrayList<>();
    String skills = "";
    String field = "";

    private void showFilterDialog() {
        DialogFilterBinding filterBinding = DialogFilterBinding.inflate(getLayoutInflater());

        Dialog dialog = new Dialog(requireContext());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(filterBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.animationName);

        WindowManager.LayoutParams params = MainActivity.context.getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        MainActivity.context.getWindow().setAttributes(params);
        filterBinding.relativeClose.setOnClickListener(view -> dialog.dismiss());

        filterBinding.field.setOnClickListener(view -> {
            dialogField(getString(R.string.choose_your_field),
                    filterBinding.tvField, filterBinding.tvSkills, Constants.field());
        });

        filterBinding.skills.setOnClickListener(view -> {
            if (!skillsString.isEmpty()) {
                dialogSkills(getString(R.string.choose_your_skills), filterBinding.tvSkills, skillsString);
            } else {
                showAlert(MainActivity.context, getString(R.string.must_select_jobField), R.color.orange);
            }
        });

        if (!skills.isEmpty()) {
            filterBinding.tvSkills.setText(skills);
            filterBinding.tvField.setText(field);
        }

        filterBinding.btnApply.setOnClickListener(view -> {
            if (!skills.isEmpty()) {
                binding.text.setText(skills);
                binding.text.setVisibility(View.VISIBLE);
            }
            initProposal(2, favoritesList, submitsList);
            skillsString.clear();
            dialog.dismiss();
        });

        filterBinding.btbReset.setOnClickListener(view -> {
            filterBinding.tvField.setText("");
            filterBinding.tvSkills.setText("");
            binding.text.setText("");
            binding.text.setVisibility(View.GONE);
            skillsString.clear();
            skills = "";
            field = "";
            initProposal(1, favoritesList, submitsList);
        });

        dialog.show();
    }

    private void dialogField(String title, TextView tvField, TextView tvSkills, ArrayList<String> list) {
        Dialog dialog = new Dialog(MainActivity.context);
        CustomDialogListBinding dialogBinding = CustomDialogListBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.tvTitle.setText(title);
        DialogAdapter dialogAdapter = new DialogAdapter(MainActivity.context);
        dialogAdapter.setAnInterface(model -> {
            skillsString = Constants.fieldSkills(model);
            tvField.setText(model);
            field = model;
            tvSkills.setText("");
            dialog.dismiss();
        });
        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.context));
        dialogBinding.recyclerView.setHasFixedSize(true);
        dialogBinding.recyclerView.setAdapter(dialogAdapter);
        dialogAdapter.setList(list);
        dialog.show();
    }

    private void dialogSkills(String title, TextView tvSkills, ArrayList<String> list) {
        Dialog dialog = new Dialog(MainActivity.context);
        CustomDialogListBinding dialogBinding = CustomDialogListBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.tvTitle.setText(title);
        DialogAdapter dialogAdapter = new DialogAdapter(MainActivity.context);
        dialogAdapter.setAnInterface(model -> {
            tvSkills.setText(model);
            skills = model;
            dialog.dismiss();
        });
        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.context));
        dialogBinding.recyclerView.setHasFixedSize(true);
        dialogBinding.recyclerView.setAdapter(dialogAdapter);
        dialogAdapter.setList(list);
        dialog.show();
    }

    @Override
    public void onRefresh() {
        initFavorite();
    }

}