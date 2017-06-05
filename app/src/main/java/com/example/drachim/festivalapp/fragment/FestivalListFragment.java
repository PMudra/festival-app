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

import java.util.ArrayList;
import java.util.List;

public class FestivalListFragment extends AbstractFestivalListFragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_festival_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                DialogFragment filterDialogFragment = new FilterDialogFragment();
                filterDialogFragment.show(getFragmentManager(), FilterDialogFragment.tag);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_festival_list, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.list);
        recyclerView.setAdapter(new FestivalRecyclerViewAdapter(new ArrayList<Festival>(), getOnFestivalListInteractionListener(), getImageLoader()));

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
        recyclerView.setAdapter(new FestivalRecyclerViewAdapter(data, getOnFestivalListInteractionListener(), getImageLoader()));
        swipeRefreshLayout.setRefreshing(false);
    }

}
