package com.example.drachim.festivalapp.fragment;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

        // Only future festivals
        List<Festival> futureFestivals = new ArrayList<>();
        for (Festival festival : data) {
            if (Calendar.getInstance().getTime().before(festival.getEndDate())) {
                futureFestivals.add(festival);
            }
        }

        // Sort by startDate
        Collections.sort(futureFestivals, new Comparator<Festival>() {
            public int compare(Festival f1, Festival f2) {
                if (f1.getStartDate() == null || f1.getStartDate() == null)
                    return 0;
                return f1.getStartDate().compareTo(f2.getStartDate());
            }
        });

        // Only the next three festivals
        final List<Festival> soonFestivals = new ArrayList<>(futureFestivals.subList(0, 3));
        soonList.setAdapter(new FestivalRecyclerViewAdapter(soonFestivals, getOnFestivalListInteractionListener(), getImageLoader()));

        // Only festivals which aren't already shown in soonList in random order
        futureFestivals.removeAll(soonFestivals);
        Collections.shuffle(futureFestivals);
        discoverList.setAdapter(new FestivalRecyclerViewAdapter(futureFestivals.subList(0, 3), getOnFestivalListInteractionListener(), getImageLoader()));

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dashboard_discover_more_card:
                getOnFestivalListInteractionListener().onMoreClicked(OnFestivalListInteractionListener.MoreOption.Discover);
                break;
            case R.id.dashboard_soon_more_card:
                getOnFestivalListInteractionListener().onMoreClicked(OnFestivalListInteractionListener.MoreOption.Discover);
                break;
        }
    }
}
