package com.android.argb.edhlc.activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/* Created by ARGB */
//http://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
public class MainPagerAdapter extends FragmentPagerAdapter {
    private List<String> tabTitles;
    private Context context;
    private List<MainFragment> fragments;

    public MainPagerAdapter(FragmentManager fm, Context context, List<MainFragment> fragments, List<String> tabTitles) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.tabTitles = tabTitles;
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
        return tabTitles.get(position);
    }
}