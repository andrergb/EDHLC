package com.android.argb.edhlc;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/* Created by ARGB */
@SuppressWarnings("unused")
public class FABScrollBehavior extends FloatingActionButton.Behavior {

    public FABScrollBehavior(Context context, AttributeSet attributeSet) {
        super();
    }

    public FABScrollBehavior() {
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.hide();
            child.setClickable(false);
        } else if (dyConsumed < 0 && child.getVisibility() == View.GONE) {
            child.show();
            child.setClickable(true);
        }
        if (dyUnconsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.hide();
            child.setClickable(false);
        } else if (dyUnconsumed < 0 && child.getVisibility() == View.GONE) {
            child.show();
            child.setClickable(true);
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }
}