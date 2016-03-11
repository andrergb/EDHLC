package com.android.argb.edhlc.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.objects.ActivePlayerNew;
import com.android.argb.edhlc.objects.Deck;

public class MainFragment extends Fragment implements View.OnClickListener {

    //Args
    private static final String ARG_PLAYER_NAME = "ARG_PLAYER_NAME";
    private static final String ARG_DECK_NAME = "ARG_DECK_NAME";
    private static final String ARG_DECK_COLOR = "ARG_DECK_COLOR";
    private static final String ARG_IS_ALIVE = "ARG_IS_ALIVE";
    private static final String ARG_LIFE = "ARG_LIFE";
    private static final String ARG_EDH1 = "ARG_EDH1";
    private static final String ARG_EDH2 = "ARG_EDH2";
    private static final String ARG_EDH3 = "ARG_EDH3";
    private static final String ARG_EDH4 = "ARG_EDH4";
    private static final String ARG_TAG = "ARG_TAG";

    //Layout
    private TextView messageTextView;
    private Button buttonPlus;

    //Interface
    private OnUpdateData onUpdateDataInterface;

    //Layout
    private TextView headerPlayerName;
    private TextView headerDeckName;

    private TextView lifeValue;
    private ImageView lifePositive;
    private ImageView lifeNegative;

    private LinearLayout mainEDH1;
    private TextView valueEDH1;
    private ImageView positiveEDH1;
    private ImageView negativeEDH1;

    private LinearLayout mainEDH2;
    private TextView valueEDH2;
    private ImageView positiveEDH2;
    private ImageView negativeEDH2;

    private LinearLayout mainEDH3;
    private TextView valueEDH3;
    private ImageView positiveEDH3;
    private ImageView negativeEDH3;

    private LinearLayout mainEDH4;
    private TextView valueEDH4;
    private ImageView positiveEDH4;
    private ImageView negativeEDH4;

    public static MainFragment newInstance(ActivePlayerNew activePlayer) {
        Bundle args = new Bundle();
        if (activePlayer != null) {
            args.putString(ARG_PLAYER_NAME, activePlayer.getPlayerDeck().getDeckOwnerName());
            args.putString(ARG_DECK_NAME, activePlayer.getPlayerDeck().getDeckName());
            args.putInt(ARG_DECK_COLOR, activePlayer.getPlayerDeck().getDeckShieldColor()[0]);
            args.putBoolean(ARG_IS_ALIVE, activePlayer.getPlayerIsAlive());
            args.putInt(ARG_LIFE, activePlayer.getPlayerLife());
            args.putInt(ARG_EDH1, activePlayer.getPlayerEDH1());
            args.putInt(ARG_EDH2, activePlayer.getPlayerEDH2());
            args.putInt(ARG_EDH3, activePlayer.getPlayerEDH3());
            args.putInt(ARG_EDH4, activePlayer.getPlayerEDH4());
            args.putInt(ARG_TAG, activePlayer.getPlayerTag());
        }

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private static ActivePlayerNew argsToPlayer(Bundle args) {
        ActivePlayerNew auxPlayer = new ActivePlayerNew();
        if (args != null) {
            auxPlayer.setPlayerDeck(new Deck(args.getString(ARG_PLAYER_NAME), args.getString(ARG_DECK_NAME), new int[]{args.getInt(ARG_DECK_COLOR)}));
            auxPlayer.setPlayerIsAlive(args.getBoolean(ARG_IS_ALIVE));
            auxPlayer.setPlayerLife(args.getInt(ARG_LIFE));
            auxPlayer.setPlayerEDH1(args.getInt(ARG_EDH1));
            auxPlayer.setPlayerEDH2(args.getInt(ARG_EDH2));
            auxPlayer.setPlayerEDH3(args.getInt(ARG_EDH3));
            auxPlayer.setPlayerEDH4(args.getInt(ARG_EDH4));
            auxPlayer.setPlayerTag(args.getInt(ARG_TAG));
        }
        return auxPlayer;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUpdateData) {
            onUpdateDataInterface = (OnUpdateData) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnUpdateData) {
            onUpdateDataInterface = (OnUpdateData) activity;
        } else {
            throw new RuntimeException(activity.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lifePositive:
                getArguments().putInt(ARG_LIFE, (getArguments().getInt(ARG_LIFE) + 1));
                setLife(getArguments().getInt(ARG_LIFE));
                break;

            case R.id.lifeNegative:
                getArguments().putInt(ARG_LIFE, (getArguments().getInt(ARG_LIFE) - 1));
                setLife(getArguments().getInt(ARG_LIFE));
                break;

            case R.id.positiveEDH1:
                if (getArguments().getInt(ARG_EDH1) < 21) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) + 1);
                    getArguments().putInt(ARG_EDH1, getArguments().getInt(ARG_EDH1) + 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(1, getArguments().getInt(ARG_EDH1));
                }
                break;

            case R.id.negativeEDH1:
                if (getArguments().getInt(ARG_EDH1) > 0) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) - 1);
                    getArguments().putInt(ARG_EDH1, getArguments().getInt(ARG_EDH1) - 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(1, getArguments().getInt(ARG_EDH1));
                }
                break;

            case R.id.positiveEDH2:
                if (getArguments().getInt(ARG_EDH2) < 21) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) + 1);
                    getArguments().putInt(ARG_EDH2, getArguments().getInt(ARG_EDH2) + 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(2, getArguments().getInt(ARG_EDH2));
                }
                break;

            case R.id.negativeEDH2:
                if (getArguments().getInt(ARG_EDH2) > 0) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) - 1);
                    getArguments().putInt(ARG_EDH2, getArguments().getInt(ARG_EDH2) - 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(2, getArguments().getInt(ARG_EDH2));
                }
                break;

            case R.id.positiveEDH3:
                if (getArguments().getInt(ARG_EDH3) < 21) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) + 1);
                    getArguments().putInt(ARG_EDH3, getArguments().getInt(ARG_EDH3) + 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(3, getArguments().getInt(ARG_EDH3));
                }
                break;

            case R.id.negativeEDH3:
                if (getArguments().getInt(ARG_EDH3) > 0) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) - 1);
                    getArguments().putInt(ARG_EDH3, getArguments().getInt(ARG_EDH3) - 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(3, getArguments().getInt(ARG_EDH3));
                }
                break;

            case R.id.positiveEDH4:
                if (getArguments().getInt(ARG_EDH4) < 21) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) + 1);
                    getArguments().putInt(ARG_EDH4, getArguments().getInt(ARG_EDH4) + 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(4, getArguments().getInt(ARG_EDH4));
                }
                break;

            case R.id.negativeEDH4:
                if (getArguments().getInt(ARG_EDH4) > 0) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) - 1);
                    getArguments().putInt(ARG_EDH4, getArguments().getInt(ARG_EDH4) - 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(4, getArguments().getInt(ARG_EDH4));
                }
                break;
        }
        onUpdateDataInterface.iUpdateActivePlayer(argsToPlayer(getArguments()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_new, container, false);

        createLayout(view);

        updateLayout(view);

        return view;
    }

    private void createLayout(View view) {
        headerPlayerName = (TextView) view.findViewById(R.id.headerPlayerName);
        headerDeckName = (TextView) view.findViewById(R.id.headerDeckName);

        lifeValue = (TextView) view.findViewById(R.id.lifeValue);
        lifePositive = (ImageView) view.findViewById(R.id.lifePositive);
        lifeNegative = (ImageView) view.findViewById(R.id.lifeNegative);

        mainEDH1 = (LinearLayout) view.findViewById(R.id.mainEDH1);
        mainEDH2 = (LinearLayout) view.findViewById(R.id.mainEDH2);
        mainEDH3 = (LinearLayout) view.findViewById(R.id.mainEDH3);
        mainEDH4 = (LinearLayout) view.findViewById(R.id.mainEDH4);

        valueEDH1 = (TextView) view.findViewById(R.id.valueEDH1);
        valueEDH2 = (TextView) view.findViewById(R.id.valueEDH2);
        valueEDH3 = (TextView) view.findViewById(R.id.valueEDH3);
        valueEDH4 = (TextView) view.findViewById(R.id.valueEDH4);

        positiveEDH1 = (ImageView) view.findViewById(R.id.positiveEDH1);
        positiveEDH2 = (ImageView) view.findViewById(R.id.positiveEDH2);
        positiveEDH3 = (ImageView) view.findViewById(R.id.positiveEDH3);
        positiveEDH4 = (ImageView) view.findViewById(R.id.positiveEDH4);

        negativeEDH1 = (ImageView) view.findViewById(R.id.negativeEDH1);
        negativeEDH2 = (ImageView) view.findViewById(R.id.negativeEDH2);
        negativeEDH3 = (ImageView) view.findViewById(R.id.negativeEDH3);
        negativeEDH4 = (ImageView) view.findViewById(R.id.negativeEDH4);

        lifePositive.setOnClickListener(this);
        lifeNegative.setOnClickListener(this);
        positiveEDH1.setOnClickListener(this);
        negativeEDH1.setOnClickListener(this);
        positiveEDH2.setOnClickListener(this);
        negativeEDH2.setOnClickListener(this);
        positiveEDH3.setOnClickListener(this);
        negativeEDH3.setOnClickListener(this);
        positiveEDH4.setOnClickListener(this);
        negativeEDH4.setOnClickListener(this);
    }

    private void setEDH(int which, int edhValue) {
        switch (which) {
            case 1:
                valueEDH1.setText("" + edhValue);
                break;

            case 2:
                valueEDH2.setText("" + edhValue);
                break;

            case 3:
                valueEDH3.setText("" + edhValue);
                break;

            case 4:
                valueEDH4.setText("" + edhValue);
                break;
        }
    }

    private void setLife(int life) {
        lifeValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, (life > 99 || life < -99) ? 120 : 180);
        lifeValue.setText("" + life);
    }

    private void updateLayout(View view) {
        boolean isAlive = getArguments().getBoolean(ARG_IS_ALIVE);
        int color = getArguments().getInt(ARG_DECK_COLOR);

        lifePositive.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        lifeNegative.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        positiveEDH1.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        negativeEDH1.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        positiveEDH2.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        negativeEDH2.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        positiveEDH3.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        negativeEDH3.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        positiveEDH4.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        negativeEDH4.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
    }

    public interface OnUpdateData {
        void iUpdateActivePlayer(ActivePlayerNew activePlayer);
    }
}