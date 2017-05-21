package com.example.drachim.festivalapp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.drachim.festivalapp.data.Festival;
import com.example.drachim.festivalapp.data.sqlite.AsyncFestivalLoader;
import com.example.drachim.festivalapp.data.sqlite.FestivalPlannerDbHelper;

import java.util.List;

public abstract class AbstractFestivalListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Festival>>, SwipeRefreshLayout.OnRefreshListener {

    private FestivalPlannerDbHelper festivalPlannerDbHelper;
    private OnListFragmentInteractionListener onListFragmentInteractionListener;

    protected abstract SwipeRefreshLayout getSwipeRefreshLayout();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        festivalPlannerDbHelper = new FestivalPlannerDbHelper(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (!getSwipeRefreshLayout().isRefreshing()) {
            getSwipeRefreshLayout().setRefreshing(true);
        }
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();

        getSwipeRefreshLayout().setRefreshing(false);
        getSwipeRefreshLayout().destroyDrawingCache();
        getSwipeRefreshLayout().clearAnimation();
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
    public void onLoaderReset(Loader<List<Festival>> loader) {

    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Festival festival);
    }

    protected OnListFragmentInteractionListener getOnListFragmentInteractionListener() {
        return onListFragmentInteractionListener;
    }
}
