package com.android.argb.edhlc;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OverviewActivity extends ActionBarActivity {

    static Player mPlayer1;
    static Player mPlayer2;
    static Player mPlayer3;
    static Player mPlayer4;

    private static TextView mTextViewP1Name;
    private static TextView mTextViewP1Life;
    private static TextView mTextViewP1EDH1;
    private static TextView mTextViewP1EDH2;
    private static TextView mTextViewP1EDH3;
    private static TextView mTextViewP1EDH4;

    private static TextView mTextViewP2Name;
    private static TextView mTextViewP2Life;
    private static TextView mTextViewP2EDH1;
    private static TextView mTextViewP2EDH2;
    private static TextView mTextViewP2EDH3;
    private static TextView mTextViewP2EDH4;

    private static LinearLayout mLinearLayoutP3;
    private static TextView mTextViewP3Name;
    private static TextView mTextViewP3Life;
    private static TextView mTextViewP3EDH1;
    private static TextView mTextViewP3EDH2;
    private static TextView mTextViewP3EDH3;
    private static TextView mTextViewP3EDH4;

    private static LinearLayout mLinearLayoutP4;
    private static TextView mTextViewP4Name;
    private static TextView mTextViewP4Life;
    private static TextView mTextViewP4EDH1;
    private static TextView mTextViewP4EDH2;
    private static TextView mTextViewP4EDH3;
    private static TextView mTextViewP4EDH4;

    private int numPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.edh_default_dark));
        }

        numPlayers = getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).getInt("NUM_PLAYERS", 4);

        createLayout(this.findViewById(android.R.id.content));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayer1 = Player.loadPlayerSharedPreferences(this, 1);
        mPlayer2 = Player.loadPlayerSharedPreferences(this, 2);
        mPlayer3 = Player.loadPlayerSharedPreferences(this, 3);
        mPlayer4 = Player.loadPlayerSharedPreferences(this, 4);

        if (getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).getInt("SCREEN_ON", 0) == 1)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        numPlayers = getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).getInt("NUM_PLAYERS", 4);

        updateLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void createLayout(View view) {
        if (view != null) {
            mTextViewP1Name = (TextView) view.findViewById(R.id.textViewOverviewP1Name);
            mTextViewP1Life = (TextView) view.findViewById(R.id.textViewOverviewP1Life);
            mTextViewP1EDH1 = (TextView) view.findViewById(R.id.textViewOverviewP1EDH1);
            mTextViewP1EDH2 = (TextView) view.findViewById(R.id.textViewOverviewP1EDH2);
            mTextViewP1EDH3 = (TextView) view.findViewById(R.id.textViewOverviewP1EDH3);
            mTextViewP1EDH4 = (TextView) view.findViewById(R.id.textViewOverviewP1EDH4);

            mTextViewP2Name = (TextView) view.findViewById(R.id.textViewOverviewP2Name);
            mTextViewP2Life = (TextView) view.findViewById(R.id.textViewOverviewP2Life);
            mTextViewP2EDH1 = (TextView) view.findViewById(R.id.textViewOverviewP2EDH1);
            mTextViewP2EDH2 = (TextView) view.findViewById(R.id.textViewOverviewP2EDH2);
            mTextViewP2EDH3 = (TextView) view.findViewById(R.id.textViewOverviewP2EDH3);
            mTextViewP2EDH4 = (TextView) view.findViewById(R.id.textViewOverviewP2EDH4);

            mLinearLayoutP3 = (LinearLayout) view.findViewById(R.id.linearLayoutP3);
            mTextViewP3Name = (TextView) view.findViewById(R.id.textViewOverviewP3Name);
            mTextViewP3Life = (TextView) view.findViewById(R.id.textViewOverviewP3Life);
            mTextViewP3EDH1 = (TextView) view.findViewById(R.id.textViewOverviewP3EDH1);
            mTextViewP3EDH2 = (TextView) view.findViewById(R.id.textViewOverviewP3EDH2);
            mTextViewP3EDH3 = (TextView) view.findViewById(R.id.textViewOverviewP3EDH3);
            mTextViewP3EDH4 = (TextView) view.findViewById(R.id.textViewOverviewP3EDH4);

            if (!isPlayerActive(3)) {
                mLinearLayoutP3.setVisibility(View.GONE);
            }

            mLinearLayoutP4 = (LinearLayout) view.findViewById(R.id.linearLayoutP4);
            mTextViewP4Name = (TextView) view.findViewById(R.id.textViewOverviewP4Name);
            mTextViewP4Life = (TextView) view.findViewById(R.id.textViewOverviewP4Life);
            mTextViewP4EDH1 = (TextView) view.findViewById(R.id.textViewOverviewP4EDH1);
            mTextViewP4EDH2 = (TextView) view.findViewById(R.id.textViewOverviewP4EDH2);
            mTextViewP4EDH3 = (TextView) view.findViewById(R.id.textViewOverviewP4EDH3);
            mTextViewP4EDH4 = (TextView) view.findViewById(R.id.textViewOverviewP4EDH4);

            if (!isPlayerActive(4)) {
                mLinearLayoutP4.setVisibility(View.GONE);
            }
        }
    }

    private void updateLayout() {
        mTextViewP1Name.setText(mPlayer1.getPlayerName());
        mTextViewP1Life.setText(String.valueOf(mPlayer1.getPlayerLife()));
        mTextViewP1EDH1.setText(String.valueOf(mPlayer1.getPlayerEDH1()));
        mTextViewP1EDH2.setText(String.valueOf(mPlayer1.getPlayerEDH2()));
        mTextViewP1EDH3.setText(String.valueOf(mPlayer1.getPlayerEDH3()));
        mTextViewP1EDH4.setText(String.valueOf(mPlayer1.getPlayerEDH4()));
        mTextViewP1Name.setTextColor(mPlayer1.getPlayerColor()[1]);
        mTextViewP1Life.setTextColor(mPlayer1.getPlayerColor()[1]);
        mTextViewP1EDH1.setTextColor(mPlayer1.getPlayerColor()[0]);
        mTextViewP1EDH2.setTextColor(mPlayer1.getPlayerColor()[0]);
        mTextViewP1EDH3.setTextColor(mPlayer1.getPlayerColor()[0]);
        mTextViewP1EDH4.setTextColor(mPlayer1.getPlayerColor()[0]);

        mTextViewP2Name.setText(mPlayer2.getPlayerName());
        mTextViewP2Life.setText(String.valueOf(mPlayer2.getPlayerLife()));
        mTextViewP2EDH1.setText(String.valueOf(mPlayer2.getPlayerEDH1()));
        mTextViewP2EDH2.setText(String.valueOf(mPlayer2.getPlayerEDH2()));
        mTextViewP2EDH3.setText(String.valueOf(mPlayer2.getPlayerEDH3()));
        mTextViewP2EDH4.setText(String.valueOf(mPlayer2.getPlayerEDH4()));
        mTextViewP2Name.setTextColor(mPlayer2.getPlayerColor()[1]);
        mTextViewP2Life.setTextColor(mPlayer2.getPlayerColor()[1]);
        mTextViewP2EDH1.setTextColor(mPlayer2.getPlayerColor()[0]);
        mTextViewP2EDH2.setTextColor(mPlayer2.getPlayerColor()[0]);
        mTextViewP2EDH3.setTextColor(mPlayer2.getPlayerColor()[0]);
        mTextViewP2EDH4.setTextColor(mPlayer2.getPlayerColor()[0]);

        mTextViewP3Name.setText(mPlayer3.getPlayerName());
        mTextViewP3Life.setText(String.valueOf(mPlayer3.getPlayerLife()));
        mTextViewP3EDH1.setText(String.valueOf(mPlayer3.getPlayerEDH1()));
        mTextViewP3EDH2.setText(String.valueOf(mPlayer3.getPlayerEDH2()));
        mTextViewP3EDH3.setText(String.valueOf(mPlayer3.getPlayerEDH3()));
        mTextViewP3EDH4.setText(String.valueOf(mPlayer3.getPlayerEDH4()));
        mTextViewP3Name.setTextColor(mPlayer3.getPlayerColor()[1]);
        mTextViewP3Life.setTextColor(mPlayer3.getPlayerColor()[1]);
        mTextViewP3EDH1.setTextColor(mPlayer3.getPlayerColor()[0]);
        mTextViewP3EDH2.setTextColor(mPlayer3.getPlayerColor()[0]);
        mTextViewP3EDH3.setTextColor(mPlayer3.getPlayerColor()[0]);
        mTextViewP3EDH4.setTextColor(mPlayer3.getPlayerColor()[0]);

        mTextViewP4Name.setText(mPlayer4.getPlayerName());
        mTextViewP4Life.setText(String.valueOf(mPlayer4.getPlayerLife()));
        mTextViewP4EDH1.setText(String.valueOf(mPlayer4.getPlayerEDH1()));
        mTextViewP4EDH2.setText(String.valueOf(mPlayer4.getPlayerEDH2()));
        mTextViewP4EDH3.setText(String.valueOf(mPlayer4.getPlayerEDH3()));
        mTextViewP4EDH4.setText(String.valueOf(mPlayer4.getPlayerEDH4()));
        mTextViewP4Name.setTextColor(mPlayer4.getPlayerColor()[1]);
        mTextViewP4Life.setTextColor(mPlayer4.getPlayerColor()[1]);
        mTextViewP4EDH1.setTextColor(mPlayer4.getPlayerColor()[0]);
        mTextViewP4EDH2.setTextColor(mPlayer4.getPlayerColor()[0]);
        mTextViewP4EDH3.setTextColor(mPlayer4.getPlayerColor()[0]);
        mTextViewP4EDH4.setTextColor(mPlayer4.getPlayerColor()[0]);
    }

    public void onClickP1(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TAG", "1");
        startActivity(intent);
        this.finish();
    }

    public void onClickP2(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("TAG", "2");
        startActivity(intent);
        this.finish();
    }

    public void onClickP3(View view) {
        if (isPlayerActive(3)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "3");
            startActivity(intent);
            this.finish();
        }
    }

    public void onClickP4(View view) {
        if (isPlayerActive(4)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "4");
            startActivity(intent);
            this.finish();
        }
    }


    private boolean isPlayerActive(int i) {
        if (numPlayers < i)
            return false;
        return true;
    }
}
