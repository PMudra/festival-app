package com.example.drachim.festivalapp.fragment;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.view.Menu;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.common.Application;
import com.example.drachim.festivalapp.common.Utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class SettingsFragment extends PreferenceFragment implements FragmentCompat.OnRequestPermissionsResultCallback, SharedPreferences.OnSharedPreferenceChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST_CODE = 1;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10 * 1000;      /* 10 secs */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 2000;   /* 2 secs */

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location currentLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        initSummary(getPreferenceScreen());
        setHasOptionsMenu(true);

        Preference determineLocationButton = findPreference("pref_determine_location");
        determineLocationButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (Utilities.checkAndRequestAccessFineLocationPermission(SettingsFragment.this, ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST_CODE)) {
                    initLocationService();
                }

                return true;
            }
        });
    }

    private void initSummary(Preference preference) {
        if (preference instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) preference;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePreferenceSummary(preference);
        }
    }

    private void updatePreferenceSummary(Preference preference) {
        if (preference instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) preference;
            preference.setSummary(editTextPref.getText());
        }
    }

    private void initLocationService() {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(Application.getAppContext(), this, this).addApi(LocationServices.API).build();
        }

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private void requestLocation() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        if (Utilities.checkAccessFineLocationPermission(getActivity())) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocationService();
                    break;
                } else {
                    break;
                }
            default:
                break;
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreferenceSummary(findPreference(key));
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        updateHomeAddress();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    private void updateHomeAddress() {
        EditTextPreference prefHomeAddress = (EditTextPreference) findPreference("pref_home_address");
        prefHomeAddress.setText("Lat " + currentLocation.getLatitude() + ", Long " + currentLocation.getLongitude());
    }

}