package com.example.drachim.festivalapp.fragment;

import android.app.DialogFragment;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.common.Application;
import com.example.drachim.festivalapp.data.Festival;
import com.example.drachim.festivalapp.data.FestivalRecyclerViewAdapter;
import com.example.drachim.festivalapp.data.LocalStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FestivalListFragment extends AbstractFestivalListFragment implements FilterDialogFragment.OnFilterListener {

    public static final String SHOW_FAVORITES_ONLY = "SHOW_FAVORITES_ONLY";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private boolean showFavoritesOnly;
    private List<Festival> festivals;
    private String searchQuery = "";
    private FilterDialogFragment.Filter filter;

    public static FestivalListFragment newInstance(final boolean showFavoritesOnly) {
        FestivalListFragment festivalListFragment = new FestivalListFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_FAVORITES_ONLY, showFavoritesOnly);
        festivalListFragment.setArguments(bundle);

        return festivalListFragment;
    }

    public FestivalListFragment() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.YEAR, 1);
        Date inOneYear = calendar.getTime();
        filter = new FilterDialogFragment.Filter(FilterDialogFragment.Distance.KM500, true, now, inOneYear);
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

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText;
                applyFilters();
                return true;
            }
        });
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

                final String location = getHomeLocation();
                if (location == null) {
                    Toast.makeText(getActivity(), R.string.filter_no_location_given, Toast.LENGTH_LONG).show();
                } else {
                    DialogFragment filterDialogFragment = FilterDialogFragment.newInstance(filter);
                    filterDialogFragment.show(getFragmentManager(), FilterDialogFragment.TAG);
                }
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
        festivals = data;
        applyFilters();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void sortFestivals(List<Festival> filteredFestivals) {
        String homeLocation = getHomeLocation();
        if (!filter.isSortByDate() && homeLocation != null) {
            // Sort by distance
            final Map<Festival, Float> festivalDistance = new HashMap<>();
            for (Festival festival : filteredFestivals) {
                festivalDistance.put(festival, getDistanceToHomeLocation(festival, homeLocation));
            }
            Collections.sort(filteredFestivals, new Comparator<Festival>() {
                @Override
                public int compare(Festival festival1, Festival festival2) {
                    return festivalDistance.get(festival1).compareTo(festivalDistance.get(festival2));
                }
            });
        } else {
            // Sort by date
            Collections.sort(filteredFestivals, new Comparator<Festival>() {
                @Override
                public int compare(Festival festival1, Festival festival2) {
                    return festival1.getStartDate().compareTo(festival2.getEndDate());
                }
            });
        }
    }

    private void applyFilters() {
        List<Festival> filteredList = new ArrayList<>();
        for (Festival festival : festivals) {
            // favorites only
            if (showFavoritesOnly && !LocalStorage.isFavorite(Application.getAppContext(), festival.getId())) {
                continue;
            }

            // search for name
            if (!festival.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                continue;
            }

            // distance filter
            String homeLocation = getHomeLocation();
            if (homeLocation != null) {
                float distanceToHomeLocation = getDistanceToHomeLocation(festival, homeLocation);
                int filterDistance = filter.getDistance().getKilometres();
                if (distanceToHomeLocation > filterDistance * 1000) {
                    continue;
                }
            }

            // date filter
            if (filter.getFromDate().after(festival.getEndDate()) || filter.getToDate().before(festival.getStartDate())) {
                continue;
            }

            filteredList.add(festival);
        }

        sortFestivals(filteredList);
        recyclerView.setAdapter(new FestivalRecyclerViewAdapter(filteredList, getOnFestivalListInteractionListener(), getImageLoader()));
    }

    private float getDistanceToHomeLocation(Festival festival, String homeLocation) {
        String[] latLng = homeLocation.split("\n");
        double lat = Double.parseDouble(latLng[1]);
        double lng = Double.parseDouble(latLng[2]);

        float[] result = new float[1];
        Location.distanceBetween(lat, lng, festival.getLatitude(), festival.getLongitude(), result);
        return result[0];
    }

    private String getHomeLocation() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.pref_home_address_key), null);
    }

    @Override
    public void onFilterSet(FilterDialogFragment.Filter filter) {
        this.filter = filter;
        applyFilters();
    }
}
