package com.mrq.paljobs.controller.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.DialogAdapter;
import com.mrq.paljobs.controller.adapters.SearchAdapter;
import com.mrq.paljobs.databinding.ActivitySearchBinding;
import com.mrq.paljobs.databinding.CustomDialogListBinding;
import com.mrq.paljobs.databinding.DialogFilterBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Proposal;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    ActivitySearchBinding binding;
    SearchAdapter adapter;
    ArrayList<String> skillsString = new ArrayList<>();

    String skills = "";
    String field = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.appbar.tvTool.setText(getString(R.string.search));
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());
        binding.include.swipeToRefresh.setOnRefreshListener(this);
        adapter = new SearchAdapter(this);
        binding.include.recyclerView.setAdapter(adapter);
        binding.include.recyclerView.setHasFixedSize(true);
        binding.include.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.filter.setOnClickListener(view -> {
            showFilterDialog();
        });
        binding.search.setOnEditorActionListener((textView, i, keyEvent) -> {
            initProposal();
            return false;
        });


    }

    private void initProposal() {
        new ApiRequest<Proposal>().getData(
                this,
                "Proposal",
                Proposal.class,
                new Results<ArrayList<Proposal>>() {
                    @Override
                    public void onSuccess(ArrayList<Proposal> proposals) {
                        adapter.setList(new ArrayList<>());
                        for (int i = 0; i < proposals.size(); i++) {
                            for (int j = 0; j < proposals.get(i).getSkills().size(); j++) {
                                if (proposals.get(i).getSkills().get(j).equals(skills)) {
                                    adapter.addItem(proposals.get(i));
                                }
                            }
                        }
                        if (adapter.getList().isEmpty()) {
                            binding.include.statefulLayout.showEmpty();
                        } else
                            binding.include.statefulLayout.showContent();
//                        adapter.getFilter().filter(getText(binding.search));
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        binding.include.statefulLayout.showOffline(offline, view -> initProposal());
                    }

                    @Override
                    public void onEmpty() {
                        binding.include.statefulLayout.showEmpty();
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        binding.include.statefulLayout.showError(exception, view -> initProposal());
                    }

                    @Override
                    public void onLoading(boolean loading) {

                    }
                }
        );
    }

    private void showFilterDialog() {
        DialogFilterBinding filterBinding = DialogFilterBinding.inflate(getLayoutInflater());

        Dialog dialog = new Dialog(this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(filterBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.animationName);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        filterBinding.relativeClose.setOnClickListener(view -> dialog.dismiss());

        filterBinding.field.setOnClickListener(view -> {
            dialogField(getString(R.string.choose_your_field),
                    filterBinding.tvField, filterBinding.tvSkills, Constants.field());
        });

        filterBinding.skills.setOnClickListener(view -> {
            if (!skillsString.isEmpty()) {
                dialogSkills(getString(R.string.choose_your_skills), filterBinding.tvSkills, skillsString);
            } else {
                showAlert(SearchActivity.this, getString(R.string.must_select_jobField), R.color.orange);
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
            initProposal();
            skillsString.clear();
            dialog.dismiss();
        });

        filterBinding.btbReset.setOnClickListener(view -> {
            filterBinding.tvField.setText("");
            filterBinding.tvSkills.setText("");
            binding.search.setText("");
            binding.text.setText("");
            binding.text.setVisibility(View.GONE);
            skillsString.clear();
            skills = "";
            field = "";
        });

        dialog.show();
    }

    private void dialogField(String title, TextView tvField, TextView tvSkills, ArrayList<String> list) {
        Dialog dialog = new Dialog(this);
        CustomDialogListBinding dialogBinding = CustomDialogListBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.tvTitle.setText(title);
        DialogAdapter dialogAdapter = new DialogAdapter(this);
        dialogAdapter.setAnInterface(model -> {
            skillsString = Constants.fieldSkills(model);
            tvField.setText(model);
            field = model;
            tvSkills.setText("");
            dialog.dismiss();
        });
        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.recyclerView.setHasFixedSize(true);
        dialogBinding.recyclerView.setAdapter(dialogAdapter);
        dialogAdapter.setList(list);
        dialog.show();
    }

    private void dialogSkills(String title, TextView tvSkills, ArrayList<String> list) {
        Dialog dialog = new Dialog(this);
        CustomDialogListBinding dialogBinding = CustomDialogListBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.tvTitle.setText(title);
        DialogAdapter dialogAdapter = new DialogAdapter(this);
        dialogAdapter.setAnInterface(model -> {
            tvSkills.setText(model);
            skills = model;
            dialog.dismiss();
        });
        dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dialogBinding.recyclerView.setHasFixedSize(true);
        dialogBinding.recyclerView.setAdapter(dialogAdapter);
        dialogAdapter.setList(list);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onRefresh() {
        binding.include.swipeToRefresh.setRefreshing(false);
    }
}