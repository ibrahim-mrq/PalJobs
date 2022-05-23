package com.mrq.paljobs.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.adapters.ViewPagerAdapter;
import com.mrq.paljobs.controller.fragments.BoardFragment;
import com.mrq.paljobs.databinding.ActivityBoardBinding;
import com.mrq.paljobs.helpers.BaseActivity;

public class BoardActivity extends BaseActivity {

    ActivityBoardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
//        Hawk.put(Constants.IS_FIRST_START, true);
        initPager();
        binding.skip.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        binding.next.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

    }

    ViewPagerAdapter adapter;

    private void initPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BoardFragment.newInstance("Main Title", "Ut labore et dolore roipi mana aliqua. Enim \n" + "adeop minim veeniam nostruklad"), "");
        adapter.addFragment(BoardFragment.newInstance("Main Title", "Ut labore et dolore roipi mana aliqua. Enim \n" + "adeop minim veeniam nostruklad"), "");
        adapter.addFragment(BoardFragment.newInstance("Main Title", "Ut labore et dolore roipi mana aliqua. Enim \n" + "adeop minim veeniam nostruklad"), "");
        binding.viewpager.setAdapter(adapter);
        loadDots(0);
        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                loadDots(position);
                if (position == adapter.getCount() - 1) {
                    binding.next.setVisibility(View.VISIBLE);
                } else {
                    binding.next.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void loadDots(int position) {
        int count = adapter.getCount();
        TextView[] textDots = new TextView[count];
        binding.dots.removeAllViews();
        for (int i = 0; i < count; i++) {
            textDots[i] = new TextView(this);
            textDots[i].setText(Html.fromHtml("&#8226;"));
            textDots[i].setTextSize(40);
            textDots[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.dots.addView(textDots[i]);
        }
        if (textDots.length > 0) {
            textDots[position].setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}