package com.android.argb.edhlc.turn;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.argb.edhlc.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class Turn2PlaceholderFragment extends Fragment {
    public static Turn2PlaceholderFragment newInstance() {
        Turn2PlaceholderFragment fragment = new Turn2PlaceholderFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_turn2, container, false);
        return v;
    }
}