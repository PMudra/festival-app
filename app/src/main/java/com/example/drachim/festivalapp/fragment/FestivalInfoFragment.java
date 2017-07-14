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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FestivalInfoFragment extends Fragment implements
        OnMapReadyCallback {

    private GoogleMap googleMap;
    private Festival festival;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_festival_info, container, false);

        festival = ((FestivalActivity) getActivity()).getFestival();

        ((TextView) view.findViewById(R.id.info_name_title)).setText(festival.getName());
        ((TextView) view.findViewById(R.id.info_date)).setText(FestivalHelper.getDateRange(festival, getActivity()));
        ((TextView) view.findViewById(R.id.info_description)).setText(festival.getDescription());

        String distance = FestivalHelper.getFriendlyDistanceToHomeLocation(festival);
        if (!distance.isEmpty()) {
            view.findViewById(R.id.info_distance_layout).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.info_distance)).setText(distance);
        }

        ((TextView) view.findViewById(R.id.info_place)).setText(FestivalHelper.getPlace(festival));

        MapView mapView = (MapView) view.findViewById(R.id.info_direction_map_view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        FestivalActivity activity = (FestivalActivity) getActivity();
        if (activity.hasFestivalAccentColor()) {
            ((TextView) view.findViewById(R.id.info_name_title)).setTextColor(activity.getFestivalAccentColor());
            ((TextView) view.findViewById(R.id.info_direction_title)).setTextColor(activity.getFestivalAccentColor());
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);

        LatLng latLng = new LatLng(festival.getLatitude(), festival.getLongitude());
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        this.googleMap.addMarker(new MarkerOptions().position(latLng).title(festival.getName()));
    }
}