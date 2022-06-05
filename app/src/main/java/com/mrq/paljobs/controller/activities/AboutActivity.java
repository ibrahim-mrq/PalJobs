package com.mrq.paljobs.controller.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.ActivityAboutBinding;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;

public class AboutActivity extends BaseActivity {

    ActivityAboutBinding binding;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        type = getIntent().getStringExtra(Constants.TYPE_TITLE);

        binding.appbar.tvTool.setText(getString(R.string.complete_profile));
        binding.appbar.imgBack.setOnClickListener(view -> onBackPressed());

        String styledText = "If you have any questions about how we use your personal data, <font color='#2699FB'>Contact us at: </font>";
        binding.contact.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);


        switch (type) {
            case Constants.TYPE_ABOUT:
                binding.about.setVisibility(View.VISIBLE);
                binding.social.setVisibility(View.VISIBLE);
                break;
            case Constants.TYPE_HELP:
                binding.help.setVisibility(View.VISIBLE);
                binding.social.setVisibility(View.GONE);
                break;
            case Constants.TYPE_PRIVACY:
                binding.privacy.setVisibility(View.VISIBLE);
                binding.social.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}