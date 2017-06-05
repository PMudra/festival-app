package com.example.drachim.festivalapp.common;

import android.content.Context;

// copied from https://github.com/CypressNorth/Volley-Singleton/blob/master/MyApplication.java
public class Application extends android.app.Application {
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.setAppContext(getApplicationContext());
    }

    public static Context getAppContext() {
        return mAppContext;
    }
    private void setAppContext(Context mAppContext) {
        Application.mAppContext = mAppContext;
    }
}