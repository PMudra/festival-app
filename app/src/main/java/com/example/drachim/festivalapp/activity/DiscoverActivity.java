package com.example.drachim.festivalapp.activity;

import android.app.Fragment;

import com.example.drachim.festivalapp.data.DummyContent;
import com.example.drachim.festivalapp.fragment.FestivalListFragment;

public class DiscoverActivity extends RootActivity implements FestivalListFragment.OnListFragmentInteractionListener {

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    protected Fragment CreateFragment() {
        return new FestivalListFragment();
    }
}
