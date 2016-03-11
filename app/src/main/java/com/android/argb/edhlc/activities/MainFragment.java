package com.android.argb.edhlc.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.objects.ActivePlayerNew;
import com.android.argb.edhlc.objects.Deck;

public class MainFragment extends Fragment implements View.OnClickListener {

    //Args
    private static final String ARG_PLAYER_NAME = "ARG_PLAYER_NAME";
    private static final String ARG_DECK_NAME = "ARG_DECK_NAME";
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

    public static MainFragment newInstance(ActivePlayerNew activePlayer) {
        Bundle args = new Bundle();
        if (activePlayer != null) {
            args.putString(ARG_PLAYER_NAME, activePlayer.getPlayerDeck().getDeckOwnerName());
            args.putString(ARG_DECK_NAME, activePlayer.getPlayerDeck().getDeckName());
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
            auxPlayer.setPlayerDeck(new Deck(args.getString(ARG_PLAYER_NAME), args.getString(ARG_DECK_NAME)));
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
            case R.id.plus:
                break;
        }
        onUpdateDataInterface.iUpdateActivePlayer(argsToPlayer(getArguments()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_new, container, false);

        messageTextView = (TextView) view.findViewById(R.id.test);
        buttonPlus = (Button) view.findViewById(R.id.plus);
        buttonPlus.setOnClickListener(this);

        messageTextView.setText("" + getArguments().getString(ARG_PLAYER_NAME));

        return view;
    }

    public interface OnUpdateData {
        void iUpdateActivePlayer(ActivePlayerNew activePlayer);
    }
}