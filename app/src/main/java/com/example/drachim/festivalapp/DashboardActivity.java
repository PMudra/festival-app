package com.example.drachim.festivalapp;

import android.app.Fragment;

import com.example.drachim.festivalapp.fragment.DashboardFragment;

public class DashboardActivity extends RootActivity {

    @Override
    protected Fragment CreateFragment() {
        return new DashboardFragment();
    }
}
