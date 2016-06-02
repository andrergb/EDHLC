package com.android.argb.edhlc;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.argb.edhlc.objects.ActivePlayer;

import java.text.MessageFormat;
import java.util.List;

/**
 * - Created by agbarros on 31/05/2016.
 */
public class HistoryBottomSheet extends BottomSheetDialogFragment {

    private CoordinatorLayout.Behavior behavior;
    private int bottomSheetsHeight = 800;
    private boolean mIsScrolling = false;

    private int totalPlayer;
    private ActivePlayer activePlayer;
    private List<ActivePlayer> activePlayers;

    private ListView listBottomSheet;
    private TextView titleBottomSheet;
    private TextView player1BottomSheet;
    private TextView player2BottomSheet;
    private TextView player3BottomSheet;
    private TextView player4BottomSheet;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            switch (newState) {
                case BottomSheetBehavior.STATE_DRAGGING:
                    if (mIsScrolling)
                        ((BottomSheetBehavior) behavior).setState(BottomSheetBehavior.STATE_EXPANDED);
                    break;

                case BottomSheetBehavior.STATE_HIDDEN:
                    dismiss();
                    break;

                case BottomSheetBehavior.STATE_SETTLING:
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                    break;
            }
        }
    };

    public void setBottomSheetsHeight(int height) {
        this.bottomSheetsHeight = height;
    }

    public void setPlayers(int currentPlayer, List<ActivePlayer> activePlayers) {
        this.totalPlayer = activePlayers.size();
        this.activePlayers = activePlayers;
        this.activePlayer = activePlayers.get(currentPlayer);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_history_bottom_sheet, null);
        dialog.setContentView(contentView);

        titleBottomSheet = (TextView) contentView.findViewById(R.id.historyTitleBottomSheet);
        player1BottomSheet = (TextView) contentView.findViewById(R.id.textViewEDH1);
        player2BottomSheet = (TextView) contentView.findViewById(R.id.textViewEDH2);
        player3BottomSheet = (TextView) contentView.findViewById(R.id.textViewEDH3);
        player4BottomSheet = (TextView) contentView.findViewById(R.id.textViewEDH4);
        listBottomSheet = (ListView) contentView.findViewById(R.id.historyListBottomSheet);
        listBottomSheet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mIsScrolling = !listIsAtTop(listBottomSheet);
                return false;
            }
        });

        LinearLayout historyLinearBottomSheet = (LinearLayout) contentView.findViewById(R.id.historyLinearBottomSheet);
        historyLinearBottomSheet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mIsScrolling = false;
                return false;
            }
        });

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        updateHistoryLayout(contentView);
    }

    private boolean listIsAtTop(ListView listView) {
        return listView.getChildCount() == 0 || listView.getChildAt(0).getTop() == 0;
    }

    private void updateHistoryLayout(final View view) {
        int activePlayerColor = activePlayer.getPlayerDeck().getDeckShieldColor()[0];

        titleBottomSheet.setText(MessageFormat.format("{0} history", activePlayer.getPlayerDeck().getDeckOwnerName()));
        titleBottomSheet.setTextColor(activePlayerColor);

        player1BottomSheet.setText(activePlayers.get(0).getPlayerDeck().getDeckName());
        player1BottomSheet.setTextColor(activePlayerColor);

        player2BottomSheet.setText(activePlayers.get(1).getPlayerDeck().getDeckName());
        player2BottomSheet.setTextColor(activePlayerColor);

        if (totalPlayer >= 3) {
            player3BottomSheet.setTextColor(activePlayerColor);
            player3BottomSheet.setText(activePlayers.get(2).getPlayerDeck().getDeckName());
        } else {
            player3BottomSheet.setVisibility(View.GONE);
        }

        if (totalPlayer >= 4) {
            player4BottomSheet.setTextColor(activePlayerColor);
            player4BottomSheet.setText(activePlayers.get(3).getPlayerDeck().getDeckName());
        } else {
            player4BottomSheet.setVisibility(View.GONE);
        }

        String latestSavedLifePreferences = view.getContext().getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE).getString(Constants.PLAYER_HISTORY_LIFE + activePlayer.getPlayerTag(), "40");
        String latestSavedEDHPreferences = view.getContext().getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE).getString(Constants.PLAYER_EDH_PREFIX + activePlayer.getPlayerTag(), "0@0@0@0");
        if (!latestSavedLifePreferences.isEmpty() && !latestSavedEDHPreferences.isEmpty()) {
            String[] latestSavedLifeArray = latestSavedLifePreferences.split("_");
            String[] latestSavedEDHArray = latestSavedEDHPreferences.split("_");
            listBottomSheet.setAdapter(new Utils.customHistoryListViewAdapter(view.getContext(), totalPlayer, latestSavedLifeArray, latestSavedEDHArray, activePlayerColor));
        }

        listBottomSheet.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                listBottomSheet.getViewTreeObserver().removeOnPreDrawListener(this);
                //Set peek and height based on the content of the bottom sheets
                int layoutSize = (int) Utils.convertDpToPixel((56 + 30 + 8), view.getContext()) + listBottomSheet.getHeight();
                ((BottomSheetBehavior) behavior).setPeekHeight(layoutSize < bottomSheetsHeight ? layoutSize : bottomSheetsHeight);
                view.getLayoutParams().height = layoutSize < bottomSheetsHeight ? layoutSize : bottomSheetsHeight;
                return true;
            }
        });


    }
}