package com.example.drachim.festivalapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.drachim.festivalapp.FestivalActivityPager;
import com.example.drachim.festivalapp.R;
import com.example.drachim.festivalapp.common.Utilities;
import com.example.drachim.festivalapp.data.Participant;
import com.example.drachim.festivalapp.fragment.FestivalPlanningFragment;

public class FestivalActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, FestivalPlanningFragment.OnListFragmentInteractionListener {

    private ViewPager viewPager;
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FestivalActivityPager adapter = new FestivalActivityPager(getFragmentManager(), this);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2 = (FloatingActionButton) findViewById(R.id.fab1);

        showFab(viewPager.getCurrentItem());
    }

    @Override
    public void onBackPressed() {
        if (fab1.isShown() || fab2.isShown()) {
            hideFabsMini();
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_festival, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_toggle_favorite:

                return true;

            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        showFab(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onListFragmentInteraction(Participant item) {
        // TODO:
    }

    private void hideFabs() {
        fab.hide();
        hideFabsMini();
    }

    private void hideFabsMini() {
        Utilities.animRotateBackward(fab);
        fab1.hide();
        fab2.hide();
    }

    public void showFab(int tabPosition) {
        switch (tabPosition) {
            case 2:
                fab.setRotation(0);
                fab.show();
                break;
            default:
                if (fab.isShown()) {
                    hideFabs();
                }
                break;
        }

    }
}