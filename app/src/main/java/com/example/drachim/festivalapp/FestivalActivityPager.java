package com.example.drachim.festivalapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.example.drachim.festivalapp.fragment.FestivalInfoFragment;
import com.example.drachim.festivalapp.fragment.FestivalLineupFragment;
import com.example.drachim.festivalapp.fragment.FestivalPlanningFragment;

public class FestivalActivityPager extends FragmentPagerAdapter {

    private final Context context;

    public FestivalActivityPager(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FestivalInfoFragment();
            case 1:
                return new FestivalLineupFragment();
            case 2:
                return new FestivalPlanningFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return context.getString(R.string.festival_tab_infos);
            case 1:
                return context.getString(R.string.festival_tab_lineup);
            case 2:
                return context.getString(R.string.festival_tab_planning);
            default:
                return null;
        }
    }

}
