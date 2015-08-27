package com.android.argb.edhlc;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

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
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                if (MainActivity.mPlayer1 != null)
                    return MainActivity.mPlayer1.getPlayerName();
                return "";
            case 1:
                if (MainActivity.mPlayer2 != null)
                    return MainActivity.mPlayer2.getPlayerName();
                return "";
            case 2:
                if (MainActivity.mPlayer3 != null)
                    return MainActivity.mPlayer3.getPlayerName();
                return "";
            case 3:
                if (MainActivity.mPlayer4 != null)
                    return MainActivity.mPlayer4.getPlayerName();
                return "";
        }
        return null;
    }
}
