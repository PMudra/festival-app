package com.example.drachim.festivalapp.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.drachim.festivalapp.data.sqlite.AsyncFestivalLoader;
import com.example.drachim.festivalapp.data.sqlite.FestivalPlannerDbHelper;

import java.util.ArrayList;
import java.util.List;

public class FestivalListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Festival>>, SwipeRefreshLayout.OnRefreshListener {

    private OnListFragmentInteractionListener onListFragmentInteractionListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FestivalPlannerDbHelper festivalPlannerDbHelper;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onRefresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_festival_list, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.list);
        festivalPlannerDbHelper = new FestivalPlannerDbHelper(getActivity());
        recyclerView.setAdapter(new FestivalRecyclerViewAdapter(new ArrayList<Festival>(), onListFragmentInteractionListener));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.destroyDrawingCache();
        swipeRefreshLayout.clearAnimation();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            onListFragmentInteractionListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    /**
     * Necessary for devices older than API 23.
     * Alternatively one could use Fragment class from SupportLibrary.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListFragmentInteractionListener) {
            onListFragmentInteractionListener = (OnListFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onListFragmentInteractionListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (festivalPlannerDbHelper != null) {
            festivalPlannerDbHelper.close();
        }
    }

    @Override
    public Loader<List<Festival>> onCreateLoader(int id, Bundle args) {
        return new AsyncFestivalLoader(getActivity(), festivalPlannerDbHelper);
    }

    @Override
    public void onLoadFinished(Loader<List<Festival>> loader, List<Festival> data) {
        recyclerView.setAdapter(new FestivalRecyclerViewAdapter(data, onListFragmentInteractionListener));
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<List<Festival>> loader) {

    }

    @Override
    public void onRefresh() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
        getLoaderManager().initLoader(0, null, this);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Festival festival);
    }
}
