package com.mrq.paljobs.controller.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.mrq.paljobs.R;
import com.mrq.paljobs.helpers.BaseActivity;
import com.mrq.paljobs.helpers.Constants;
import com.orhanobut.hawk.Hawk;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        new Handler().postDelayed(() -> {
            if (!Hawk.get(Constants.IS_FIRST_START, false)) {
                startActivity(new Intent(SplashActivity.this, BoardActivity.class));
            } else {
                if (Hawk.get(Constants.IS_LOGIN, false)) {
                    if (Hawk.get(Constants.USER_TYPE).equals(Constants.TYPE_COMPANY)) {
                        startActivity(new Intent(this, CompanyActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }
            finish();
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}