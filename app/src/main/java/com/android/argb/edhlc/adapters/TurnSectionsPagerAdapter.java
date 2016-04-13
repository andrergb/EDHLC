package com.android.argb.edhlc.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.android.argb.edhlc.turn.Turn1PlaceholderFragment;
import com.android.argb.edhlc.turn.Turn2PlaceholderFragment;
import com.android.argb.edhlc.turn.Turn3PlaceholderFragment;
import com.android.argb.edhlc.turn.Turn4PlaceholderFragment;
import com.android.argb.edhlc.turn.Turn5PlaceholderFragment;


/* Created by ARGB */
public class TurnSectionsPagerAdapter extends FragmentStatePagerAdapter {

    public TurnSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return Turn1PlaceholderFragment.newInstance();
            case 1:
                return Turn2PlaceholderFragment.newInstance();
            case 2:
                return Turn3PlaceholderFragment.newInstance();
            case 3:
                return Turn4PlaceholderFragment.newInstance();
            case 4:
                return Turn5PlaceholderFragment.newInstance();
        }
        return Turn1PlaceholderFragment.newInstance();
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
    public boolean isViewFromObject(View view, Object object) {
        return object != null && ((Fragment) object).getView() == view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
