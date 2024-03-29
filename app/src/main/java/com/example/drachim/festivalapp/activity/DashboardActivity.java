package com.example.drachim.festivalapp.activity;

import android.animation.Animator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.fragment.AbstractFestivalListFragment;
import com.example.drachim.festivalapp.fragment.DashboardFragment;
import com.example.drachim.festivalapp.fragment.DateDialogFragment;
import com.example.drachim.festivalapp.fragment.FestivalListFragment;
import com.example.drachim.festivalapp.fragment.FilterDialogFragment;
import com.example.drachim.festivalapp.fragment.SettingsFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.Date;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AbstractFestivalListFragment.OnFestivalListInteractionListener,
        DateDialogFragment.OnDateListener,
        FilterDialogFragment.OnFilterListener {

    private static final String CURRENT_FRAGMENT_KEY = "fragment_key";
    private static final String CONTENT_FRAGMENT_TAG = "content_fragment";
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private int currentFragmentId;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private void checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(result)) {
                googleApiAvailability.makeGooglePlayServicesAvailable(this).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        finish();
                    }
                });
            } else {
                Log.i("DashboardActivity", "This device is not supported.");
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPlayServices();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_dashboard);

        fragmentManager = getFragmentManager();
        if (savedInstanceState != null) {
            switchContentFragment(savedInstanceState.getInt(CURRENT_FRAGMENT_KEY, R.id.nav_dashboard));
        } else {
            switchContentFragment(R.id.nav_dashboard);
        }

        if (getIntent().getExtras() != null && getIntent().getExtras().get(FestivalActivity.EXTRA_FESTIVAL_ID) != null) {
            final String festivalId = getIntent().getExtras().getString(FestivalActivity.EXTRA_FESTIVAL_ID);
            onFestivalClicked(Integer.parseInt(festivalId));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CURRENT_FRAGMENT_KEY, currentFragmentId);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentFragmentId != R.id.nav_dashboard) {
            navigationView.setCheckedItem(R.id.nav_dashboard);
            switchContentFragment(R.id.nav_dashboard);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                // Ab Android Lollipop die CircularReveal-Animation anzeigen
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    // previously invisible view
                    View myView = findViewById(R.id.toolbar);

                    // get the center for the clipping circle
                    int cx = myView.getWidth() / 2;
                    int cy = myView.getHeight() / 2;

                    // get the final radius for the clipping circle
                    float finalRadius = (float) Math.hypot(cx, cy);

                    // create the animator for this view (the start radius is zero)
                    Animator anim;
                    anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

                    // make the view visible and start the animation
                    myView.setVisibility(View.VISIBLE);
                    anim.start();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switchContentFragment(item.getItemId());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchContentFragment(int itemId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        currentFragmentId = itemId;
        switch (itemId) {
            case R.id.nav_dashboard:
                fragment = new DashboardFragment();
                toolbar.setTitle(R.string.app_name);
                break;
            case R.id.nav_favorites:
                fragment = FestivalListFragment.newInstance(true);
                toolbar.setTitle(R.string.nav_favorites);
                break;
            case R.id.nav_discover:
                fragment = new FestivalListFragment();
                toolbar.setTitle(R.string.nav_discover);
                break;
            case R.id.nav_preferences:
                fragment = new SettingsFragment();
                toolbar.setTitle(R.string.nav_settings);
                break;
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment, CONTENT_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onFestivalClicked(final int festivalId) {
        Intent intent = new Intent(this, FestivalActivity.class);
        intent.putExtra(FestivalActivity.EXTRA_FESTIVAL_ID, festivalId);
        startActivity(intent);
    }

    @Override
    public void onMoreClicked(MoreOption moreOption) {
        switch (moreOption) {

            case Discover:
                navigationView.setCheckedItem(R.id.nav_discover);
                switchContentFragment(R.id.nav_discover);
                break;
            case Soon:
                navigationView.setCheckedItem(R.id.nav_discover);
                switchContentFragment(R.id.nav_discover);
                break;
        }
    }

    @Override
    public void onDateSet(final String tag, final Date date) {
        ((FilterDialogFragment) getFragmentManager().findFragmentByTag(FilterDialogFragment.TAG)).onDateSet(tag, date);
    }

    @Override
    public void onFilterSet(FilterDialogFragment.Filter filter) {
        ((FestivalListFragment) getFragmentManager().findFragmentByTag(CONTENT_FRAGMENT_TAG)).onFilterSet(filter);
    }
}