package com.android.argb.edhlc;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/* Created by ARGB */
public abstract class GestureTouchListener implements OnTouchListener {

    private final GestureDetector mGestureDetector;

    public GestureTouchListener(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureListener(this));
    }

    public void onClick() {
    }

    public void onDoubleTap() {
    }

    public void onSwipeBottom() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeRight() {
    }

    public void onSwipeTop() {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private static final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        private GestureTouchListener mHelper;

        public GestureListener(GestureTouchListener helper) {
            mHelper = helper;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mHelper.onDoubleTap();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            mHelper.onSwipeRight();
                        } else {
                            mHelper.onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            mHelper.onSwipeBottom();
                        } else {
                            mHelper.onSwipeTop();
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mHelper.onClick();
            return true;
        }
    }

}