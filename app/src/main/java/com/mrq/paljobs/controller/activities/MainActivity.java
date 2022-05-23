package com.mrq.paljobs.controller.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.mrq.paljobs.R;
import com.mrq.paljobs.databinding.ActivityMainBinding;
import com.mrq.paljobs.helpers.BaseActivity;

@SuppressLint("StaticFieldLeak,NonConstantResourceId")
public class MainActivity extends BaseActivity {

    public static ActivityMainBinding binding;
    ActionBarDrawerToggle toggle;
    public static Context context;

    private Toast backToasty;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        context = MainActivity.this;
        toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.main.appbar.toolbar,
                R.string.open, R.string.close);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();
        setSupportActionBar(binding.main.appbar.toolbar);
        initNavView();
    }

    private void initNavView() {
        binding.navView.setCheckedItem(R.id.nav_home);
        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    break;
            }
            binding.drawer.close();
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawers();
        } else {
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

}