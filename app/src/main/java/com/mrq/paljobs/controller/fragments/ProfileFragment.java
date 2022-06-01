package com.mrq.paljobs.controller.fragments;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        loadData();
        if (Hawk.get(Constants.USER_TYPE, Constants.TYPE_EMPLOYEE).equals(Constants.TYPE_COMPANY)) {
            binding.linearUser.setVisibility(View.GONE);
        }

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

                        adapter.setList(user.getSkills());

                        if (user.getSkills().isEmpty()) {
                            binding.tvSkills.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.GONE);
                        }

                        if (!user.getPhoto().isEmpty())
                            Picasso.get().load(user.getPhoto()).placeholder(R.drawable.shape_accent).into(binding.photo);

                        binding.cv.setOnClickListener(view -> load(user.getCv()));
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

    private void load(String url) {
        DownloadManager downloadmanager = (DownloadManager)
                requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("My CV File");
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        downloadmanager.enqueue(request);
    }

}