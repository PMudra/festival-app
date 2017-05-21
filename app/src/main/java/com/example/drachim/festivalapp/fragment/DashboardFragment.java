package com.example.drachim.festivalapp.fragment;

import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.data.Festival;

import java.util.List;

public class DashboardFragment extends AbstractFestivalListFragment {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.dashboard_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    public void onLoadFinished(Loader<List<Festival>> loader, List<Festival> data) {
        swipeRefreshLayout.setRefreshing(false);
    }
}
