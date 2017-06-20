package com.example.drachim.festivalapp.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.data.Festival;
import com.example.drachim.festivalapp.data.FestivalRecyclerViewAdapter;
import com.example.drachim.festivalapp.data.LocalStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FestivalListFragment extends AbstractFestivalListFragment {

    public static final String SHOW_FAVORITES_ONLY = "SHOW_FAVORITES_ONLY";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private boolean showFavoritesOnly;

    public static FestivalListFragment newInstance(final boolean showFavroritesOnly) {
        FestivalListFragment festivalListFragment = new FestivalListFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_FAVORITES_ONLY, showFavroritesOnly);
        festivalListFragment.setArguments(bundle);

        return festivalListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            showFavoritesOnly = arguments.getBoolean(SHOW_FAVORITES_ONLY);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_festival_filter, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                DialogFragment filterDialogFragment = new FilterDialogFragment();
                filterDialogFragment.show(getChildFragmentManager(), FilterDialogFragment.tag);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_festival_list, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(new FestivalRecyclerViewAdapter(Collections.<Festival>emptyList(), getOnFestivalListInteractionListener(), getImageLoader()));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    protected SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    protected void onLoadFinished(List<Festival> data) {
        List<Festival> filteredList = new ArrayList<>();
        for (Festival festival : data) {
            if (!showFavoritesOnly || LocalStorage.isFavorite(this.getActivity(), festival.getId())) {
                filteredList.add(festival);
            }
        }

        recyclerView.setAdapter(new FestivalRecyclerViewAdapter(filteredList, getOnFestivalListInteractionListener(), getImageLoader()));
        swipeRefreshLayout.setRefreshing(false);
    }

}
