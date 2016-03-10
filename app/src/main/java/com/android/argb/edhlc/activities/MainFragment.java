package com.android.argb.edhlc.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.objects.ActivePlayerNew;

public class MainFragment extends Fragment {

    //Args
    private static final String ARG_HAS_ACTIVE_PLAYERS = "ARG_HAS_ACTIVE_PLAYERS";
    private static final String ARG_PLAYER_NAME = "ARG_PLAYER_NAME";
    private static final String ARG_DECK_NAME = "ARG_DECK_NAME";
    private static final String ARG_LIFE = "ARG_LIFE";
    private static final String ARG_EDH1 = "ARG_EDH1";
    private static final String ARG_EDH2 = "ARG_EDH2";
    private static final String ARG_EDH3 = "ARG_EDH3";
    private static final String ARG_EDH4 = "ARG_EDH4";
    private static final String ARG_TAG = "ARG_TAG";

    //Layout
    private TextView messageTextView;

    public static MainFragment newInstance(ActivePlayerNew activePlayer) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        playerToArgs(activePlayer, args);
        fragment.setArguments(args);
        return fragment;
    }

    private static void playerToArgs(ActivePlayerNew activePlayer, Bundle args) {
        if (activePlayer != null) {
            args.putBoolean(ARG_HAS_ACTIVE_PLAYERS, true);
            args.putString(ARG_PLAYER_NAME, activePlayer.getPlayerDeck().getDeckOwnerName());
            args.putString(ARG_DECK_NAME, activePlayer.getPlayerDeck().getDeckName());
            args.putInt(ARG_LIFE, activePlayer.getPlayerLife());
            args.putInt(ARG_EDH1, activePlayer.getPlayerEDH1());
            args.putInt(ARG_EDH2, activePlayer.getPlayerEDH2());
            args.putInt(ARG_EDH3, activePlayer.getPlayerEDH3());
            args.putInt(ARG_EDH4, activePlayer.getPlayerEDH4());
            args.putInt(ARG_TAG, activePlayer.getPlayerTag());
        } else {
            args.putBoolean(ARG_HAS_ACTIVE_PLAYERS, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_new, container, false);

        createLayout(view);

        if (hasActivePlayer())
            refreshLayout();

        return view;
    }

    //TODO
    private void createLayout(View view) {
        messageTextView = (TextView) view.findViewById(R.id.test);
    }

    //TODO
    private void refreshLayout() {
        String message = getArguments().getString(ARG_PLAYER_NAME);
        messageTextView.setText("" + message);
    }

    private boolean hasActivePlayer() {
        return getArguments().getBoolean(ARG_HAS_ACTIVE_PLAYERS);
    }

    public void updateFragmentData(ActivePlayerNew activePlayer) {
        playerToArgs(activePlayer, getArguments());

        if (hasActivePlayer())
            refreshLayout();
    }
}