package com.android.argb.edhlc.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.objects.ActivePlayer;
import com.android.argb.edhlc.objects.Drawer.DrawerOverview;

import java.util.ArrayList;
import java.util.List;

public class OverviewActivity extends ActionBarActivity {

    static ActivePlayer mActivePlayer1;
    static ActivePlayer mActivePlayer2;
    static ActivePlayer mActivePlayer3;
    static ActivePlayer mActivePlayer4;

    private static ImageView mImageViewThroneP1;
    private static ImageView mImageViewThroneP2;
    private static ImageView mImageViewThroneP3;
    private static ImageView mImageViewThroneP4;

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

    private DrawerOverview drawerOverview;

    public void createLayout(View view) {
        if (view != null) {
            mImageViewThroneP1 = (ImageView) view.findViewById(R.id.imageViewThroneP1);
            mImageViewThroneP2 = (ImageView) view.findViewById(R.id.imageViewThroneP2);
            mImageViewThroneP3 = (ImageView) view.findViewById(R.id.imageViewThroneP3);
            mImageViewThroneP4 = (ImageView) view.findViewById(R.id.imageViewThroneP4);

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
            if (numPlayers < 3)
                mLinearLayoutP3.setVisibility(View.GONE);

            mLinearLayoutP4 = (LinearLayout) view.findViewById(R.id.linearLayoutP4);
            mTextViewP4Name = (TextView) view.findViewById(R.id.textViewOverviewP4Name);
            mTextViewP4Life = (TextView) view.findViewById(R.id.textViewOverviewP4Life);
            mTextViewP4EDH1 = (TextView) view.findViewById(R.id.textViewOverviewP4EDH1);
            mTextViewP4EDH2 = (TextView) view.findViewById(R.id.textViewOverviewP4EDH2);
            mTextViewP4EDH3 = (TextView) view.findViewById(R.id.textViewOverviewP4EDH3);
            mTextViewP4EDH4 = (TextView) view.findViewById(R.id.textViewOverviewP4EDH4);
            if (numPlayers < 4)
                mLinearLayoutP4.setVisibility(View.GONE);

        }
    }

    @Override
    public void onBackPressed() {
        if (drawerOverview.isDrawerOpen()) {
            drawerOverview.dismiss();
        } else {
            super.onBackPressed();
            this.finish();
        }
    }

    public void onClickP1(View view) {
        if (numPlayers >= 1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "1");
            startActivity(intent);
            this.finish();
        }
    }

    public void onClickP2(View view) {
        if (numPlayers >= 2) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "2");
            startActivity(intent);
            this.finish();
        }
    }

    public void onClickP3(View view) {
        if (numPlayers >= 3) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "3");
            startActivity(intent);
            this.finish();
        }
    }

    public void onClickP4(View view) {
        if (numPlayers >= 4) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "4");
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerOverview.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //DrawerMain menu
        if (drawerOverview.getDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        }

        //Option menu
        int id = item.getItemId();
        if (id == R.id.action_history) {
            startActivity(new Intent(this, HistoryActivity.class));
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        //DrawerMain menu
        List<String[]> drawerLists = new ArrayList<>();
        drawerLists.add(getResources().getStringArray(R.array.string_menu_overview_1));
        drawerLists.add(getResources().getStringArray(R.array.string_menu_overview_2));

        assert getSupportActionBar() != null;
        drawerOverview = new DrawerOverview(this, drawerLists);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.secondary_color));
        }

        numPlayers = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.TOTAL_PLAYERS, 4);

        createLayout(this.findViewById(android.R.id.content));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerOverview.getDrawerToggle().syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivePlayer1 = ActivePlayer.loadPlayerSharedPreferences(this, 1);
        mActivePlayer2 = ActivePlayer.loadPlayerSharedPreferences(this, 2);
        mActivePlayer3 = ActivePlayer.loadPlayerSharedPreferences(this, 3);
        mActivePlayer4 = ActivePlayer.loadPlayerSharedPreferences(this, 4);

        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        numPlayers = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.TOTAL_PLAYERS, 4);

        updateLayout();
    }

    private boolean isPlayerActiveAndAlive(int i) {
        if (numPlayers < i)
            return false;
        else {
            switch (i) {
                case 1:
                    return mActivePlayer1.getPlayerIsAlive();
                case 2:
                    return mActivePlayer2.getPlayerIsAlive();
                case 3:
                    return mActivePlayer3.getPlayerIsAlive();
                case 4:
                    return mActivePlayer4.getPlayerIsAlive();
                default:
                    return true;
            }
        }
    }

    private void updateDethroneIcon() {
        mImageViewThroneP1.setVisibility(View.INVISIBLE);
        mImageViewThroneP2.setVisibility(View.INVISIBLE);
        mImageViewThroneP3.setVisibility(View.INVISIBLE);
        mImageViewThroneP4.setVisibility(View.INVISIBLE);

        //P1
        if (isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4))
            mImageViewThroneP1.setVisibility(View.VISIBLE);

        //P2
        if (!isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4))
            mImageViewThroneP2.setVisibility(View.VISIBLE);

        //P3
        if (!isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4))
            mImageViewThroneP3.setVisibility(View.VISIBLE);

        //P4
        if (!isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4))
            mImageViewThroneP4.setVisibility(View.VISIBLE);

        //P1 and P2
        if (isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4)) {
            if (mActivePlayer1.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThroneP1.setVisibility(View.VISIBLE);
            if (mActivePlayer2.getPlayerLife() >= mActivePlayer1.getPlayerLife())
                mImageViewThroneP2.setVisibility(View.VISIBLE);
        }

        //P1 and P3
        if (isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4)) {
            if (mActivePlayer1.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThroneP1.setVisibility(View.VISIBLE);
            if (mActivePlayer3.getPlayerLife() >= mActivePlayer1.getPlayerLife())
                mImageViewThroneP3.setVisibility(View.VISIBLE);
        }

        //P1 and P4
        if (isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (mActivePlayer1.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP1.setVisibility(View.VISIBLE);
            if (mActivePlayer4.getPlayerLife() >= mActivePlayer1.getPlayerLife())
                mImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P2 and P3
        if (!isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4)) {
            if (mActivePlayer2.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThroneP2.setVisibility(View.VISIBLE);
            if (mActivePlayer3.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThroneP3.setVisibility(View.VISIBLE);
        }

        //P2 and P4
        if (!isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (mActivePlayer2.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP2.setVisibility(View.VISIBLE);
            if (mActivePlayer4.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P3 and P4
        if (!isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (mActivePlayer3.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP3.setVisibility(View.VISIBLE);
            if (mActivePlayer4.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P1, P2 and P3
        if (isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && !isPlayerActiveAndAlive(4)) {
            if (mActivePlayer1.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer1.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThroneP1.setVisibility(View.VISIBLE);
            if (mActivePlayer2.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer2.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThroneP2.setVisibility(View.VISIBLE);
            if (mActivePlayer3.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer3.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThroneP3.setVisibility(View.VISIBLE);
        }

        //P1, P2 and P4
        if (isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (mActivePlayer1.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer1.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP1.setVisibility(View.VISIBLE);
            if (mActivePlayer2.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer2.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP2.setVisibility(View.VISIBLE);
            if (mActivePlayer4.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer4.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P1, P3 and P4
        if (isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (mActivePlayer1.getPlayerLife() >= mActivePlayer3.getPlayerLife() && mActivePlayer1.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP1.setVisibility(View.VISIBLE);
            if (mActivePlayer3.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer3.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP3.setVisibility(View.VISIBLE);
            if (mActivePlayer4.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer4.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P2, P3 and P4
        if (!isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (mActivePlayer2.getPlayerLife() >= mActivePlayer3.getPlayerLife() && mActivePlayer2.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP2.setVisibility(View.VISIBLE);
            if (mActivePlayer3.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer3.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP3.setVisibility(View.VISIBLE);
            if (mActivePlayer4.getPlayerLife() >= mActivePlayer3.getPlayerLife() && mActivePlayer4.getPlayerLife() >= mActivePlayer2.getPlayerLife())
                mImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P1, P2, P3 and P4
        if (isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3) && isPlayerActiveAndAlive(4)) {
            if (mActivePlayer1.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer1.getPlayerLife() >= mActivePlayer3.getPlayerLife() && mActivePlayer1.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP1.setVisibility(View.VISIBLE);
            if (mActivePlayer2.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer2.getPlayerLife() >= mActivePlayer3.getPlayerLife() && mActivePlayer2.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP2.setVisibility(View.VISIBLE);
            if (mActivePlayer3.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer3.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer3.getPlayerLife() >= mActivePlayer4.getPlayerLife())
                mImageViewThroneP3.setVisibility(View.VISIBLE);
            if (mActivePlayer4.getPlayerLife() >= mActivePlayer1.getPlayerLife() && mActivePlayer4.getPlayerLife() >= mActivePlayer2.getPlayerLife() && mActivePlayer4.getPlayerLife() >= mActivePlayer3.getPlayerLife())
                mImageViewThroneP4.setVisibility(View.VISIBLE);
        }
    }

    private void updateLayout() {
        updateDethroneIcon();

        mTextViewP1Name.setText(mActivePlayer1.getPlayerName());
        mTextViewP1Life.setText(String.valueOf(mActivePlayer1.getPlayerLife()));
        mTextViewP1EDH1.setText(String.valueOf(mActivePlayer1.getPlayerEDH1()));
        mTextViewP1EDH2.setText(String.valueOf(mActivePlayer1.getPlayerEDH2()));
        mTextViewP1EDH3.setText(String.valueOf(mActivePlayer1.getPlayerEDH3()));
        mTextViewP1EDH4.setText(String.valueOf(mActivePlayer1.getPlayerEDH4()));
        mTextViewP1Name.setEnabled(mActivePlayer1.getPlayerIsAlive());
        mTextViewP1Life.setEnabled(mActivePlayer1.getPlayerIsAlive());
        mTextViewP1EDH1.setEnabled(mActivePlayer1.getPlayerIsAlive());
        mTextViewP1EDH2.setEnabled(mActivePlayer1.getPlayerIsAlive());
        mTextViewP1EDH3.setEnabled(mActivePlayer1.getPlayerIsAlive());
        mTextViewP1EDH4.setEnabled(mActivePlayer1.getPlayerIsAlive());
        mTextViewP1Name.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerColor()[1] : Color.LTGRAY);
        mTextViewP1Life.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerColor()[1] : Color.LTGRAY);
        mTextViewP1EDH1.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP1EDH2.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP1EDH3.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP1EDH4.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerColor()[0] : Color.LTGRAY);

        mTextViewP2Name.setText(mActivePlayer2.getPlayerName());
        mTextViewP2Life.setText(String.valueOf(mActivePlayer2.getPlayerLife()));
        mTextViewP2EDH1.setText(String.valueOf(mActivePlayer2.getPlayerEDH1()));
        mTextViewP2EDH2.setText(String.valueOf(mActivePlayer2.getPlayerEDH2()));
        mTextViewP2EDH3.setText(String.valueOf(mActivePlayer2.getPlayerEDH3()));
        mTextViewP2EDH4.setText(String.valueOf(mActivePlayer2.getPlayerEDH4()));
        mTextViewP2Name.setEnabled(mActivePlayer2.getPlayerIsAlive());
        mTextViewP2Life.setEnabled(mActivePlayer2.getPlayerIsAlive());
        mTextViewP2EDH1.setEnabled(mActivePlayer2.getPlayerIsAlive());
        mTextViewP2EDH2.setEnabled(mActivePlayer2.getPlayerIsAlive());
        mTextViewP2EDH3.setEnabled(mActivePlayer2.getPlayerIsAlive());
        mTextViewP2EDH4.setEnabled(mActivePlayer2.getPlayerIsAlive());
        mTextViewP2Name.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerColor()[1] : Color.LTGRAY);
        mTextViewP2Life.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerColor()[1] : Color.LTGRAY);
        mTextViewP2EDH1.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP2EDH2.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP2EDH3.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP2EDH4.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerColor()[0] : Color.LTGRAY);

        mTextViewP3Name.setText(mActivePlayer3.getPlayerName());
        mTextViewP3Life.setText(String.valueOf(mActivePlayer3.getPlayerLife()));
        mTextViewP3EDH1.setText(String.valueOf(mActivePlayer3.getPlayerEDH1()));
        mTextViewP3EDH2.setText(String.valueOf(mActivePlayer3.getPlayerEDH2()));
        mTextViewP3EDH3.setText(String.valueOf(mActivePlayer3.getPlayerEDH3()));
        mTextViewP3EDH4.setText(String.valueOf(mActivePlayer3.getPlayerEDH4()));
        mTextViewP3Name.setEnabled(mActivePlayer3.getPlayerIsAlive());
        mTextViewP3Life.setEnabled(mActivePlayer3.getPlayerIsAlive());
        mTextViewP3EDH1.setEnabled(mActivePlayer3.getPlayerIsAlive());
        mTextViewP3EDH2.setEnabled(mActivePlayer3.getPlayerIsAlive());
        mTextViewP3EDH3.setEnabled(mActivePlayer3.getPlayerIsAlive());
        mTextViewP3EDH4.setEnabled(mActivePlayer3.getPlayerIsAlive());
        mTextViewP3Name.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerColor()[1] : Color.LTGRAY);
        mTextViewP3Life.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerColor()[1] : Color.LTGRAY);
        mTextViewP3EDH1.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP3EDH2.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP3EDH3.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP3EDH4.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerColor()[0] : Color.LTGRAY);

        mTextViewP4Name.setText(mActivePlayer4.getPlayerName());
        mTextViewP4Life.setText(String.valueOf(mActivePlayer4.getPlayerLife()));
        mTextViewP4EDH1.setText(String.valueOf(mActivePlayer4.getPlayerEDH1()));
        mTextViewP4EDH2.setText(String.valueOf(mActivePlayer4.getPlayerEDH2()));
        mTextViewP4EDH3.setText(String.valueOf(mActivePlayer4.getPlayerEDH3()));
        mTextViewP4EDH4.setText(String.valueOf(mActivePlayer4.getPlayerEDH4()));
        mTextViewP4Name.setEnabled(mActivePlayer4.getPlayerIsAlive());
        mTextViewP4Life.setEnabled(mActivePlayer4.getPlayerIsAlive());
        mTextViewP4EDH1.setEnabled(mActivePlayer4.getPlayerIsAlive());
        mTextViewP4EDH2.setEnabled(mActivePlayer4.getPlayerIsAlive());
        mTextViewP4EDH3.setEnabled(mActivePlayer4.getPlayerIsAlive());
        mTextViewP4EDH4.setEnabled(mActivePlayer4.getPlayerIsAlive());
        mTextViewP4Name.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerColor()[1] : Color.LTGRAY);
        mTextViewP4Life.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerColor()[1] : Color.LTGRAY);
        mTextViewP4EDH1.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP4EDH2.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP4EDH3.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerColor()[0] : Color.LTGRAY);
        mTextViewP4EDH4.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerColor()[0] : Color.LTGRAY);
    }
}
