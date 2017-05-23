package com.example.drachim.festivalapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.activity.FestivalActivity;
import com.example.drachim.festivalapp.data.Festival;
import com.example.drachim.festivalapp.data.FestivalHelper;

public class FestivalInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_festival_info, container, false);

        Festival festival = (Festival) getActivity().getIntent().getExtras().get(FestivalActivity.EXTRA_FESTIVAL);

        ((TextView) view.findViewById(R.id.info_name_title)).setText(festival.getName());
        ((TextView) view.findViewById(R.id.info_date)).setText(FestivalHelper.getDateRange(festival, getActivity()));
        ((TextView) view.findViewById(R.id.info_place)).setText(FestivalHelper.getPlace(festival));
        ((TextView) view.findViewById(R.id.info_description)).setText(festival.getDescription());

        FestivalActivity activity = (FestivalActivity) getActivity();
        if (activity.hasFestivalAccentColor()) {
            ((TextView) view.findViewById(R.id.info_name_title)).setTextColor(activity.getFestivalAccentColor());
            ((TextView) view.findViewById(R.id.info_description_title)).setTextColor(activity.getFestivalAccentColor());
            ((TextView) view.findViewById(R.id.info_tickets_title)).setTextColor(activity.getFestivalAccentColor());
        }

        return view;
    }

}