package com.mrq.paljobs.controller.activities;

import androidx.annotation.StringRes;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mrq.paljobs.R;
import com.mrq.paljobs.controller.fragments.AboutFragment;
import com.mrq.paljobs.controller.fragments.CompanyFragment;
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

    // TODO : company
    CompanyFragment companyFragment = CompanyFragment.newInstance();
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

        binding.main.bottomNavigation.getMenu().clear();
        if (Hawk.get(Constants.USER_TYPE, Constants.TYPE_EMPLOYEE).equals(Constants.TYPE_COMPANY)) {
            binding.main.bottomNavigation.inflateMenu(R.menu.menu_bottom_company);
            replaceFragment(companyFragment, R.string.my_jobs);
            binding.main.bottomNavigation.setSelectedItemId(R.id.bottom_c_home);
            binding.main.appbar.imgSearch.setImageResource(R.drawable.ic_settings);
            binding.main.appbar.imgMenu.setVisibility(View.GONE);
        } else {
            binding.main.bottomNavigation.inflateMenu(R.menu.menu_bottom);
            replaceFragment(homeFragment, R.string.jobs);
            binding.main.bottomNavigation.setSelectedItemId(R.id.bottom_home);
            binding.main.appbar.imgSearch.setImageResource(R.drawable.ic_search);
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
        binding.main.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    binding.navView.setCheckedItem(R.id.nav_explore);
                    replaceFragment(homeFragment, R.string.jobs);
                    return true;
                case R.id.bottom_save:
                    binding.navView.setCheckedItem(R.id.nav_save);
                    replaceFragment(favoriteFragment, R.string.save_jobs);
                    return true;
                case R.id.bottom_submit:
                    binding.navView.setCheckedItem(R.id.nav_submit);
                    replaceFragment(submitFragment, R.string.submit_jobs);
                    return true;
                case R.id.bottom_profile:
                    replaceFragment(profileFragment, R.string.profile);
                    return true;
                // TODO : Company
                case R.id.bottom_c_home:
                    replaceFragment(companyFragment, R.string.our_jobs);
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
                    binding.main.bottomNavigation.setSelectedItemId(R.id.bottom_home);
                    replaceFragment(homeFragment, R.string.jobs);
                    break;
                case R.id.nav_save:
                    binding.main.bottomNavigation.setSelectedItemId(R.id.bottom_save);
                    replaceFragment(favoriteFragment, R.string.save_jobs);
                    break;
                case R.id.nav_submit:
                    binding.main.bottomNavigation.setSelectedItemId(R.id.bottom_submit);
                    replaceFragment(submitFragment, R.string.submit_jobs);
                    break;
                case R.id.nav_search:
                    startActivity(new Intent(this, SearchActivity.class));
                    break;
                case R.id.nav_setting:
                    replaceFragment(settingFragment, R.string.setting);
                    break;
                case R.id.nav_about:
                    replaceFragment(aboutFragment, R.string.about);
                    break;
                // TODO : Company
                case R.id.nav_out_job:
                    replaceFragment(companyFragment, R.string.our_jobs);
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