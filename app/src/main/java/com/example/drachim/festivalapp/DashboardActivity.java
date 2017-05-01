package com.example.drachim.festivalapp;

import android.app.Fragment;
import android.content.Intent;
import android.view.View;

import com.example.drachim.festivalapp.fragment.DashboardFragment;

public class DashboardActivity extends RootActivity {

    @Override
    protected Fragment CreateFragment() {
        return new DashboardFragment();
    }

    public void openFestivalDetail(View view) {
        startActivity(new Intent(this, FestivalActivity.class));
    }
}
