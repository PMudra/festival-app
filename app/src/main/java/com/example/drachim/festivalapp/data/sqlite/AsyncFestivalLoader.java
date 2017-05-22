package com.example.drachim.festivalapp.data.sqlite;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.drachim.festivalapp.data.Festival;

import java.util.List;

public class AsyncFestivalLoader extends AsyncTaskLoader<List<Festival>> {

    private final FestivalPlannerDbHelper festivalPlannerDbHelper;

    public AsyncFestivalLoader(final Context context, final FestivalPlannerDbHelper festivalPlannerDbHelper) {
        super(context);
        this.festivalPlannerDbHelper = festivalPlannerDbHelper;
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    public List<Festival> loadInBackground() {
        return festivalPlannerDbHelper.ReadFestivals();
    }
}
