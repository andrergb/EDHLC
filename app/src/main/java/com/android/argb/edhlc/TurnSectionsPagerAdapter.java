package com.android.argb.edhlc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TurnSectionsPagerAdapter extends FragmentStatePagerAdapter {

    public TurnSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return Turn1PlaceholderFragment.newInstance();
            case 1: return Turn2PlaceholderFragment.newInstance();
            case 2: return Turn3PlaceholderFragment.newInstance();
            case 3: return Turn4PlaceholderFragment.newInstance();
            case 4: return Turn5PlaceholderFragment.newInstance();
        }
        return Turn1PlaceholderFragment.newInstance();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object != null && ((Fragment) object).getView() == view;
    }

    @Override
    public int getCount() {
        int numPages = 5;
        return numPages;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
