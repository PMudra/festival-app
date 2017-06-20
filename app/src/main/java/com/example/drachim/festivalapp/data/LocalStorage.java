package com.example.drachim.festivalapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Set;

public class LocalStorage {

    private static final String FAVORITES = "favorites";
    private static final String PREFERENCES_NAME = "com.example.drachim.festivalapp.data";

    public static boolean isFavorite(final Context context, final int festivalId) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        Set<String> stringSet = sharedPreferences.getStringSet(FAVORITES, Collections.<String>emptySet());
        return stringSet.contains(String.valueOf(festivalId));
    }

    public static void setFavorite(final Context context, final int festivalId, final boolean favorite) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        final Set<String> favorites = sharedPreferences.getStringSet(FAVORITES, Collections.<String>emptySet());

        if (favorite) {
            favorites.add(String.valueOf(festivalId));
        } else {
            favorites.remove(String.valueOf(festivalId));
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Clearing enables the storage on the device for some reason.
        editor.clear();
        editor.putStringSet(FAVORITES, favorites);
        editor.apply();
    }
}
