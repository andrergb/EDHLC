package com.android.argb.edhlc;


import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

/* Created by ARGB */
public class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

    private Runnable runnable;
    private Activity act;

    public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        act = activity;
    }

    @Override
    public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        ActivityCompat.invalidateOptionsMenu(act);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        ActivityCompat.invalidateOptionsMenu(act);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        super.onDrawerStateChanged(newState);
        if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
            runnable.run();
            runnable = null;
        }
    }

    public void runWhenIdle(Runnable runnable) {
        this.runnable = runnable;
    }
}