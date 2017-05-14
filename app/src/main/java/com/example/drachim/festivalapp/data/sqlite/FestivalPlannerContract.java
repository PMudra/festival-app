package com.example.drachim.festivalapp.data.sqlite;

import android.provider.BaseColumns;

public final class FestivalPlannerContract {
    private FestivalPlannerContract() {
    }

    public static class FestivalEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
    }
}
