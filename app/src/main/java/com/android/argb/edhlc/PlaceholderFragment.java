package com.android.argb.edhlc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_TAG = "section_tag";

    private Button buttonLifePositive;
    private Button buttonLifeNegative;

    private Button buttonEDH1Positive;
    private Button buttonEDH1Negative;

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt(ARG_SECTION_TAG, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        buttonLifePositive = (Button) v.findViewById(R.id.buttonLifePositive);
        buttonLifeNegative = (Button) v.findViewById(R.id.buttonLifeNegative);

        buttonEDH1Positive = (Button) v.findViewById(R.id.buttonEDH1Positive);
        buttonEDH1Negative = (Button) v.findViewById(R.id.buttonEDH1Negative);


        buttonLifePositive.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int size = buttonLifePositive.getWidth() < buttonLifePositive.getHeight() ? buttonLifePositive.getWidth() : buttonLifePositive.getHeight();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(size, size));
                layoutParams.setMargins(5, 5, 5, 20);
                buttonLifePositive.setLayoutParams(layoutParams);
            }
        });

        buttonLifeNegative.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int size = buttonLifeNegative.getWidth() < buttonLifeNegative.getHeight() ? buttonLifeNegative.getWidth() : buttonLifeNegative.getHeight();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(size, size));
                layoutParams.setMargins(5, 20, 5, 5);
                buttonLifeNegative.setLayoutParams(layoutParams);
            }
        });

        return v;
    }


}