package com.example.drachim.festivalapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class LocalStorage {

    private static final String FAVORITES = "favorites_";
    private static final String PARTICIPANTS = "participants_";
    private static final String PREFERENCES_NAME = "com.example.drachim.festivalapp.data";
    private static final String PARTICIPANT_SEPERATOR = ";;;";
    private static final String VALUE_SEPERATOR = ":::";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isFavorite(final Context context, final int festivalId) {
        final SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(FAVORITES + festivalId, false);
    }

    public static void setFavorite(final Context context, final int festivalId, final boolean favorite) {
        final SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FAVORITES + festivalId, favorite);
        editor.apply();
    }

    public static ArrayList<Participant> getParticipants(final Context context, final int festivalId) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        String storage = sharedPreferences.getString(PARTICIPANTS + festivalId, PARTICIPANT_SEPERATOR);
        storage = storage.equals("") ? PARTICIPANT_SEPERATOR : storage;

        ArrayList<Participant> participants = new ArrayList<>();
        for (String participant : storage.split(PARTICIPANT_SEPERATOR)) {
            String[] split = participant.split(VALUE_SEPERATOR);
            participants.add(new Participant(split[0], split[1], Boolean.parseBoolean(split[2])));
        }
        return participants;
    }

    public static void saveParticipants(final Context context, final int festivalId, final List<Participant> participants) {

        String storage = "";
        for (Participant participant : participants) {
            storage += participant.getName() + VALUE_SEPERATOR;
            storage += participant.getPhoto() + VALUE_SEPERATOR;
            storage += participant.isInterested() + PARTICIPANT_SEPERATOR;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PARTICIPANTS + festivalId, storage);
        editor.apply();
    }
}
