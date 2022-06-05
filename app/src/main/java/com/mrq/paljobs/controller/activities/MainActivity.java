package com.mrq.paljobs.controller.activities;

import androidx.annotation.StringRes;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.fragments.AboutFragment;
import com.mrq.paljobs.controller.fragments.HomeFragment;
import com.mrq.paljobs.controller.fragments.ProfileFragment;
import com.mrq.paljobs.controller.fragments.FavoriteFragment;
import com.mrq.paljobs.controller.fragments.SettingFragment;
import com.mrq.paljobs.controller.fragments.SubmitFragment;
import com.mrq.paljobs.databinding.ActivityMainBinding;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.orhanobut.hawk.Hawk;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    public static MainActivity context;
    Toast backToasty;
    long backPressedTime;

    // TODO : Employee
    HomeFragment homeFragment = HomeFragment.newInstance();
    FavoriteFragment favoriteFragment = FavoriteFragment.newInstance();
    SubmitFragment submitFragment = SubmitFragment.newInstance();
    ProfileFragment profileFragment = ProfileFragment.newInstance();
    SettingFragment settingFragment = SettingFragment.newInstance();
    AboutFragment aboutFragment = AboutFragment.newInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        context = MainActivity.this;

        if (Hawk.get(Constants.USER_TYPE, Constants.TYPE_EMPLOYEE).equals(Constants.TYPE_COMPANY)) {
            startActivity(new Intent(this, CompanyActivity.class));
            finishAffinity();
        }
        initBottomNavigation();
        initNavView();

        binding.main.appbar.imgMenu.setOnClickListener(view -> {
            binding.drawer.open();
        });

        binding.main.appbar.imgSearch.setOnClickListener(view -> {
            if (Hawk.get(Constants.USER_TYPE, Constants.TYPE_EMPLOYEE).equals(Constants.TYPE_COMPANY)) {
                replaceFragment(settingFragment, R.string.setting);
            } else {
                startActivity(new Intent(this, SearchActivity.class));
            }
        });

    }

    private void initBottomNavigation() {
        replaceFragment(homeFragment, R.string.jobs);
        binding.main.bottomNavigation.setSelectedItemId(R.id.bottom_home);
        binding.main.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    replaceFragment(homeFragment, R.string.jobs);
                    return true;
                case R.id.bottom_save:
                    replaceFragment(favoriteFragment, R.string.save_jobs);
                    return true;
                case R.id.bottom_submit:
                    replaceFragment(submitFragment, R.string.submit_jobs);
                    return true;
                case R.id.bottom_profile:
                    replaceFragment(profileFragment, R.string.profile);
                    return true;
            }
            return false;
        });
    }

    private void initNavView() {
        binding.navView.setCheckedItem(R.id.nav_home);
        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    binding.main.bottomNavigation.setSelectedItemId(R.id.bottom_home);
                    replaceFragment(homeFragment, R.string.jobs);
                    break;
                case R.id.nav_setting:
                    replaceFragment(settingFragment, R.string.setting);
                    break;
                case R.id.nav_about:
//                    startActivity();
                    replaceFragment(aboutFragment, R.string.about);
                    break;
                case R.id.nav_logout:
                    Constants.logout(this);
                    break;
            }
            binding.drawer.close();
            return true;
        });
    }

    public void replaceFragment(Fragment fragment, @StringRes int title) {
        binding.main.appbar.tvTool.setText(getResources().getText(title));
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.commit();
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