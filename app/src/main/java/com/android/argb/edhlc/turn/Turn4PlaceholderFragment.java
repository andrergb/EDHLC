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
public class Turn4PlaceholderFragment extends Fragment {
    public static Turn4PlaceholderFragment newInstance() {
        Turn4PlaceholderFragment fragment = new Turn4PlaceholderFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_turn4, container, false);
        return v;
    }
}