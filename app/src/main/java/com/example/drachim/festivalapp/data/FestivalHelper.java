package com.example.drachim.festivalapp.data;

import android.content.Context;
import android.text.format.DateUtils;

public final class FestivalHelper {
    private FestivalHelper() {
    }

    public static String getDateRange(Festival festival, Context context) {
        return DateUtils.formatDateRange(context, festival.getStartDate().getTime(), festival.getEndDate().getTime(), DateUtils.FORMAT_SHOW_DATE);
    }

    public static String getPlace(Festival festival) {
        // todo
        return festival.getPlace();
    }
}
