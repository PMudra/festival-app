package com.example.drachim.festivalapp.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.fragment.DashboardFragment;

public class DashboardActivity extends RootActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected Fragment CreateFragment() {
        return new DashboardFragment();
    }

    @Override
    protected int createTitle() {
        return R.string.title_activity_dashboard;
    }

    public void openFestivalDetail(View view) {
        startActivity(new Intent(this, FestivalActivity.class));
    }
}
