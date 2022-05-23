package com.mrq.paljobs.controller.activities;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.fragments.HomeFragment;
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
//        binding.main.appbar.toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(binding.main.appbar.toolbar);
        replaceFragment(HomeFragment.newInstance(), R.string.explore);
        initNavView();
        bottomNavigation();
    }

    private void bottomNavigation() {
        binding.main.bottomNavigation.setSelectedItemId(R.id.bottom_home);
        binding.main.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    replaceFragment(HomeFragment.newInstance(), R.string.explore);
                    return true;
                case R.id.bottom_save:
                    replaceFragment(HomeFragment.newInstance(), R.string.save);
                    return true;
                case R.id.bottom_submit:
                    replaceFragment(HomeFragment.newInstance(), R.string.submit);
                    return true;
                case R.id.bottom_profile:
                    replaceFragment(HomeFragment.newInstance(), R.string.profile);
                    return true;
            }
            return false;
        });
    }

    private void initNavView() {
        binding.navView.setCheckedItem(R.id.nav_explore);
        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_explore:
                    replaceFragment(HomeFragment.newInstance(), R.string.explore);
                    break;
                case R.id.nav_save:
                    replaceFragment(HomeFragment.newInstance(), R.string.save);
                    break;
                case R.id.nav_submit:
                    replaceFragment(HomeFragment.newInstance(), R.string.submit_jobs);
                    break;
                case R.id.nav_search:
                    replaceFragment(HomeFragment.newInstance(), R.string.search);
                    break;
                case R.id.nav_setting:
                    replaceFragment(HomeFragment.newInstance(), R.string.setting);
                    break;
                case R.id.nav_about:
                    replaceFragment(HomeFragment.newInstance(), R.string.about);
                    break;
            }
            binding.drawer.close();
            return true;
        });
    }

    public void replaceFragment(Fragment fragment, @StringRes int title) {
        binding.main.appbar.toolbar.setTitle(getResources().getText(title));
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.commit();
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