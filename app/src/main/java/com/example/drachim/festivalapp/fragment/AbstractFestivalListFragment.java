package com.example.drachim.festivalapp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.drachim.festivalapp.data.DatabaseUtil;
import com.example.drachim.festivalapp.data.Festival;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFestivalListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ValueEventListener {

    private OnFestivalListInteractionListener onFestivalListInteractionListener;

    protected abstract SwipeRefreshLayout getSwipeRefreshLayout();

    protected abstract void onLoadFinished(List<Festival> data);

    @Override
    public void onResume() {
        super.onResume();

        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (!getSwipeRefreshLayout().isRefreshing()) {
            getSwipeRefreshLayout().setRefreshing(true);
        }

        final FirebaseDatabase database = DatabaseUtil.getDatabase();
        DatabaseReference festivals = database.getReference("festivals");
        festivals.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        final List<Festival> festivals = new ArrayList<>();
        for (DataSnapshot festival : dataSnapshot.getChildren()) {
            festivals.add(festival.getValue(Festival.class));
        }
        onLoadFinished(festivals);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

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
        if (context instanceof OnFestivalListInteractionListener) {
            onFestivalListInteractionListener = (OnFestivalListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFestivalListInteractionListener");
        }
    }

    /**
     * Necessary for devices older than API 23.
     * Alternatively one could use Fragment class from SupportLibrary.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnFestivalListInteractionListener) {
            onFestivalListInteractionListener = (OnFestivalListInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString() + " must implement OnFestivalListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFestivalListInteractionListener = null;
    }

    OnFestivalListInteractionListener getOnFestivalListInteractionListener() {
        return onFestivalListInteractionListener;
    }

    public interface OnFestivalListInteractionListener {
        void onFestivalClicked(final int festivalId);

        void onMoreClicked(final MoreOption moreOption);

        enum MoreOption {
            Discover,
            Soon
        }
    }
}
