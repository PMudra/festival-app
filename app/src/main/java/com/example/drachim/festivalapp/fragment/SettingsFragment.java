package com.example.drachim.festivalapp.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Menu;

import com.example.drachim.festivalapp.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}