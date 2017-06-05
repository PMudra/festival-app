package com.example.drachim.festivalapp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.drachim.festivalapp.common.VolleySingleton;
import com.example.drachim.festivalapp.data.Festival;
import com.example.drachim.festivalapp.data.FestivalRequest;

import java.util.List;

public abstract class AbstractFestivalListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFestivalListInteractionListener onFestivalListInteractionListener;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    ImageLoader getImageLoader() {
        return imageLoader;
    }

    protected abstract SwipeRefreshLayout getSwipeRefreshLayout();

    protected abstract void onLoadFinished(List<Festival> data);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        imageLoader = VolleySingleton.getInstance().getImageLoader();
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

        FestivalRequest festivalRequest = new FestivalRequest("http://amgbr.us.to:3000/festivals", null, new Response.Listener<List<Festival>>() {
            @Override
            public void onResponse(List<Festival> response) {
                onLoadFinished(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(festivalRequest);
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
        void onFestivalClicked(Festival festival);

        void onMoreClicked(MoreOption moreOption);

        enum MoreOption {
            Discover,
            Soon
        }
    }
}
