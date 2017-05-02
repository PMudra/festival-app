package com.example.drachim.festivalapp.activity;

import android.app.Fragment;
import android.os.Bundle;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.data.DummyContent;
import com.example.drachim.festivalapp.fragment.FestivalListFragment;

public class DiscoverActivity extends RootActivity implements FestivalListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_discover);
        }
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    protected Fragment CreateFragment() {
        return new FestivalListFragment();
    }
}
