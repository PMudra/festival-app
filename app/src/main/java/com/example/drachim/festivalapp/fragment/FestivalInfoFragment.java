package com.example.drachim.festivalapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drachim.festivalapp.R;

/**
 * Created by Dr. Achim on 01.05.2017.
 */

public class FestivalInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_festival_info, container, false);
    }

}