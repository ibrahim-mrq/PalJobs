package com.mrq.paljobs.controller.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.activities.EditProfileActivity;
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

@SuppressLint("SetTextI18n")
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
        binding.edit.setOnClickListener(view -> {
            startActivity(new Intent(requireContext(), EditProfileActivity.class)
                    .putExtra(Constants.TYPE_TITLE, Hawk.get(Constants.USER_TYPE, ""))
            );
        });
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
                        binding.name.setText(user.getFirstName() + " " + user.getLastName());
                        binding.email.setText(user.getEmail());
                        binding.address.setText(user.getAddress());
                        binding.about.setText(user.getAbout());
                        binding.jobField.setText(user.getJobField());
                        binding.phone.setText(user.getPhone());
                        adapter.setList(user.getSkills());

                        if (user.getSkills().isEmpty()) {
                            binding.tvSkills.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.GONE);
                        }

                        if (!user.getPhoto().isEmpty())
                            Picasso.get().load(user.getPhoto()).placeholder(R.drawable.shape_accent).into(binding.photo);
                        else Picasso.get().load(R.drawable.ic_user_logo).into(binding.photo);

                        binding.cv.setOnClickListener(view -> {
                            Constants.loadFile(requireContext(), user.getCv());
                        });
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
                        if (loading)
                            showCustomProgress(false);
                        else dismissCustomProgress();
                    }
                });
    }

}