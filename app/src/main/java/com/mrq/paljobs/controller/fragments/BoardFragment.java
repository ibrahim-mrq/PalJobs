package com.mrq.paljobs.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrq.paljobs.databinding.FragmentBoardBinding;
import com.mrq.paljobs.helpers.BaseFragment;

import org.jetbrains.annotations.NotNull;

public class BoardFragment extends BaseFragment {

    private static final String TITLE = "title";
    private static final String CONTENT = "content";

    private String title;
    private String content;

    public BoardFragment() {
        // Required empty public constructor
    }

    public static BoardFragment newInstance(String title, String content) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            content = getArguments().getString(CONTENT);
        }
    }

    FragmentBoardBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBoardBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.title.setText(title);
        binding.content.setText(content);
    }
}