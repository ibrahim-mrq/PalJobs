package com.mrq.paljobs.helpers;

import android.app.Application;
import android.content.Context;

import com.orhanobut.hawk.Hawk;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(getBaseContext()).build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

}
