package com.example.drachim.festivalapp.common;

import android.content.Context;

// copied from https://github.com/CypressNorth/Volley-Singleton/blob/master/MyApplication.java
public class Application extends android.app.Application {
    private static Application mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        this.setAppContext(getApplicationContext());
    }

    public static Application getInstance(){
        return mInstance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }
}