package com.example.drachim.festivalapp.activity;

import android.animation.Animator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.data.DummyContent;
import com.example.drachim.festivalapp.fragment.DashboardFragment;
import com.example.drachim.festivalapp.fragment.DateDialogFragment;
import com.example.drachim.festivalapp.fragment.FestivalListFragment;
import com.example.drachim.festivalapp.fragment.FilterDialogFragment;

import java.util.Date;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FestivalListFragment.OnListFragmentInteractionListener, DateDialogFragment.OnDateListener {

    public static final String CURRENT_FRAGMENT_KEY = "fragment_key";
    private DrawerLayout drawer;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    public void openFestivalDetail(View view) {
        final View festivalCover = findViewById(R.id.festivalCover);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, festivalCover, "festivalCover");
        startActivity(new Intent(this, FestivalActivity.class), options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_dashboard);

        fragmentManager = getFragmentManager();
        if (savedInstanceState != null) {
            switchContentFragment(savedInstanceState.getInt(CURRENT_FRAGMENT_KEY, R.id.nav_dashboard));
        } else {
            switchContentFragment(R.id.nav_dashboard);
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
                    Animator anim = null;
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
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_notification:
                break;
            default:
                switchContentFragment(item.getItemId());
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private int currentFragmentId;

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
                fragment = new FestivalListFragment();
                toolbar.setTitle(R.string.nav_favorites);
                break;
            case R.id.nav_discover:
                fragment = new FestivalListFragment();
                toolbar.setTitle(R.string.nav_discover);
                break;
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        // todo
    }

    @Override
    public void onDateSet(final String tag, final Date date) {
        ((FilterDialogFragment) getFragmentManager().findFragmentByTag(FilterDialogFragment.tag)).onDateSet(tag, date);
    }
}