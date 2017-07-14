package com.example.drachim.festivalapp.data;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.common.Application;

public final class FestivalHelper {
    private FestivalHelper() {
    }

    public static String getDateRange(Festival festival, Context context) {
        return DateUtils.formatDateRange(context, festival.getStartDate().getTime(), festival.getEndDate().getTime(), DateUtils.FORMAT_SHOW_DATE);
    }

    public static String getPlace(Festival festival) {
        return festival.getPlace();
    }

    public static float getDistanceToHomeLocation(Festival festival) {
        String homeLocation = getHomeLocation();
        String[] latLng = homeLocation.split("\n");
        double lat = Double.parseDouble(latLng[1]);
        double lng = Double.parseDouble(latLng[2]);

        float[] result = new float[1];
        Location.distanceBetween(lat, lng, festival.getLatitude(), festival.getLongitude(), result);
        return result[0];
    }

    public static String getFriendlyDistanceToHomeLocation(Festival festival) {
        int distanceToHomeLocation = Math.round(FestivalHelper.getDistanceToHomeLocation(festival) / 1000);
        return distanceToHomeLocation + " km";
    }

    public static boolean isHomeLocationSet() {
        return getHomeLocation() != null;
    }

    private static String getHomeLocation() {
        return PreferenceManager.getDefaultSharedPreferences(Application.getAppContext()).getString(Application.getAppContext().getString(R.string.pref_home_address_key), null);
    }
}
