package com.mrq.paljobs.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.activities.MainActivity;
import com.mrq.paljobs.controller.adapters.SkillsAdapter;
import com.mrq.paljobs.databinding.FragmentProfileBinding;
import com.mrq.paljobs.firebase.ApiRequest;
import com.mrq.paljobs.firebase.Results;
import com.mrq.paljobs.helpers.BaseFragment;
import com.mrq.paljobs.helpers.Constants;
import com.mrq.paljobs.models.User;
import com.orhanobut.hawk.Hawk;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends BaseFragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    FragmentProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SkillsAdapter adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        adapter = new SkillsAdapter(requireActivity());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        binding.btnUpdate.setOnClickListener(view -> update());

        if (Hawk.get(Constants.USER_TYPE, Constants.TYPE_EMPLOYEE).equals(Constants.TYPE_COMPANY)) {
            binding.tvSkills.setVisibility(View.GONE);
            binding.uploadSkills.setVisibility(View.GONE);
            binding.tvJobTitle.setVisibility(View.GONE);
            binding.tvGender.setVisibility(View.GONE);
            binding.tvCv.setVisibility(View.GONE);
        }

    }

    private void update() {
        enableElements(false);
        DocumentReference docRef = db.collection("User").document(Hawk.get(Constants.USER_TOKEN));
        docRef.update("firstName", getText(binding.etFName));
        docRef.update("lastName", getText(binding.etLName));
        docRef.update("phone", getText(binding.etPhone));
        docRef.update("address", getText(binding.etAddress));
        docRef.update("jobTitle", getText(binding.etJobTitle));
        docRef.addSnapshotListener((value, error) -> {
            showAlert(requireActivity(), getString(R.string.update_profile_successfully), R.color.green_success);
            enableElements(true);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        new ApiRequest<User>().getData(
                MainActivity.context,
                "User",
                Hawk.get(Constants.USER_TOKEN),
                User.class,
                new Results<User>() {
                    @Override
                    public void onSuccess(User user) {
                        binding.etFName.setText(user.getFirstName());
                        binding.etLName.setText(user.getLastName());
                        binding.etEmail.setText(user.getEmail());
                        binding.etPhone.setText(user.getPhone());
                        binding.etPassword.setText(user.getPassword());
                        binding.etAddress.setText(user.getAddress());
                        binding.etJobTitle.setText(user.getJobTitle());
                        binding.etGender.setText(user.getGender());
                        adapter.setList(user.getSkills());

                        if (user.getSkills().isEmpty()) {
                            binding.tvSkills.setVisibility(View.GONE);
                            binding.uploadSkills.setVisibility(View.GONE);
                        }

                        if (!user.getPhotoCover().isEmpty())
                            Picasso.get().load(user.getPhotoCover())
                                    .placeholder(R.drawable.ic_img_blank)
                                    .into(binding.cover);
                        if (!user.getPhoto().isEmpty())
                            Picasso.get().load(user.getPhoto()).placeholder(R.drawable.shape_accent).into(binding.photo);
                    }

                    @Override
                    public void onFailureInternet(@NotNull String offline) {
                        showAlert(requireActivity(), getString(R.string.no_internet), R.color.orange);
                    }

                    @Override
                    public void onException(@NotNull String exception) {
                        showAlert(requireActivity(), getString(R.string.error), R.color.red);
                    }

                    @Override
                    public void onEmpty() {

                    }

                    @Override
                    public void onLoading(boolean loading) {
                        enableElements(!loading);
                    }
                });
    }

    private void enableElements(boolean enable) {
        binding.btnUpdate.setEnabled(enable);
        if (!enable) {
            binding.btnUpdate.setBackgroundResource(R.drawable.shape_gray);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.btnUpdate.setBackgroundResource(R.drawable.shape_accent);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
        binding.etFName.setEnabled(enable);
        binding.etLName.setEnabled(enable);
        binding.etPhone.setEnabled(enable);
        binding.etAddress.setEnabled(enable);
        binding.etJobTitle.setEnabled(enable);
    }

}