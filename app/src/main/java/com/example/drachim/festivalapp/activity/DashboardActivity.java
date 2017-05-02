package com.example.drachim.festivalapp.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
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
        final View festivalCover = findViewById(R.id.festivalCover);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, festivalCover, "festivalCover");
        startActivity(new Intent(this, FestivalActivity.class), options.toBundle());
    }
}