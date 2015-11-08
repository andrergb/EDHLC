package com.android.argb.edhlc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.android.argb.edhlc.activities.MainActivity;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private int numPages = 4;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object != null && ((Fragment) object).getView() == view;
    }

    @Override
    public int getCount() {
        return numPages;
    }

    public void setCount(int numPages) {
        this.numPages = numPages;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                if (MainActivity.mActivePlayer1 != null)
                    return MainActivity.mActivePlayer1.getPlayerName();
                return "";
            case 1:
                if (MainActivity.mActivePlayer2 != null)
                    return MainActivity.mActivePlayer2.getPlayerName();
                return "";
            case 2:
                if (MainActivity.mActivePlayer3 != null)
                    return MainActivity.mActivePlayer3.getPlayerName();
                return "";
            case 3:
                if (MainActivity.mActivePlayer4 != null)
                    return MainActivity.mActivePlayer4.getPlayerName();
                return "";
        }
        return null;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
