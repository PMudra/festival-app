package com.example.drachim.festivalapp.fragment;

import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.data.Festival;
import com.example.drachim.festivalapp.data.FestivalRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DashboardFragment extends AbstractFestivalListFragment implements View.OnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView discoverList;
    private RecyclerView soonList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.dashboard_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        discoverList = (RecyclerView)view.findViewById(R.id.dashboard_discover_list);
        discoverList.setAdapter(new FestivalRecyclerViewAdapter(new ArrayList<Festival>(), getOnFestivalListInteractionListener(), getImageLoader()));

        soonList = (RecyclerView)view.findViewById(R.id.dashboard_soon_list);
        soonList.setAdapter(new FestivalRecyclerViewAdapter(new ArrayList<Festival>(), getOnFestivalListInteractionListener(), getImageLoader()));

        view.findViewById(R.id.dashboard_discover_more_card).setOnClickListener(this);
        view.findViewById(R.id.dashboard_soon_more_card).setOnClickListener(this);

        return view;
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    protected void onLoadFinished(List<Festival> data) {
        discoverList.setAdapter(new FestivalRecyclerViewAdapter(data, getOnFestivalListInteractionListener(), getImageLoader()));
        soonList.setAdapter(new FestivalRecyclerViewAdapter(Arrays.asList(data.get(0)), getOnFestivalListInteractionListener(), getImageLoader()));
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dashboard_discover_more_card:
                getOnFestivalListInteractionListener().onMoreClicked(OnFestivalListInteractionListener.MoreOption.Discover);
                break;
            case R.id.dashboard_soon_more_card:
                break;
        }
    }
}
