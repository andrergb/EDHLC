package com.android.argb.edhlc.activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

//http://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
public class MainPagerAdapter extends FragmentPagerAdapter {
    private String tabTitles[] = new String[]{"Tab1", "Tab2", "Tab3", "Tab4"};
    private Context context;
    private List<MainFragment> fragments;

    public MainPagerAdapter(FragmentManager fm, Context context, List<MainFragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}