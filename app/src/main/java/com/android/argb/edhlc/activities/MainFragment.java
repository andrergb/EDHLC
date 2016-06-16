package com.android.argb.edhlc.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.GestureTouchListener;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.objects.ActivePlayer;
import com.android.argb.edhlc.objects.Deck;

import java.util.Locale;

/* Created by ARGB */
public class MainFragment extends Fragment implements View.OnClickListener {

    //Args
    private static final String ARG_PLAYER_NAME = "ARG_PLAYER_NAME";
    private static final String ARG_DECK_NAME = "ARG_DECK_NAME";
    private static final String ARG_DECK_COLOR = "ARG_DECK_COLOR";
    //    private static final String ARG_IS_ALIVE = "ARG_IS_ALIVE";
    private static final String ARG_LIFE = "ARG_LIFE";
    private static final String ARG_EDH1 = "ARG_EDH1";
    private static final String ARG_EDH2 = "ARG_EDH2";
    private static final String ARG_EDH3 = "ARG_EDH3";
    private static final String ARG_EDH4 = "ARG_EDH4";
    private static final String ARG_TAG = "ARG_TAG";
    private static final String ARG_TOTAL_PLAYERS = "ARG_TOTAL_PLAYERS";

    //Interface
    private OnUpdateData onUpdateDataInterface;
    private ImageView imageViewThrone;

    //Layout
    private TextView headerPlayerName;
    private TextView headerDeckName;

    private TextView lifeValue;
    private ImageView lifePositive;
    private ImageView lifeNegative;

    private TextView valueEDH1;
    private ImageView positiveEDH1;
    private ImageView negativeEDH1;

    private TextView valueEDH2;
    private ImageView positiveEDH2;
    private ImageView negativeEDH2;

    private TextView valueEDH3;
    private ImageView positiveEDH3;
    private ImageView negativeEDH3;

    private TextView valueEDH4;
    private ImageView positiveEDH4;
    private ImageView negativeEDH4;

    public static MainFragment newInstance(ActivePlayer activePlayer, int totalPlayers) {
        Bundle args = new Bundle();
        if (activePlayer != null) {
            args.putString(ARG_PLAYER_NAME, activePlayer.getPlayerDeck().getDeckOwnerName());
            args.putString(ARG_DECK_NAME, activePlayer.getPlayerDeck().getDeckName());
            args.putInt(ARG_DECK_COLOR, activePlayer.getPlayerDeck().getDeckShieldColor()[0]);
//            args.putBoolean(ARG_IS_ALIVE, activePlayer.getPlayerIsAlive());
            args.putInt(ARG_LIFE, activePlayer.getPlayerLife());
            args.putInt(ARG_EDH1, activePlayer.getPlayerEDH1());
            args.putInt(ARG_EDH2, activePlayer.getPlayerEDH2());
            args.putInt(ARG_EDH3, activePlayer.getPlayerEDH3());
            args.putInt(ARG_EDH4, activePlayer.getPlayerEDH4());
            args.putInt(ARG_TAG, activePlayer.getPlayerTag());

            args.putInt(ARG_TOTAL_PLAYERS, totalPlayers);
        }

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private static ActivePlayer argsToPlayer(Bundle args) {
        ActivePlayer auxPlayer = new ActivePlayer();
        if (args != null) {
            auxPlayer.setPlayerDeck(new Deck(args.getString(ARG_PLAYER_NAME), args.getString(ARG_DECK_NAME), new int[]{args.getInt(ARG_DECK_COLOR)}));
//            auxPlayer.setPlayerIsAlive(args.getBoolean(ARG_IS_ALIVE));
            auxPlayer.setPlayerLife(args.getInt(ARG_LIFE));
            auxPlayer.setPlayerEDH1(args.getInt(ARG_EDH1));
            auxPlayer.setPlayerEDH2(args.getInt(ARG_EDH2));
            auxPlayer.setPlayerEDH3(args.getInt(ARG_EDH3));
            auxPlayer.setPlayerEDH4(args.getInt(ARG_EDH4));
            auxPlayer.setPlayerTag(args.getInt(ARG_TAG));
        }
        return auxPlayer;
    }

    public void createLifeDialog(final View view) {
        @SuppressLint("InflateParams")
        View playerLifeView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_player_life, null);
        final EditText userInput = (EditText) playerLifeView.findViewById(R.id.editTextPlayerLife);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(playerLifeView);
        alertDialogBuilder.setTitle(getArguments().getString(ARG_PLAYER_NAME) + ": " + getArguments().getInt(ARG_LIFE));

        alertDialogBuilder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String stringLife = userInput.getText().toString();
                        try {
                            if (!stringLife.equalsIgnoreCase("")) {
                                int intLife = Integer.valueOf(stringLife);
                                if (intLife > Constants.MAX_LIFE)
                                    intLife = Constants.MAX_LIFE;
                                else if (intLife < Constants.MIN_LIFE)
                                    intLife = Constants.MIN_LIFE;
                                getArguments().putInt(ARG_LIFE, intLife);
                                setLife(getArguments().getInt(ARG_LIFE));

                                onUpdateDataInterface.iUpdateActivePlayer(argsToPlayer(getArguments()));
                                onUpdateDataInterface.iUpdateDethrone();
                                onUpdateDataInterface.iUpdateHistory();
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
        );
        alertDialogBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }
        );
//        alertDialogBuilder.setNeutralButton("Lose Game",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        getArguments().putBoolean(ARG_IS_ALIVE, false);
//
//                        onUpdateDataInterface.iUpdateActivePlayer(argsToPlayer(getArguments()));
//                        onUpdateDataInterface.iUpdateDethrone();
//                        onUpdateDataInterface.iUpdateHistory();
//                    }
//                }
//        );
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        alertDialog.show();
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lifeValue:
                createLifeDialog(v);
                break;

            case R.id.lifePositive:
                if (getArguments().getInt(ARG_LIFE) < Constants.MAX_LIFE) {
                    getArguments().putInt(ARG_LIFE, (getArguments().getInt(ARG_LIFE) + 1));
                    setLife(getArguments().getInt(ARG_LIFE));
                }
                break;

            case R.id.lifeNegative:
                if (getArguments().getInt(ARG_LIFE) > Constants.MIN_LIFE) {
                    getArguments().putInt(ARG_LIFE, (getArguments().getInt(ARG_LIFE) - 1));
                    setLife(getArguments().getInt(ARG_LIFE));
                }
                break;

            case R.id.positiveEDH1:
                if (getArguments().getInt(ARG_EDH1) < Constants.MAX_EDH) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) - 1);
                    getArguments().putInt(ARG_EDH1, getArguments().getInt(ARG_EDH1) + 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(1, getArguments().getInt(ARG_EDH1));
                }
                break;

            case R.id.negativeEDH1:
                if (getArguments().getInt(ARG_EDH1) > Constants.MIN_EDH) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) + 1);
                    getArguments().putInt(ARG_EDH1, getArguments().getInt(ARG_EDH1) - 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(1, getArguments().getInt(ARG_EDH1));
                }
                break;

            case R.id.positiveEDH2:
                if (getArguments().getInt(ARG_EDH2) < Constants.MAX_EDH) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) - 1);
                    getArguments().putInt(ARG_EDH2, getArguments().getInt(ARG_EDH2) + 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(2, getArguments().getInt(ARG_EDH2));
                }
                break;

            case R.id.negativeEDH2:
                if (getArguments().getInt(ARG_EDH2) > Constants.MIN_EDH) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) + 1);
                    getArguments().putInt(ARG_EDH2, getArguments().getInt(ARG_EDH2) - 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(2, getArguments().getInt(ARG_EDH2));
                }
                break;

            case R.id.positiveEDH3:
                if (getArguments().getInt(ARG_EDH3) < Constants.MAX_EDH) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) - 1);
                    getArguments().putInt(ARG_EDH3, getArguments().getInt(ARG_EDH3) + 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(3, getArguments().getInt(ARG_EDH3));
                }
                break;

            case R.id.negativeEDH3:
                if (getArguments().getInt(ARG_EDH3) > Constants.MIN_EDH) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) + 1);
                    getArguments().putInt(ARG_EDH3, getArguments().getInt(ARG_EDH3) - 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(3, getArguments().getInt(ARG_EDH3));
                }
                break;

            case R.id.positiveEDH4:
                if (getArguments().getInt(ARG_EDH4) < Constants.MAX_EDH) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) - 1);
                    getArguments().putInt(ARG_EDH4, getArguments().getInt(ARG_EDH4) + 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(4, getArguments().getInt(ARG_EDH4));
                }
                break;

            case R.id.negativeEDH4:
                if (getArguments().getInt(ARG_EDH4) > Constants.MIN_EDH) {
                    getArguments().putInt(ARG_LIFE, getArguments().getInt(ARG_LIFE) + 1);
                    getArguments().putInt(ARG_EDH4, getArguments().getInt(ARG_EDH4) - 1);
                    setLife(getArguments().getInt(ARG_LIFE));
                    setEDH(4, getArguments().getInt(ARG_EDH4));
                }
                break;
        }
        onUpdateDataInterface.iUpdateActivePlayer(argsToPlayer(getArguments()));

        onUpdateDataInterface.iUpdateDethrone();

        onUpdateDataInterface.iUpdateHistory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_new, container, false);

        createLayout(view);

        updateLayout();

        return view;
    }

    public void updateFragmentDethrone(boolean isOnThrone) {
        if (imageViewThrone != null)
            imageViewThrone.setVisibility(isOnThrone ? View.VISIBLE : View.GONE);
    }

    public void updateLife(int value) {
        getArguments().putInt(ARG_LIFE, value);
        setLife(getArguments().getInt(ARG_LIFE));
    }

    private void createLayout(final View view) {
        LinearLayout mainEDH = (LinearLayout) view.findViewById(R.id.mainEDH);
        mainEDH.setWeightSum(getTotalPlayers());

//        view.findViewById(R.id.cardMain).setOnTouchListener(new GestureTouchListener(view.getContext()) {
//            @Override
//            public void onSwipeBottom() {
//                onUpdateDataInterface.iSwipe(Constants.SWIPE_BOTTOM);
//            }
//
//            @Override
//            public void onSwipeTop() {
//                onUpdateDataInterface.iSwipe(Constants.SWIPE_TOP);
//            }
//        });

        headerPlayerName = (TextView) view.findViewById(R.id.headerPlayerName);
        headerDeckName = (TextView) view.findViewById(R.id.headerDeckName);
        imageViewThrone = (ImageView) view.findViewById(R.id.imageViewThrone);

        lifeValue = (TextView) view.findViewById(R.id.lifeValue);
        lifeValue.setOnTouchListener(new GestureTouchListener(view.getContext()) {
            @Override
            public void onClick() {
                createLifeDialog(view);
            }

//            @Override
//            public void onSwipeBottom() {
//                onUpdateDataInterface.iSwipe(Constants.SWIPE_BOTTOM);
//            }
//
//            @Override
//            public void onSwipeTop() {
//                onUpdateDataInterface.iSwipe(Constants.SWIPE_TOP);
//            }
        });

        lifePositive = (ImageView) view.findViewById(R.id.lifePositive);
        lifeNegative = (ImageView) view.findViewById(R.id.lifeNegative);

        LinearLayout mainEDH3 = (LinearLayout) view.findViewById(R.id.mainEDH3);
        LinearLayout mainEDH4 = (LinearLayout) view.findViewById(R.id.mainEDH4);

        valueEDH1 = (TextView) view.findViewById(R.id.valueEDH1);
        valueEDH2 = (TextView) view.findViewById(R.id.valueEDH2);

        positiveEDH1 = (ImageView) view.findViewById(R.id.positiveEDH1);
        positiveEDH2 = (ImageView) view.findViewById(R.id.positiveEDH2);

        negativeEDH1 = (ImageView) view.findViewById(R.id.negativeEDH1);
        negativeEDH2 = (ImageView) view.findViewById(R.id.negativeEDH2);

        lifeValue.setOnClickListener(this);
        lifePositive.setOnClickListener(this);
        lifeNegative.setOnClickListener(this);
        positiveEDH1.setOnClickListener(this);
        negativeEDH1.setOnClickListener(this);
        positiveEDH2.setOnClickListener(this);
        negativeEDH2.setOnClickListener(this);

        if (getTotalPlayers() >= 3) {
            mainEDH3.setVisibility(View.VISIBLE);
            valueEDH3 = (TextView) view.findViewById(R.id.valueEDH3);
            positiveEDH3 = (ImageView) view.findViewById(R.id.positiveEDH3);
            negativeEDH3 = (ImageView) view.findViewById(R.id.negativeEDH3);
            positiveEDH3.setOnClickListener(this);
            negativeEDH3.setOnClickListener(this);
        } else {
            mainEDH3.setVisibility(View.GONE);
        }

        if (getTotalPlayers() >= 4) {
            mainEDH4.setVisibility(View.VISIBLE);
            valueEDH4 = (TextView) view.findViewById(R.id.valueEDH4);
            positiveEDH4 = (ImageView) view.findViewById(R.id.positiveEDH4);
            negativeEDH4 = (ImageView) view.findViewById(R.id.negativeEDH4);
            positiveEDH4.setOnClickListener(this);
            negativeEDH4.setOnClickListener(this);
        } else {
            mainEDH4.setVisibility(View.GONE);
        }
    }

    private int getTotalPlayers() {
        return getArguments().getInt(ARG_TOTAL_PLAYERS);
    }

    private void setEDH(int which, int edhValue) {
        switch (which) {
            case 1:
                valueEDH1.setText(String.format(Locale.US, "%d", edhValue));
                break;

            case 2:
                valueEDH2.setText(String.format(Locale.US, "%d", edhValue));
                break;

            case 3:
                valueEDH3.setText(String.format(Locale.US, "%d", edhValue));
                break;

            case 4:
                valueEDH4.setText(String.format(Locale.US, "%d", edhValue));
                break;
        }
    }

    private void setLife(int life) {
        if (lifeValue != null) {
            lifeValue.setTextSize(TypedValue.COMPLEX_UNIT_SP, (life > 99 || life < -99) ? 120 : 180);
            lifeValue.setText(String.format(Locale.US, "%d", life));
        }
    }

    private void updateLayout() {
        boolean isAlive = true;
//        boolean isAlive = getArguments().getBoolean(ARG_IS_ALIVE);
        int color = getArguments().getInt(ARG_DECK_COLOR);

        onUpdateDataInterface.iUpdateDethrone();

        headerPlayerName.setText(getArguments().getString(ARG_PLAYER_NAME));
        headerDeckName.setText(getArguments().getString(ARG_DECK_NAME));

        setLife(getArguments().getInt(ARG_LIFE));
        setEDH(1, getArguments().getInt(ARG_EDH1));
        setEDH(2, getArguments().getInt(ARG_EDH2));

        lifePositive.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        lifeNegative.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);

        positiveEDH1.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        negativeEDH1.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        positiveEDH2.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        negativeEDH2.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);

        if (getTotalPlayers() >= 3) {
            setEDH(3, getArguments().getInt(ARG_EDH3));
            positiveEDH3.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
            negativeEDH3.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        }

        if (getTotalPlayers() >= 4) {
            setEDH(4, getArguments().getInt(ARG_EDH4));
            positiveEDH4.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
            negativeEDH4.setColorFilter(isAlive ? color : Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        }
    }

    public interface OnUpdateData {
//        void iSwipe(int direction);

        void iUpdateActivePlayer(ActivePlayer activePlayer);

        void iUpdateDethrone();

        void iUpdateHistory();
    }
}