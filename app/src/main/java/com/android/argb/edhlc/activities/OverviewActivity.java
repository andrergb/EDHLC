package com.android.argb.edhlc.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.objects.ActivePlayerNew;

public class OverviewActivity extends AppCompatActivity {

    static ActivePlayerNew mActivePlayer1;
    static ActivePlayerNew mActivePlayer2;
    static ActivePlayerNew mActivePlayer3;
    static ActivePlayerNew mActivePlayer4;

    private static ImageView mImageViewThroneP1;
    private static ImageView mImageViewThroneP2;
    private static ImageView mImageViewThroneP3;
    private static ImageView mImageViewThroneP4;

    private static TextView mTextViewP1Name;
    private static TextView mTextViewP1Life;
    private static ImageView lifePositiveP1;
    private static ImageView lifeNegativeP1;
    private static TextView mTextViewP1EDH1;
    private static TextView mTextViewP1EDH2;
    private static TextView mTextViewP1EDH3;
    private static TextView mTextViewP1EDH4;

    private static TextView mTextViewP2Name;
    private static TextView mTextViewP2Life;
    private static ImageView lifePositiveP2;
    private static ImageView lifeNegativeP2;
    private static TextView mTextViewP2EDH1;
    private static TextView mTextViewP2EDH2;
    private static TextView mTextViewP2EDH3;
    private static TextView mTextViewP2EDH4;

    private static TextView mTextViewP3Name;
    private static TextView mTextViewP3Life;
    private static ImageView lifePositiveP3;
    private static ImageView lifeNegativeP3;
    private static TextView mTextViewP3EDH1;
    private static TextView mTextViewP3EDH2;
    private static TextView mTextViewP3EDH3;
    private static TextView mTextViewP3EDH4;

    private static TextView mTextViewP4Name;
    private static TextView mTextViewP4Life;
    private static ImageView lifePositiveP4;
    private static ImageView lifeNegativeP4;
    private static TextView mTextViewP4EDH1;
    private static TextView mTextViewP4EDH2;
    private static TextView mTextViewP4EDH3;
    private static TextView mTextViewP4EDH4;
    private int totalPlayers;

    public void createLayout() {
        mImageViewThroneP1 = (ImageView) findViewById(R.id.imageViewThroneP1);
        mImageViewThroneP2 = (ImageView) findViewById(R.id.imageViewThroneP2);
        mImageViewThroneP3 = (ImageView) findViewById(R.id.imageViewThroneP3);
        mImageViewThroneP4 = (ImageView) findViewById(R.id.imageViewThroneP4);

        mTextViewP1Name = (TextView) findViewById(R.id.textViewOverviewP1Name);
        mTextViewP1Life = (TextView) findViewById(R.id.textViewOverviewP1Life);
        LinearLayout mLinearEDH1 = (LinearLayout) findViewById(R.id.linearEDH1);
        mLinearEDH1.setWeightSum(totalPlayers);
        lifePositiveP1 = (ImageView) findViewById(R.id.lifePositiveP1);
        lifeNegativeP1 = (ImageView) findViewById(R.id.lifeNegativeP1);
        mTextViewP1EDH1 = (TextView) findViewById(R.id.textViewOverviewP1EDH1);
        mTextViewP1EDH2 = (TextView) findViewById(R.id.textViewOverviewP1EDH2);
        if (totalPlayers >= 3)
            mTextViewP1EDH3 = (TextView) findViewById(R.id.textViewOverviewP1EDH3);
        if (totalPlayers >= 4)
            mTextViewP1EDH4 = (TextView) findViewById(R.id.textViewOverviewP1EDH4);

        mTextViewP2Name = (TextView) findViewById(R.id.textViewOverviewP2Name);
        mTextViewP2Life = (TextView) findViewById(R.id.textViewOverviewP2Life);
        LinearLayout mLinearEDH2 = (LinearLayout) findViewById(R.id.linearEDH2);
        mLinearEDH2.setWeightSum(totalPlayers);
        lifePositiveP2 = (ImageView) findViewById(R.id.lifePositiveP2);
        lifeNegativeP2 = (ImageView) findViewById(R.id.lifeNegativeP2);
        mTextViewP2EDH1 = (TextView) findViewById(R.id.textViewOverviewP2EDH1);
        mTextViewP2EDH2 = (TextView) findViewById(R.id.textViewOverviewP2EDH2);
        if (totalPlayers >= 3)
            mTextViewP2EDH3 = (TextView) findViewById(R.id.textViewOverviewP2EDH3);
        if (totalPlayers >= 4)
            mTextViewP2EDH4 = (TextView) findViewById(R.id.textViewOverviewP2EDH4);

        if (totalPlayers >= 3) {
            mTextViewP3Name = (TextView) findViewById(R.id.textViewOverviewP3Name);
            mTextViewP3Life = (TextView) findViewById(R.id.textViewOverviewP3Life);
            LinearLayout mLinearEDH3 = (LinearLayout) findViewById(R.id.linearEDH3);
            mLinearEDH3.setWeightSum(totalPlayers);
            lifePositiveP3 = (ImageView) findViewById(R.id.lifePositiveP3);
            lifeNegativeP3 = (ImageView) findViewById(R.id.lifeNegativeP3);
            mTextViewP3EDH1 = (TextView) findViewById(R.id.textViewOverviewP3EDH1);
            mTextViewP3EDH2 = (TextView) findViewById(R.id.textViewOverviewP3EDH2);
            mTextViewP3EDH3 = (TextView) findViewById(R.id.textViewOverviewP3EDH3);
            if (totalPlayers >= 4)
                mTextViewP3EDH4 = (TextView) findViewById(R.id.textViewOverviewP3EDH4);
        }

        if (totalPlayers >= 4) {
            mTextViewP4Name = (TextView) findViewById(R.id.textViewOverviewP4Name);
            mTextViewP4Life = (TextView) findViewById(R.id.textViewOverviewP4Life);
            LinearLayout mLinearEDH4 = (LinearLayout) findViewById(R.id.linearEDH4);
            mLinearEDH4.setWeightSum(totalPlayers);
            lifePositiveP4 = (ImageView) findViewById(R.id.lifePositiveP4);
            lifeNegativeP4 = (ImageView) findViewById(R.id.lifeNegativeP4);
            mTextViewP4EDH1 = (TextView) findViewById(R.id.textViewOverviewP4EDH1);
            mTextViewP4EDH2 = (TextView) findViewById(R.id.textViewOverviewP4EDH2);
            mTextViewP4EDH3 = (TextView) findViewById(R.id.textViewOverviewP4EDH3);
            mTextViewP4EDH4 = (TextView) findViewById(R.id.textViewOverviewP4EDH4);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    public void onClickP1(View view) {
        if (totalPlayers >= 1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "1");
            startActivity(intent);
            this.finish();
        }
    }

    public void onClickP2(View view) {
        if (totalPlayers >= 2) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "2");
            startActivity(intent);
            this.finish();
        }
    }

    public void onClickP3(View view) {
        if (totalPlayers >= 3) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "3");
            startActivity(intent);
            this.finish();
        }
    }

    public void onClickP4(View view) {
        if (totalPlayers >= 4) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("TAG", "4");
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        totalPlayers = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.TOTAL_PLAYERS, 4);
        if (totalPlayers == 2)
            setContentView(R.layout.activity_overview_new_2p);
        else if (totalPlayers == 3)
            setContentView(R.layout.activity_overview_new_3p);
        else
            setContentView(R.layout.activity_overview_new_4p);


        createStatusBar();
        createToolbar();
        createLayout();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        totalPlayers = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.TOTAL_PLAYERS, 4);
        mActivePlayer1 = Utils.loadPlayerFromSharedPreferences(this, 0);
        mActivePlayer2 = Utils.loadPlayerFromSharedPreferences(this, 1);
        if (totalPlayers >= 3)
            mActivePlayer3 = Utils.loadPlayerFromSharedPreferences(this, 2);
        if (totalPlayers >= 4)
            mActivePlayer4 = Utils.loadPlayerFromSharedPreferences(this, 3);

        updateLayout();
    }

    private void createStatusBar() {
        View statusBarBackground = findViewById(R.id.statusBarBackground);
        ViewGroup.LayoutParams params = statusBarBackground.getLayoutParams();
        params.height = Utils.getStatusBarHeight(this);
        statusBarBackground.setLayoutParams(params);
        statusBarBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_color));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black_transparent));
        }
    }

    private void createToolbar() {
        assert getSupportActionBar() != null;
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)));
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("EDHLC: Overview");
    }

    private boolean isPlayerActiveAndAlive(int i) {
        if (totalPlayers < i)
            return false;
        else {
            switch (i) {
                case 1:
                    if (mActivePlayer1 != null)
                        return mActivePlayer1.getPlayerIsAlive();
                case 2:
                    if (mActivePlayer2 != null)
                        return mActivePlayer2.getPlayerIsAlive();
                case 3:
                    if (mActivePlayer3 != null)
                        return mActivePlayer3.getPlayerIsAlive();
                case 4:
                    if (mActivePlayer4 != null)
                        return mActivePlayer4.getPlayerIsAlive();
                default:
                    return false;
            }
        }
    }

    private void updateDethroneIcon() {
        mImageViewThroneP1.setVisibility(View.INVISIBLE);
        mImageViewThroneP2.setVisibility(View.INVISIBLE);
        if (totalPlayers >= 3)
            mImageViewThroneP3.setVisibility(View.INVISIBLE);
        if (totalPlayers >= 4)
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

        mTextViewP1Name.setText(mActivePlayer1.getPlayerDeck().getDeckOwnerName());
        mTextViewP1Name.setEnabled(mActivePlayer1.getPlayerIsAlive());
        mTextViewP1Name.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        mTextViewP1Life.setText(String.valueOf(mActivePlayer1.getPlayerLife()));
        mTextViewP1Life.setEnabled(mActivePlayer1.getPlayerIsAlive());
//        mTextViewP1Life.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        lifePositiveP1.setColorFilter(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        lifeNegativeP1.setColorFilter(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        mTextViewP1EDH1.setText(String.valueOf(mActivePlayer1.getPlayerEDH1()));
        mTextViewP1EDH1.setEnabled(mActivePlayer1.getPlayerIsAlive());
//        mTextViewP1EDH1.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        mTextViewP1EDH2.setText(String.valueOf(mActivePlayer1.getPlayerEDH2()));
        mTextViewP1EDH2.setEnabled(mActivePlayer1.getPlayerIsAlive());
//        mTextViewP1EDH2.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        if (totalPlayers >= 3) {
            mTextViewP1EDH3.setText(String.valueOf(mActivePlayer1.getPlayerEDH3()));
            mTextViewP1EDH3.setEnabled(mActivePlayer1.getPlayerIsAlive());
//            mTextViewP1EDH3.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        }
        if (totalPlayers >= 4) {
            mTextViewP1EDH4.setText(String.valueOf(mActivePlayer1.getPlayerEDH4()));
            mTextViewP1EDH4.setEnabled(mActivePlayer1.getPlayerIsAlive());
//            mTextViewP1EDH4.setTextColor(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        }

        mTextViewP2Name.setText(mActivePlayer2.getPlayerDeck().getDeckOwnerName());
        mTextViewP2Name.setEnabled(mActivePlayer2.getPlayerIsAlive());
        mTextViewP2Name.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        mTextViewP2Life.setText(String.valueOf(mActivePlayer2.getPlayerLife()));
        mTextViewP2Life.setEnabled(mActivePlayer2.getPlayerIsAlive());
//        mTextViewP2Life.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        lifePositiveP2.setColorFilter(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        lifeNegativeP2.setColorFilter(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        mTextViewP2EDH1.setText(String.valueOf(mActivePlayer2.getPlayerEDH1()));
        mTextViewP2EDH1.setEnabled(mActivePlayer2.getPlayerIsAlive());
//        mTextViewP2EDH1.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        mTextViewP2EDH2.setText(String.valueOf(mActivePlayer2.getPlayerEDH2()));
        mTextViewP2EDH2.setEnabled(mActivePlayer2.getPlayerIsAlive());
//        mTextViewP2EDH2.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        if (totalPlayers >= 3) {
            mTextViewP2EDH3.setText(String.valueOf(mActivePlayer2.getPlayerEDH3()));
            mTextViewP2EDH3.setEnabled(mActivePlayer2.getPlayerIsAlive());
//            mTextViewP2EDH3.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        }
        if (totalPlayers >= 4) {
            mTextViewP2EDH4.setText(String.valueOf(mActivePlayer2.getPlayerEDH4()));
            mTextViewP2EDH4.setEnabled(mActivePlayer2.getPlayerIsAlive());
//            mTextViewP2EDH4.setTextColor(mActivePlayer2.getPlayerIsAlive() ? mActivePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        }

        if (totalPlayers >= 3) {
            mTextViewP3Name.setText(mActivePlayer3.getPlayerDeck().getDeckOwnerName());
            mTextViewP3Name.setEnabled(mActivePlayer3.getPlayerIsAlive());
            mTextViewP3Name.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            mTextViewP3Life.setText(String.valueOf(mActivePlayer3.getPlayerLife()));
            mTextViewP3Life.setEnabled(mActivePlayer3.getPlayerIsAlive());
//            mTextViewP3Life.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            lifePositiveP3.setColorFilter(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            lifeNegativeP3.setColorFilter(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            mTextViewP3EDH1.setText(String.valueOf(mActivePlayer3.getPlayerEDH1()));
            mTextViewP3EDH1.setEnabled(mActivePlayer3.getPlayerIsAlive());
//            mTextViewP3EDH1.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            mTextViewP3EDH2.setText(String.valueOf(mActivePlayer3.getPlayerEDH2()));
            mTextViewP3EDH2.setEnabled(mActivePlayer3.getPlayerIsAlive());
//            mTextViewP3EDH2.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            mTextViewP3EDH3.setText(String.valueOf(mActivePlayer3.getPlayerEDH3()));
            mTextViewP3EDH3.setEnabled(mActivePlayer3.getPlayerIsAlive());
//            mTextViewP3EDH3.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            if (totalPlayers >= 4) {
                mTextViewP3EDH4.setText(String.valueOf(mActivePlayer3.getPlayerEDH4()));
                mTextViewP3EDH4.setEnabled(mActivePlayer3.getPlayerIsAlive());
//                mTextViewP3EDH4.setTextColor(mActivePlayer3.getPlayerIsAlive() ? mActivePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            }
        }

        if (totalPlayers >= 4) {
            mTextViewP4Name.setText(mActivePlayer4.getPlayerDeck().getDeckOwnerName());
            mTextViewP4Name.setEnabled(mActivePlayer4.getPlayerIsAlive());
            mTextViewP4Name.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            mTextViewP4Life.setText(String.valueOf(mActivePlayer4.getPlayerLife()));
            mTextViewP4Life.setEnabled(mActivePlayer4.getPlayerIsAlive());
//            mTextViewP4Life.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            lifePositiveP4.setColorFilter(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            lifeNegativeP4.setColorFilter(mActivePlayer1.getPlayerIsAlive() ? mActivePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            mTextViewP4EDH1.setText(String.valueOf(mActivePlayer4.getPlayerEDH1()));
            mTextViewP4EDH1.setEnabled(mActivePlayer4.getPlayerIsAlive());
//            mTextViewP4EDH1.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            mTextViewP4EDH2.setText(String.valueOf(mActivePlayer4.getPlayerEDH2()));
            mTextViewP4EDH2.setEnabled(mActivePlayer4.getPlayerIsAlive());
//            mTextViewP4EDH2.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            mTextViewP4EDH3.setText(String.valueOf(mActivePlayer4.getPlayerEDH3()));
            mTextViewP4EDH3.setEnabled(mActivePlayer4.getPlayerIsAlive());
//            mTextViewP4EDH3.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            mTextViewP4EDH4.setText(String.valueOf(mActivePlayer4.getPlayerEDH4()));
            mTextViewP4EDH4.setEnabled(mActivePlayer4.getPlayerIsAlive());
//            mTextViewP4EDH4.setTextColor(mActivePlayer4.getPlayerIsAlive() ? mActivePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
        }
    }
}
