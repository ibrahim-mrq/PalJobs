package com.mrq.paljobs.controller.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.CompanyAdapter;
import com.mrq.paljobs.databinding.ActivityCompanyBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.Proposal;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CompanyActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActivityCompanyBinding binding;
    CompanyAdapter adapter;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.tvTool.setText(getString(R.string.our_jobs));

        binding.fab.setOnClickListener(view -> {
            startActivity(new Intent(this, AddProposalActivity.class)
                    .putExtra(Constants.TYPE_ID, Constants.TYPE_ADD)
            );
        });

        binding.setting.setOnClickListener(view -> {
            startActivity(new Intent(this, SettingActivity.class));
        });

        adapter = new CompanyAdapter(this);
        adapter.deleteInterface(this::delete);
        binding.include.swipeToRefresh.setOnRefreshListener(this);
        binding.include.recyclerView.setAdapter(adapter);
        binding.include.recyclerView.setHasFixedSize(true);
        binding.include.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initJobs();

    }

    private void initJobs() {
        binding.include.swipeToRefresh.setRefreshing(false);
        new ApiRequest<Proposal>().getData(
                this,
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

    private void delete(Proposal model) {
        dialog = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this proposal?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    deleteProposal(model);
                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.cancel();
                })
                .create();

        dialog.show();
    }

    private void deleteProposal(Proposal model) {
        DocumentReference docRef = db.collection("Proposal")
                .document(model.getId());
        docRef.delete();
        docRef.addSnapshotListener((value, error) -> {
            showAlert(this, getString(R.string.delete_proposal_successfully), R.color.green_success);
            dialog.dismiss();
        });
    }

    Toast backToasty;
    long backPressedTime;

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                backToasty.cancel();
                super.onBackPressed();
                return;
            } else {
                backToasty = Toast.makeText(this, getString(R.string.back_exit), Toast.LENGTH_SHORT);
                backToasty.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }
}
