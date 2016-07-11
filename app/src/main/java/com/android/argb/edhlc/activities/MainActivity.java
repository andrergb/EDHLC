package com.android.argb.edhlc.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Scene;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.HistoryBottomSheet;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.SmoothActionBarDrawerToggle;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;
import com.android.argb.edhlc.objects.ActivePlayer;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* Created by ARGB */
public class MainActivity extends AppCompatActivity {

    //HistoryThreads
    private static Thread mThreadLife1;
    private static Thread mThreadLife2;
    private static Thread mThreadLife3;
    private static Thread mThreadLife4;

    ///Drawer
    private RelativeLayout pBar;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mPlayerDrawer;
    private SmoothActionBarDrawerToggle mDrawerToggle;
    private Switch switchScreen;
    private View statusBarBackground;
    private ActionBar mActionBar;
    private SharedPreferences mSharedPreferences;

    //Players
    private int totalPlayers;
    private ActivePlayer activePlayer1;
    private ActivePlayer activePlayer2;
    private ActivePlayer activePlayer3;
    private ActivePlayer activePlayer4;

    private boolean isInAnimation;
    private TextView textViewResult;
    private ProgressBar progressRandomBar;
    private TextView textViewDiceResult;
    private Menu optionMenu;
    private FrameLayout container;

    private TransitionManager mTransitionManager;

    private Scene overview_scene;
    private Scene big11_scene;
    private Scene big12_scene;
    private Scene big21_scene;
    private Scene big22_scene;

    private int currentSceneOverview = -1;
    private int currentScene11 = 0;
    private int currentScene12 = 1;
    private int currentScene21 = 2;
    private int currentScene22 = 3;

    private int currentScene;
    private int previousScene;
    private float currentScaleLife = 1.0f;

    private ImageView layout11_throne;
    private TextView layout11_name;
    private TextView layout11_deck;
    private TextView layout11_life;
    private ImageView layout11_life_positive;
    private ImageView layout11_life_negative;
    private TextView layout11_edh1;
    private ImageView layout11_negative_EDH1;
    private ImageView layout11_positive_EDH1;
    private TextView layout11_edh1_name;
    private TextView layout11_edh2;
    private ImageView layout11_negative_EDH2;
    private ImageView layout11_positive_EDH2;
    private TextView layout11_edh2_name;
    private TextView layout11_edh3;
    private ImageView layout11_negative_EDH3;
    private ImageView layout11_positive_EDH3;
    private TextView layout11_edh3_name;
    private TextView layout11_edh4;
    private ImageView layout11_negative_EDH4;
    private ImageView layout11_positive_EDH4;
    private TextView layout11_edh4_name;

    private ImageView layout12_throne;
    private TextView layout12_name;
    private TextView layout12_deck;
    private TextView layout12_life;
    private ImageView layout12_life_positive;
    private ImageView layout12_life_negative;
    private TextView layout12_edh1;
    private ImageView layout12_negative_EDH1;
    private ImageView layout12_positive_EDH1;
    private TextView layout12_edh1_name;
    private TextView layout12_edh2;
    private ImageView layout12_negative_EDH2;
    private ImageView layout12_positive_EDH2;
    private TextView layout12_edh2_name;
    private TextView layout12_edh3;
    private ImageView layout12_negative_EDH3;
    private ImageView layout12_positive_EDH3;
    private TextView layout12_edh3_name;
    private TextView layout12_edh4;
    private ImageView layout12_negative_EDH4;
    private ImageView layout12_positive_EDH4;
    private TextView layout12_edh4_name;

    private ImageView layout21_throne;
    private TextView layout21_name;
    private TextView layout21_deck;
    private TextView layout21_life;
    private ImageView layout21_life_positive;
    private ImageView layout21_life_negative;
    private TextView layout21_edh1;
    private ImageView layout21_negative_EDH1;
    private ImageView layout21_positive_EDH1;
    private TextView layout21_edh1_name;
    private TextView layout21_edh2;
    private ImageView layout21_negative_EDH2;
    private ImageView layout21_positive_EDH2;
    private TextView layout21_edh2_name;
    private TextView layout21_edh3;
    private ImageView layout21_negative_EDH3;
    private ImageView layout21_positive_EDH3;
    private TextView layout21_edh3_name;
    private TextView layout21_edh4;
    private ImageView layout21_negative_EDH4;
    private ImageView layout21_positive_EDH4;
    private TextView layout21_edh4_name;

    private ImageView layout22_throne;
    private TextView layout22_name;
    private TextView layout22_deck;
    private TextView layout22_life;
    private ImageView layout22_life_positive;
    private ImageView layout22_life_negative;
    private TextView layout22_edh1;
    private ImageView layout22_negative_EDH1;
    private ImageView layout22_positive_EDH1;
    private TextView layout22_edh1_name;
    private TextView layout22_edh2;
    private ImageView layout22_negative_EDH2;
    private ImageView layout22_positive_EDH2;
    private TextView layout22_edh2_name;
    private TextView layout22_edh3;
    private ImageView layout22_negative_EDH3;
    private ImageView layout22_positive_EDH3;
    private TextView layout22_edh3_name;
    private TextView layout22_edh4;
    private ImageView layout22_negative_EDH4;
    private ImageView layout22_positive_EDH4;
    private TextView layout22_edh4_name;

    @Override
    public void onBackPressed() {
        if (!isInAnimation) {
            if (mDrawerLayout.isDrawerOpen(mPlayerDrawer))
                mDrawerLayout.closeDrawers();
            else if (currentScene != this.currentSceneOverview)
                goToScene(this.currentSceneOverview);
            else
                super.onBackPressed();
        }
    }

    public void onClickDrawerItem(View view) {
        switch (view.getId()) {
            case R.id.drawerItemHome:
                mDrawerLayout.closeDrawers();
                break;

            case R.id.drawerItemPlayers:
                mDrawerLayout.closeDrawers();
                pBar.setVisibility(View.VISIBLE);
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        Intent intentPlayerList = new Intent(MainActivity.this, PlayerListActivity.class);
                        intentPlayerList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentPlayerList);
                    }
                });
                break;

            case R.id.drawerItemRecords:
                mDrawerLayout.closeDrawers();
                pBar.setVisibility(View.VISIBLE);
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, RecordsActivity.class));
                    }
                });
                break;

            case R.id.drawerItemScreen:
                switchScreen.setChecked(!switchScreen.isChecked());
                if (!switchScreen.isChecked()) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).apply();
                } else {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).apply();
                }
                break;

            case R.id.drawerItemSettings:
                mDrawerLayout.closeDrawers();
                pBar.setVisibility(View.VISIBLE);
                mDrawerToggle.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    }
                });
                break;

            case R.id.drawerItemAbout:
                mDrawerLayout.closeDrawers();
                break;
        }
    }

    public void onClickLayout(View view) {
        if (!isInAnimation) {
            switch (view.getId()) {
                //Player 11
                case R.id.layout11_header:
                    if (view.getTag().toString().equalsIgnoreCase(getString(R.string.tag_big)))
                        goToScene(this.currentSceneOverview);
                    else
                        goToScene(this.currentScene11);
                    break;
                case R.id.layout11_life_positive:
                    if (activePlayer1.getPlayerLife() < Constants.MAX_LIFE) {
                        activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() + 1);
                        historyHandler(activePlayer1);
                        updateLayout11();
                        adjustLifeSize(activePlayer1.getPlayerTag(), layout11_life);
                        layout11_life.setAnimation(getBounceAnimation(true, 5));
                    }
                    break;
                case R.id.layout11_life_negative:
                    if (activePlayer1.getPlayerLife() > Constants.MIN_LIFE) {
                        activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() - 1);
                        historyHandler(activePlayer1);
                        updateLayout11();
                        adjustLifeSize(activePlayer1.getPlayerTag(), layout11_life);
                        layout11_life.setAnimation(getBounceAnimation(false, 5));
                    }
                    break;
                case R.id.layout11_positive_EDH1:
                    if (activePlayer1.getPlayerEDH1() < Constants.MAX_EDH) {
                        activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() - 1);
                        activePlayer1.setPlayerEDH1(activePlayer1.getPlayerEDH1() + 1);
                        historyHandler(activePlayer1);
                        updateLayout11();
                        adjustLifeSize(activePlayer1.getPlayerTag(), layout11_life);
                        layout11_life.setAnimation(getBounceAnimation(false, 5));
                        layout11_edh1.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout11_negative_EDH1:
                    if (activePlayer1.getPlayerEDH1() > Constants.MIN_EDH) {
                        activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() + 1);
                        activePlayer1.setPlayerEDH1(activePlayer1.getPlayerEDH1() - 1);
                        historyHandler(activePlayer1);
                        updateLayout11();
                        adjustLifeSize(activePlayer1.getPlayerTag(), layout11_life);
                        layout11_life.setAnimation(getBounceAnimation(true, 5));
                        layout11_edh1.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout11_positive_EDH2:
                    if (activePlayer1.getPlayerEDH2() < Constants.MAX_EDH) {
                        activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() - 1);
                        activePlayer1.setPlayerEDH2(activePlayer1.getPlayerEDH2() + 1);
                        historyHandler(activePlayer1);
                        updateLayout11();
                        adjustLifeSize(activePlayer1.getPlayerTag(), layout11_life);
                        layout11_life.setAnimation(getBounceAnimation(false, 5));
                        layout11_edh2.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout11_negative_EDH2:
                    if (activePlayer1.getPlayerEDH2() > Constants.MIN_EDH) {
                        activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() + 1);
                        activePlayer1.setPlayerEDH2(activePlayer1.getPlayerEDH2() - 1);
                        historyHandler(activePlayer1);
                        updateLayout11();
                        adjustLifeSize(activePlayer1.getPlayerTag(), layout11_life);
                        layout11_life.setAnimation(getBounceAnimation(true, 5));
                        layout11_edh2.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout11_positive_EDH3:
                    if (activePlayer1.getPlayerEDH3() < Constants.MAX_EDH) {
                        activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() - 1);
                        activePlayer1.setPlayerEDH3(activePlayer1.getPlayerEDH3() + 1);
                        historyHandler(activePlayer1);
                        updateLayout11();
                        adjustLifeSize(activePlayer1.getPlayerTag(), layout11_life);
                        layout11_life.setAnimation(getBounceAnimation(false, 5));
                        layout11_edh3.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout11_negative_EDH3:
                    if (activePlayer1.getPlayerEDH3() > Constants.MIN_EDH) {
                        activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() + 1);
                        activePlayer1.setPlayerEDH3(activePlayer1.getPlayerEDH3() - 1);
                        historyHandler(activePlayer1);
                        updateLayout11();
                        adjustLifeSize(activePlayer1.getPlayerTag(), layout11_life);
                        layout11_life.setAnimation(getBounceAnimation(true, 5));
                        layout11_edh3.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout11_positive_EDH4:
                    if (activePlayer1.getPlayerEDH4() < Constants.MAX_EDH) {
                        activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() - 1);
                        activePlayer1.setPlayerEDH4(activePlayer1.getPlayerEDH4() + 1);
                        historyHandler(activePlayer1);
                        updateLayout11();
                        adjustLifeSize(activePlayer1.getPlayerTag(), layout11_life);
                        layout11_life.setAnimation(getBounceAnimation(false, 5));
                        layout11_edh4.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout11_negative_EDH4:
                    if (activePlayer1.getPlayerEDH4() > Constants.MIN_EDH) {
                        activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() + 1);
                        activePlayer1.setPlayerEDH4(activePlayer1.getPlayerEDH4() - 1);
                        historyHandler(activePlayer1);
                        updateLayout11();
                        adjustLifeSize(activePlayer1.getPlayerTag(), layout11_life);
                        layout11_life.setAnimation(getBounceAnimation(true, 5));
                        layout11_edh4.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;

                //Player 12
                case R.id.layout12_header:
                    if (view.getTag().toString().equalsIgnoreCase(getString(R.string.tag_big)))
                        goToScene(this.currentSceneOverview);
                    else
                        goToScene(this.currentScene12);
                    break;
                case R.id.layout12_life_positive:
                    if (activePlayer2.getPlayerLife() < Constants.MAX_LIFE) {
                        activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() + 1);
                        historyHandler(activePlayer2);
                        updateLayout12();
                        adjustLifeSize(activePlayer2.getPlayerTag(), layout12_life);
                        layout12_life.setAnimation(getBounceAnimation(true, 5));
                    }
                    break;
                case R.id.layout12_life_negative:
                    if (activePlayer2.getPlayerLife() > Constants.MIN_LIFE) {
                        activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() - 1);
                        historyHandler(activePlayer2);
                        updateLayout12();
                        adjustLifeSize(activePlayer2.getPlayerTag(), layout12_life);
                        layout12_life.setAnimation(getBounceAnimation(false, 5));
                    }
                    break;
                case R.id.layout12_positive_EDH1:
                    if (activePlayer2.getPlayerEDH1() < Constants.MAX_EDH) {
                        activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() - 1);
                        activePlayer2.setPlayerEDH1(activePlayer2.getPlayerEDH1() + 1);
                        historyHandler(activePlayer2);
                        updateLayout12();
                        adjustLifeSize(activePlayer2.getPlayerTag(), layout12_life);
                        layout12_life.setAnimation(getBounceAnimation(false, 5));
                        layout12_edh1.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout12_negative_EDH1:
                    if (activePlayer2.getPlayerEDH1() > Constants.MIN_EDH) {
                        activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() + 1);
                        activePlayer2.setPlayerEDH1(activePlayer2.getPlayerEDH1() - 1);
                        historyHandler(activePlayer2);
                        updateLayout12();
                        adjustLifeSize(activePlayer2.getPlayerTag(), layout12_life);
                        layout12_life.setAnimation(getBounceAnimation(true, 5));
                        layout12_edh1.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout12_positive_EDH2:
                    if (activePlayer2.getPlayerEDH2() < Constants.MAX_EDH) {
                        activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() - 1);
                        activePlayer2.setPlayerEDH2(activePlayer2.getPlayerEDH2() + 1);
                        historyHandler(activePlayer2);
                        updateLayout12();
                        adjustLifeSize(activePlayer2.getPlayerTag(), layout12_life);
                        layout12_life.setAnimation(getBounceAnimation(false, 5));
                        layout12_edh2.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout12_negative_EDH2:
                    if (activePlayer2.getPlayerEDH2() > Constants.MIN_EDH) {
                        activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() + 1);
                        activePlayer2.setPlayerEDH2(activePlayer2.getPlayerEDH2() - 1);
                        historyHandler(activePlayer2);
                        updateLayout12();
                        adjustLifeSize(activePlayer2.getPlayerTag(), layout12_life);
                        layout12_life.setAnimation(getBounceAnimation(true, 5));
                        layout12_edh2.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout12_positive_EDH3:
                    if (activePlayer2.getPlayerEDH3() < Constants.MAX_EDH) {
                        activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() - 1);
                        activePlayer2.setPlayerEDH3(activePlayer2.getPlayerEDH3() + 1);
                        historyHandler(activePlayer2);
                        updateLayout12();
                        adjustLifeSize(activePlayer2.getPlayerTag(), layout12_life);
                        layout12_life.setAnimation(getBounceAnimation(false, 5));
                        layout12_edh3.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout12_negative_EDH3:
                    if (activePlayer2.getPlayerEDH3() > Constants.MIN_EDH) {
                        activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() + 1);
                        activePlayer2.setPlayerEDH3(activePlayer2.getPlayerEDH3() - 1);
                        historyHandler(activePlayer2);
                        updateLayout12();
                        adjustLifeSize(activePlayer2.getPlayerTag(), layout12_life);
                        layout12_life.setAnimation(getBounceAnimation(true, 5));
                        layout12_edh3.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout12_positive_EDH4:
                    if (activePlayer2.getPlayerEDH4() < Constants.MAX_EDH) {
                        activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() - 1);
                        activePlayer2.setPlayerEDH4(activePlayer2.getPlayerEDH4() + 1);
                        historyHandler(activePlayer2);
                        updateLayout12();
                        adjustLifeSize(activePlayer2.getPlayerTag(), layout12_life);
                        layout12_life.setAnimation(getBounceAnimation(false, 5));
                        layout12_edh4.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout12_negative_EDH4:
                    if (activePlayer2.getPlayerEDH4() > Constants.MIN_EDH) {
                        activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() + 1);
                        activePlayer2.setPlayerEDH4(activePlayer2.getPlayerEDH4() - 1);
                        historyHandler(activePlayer2);
                        updateLayout12();
                        adjustLifeSize(activePlayer2.getPlayerTag(), layout12_life);
                        layout12_life.setAnimation(getBounceAnimation(true, 5));
                        layout12_edh4.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;

                //Player 21
                case R.id.layout21_header:
                    if (view.getTag().toString().equalsIgnoreCase(getString(R.string.tag_big)))
                        goToScene(this.currentSceneOverview);
                    else
                        goToScene(this.currentScene21);
                    break;
                case R.id.layout21_life_positive:
                    if (activePlayer3.getPlayerLife() < Constants.MAX_LIFE) {
                        activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() + 1);
                        historyHandler(activePlayer3);
                        updateLayout21();
                        adjustLifeSize(activePlayer3.getPlayerTag(), layout21_life);
                        layout21_life.setAnimation(getBounceAnimation(true, 5));
                    }
                    break;
                case R.id.layout21_life_negative:
                    if (activePlayer3.getPlayerLife() > Constants.MIN_LIFE) {
                        activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() - 1);
                        historyHandler(activePlayer3);
                        updateLayout21();
                        adjustLifeSize(activePlayer3.getPlayerTag(), layout21_life);
                        layout21_life.setAnimation(getBounceAnimation(false, 5));
                    }
                    break;
                case R.id.layout21_positive_EDH1:
                    if (activePlayer3.getPlayerEDH1() < Constants.MAX_EDH) {
                        activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() - 1);
                        activePlayer3.setPlayerEDH1(activePlayer3.getPlayerEDH1() + 1);
                        historyHandler(activePlayer3);
                        updateLayout21();
                        adjustLifeSize(activePlayer3.getPlayerTag(), layout21_life);
                        layout21_life.setAnimation(getBounceAnimation(false, 5));
                        layout21_edh1.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout21_negative_EDH1:
                    if (activePlayer3.getPlayerEDH1() > Constants.MIN_EDH) {
                        activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() + 1);
                        activePlayer3.setPlayerEDH1(activePlayer3.getPlayerEDH1() - 1);
                        historyHandler(activePlayer3);
                        updateLayout21();
                        adjustLifeSize(activePlayer3.getPlayerTag(), layout21_life);
                        layout21_life.setAnimation(getBounceAnimation(true, 5));
                        layout21_edh1.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout21_positive_EDH2:
                    if (activePlayer3.getPlayerEDH2() < Constants.MAX_EDH) {
                        activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() - 1);
                        activePlayer3.setPlayerEDH2(activePlayer3.getPlayerEDH2() + 1);
                        historyHandler(activePlayer3);
                        updateLayout21();
                        adjustLifeSize(activePlayer3.getPlayerTag(), layout21_life);
                        layout21_life.setAnimation(getBounceAnimation(false, 5));
                        layout21_edh2.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout21_negative_EDH2:
                    if (activePlayer3.getPlayerEDH2() > Constants.MIN_EDH) {
                        activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() + 1);
                        activePlayer3.setPlayerEDH2(activePlayer3.getPlayerEDH2() - 1);
                        historyHandler(activePlayer3);
                        updateLayout21();
                        adjustLifeSize(activePlayer3.getPlayerTag(), layout21_life);
                        layout21_life.setAnimation(getBounceAnimation(true, 5));
                        layout21_edh2.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout21_positive_EDH3:
                    if (activePlayer3.getPlayerEDH3() < Constants.MAX_EDH) {
                        activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() - 1);
                        activePlayer3.setPlayerEDH3(activePlayer3.getPlayerEDH3() + 1);
                        historyHandler(activePlayer3);
                        updateLayout21();
                        adjustLifeSize(activePlayer3.getPlayerTag(), layout21_life);
                        layout21_life.setAnimation(getBounceAnimation(false, 5));
                        layout21_edh3.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout21_negative_EDH3:
                    if (activePlayer3.getPlayerEDH3() > Constants.MIN_EDH) {
                        activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() + 1);
                        activePlayer3.setPlayerEDH3(activePlayer3.getPlayerEDH3() - 1);
                        historyHandler(activePlayer3);
                        updateLayout21();
                        adjustLifeSize(activePlayer3.getPlayerTag(), layout21_life);
                        layout21_life.setAnimation(getBounceAnimation(true, 5));
                        layout21_edh3.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout21_positive_EDH4:
                    if (activePlayer3.getPlayerEDH4() < Constants.MAX_EDH) {
                        activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() - 1);
                        activePlayer3.setPlayerEDH4(activePlayer3.getPlayerEDH4() + 1);
                        historyHandler(activePlayer3);
                        updateLayout21();
                        adjustLifeSize(activePlayer3.getPlayerTag(), layout21_life);
                        layout21_life.setAnimation(getBounceAnimation(false, 5));
                        layout21_edh4.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout21_negative_EDH4:
                    if (activePlayer3.getPlayerEDH4() > Constants.MIN_EDH) {
                        activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() + 1);
                        activePlayer3.setPlayerEDH4(activePlayer3.getPlayerEDH4() - 1);
                        historyHandler(activePlayer3);
                        updateLayout21();
                        adjustLifeSize(activePlayer3.getPlayerTag(), layout21_life);
                        layout21_life.setAnimation(getBounceAnimation(true, 5));
                        layout21_edh4.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;

                //Player 22
                case R.id.layout22_header:
                    if (view.getTag().toString().equalsIgnoreCase(getString(R.string.tag_big)))
                        goToScene(this.currentSceneOverview);
                    else
                        goToScene(this.currentScene22);
                    break;
                case R.id.layout22_life_positive:
                    if (activePlayer4.getPlayerLife() < Constants.MAX_LIFE) {
                        activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() + 1);
                        historyHandler(activePlayer4);
                        updateLayout22();
                        adjustLifeSize(activePlayer4.getPlayerTag(), layout22_life);
                        layout22_life.setAnimation(getBounceAnimation(true, 5));
                    }
                    break;
                case R.id.layout22_life_negative:
                    if (activePlayer4.getPlayerLife() > Constants.MIN_LIFE) {
                        activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() - 1);
                        historyHandler(activePlayer4);
                        updateLayout22();
                        adjustLifeSize(activePlayer4.getPlayerTag(), layout22_life);
                        layout22_life.setAnimation(getBounceAnimation(false, 5));
                    }
                    break;
                case R.id.layout22_positive_EDH1:
                    if (activePlayer4.getPlayerEDH1() < Constants.MAX_EDH) {
                        activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() - 1);
                        activePlayer4.setPlayerEDH1(activePlayer4.getPlayerEDH1() + 1);
                        historyHandler(activePlayer4);
                        updateLayout22();
                        adjustLifeSize(activePlayer4.getPlayerTag(), layout22_life);
                        layout22_life.setAnimation(getBounceAnimation(false, 5));
                        layout22_edh1.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout22_negative_EDH1:
                    if (activePlayer4.getPlayerEDH1() > Constants.MIN_EDH) {
                        activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() + 1);
                        activePlayer4.setPlayerEDH1(activePlayer4.getPlayerEDH1() - 1);
                        historyHandler(activePlayer4);
                        updateLayout22();
                        adjustLifeSize(activePlayer4.getPlayerTag(), layout22_life);
                        layout22_life.setAnimation(getBounceAnimation(true, 5));
                        layout22_edh1.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout22_positive_EDH2:
                    if (activePlayer4.getPlayerEDH2() < Constants.MAX_EDH) {
                        activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() - 1);
                        activePlayer4.setPlayerEDH2(activePlayer4.getPlayerEDH2() + 1);
                        historyHandler(activePlayer4);
                        updateLayout22();
                        adjustLifeSize(activePlayer4.getPlayerTag(), layout22_life);
                        layout22_life.setAnimation(getBounceAnimation(false, 5));
                        layout22_edh2.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout22_negative_EDH2:
                    if (activePlayer4.getPlayerEDH2() > Constants.MIN_EDH) {
                        activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() + 1);
                        activePlayer4.setPlayerEDH2(activePlayer4.getPlayerEDH2() - 1);
                        historyHandler(activePlayer4);
                        updateLayout22();
                        adjustLifeSize(activePlayer4.getPlayerTag(), layout22_life);
                        layout22_life.setAnimation(getBounceAnimation(true, 5));
                        layout22_edh2.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout22_positive_EDH3:
                    if (activePlayer4.getPlayerEDH3() < Constants.MAX_EDH) {
                        activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() - 1);
                        activePlayer4.setPlayerEDH3(activePlayer4.getPlayerEDH3() + 1);
                        historyHandler(activePlayer4);
                        updateLayout22();
                        adjustLifeSize(activePlayer4.getPlayerTag(), layout22_life);
                        layout22_life.setAnimation(getBounceAnimation(false, 5));
                        layout22_edh3.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout22_negative_EDH3:
                    if (activePlayer4.getPlayerEDH3() > Constants.MIN_EDH) {
                        activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() + 1);
                        activePlayer4.setPlayerEDH3(activePlayer4.getPlayerEDH3() - 1);
                        historyHandler(activePlayer4);
                        updateLayout22();
                        adjustLifeSize(activePlayer4.getPlayerTag(), layout22_life);
                        layout22_life.setAnimation(getBounceAnimation(true, 5));
                        layout22_edh3.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;
                case R.id.layout22_positive_EDH4:
                    if (activePlayer4.getPlayerEDH4() < Constants.MAX_EDH) {
                        activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() - 1);
                        activePlayer4.setPlayerEDH4(activePlayer4.getPlayerEDH4() + 1);
                        historyHandler(activePlayer4);
                        updateLayout22();
                        adjustLifeSize(activePlayer4.getPlayerTag(), layout22_life);
                        layout22_life.setAnimation(getBounceAnimation(false, 5));
                        layout22_edh4.setAnimation(getBounceAnimation(true, 10));
                    }
                    break;
                case R.id.layout22_negative_EDH4:
                    if (activePlayer4.getPlayerEDH4() > Constants.MIN_EDH) {
                        activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() + 1);
                        activePlayer4.setPlayerEDH4(activePlayer4.getPlayerEDH4() - 1);
                        historyHandler(activePlayer4);
                        updateLayout22();
                        adjustLifeSize(activePlayer4.getPlayerTag(), layout22_life);
                        layout22_life.setAnimation(getBounceAnimation(true, 5));
                        layout22_edh4.setAnimation(getBounceAnimation(false, 10));
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public void onClickNewGame(View view) {
        startActivity(new Intent(this, NewGameActivity.class));
        this.finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (currentScene == this.currentSceneOverview) {
            optionMenu.getItem(Integer.valueOf(getString(R.string.menu_main_history_index))).setEnabled(false);
            optionMenu.getItem(Integer.valueOf(getString(R.string.menu_main_history_index))).setVisible(false);
        } else {
            optionMenu.getItem(Integer.valueOf(getString(R.string.menu_main_history_index))).setEnabled(true);
            optionMenu.getItem(Integer.valueOf(getString(R.string.menu_main_history_index))).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!isInAnimation) {
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }

            //Option menu
            switch (item.getItemId()) {
                case R.id.action_history:
                    if (currentScene != this.currentSceneOverview)
                        displayBottomSheetsHistory();
                    break;

                case R.id.actions_new_game:
                    Intent intent = new Intent(this, NewGameActivity.class);
                    intent.putExtra("NEW_GAME_IS_VALID", isValidGame());

                    if (isValidGame()) {
                        intent.putExtra("NEW_GAME_TOTAL_PLAYER", totalPlayers);

                        intent.putExtra("NEW_GAME_PLAYER_1", activePlayer1.getPlayerDeck().getDeckOwnerName());
                        intent.putExtra("NEW_GAME_DECK_1", activePlayer1.getPlayerDeck().getDeckName());

                        intent.putExtra("NEW_GAME_PLAYER_2", activePlayer2.getPlayerDeck().getDeckOwnerName());
                        intent.putExtra("NEW_GAME_DECK_2", activePlayer2.getPlayerDeck().getDeckName());

                        if (totalPlayers >= 3) {
                            intent.putExtra("NEW_GAME_PLAYER_3", activePlayer3.getPlayerDeck().getDeckOwnerName());
                            intent.putExtra("NEW_GAME_DECK_3", activePlayer3.getPlayerDeck().getDeckName());
                        }
                        if (totalPlayers >= 4) {
                            intent.putExtra("NEW_GAME_PLAYER_4", activePlayer4.getPlayerDeck().getDeckOwnerName());
                            intent.putExtra("NEW_GAME_DECK_4", activePlayer4.getPlayerDeck().getDeckName());
                        }
                    }

                    startActivity(intent);
                    this.finish();
                    break;

                case R.id.actions_log_game:
                    if (isValidGame()) {
                        Intent intentLog = new Intent(this, LogGameActivity.class);
                        intentLog.putExtra("LOG_GAME_TOTAL_PLAYERS", totalPlayers);

                        intentLog.putExtra("LOG_GAME_PLAYER_1", activePlayer1.getPlayerDeck().getDeckOwnerName());
                        intentLog.putExtra("LOG_GAME_DECK_1", activePlayer1.getPlayerDeck().getDeckName());

                        intentLog.putExtra("LOG_GAME_PLAYER_2", activePlayer2.getPlayerDeck().getDeckOwnerName());
                        intentLog.putExtra("LOG_GAME_DECK_2", activePlayer2.getPlayerDeck().getDeckName());

                        if (totalPlayers >= 3) {
                            intentLog.putExtra("LOG_GAME_PLAYER_3", activePlayer3.getPlayerDeck().getDeckOwnerName());
                            intentLog.putExtra("LOG_GAME_DECK_3", activePlayer3.getPlayerDeck().getDeckName());
                        }
                        if (totalPlayers >= 4) {
                            intentLog.putExtra("LOG_GAME_PLAYER_4", activePlayer4.getPlayerDeck().getDeckOwnerName());
                            intentLog.putExtra("LOG_GAME_DECK_4", activePlayer4.getPlayerDeck().getDeckName());
                        }

                        startActivity(intentLog);
                        this.finish();
                    }
                    break;

                case R.id.actions_roll_dice:
                    showDiceDialog();
                    break;

                case R.id.actions_random_player:
                    if (isValidGame())
                        showRandomPlayerDialog();
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isValidGame()) {
            if (activePlayer1 != null)
                Utils.savePlayerInSharedPreferences(this, activePlayer1);
            if (activePlayer2 != null)
                Utils.savePlayerInSharedPreferences(this, activePlayer2);
            if (totalPlayers >= 3 && activePlayer3 != null)
                Utils.savePlayerInSharedPreferences(this, activePlayer3);
            if (totalPlayers >= 4 && activePlayer4 != null)
                Utils.savePlayerInSharedPreferences(this, activePlayer4);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (FrameLayout) findViewById(R.id.container);

        mSharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE);
        totalPlayers = mSharedPreferences.getInt(Constants.CURRENT_GAME_TOTAL_PLAYERS, 0);

        createStatusBar();
        createToolbar();
        createDrawer();
        isInAnimation = false;

        if (isValidGame()) {
            createScenes();

            activePlayer1 = Utils.loadPlayerFromSharedPreferences(this, 0);
            activePlayer2 = Utils.loadPlayerFromSharedPreferences(this, 1);
            if (totalPlayers >= 3)
                activePlayer3 = Utils.loadPlayerFromSharedPreferences(this, 2);
            if (totalPlayers >= 4)
                activePlayer4 = Utils.loadPlayerFromSharedPreferences(this, 3);

            createGameLayout();
            adjustLifeSize(true);
            updateLayout();
        } else {
            container.removeAllViews();
            setActionBarColor(ContextCompat.getColor(MainActivity.this, R.color.primary_color));
            getLayoutInflater().inflate(R.layout.scene_new_game, container, true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pBar != null)
            pBar.setVisibility(View.GONE);

        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            switchScreen.setChecked(true);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            switchScreen.setChecked(false);
        }
        if (isValidGame()) {
            startTimerCounter();
        } else {
            container.removeAllViews();
            setActionBarColor(ContextCompat.getColor(MainActivity.this, R.color.primary_color));
            getLayoutInflater().inflate(R.layout.scene_new_game, container, true);
        }
    }

    @Override
    protected void onStop() {
        if (!isValidGame()) {
            if (activePlayer1 != null)
                Utils.savePlayerInSharedPreferences(this, activePlayer1);
            if (activePlayer2 != null)
                Utils.savePlayerInSharedPreferences(this, activePlayer2);
            if (totalPlayers >= 3 && activePlayer3 != null)
                Utils.savePlayerInSharedPreferences(this, activePlayer3);
            if (totalPlayers >= 4 && activePlayer4 != null)
                Utils.savePlayerInSharedPreferences(this, activePlayer4);
        }
        super.onStop();
    }

    private void adjustLifeSize(boolean updateAll) {
        if (updateAll) {
            adjustLifeSize(0, layout11_life);
            adjustLifeSize(1, layout12_life);
            if (totalPlayers >= 3)
                adjustLifeSize(2, layout21_life);
            if (totalPlayers >= 4)
                adjustLifeSize(3, layout22_life);
        } else {
            //Adjust every view, except the animated one that has been scaled on animationEnd
            if (previousScene == this.currentScene11) {
                adjustLifeSize(1, layout12_life);
                if (totalPlayers >= 3)
                    adjustLifeSize(2, layout21_life);
                if (totalPlayers >= 4)
                    adjustLifeSize(3, layout22_life);
            } else if (previousScene == this.currentScene12) {
                adjustLifeSize(0, layout11_life);
                if (totalPlayers >= 3)
                    adjustLifeSize(2, layout21_life);
                if (totalPlayers >= 4)
                    adjustLifeSize(3, layout22_life);
            } else if (totalPlayers >= 3 && previousScene == this.currentScene21) {
                adjustLifeSize(0, layout11_life);
                adjustLifeSize(1, layout12_life);
                if (totalPlayers >= 4)
                    adjustLifeSize(3, layout22_life);
            } else if (totalPlayers >= 4 && previousScene == this.currentScene22) {
                adjustLifeSize(0, layout11_life);
                adjustLifeSize(1, layout12_life);
                adjustLifeSize(2, layout21_life);
            }
        }
    }

    private void adjustLifeSize(int player, TextView lifeValue) {
        float toScale;
        TypedValue outValue = new TypedValue();
        //TWO DIGITS
        if (getActivePlayer(player).getPlayerLife() <= 99 && getActivePlayer(player).getPlayerLife() >= -9) {
            if (currentScene != this.currentSceneOverview) {
                getResources().getValue(R.dimen.scale_single_view_2_digits, outValue, true);
            } else {
                if (totalPlayers == 4 || (totalPlayers == 3 && (player == 0 || player == 1))) {
                    getResources().getValue(R.dimen.scale_overview_2_digits, outValue, true);
                } else {
                    getResources().getValue(R.dimen.scale_overview_2_digits_max, outValue, true);
                }
            }
        }
        //THREE DIGITS
        else {
            if (currentScene != this.currentSceneOverview) {
                getResources().getValue(R.dimen.scale_single_view_3_digits, outValue, true);
            } else {
                if (totalPlayers == 4 || (totalPlayers == 3 && (player == 0 || player == 1))) {
                    getResources().getValue(R.dimen.scale_overview_3_digits, outValue, true);
                } else {
                    getResources().getValue(R.dimen.scale_overview_3_digits_max, outValue, true);
                }
            }
        }

        toScale = outValue.getFloat();

        lifeValue.setScaleX(toScale);
        lifeValue.setScaleY(toScale);
    }

    private void animateEDHValue(int type) {
        TypedValue outValue = new TypedValue();
        if (type == Constants.EXPAND) {
            getResources().getValue(R.dimen.scale_single_view_edh, outValue, true);
        } else {
            getResources().getValue(R.dimen.scale_overview_edh, outValue, true);
        }

        float toScale = outValue.getFloat();

        layout11_edh1.clearAnimation();
        layout11_edh1.setAnimation(getScaleAnimation(layout11_edh1, toScale, type));
        layout11_edh2.clearAnimation();
        layout11_edh2.setAnimation(getScaleAnimation(layout11_edh2, toScale, type));
        if (totalPlayers >= 3) {
            layout11_edh3.clearAnimation();
            layout11_edh3.setAnimation(getScaleAnimation(layout11_edh3, toScale, type));
        }
        if (totalPlayers >= 4) {
            layout11_edh4.clearAnimation();
            layout11_edh4.setAnimation(getScaleAnimation(layout11_edh4, toScale, type));
        }

        layout12_edh1.clearAnimation();
        layout12_edh1.setAnimation(getScaleAnimation(layout12_edh1, toScale, type));
        layout12_edh2.clearAnimation();
        layout12_edh2.setAnimation(getScaleAnimation(layout12_edh2, toScale, type));
        if (totalPlayers >= 3) {
            layout12_edh3.clearAnimation();
            layout12_edh3.setAnimation(getScaleAnimation(layout12_edh3, toScale, type));
        }
        if (totalPlayers >= 4) {
            layout12_edh4.clearAnimation();
            layout12_edh4.setAnimation(getScaleAnimation(layout12_edh4, toScale, type));
        }

        if (totalPlayers >= 3) {
            layout21_edh1.clearAnimation();
            layout21_edh1.setAnimation(getScaleAnimation(layout21_edh1, toScale, type));
            layout21_edh2.clearAnimation();
            layout21_edh2.setAnimation(getScaleAnimation(layout21_edh2, toScale, type));
            layout21_edh3.clearAnimation();
            layout21_edh3.setAnimation(getScaleAnimation(layout21_edh3, toScale, type));
            if (totalPlayers >= 4) {
                layout21_edh4.clearAnimation();
                layout21_edh4.setAnimation(getScaleAnimation(layout21_edh4, toScale, type));
            }
        }

        if (totalPlayers >= 4) {
            layout22_edh1.clearAnimation();
            layout22_edh2.clearAnimation();
            layout22_edh3.clearAnimation();
            layout22_edh4.clearAnimation();
            layout22_edh1.setAnimation(getScaleAnimation(layout22_edh1, toScale, type));
            layout22_edh2.setAnimation(getScaleAnimation(layout22_edh2, toScale, type));
            layout22_edh3.setAnimation(getScaleAnimation(layout22_edh3, toScale, type));
            layout22_edh4.setAnimation(getScaleAnimation(layout22_edh4, toScale, type));
        }
    }

    private void animateLifeValue(int player, int type) {
        float toScale;
        TypedValue outValue = new TypedValue();

        //TWO DIGITS
        if (getActivePlayer(player).getPlayerLife() <= 99 && getActivePlayer(player).getPlayerLife() >= -9) {
            //EXPAND
            if (type == Constants.EXPAND) {
                getResources().getValue(R.dimen.scale_single_view_2_digits, outValue, true);
            } else {
                //COLLAPSE
                if (totalPlayers == 4 || (totalPlayers == 3 && (player == 0 || player == 1))) {
                    getResources().getValue(R.dimen.scale_overview_2_digits, outValue, true);
                } else {
                    getResources().getValue(R.dimen.scale_overview_2_digits_max, outValue, true);
                }
            }
        }
        //THREE DIGITS
        else {
            //EXPAND
            if (type == Constants.EXPAND) {
                getResources().getValue(R.dimen.scale_single_view_3_digits, outValue, true);
            } else {
                //COLLAPSE
                if (totalPlayers == 4 || (totalPlayers == 3 && (player == 0 || player == 1))) {
                    getResources().getValue(R.dimen.scale_overview_3_digits, outValue, true);
                } else {
                    getResources().getValue(R.dimen.scale_overview_3_digits_max, outValue, true);
                }
            }
        }

        toScale = outValue.getFloat();

        if (player == 0) {
            layout11_life.clearAnimation();
            layout11_life.setAnimation(getLifeScaleAnimation(layout11_life, toScale, type));
        } else if (player == 1) {
            layout12_life.clearAnimation();
            layout12_life.setAnimation(getLifeScaleAnimation(layout12_life, toScale, type));
        } else if (player == 2) {
            layout21_life.clearAnimation();
            layout21_life.setAnimation(getLifeScaleAnimation(layout21_life, toScale, type));
        } else if (player == 3) {
            layout22_life.clearAnimation();
            layout22_life.setAnimation(getLifeScaleAnimation(layout22_life, toScale, type));
        }
    }

    private void animateName(int type) {
        TypedValue outValue = new TypedValue();
        if (type == Constants.EXPAND) {
            getResources().getValue(R.dimen.scale_single_view_name, outValue, true);
        } else {
            getResources().getValue(R.dimen.scale_overview_name, outValue, true);
        }

        float toScale = outValue.getFloat();

        layout11_name.clearAnimation();
        layout11_name.setAnimation(getScaleAnimation(layout11_name, toScale, type));

        layout12_name.clearAnimation();
        layout12_name.setAnimation(getScaleAnimation(layout12_name, toScale, type));

        if (totalPlayers >= 3) {
            layout21_name.clearAnimation();
            layout21_name.setAnimation(getScaleAnimation(layout21_name, toScale, type));
        }

        if (totalPlayers >= 4) {
            layout22_name.clearAnimation();
            layout22_name.setAnimation(getScaleAnimation(layout22_name, toScale, type));
        }
    }

    private void createDrawer() {
        pBar = (RelativeLayout) findViewById(R.id.loading_progress);

        mPlayerDrawer = (LinearLayout) findViewById(R.id.main_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mDrawerToggle = new SmoothActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        LinearLayout drawerItemHome = (LinearLayout) findViewById(R.id.drawerItemHome);
        ImageView drawerItemIconHome = (ImageView) findViewById(R.id.drawerItemIconHome);
        TextView drawerItemTextHome = (TextView) findViewById(R.id.drawerItemTextHome);
        if (drawerItemHome != null) {
            drawerItemHome.setBackgroundColor(ContextCompat.getColor(this, R.color.gray200));
        }
        if (drawerItemIconHome != null) {
            drawerItemIconHome.setColorFilter(ContextCompat.getColor(this, R.color.accent_color));
        }
        if (drawerItemTextHome != null) {
            drawerItemTextHome.setTextColor(ContextCompat.getColor(this, R.color.accent_color));
            drawerItemTextHome.setTypeface(null, Typeface.BOLD);
        }

        switchScreen = (Switch) findViewById(R.id.switchScreen);
        switchScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switchScreen.isChecked()) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).apply();
                } else {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).apply();
                }
            }
        });
    }

    private void createGameLayout() {
        createLayout11();
        createLayout12();
        createLayout21();
        createLayout22();
    }

    private void createLayout11() {
        layout11_throne = (ImageView) container.findViewById(R.id.layout11_throne);
        layout11_throne.setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);
        layout11_name = (TextView) container.findViewById(R.id.layout11_name);
        layout11_deck = (TextView) container.findViewById(R.id.layout11_deck);
        layout11_life = (TextView) container.findViewById(R.id.layout11_life);
        layout11_life_positive = (ImageView) container.findViewById(R.id.layout11_life_positive);
        layout11_life_negative = (ImageView) container.findViewById(R.id.layout11_life_negative);

        if (currentScene != this.currentSceneOverview) {
            layout11_positive_EDH1 = (ImageView) container.findViewById(R.id.layout11_positive_EDH1);
            layout11_negative_EDH1 = (ImageView) container.findViewById(R.id.layout11_negative_EDH1);

            layout11_positive_EDH2 = (ImageView) container.findViewById(R.id.layout11_positive_EDH2);
            layout11_negative_EDH2 = (ImageView) container.findViewById(R.id.layout11_negative_EDH2);

            if (totalPlayers >= 3) {
                layout11_positive_EDH3 = (ImageView) container.findViewById(R.id.layout11_positive_EDH3);
                layout11_negative_EDH3 = (ImageView) container.findViewById(R.id.layout11_negative_EDH3);
            }

            if (totalPlayers == 4) {
                layout11_positive_EDH4 = (ImageView) container.findViewById(R.id.layout11_positive_EDH4);
                layout11_negative_EDH4 = (ImageView) container.findViewById(R.id.layout11_negative_EDH4);
            }
        }

        layout11_edh1 = (TextView) container.findViewById(R.id.layout11_edh1);
        layout11_edh2 = (TextView) container.findViewById(R.id.layout11_edh2);

        if (currentScene != this.currentSceneOverview) {
            layout11_edh1_name = (TextView) container.findViewById(R.id.layout11_edh1_name);
            layout11_edh2_name = (TextView) container.findViewById(R.id.layout11_edh2_name);
        }

        if (totalPlayers >= 3) {
            layout11_edh3 = (TextView) container.findViewById(R.id.layout11_edh3);
            if (currentScene != this.currentSceneOverview)
                layout11_edh3_name = (TextView) container.findViewById(R.id.layout11_edh3_name);
        }

        if (totalPlayers >= 4) {
            layout11_edh4 = (TextView) container.findViewById(R.id.layout11_edh4);
            if (currentScene != this.currentSceneOverview)
                layout11_edh4_name = (TextView) container.findViewById(R.id.layout11_edh4_name);
        }
    }

    private void createLayout12() {
        layout12_throne = (ImageView) container.findViewById(R.id.layout12_throne);
        layout12_throne.setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);
        layout12_name = (TextView) container.findViewById(R.id.layout12_name);
        layout12_deck = (TextView) container.findViewById(R.id.layout12_deck);
        layout12_life = (TextView) container.findViewById(R.id.layout12_life);
        layout12_life_positive = (ImageView) container.findViewById(R.id.layout12_life_positive);
        layout12_life_negative = (ImageView) container.findViewById(R.id.layout12_life_negative);

        if (currentScene != this.currentSceneOverview) {
            layout12_positive_EDH1 = (ImageView) container.findViewById(R.id.layout12_positive_EDH1);
            layout12_negative_EDH1 = (ImageView) container.findViewById(R.id.layout12_negative_EDH1);

            layout12_positive_EDH2 = (ImageView) container.findViewById(R.id.layout12_positive_EDH2);
            layout12_negative_EDH2 = (ImageView) container.findViewById(R.id.layout12_negative_EDH2);

            if (totalPlayers >= 3) {
                layout12_positive_EDH3 = (ImageView) container.findViewById(R.id.layout12_positive_EDH3);
                layout12_negative_EDH3 = (ImageView) container.findViewById(R.id.layout12_negative_EDH3);
            }

            if (totalPlayers == 4) {
                layout12_positive_EDH4 = (ImageView) container.findViewById(R.id.layout12_positive_EDH4);
                layout12_negative_EDH4 = (ImageView) container.findViewById(R.id.layout12_negative_EDH4);
            }
        }

        layout12_edh1 = (TextView) container.findViewById(R.id.layout12_edh1);
        layout12_edh2 = (TextView) container.findViewById(R.id.layout12_edh2);

        if (currentScene != this.currentSceneOverview) {
            layout12_edh1_name = (TextView) container.findViewById(R.id.layout12_edh1_name);
            layout12_edh2_name = (TextView) container.findViewById(R.id.layout12_edh2_name);
        }

        if (totalPlayers >= 3) {
            layout12_edh3 = (TextView) container.findViewById(R.id.layout12_edh3);
            if (currentScene != this.currentSceneOverview)
                layout12_edh3_name = (TextView) container.findViewById(R.id.layout12_edh3_name);
        }

        if (totalPlayers >= 4) {
            layout12_edh4 = (TextView) container.findViewById(R.id.layout12_edh4);
            if (currentScene != this.currentSceneOverview)
                layout12_edh4_name = (TextView) container.findViewById(R.id.layout12_edh4_name);
        }
    }

    private void createLayout21() {
        if (totalPlayers >= 3) {
            layout21_throne = (ImageView) container.findViewById(R.id.layout21_throne);
            layout21_throne.setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);
            layout21_name = (TextView) container.findViewById(R.id.layout21_name);
            layout21_deck = (TextView) container.findViewById(R.id.layout21_deck);
            layout21_life = (TextView) container.findViewById(R.id.layout21_life);
            layout21_life_positive = (ImageView) container.findViewById(R.id.layout21_life_positive);
            layout21_life_negative = (ImageView) container.findViewById(R.id.layout21_life_negative);

            if (currentScene != this.currentSceneOverview) {
                layout21_positive_EDH1 = (ImageView) container.findViewById(R.id.layout21_positive_EDH1);
                layout21_negative_EDH1 = (ImageView) container.findViewById(R.id.layout21_negative_EDH1);

                layout21_positive_EDH2 = (ImageView) container.findViewById(R.id.layout21_positive_EDH2);
                layout21_negative_EDH2 = (ImageView) container.findViewById(R.id.layout21_negative_EDH2);

                layout21_positive_EDH3 = (ImageView) container.findViewById(R.id.layout21_positive_EDH3);
                layout21_negative_EDH3 = (ImageView) container.findViewById(R.id.layout21_negative_EDH3);

                if (totalPlayers == 4) {
                    layout21_positive_EDH4 = (ImageView) container.findViewById(R.id.layout21_positive_EDH4);
                    layout21_negative_EDH4 = (ImageView) container.findViewById(R.id.layout21_negative_EDH4);
                }
            }

            layout21_edh1 = (TextView) container.findViewById(R.id.layout21_edh1);
            layout21_edh2 = (TextView) container.findViewById(R.id.layout21_edh2);
            layout21_edh3 = (TextView) container.findViewById(R.id.layout21_edh3);

            if (currentScene != this.currentSceneOverview) {
                layout21_edh1_name = (TextView) container.findViewById(R.id.layout21_edh1_name);
                layout21_edh2_name = (TextView) container.findViewById(R.id.layout21_edh2_name);
                layout21_edh3_name = (TextView) container.findViewById(R.id.layout21_edh3_name);
            }

            if (totalPlayers >= 4) {
                layout21_edh4 = (TextView) container.findViewById(R.id.layout21_edh4);
                if (currentScene != this.currentSceneOverview)
                    layout21_edh4_name = (TextView) container.findViewById(R.id.layout21_edh4_name);
            }
        }
    }

    private void createLayout22() {
        if (totalPlayers >= 4) {
            layout22_throne = (ImageView) container.findViewById(R.id.layout22_throne);
            layout22_throne.setColorFilter(ContextCompat.getColor(this, R.color.accent_color), PorterDuff.Mode.SRC_IN);
            layout22_name = (TextView) container.findViewById(R.id.layout22_name);
            layout22_deck = (TextView) container.findViewById(R.id.layout22_deck);
            layout22_life = (TextView) container.findViewById(R.id.layout22_life);
            layout22_life_positive = (ImageView) container.findViewById(R.id.layout22_life_positive);
            layout22_life_negative = (ImageView) container.findViewById(R.id.layout22_life_negative);

            if (currentScene != this.currentSceneOverview) {
                layout22_positive_EDH1 = (ImageView) container.findViewById(R.id.layout22_positive_EDH1);
                layout22_negative_EDH1 = (ImageView) container.findViewById(R.id.layout22_negative_EDH1);

                layout22_positive_EDH2 = (ImageView) container.findViewById(R.id.layout22_positive_EDH2);
                layout22_negative_EDH2 = (ImageView) container.findViewById(R.id.layout22_negative_EDH2);

                layout22_positive_EDH3 = (ImageView) container.findViewById(R.id.layout22_positive_EDH3);
                layout22_negative_EDH3 = (ImageView) container.findViewById(R.id.layout22_negative_EDH3);

                layout22_positive_EDH4 = (ImageView) container.findViewById(R.id.layout22_positive_EDH4);
                layout22_negative_EDH4 = (ImageView) container.findViewById(R.id.layout22_negative_EDH4);
            }

            layout22_edh1 = (TextView) container.findViewById(R.id.layout22_edh1);
            layout22_edh2 = (TextView) container.findViewById(R.id.layout22_edh2);
            layout22_edh3 = (TextView) container.findViewById(R.id.layout22_edh3);
            layout22_edh4 = (TextView) container.findViewById(R.id.layout22_edh4);

            if (currentScene != this.currentSceneOverview) {
                layout22_edh1_name = (TextView) container.findViewById(R.id.layout22_edh1_name);
                layout22_edh2_name = (TextView) container.findViewById(R.id.layout22_edh2_name);
                layout22_edh3_name = (TextView) container.findViewById(R.id.layout22_edh3_name);
                layout22_edh4_name = (TextView) container.findViewById(R.id.layout22_edh4_name);
            }
        }
    }

    private void createScenes() {
        container.removeAllViews();

        TransitionInflater transitionInflater = TransitionInflater.from(this);
        mTransitionManager = transitionInflater.inflateTransitionManager(R.transition.transition_manager, container);

        if (totalPlayers == 2) {
            getLayoutInflater().inflate(R.layout.scene_overview_2_players, container, true);
            overview_scene = Scene.getSceneForLayout(container, R.layout.scene_overview_2_players, this);
            big11_scene = Scene.getSceneForLayout(container, R.layout.big11_2p, this);
            big12_scene = Scene.getSceneForLayout(container, R.layout.big12_2p, this);
        } else if (totalPlayers == 3) {
            getLayoutInflater().inflate(R.layout.scene_overview_3_players, container, true);
            overview_scene = Scene.getSceneForLayout(container, R.layout.scene_overview_3_players, this);
            big11_scene = Scene.getSceneForLayout(container, R.layout.big11_3p, this);
            big12_scene = Scene.getSceneForLayout(container, R.layout.big12_3p, this);
            big21_scene = Scene.getSceneForLayout(container, R.layout.big21_3p, this);
        } else {
            getLayoutInflater().inflate(R.layout.scene_overview_4_players, container, true);
            overview_scene = Scene.getSceneForLayout(container, R.layout.scene_overview_4_players, this);
            big11_scene = Scene.getSceneForLayout(container, R.layout.big11_4p, this);
            big12_scene = Scene.getSceneForLayout(container, R.layout.big12_4p, this);
            big21_scene = Scene.getSceneForLayout(container, R.layout.big21_4p, this);
            big22_scene = Scene.getSceneForLayout(container, R.layout.big22_4p, this);
        }

        currentScene = this.currentSceneOverview;
    }

    private void createStatusBar() {
        statusBarBackground = findViewById(R.id.statusBarBackground);
        if (statusBarBackground != null) {
            ViewGroup.LayoutParams params = statusBarBackground.getLayoutParams();
            params.height = Utils.getStatusBarHeight(this);
            statusBarBackground.setLayoutParams(params);
            statusBarBackground.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_color));
        }

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
        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)));
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
    }

    private void displayBottomSheetsHistory() {
        List<ActivePlayer> activePlayers = new ArrayList<>();
        for (int i = 0; i < totalPlayers; i++)
            activePlayers.add(getActivePlayer(i));

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        HistoryBottomSheet bottomSheetDialogFragment = new HistoryBottomSheet();
        bottomSheetDialogFragment.setBottomSheetsHeight((int) (displaymetrics.heightPixels * 0.4));
        bottomSheetDialogFragment.setPlayers(currentScene, activePlayers);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    private ActivePlayer getActivePlayer(int position) {
        switch (position) {
            case 0:
                return activePlayer1;
            case 1:
                return activePlayer2;
            case 2:
                return activePlayer3;
            case 3:
                return activePlayer4;
            default:
                return new ActivePlayer();
        }
    }

    private Animation getBounceAnimation(boolean up, int percentage) {
        if (percentage == 5) {
            if (up)
                return AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_up_5);
            return AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_down_5);
        }
        if (up)
            return AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_up_10);
        return AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_down_10);
    }

    private Animation getLifeScaleAnimation(final TextView tv, final float toScale, final int type) {
        Animation scale = new ScaleAnimation(currentScaleLife, toScale, currentScaleLife, toScale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(type == Constants.EXPAND ? getResources().getInteger(R.integer.animation_expand_text_duration) : getResources().getInteger(R.integer.animation_collapse_text_duration));
        scale.setStartOffset(type == Constants.EXPAND ? getResources().getInteger(R.integer.animation_expand_text_delay) : 0);
        scale.setInterpolator(this, android.R.anim.overshoot_interpolator);
        scale.setFillAfter(false);
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                tv.clearAnimation();
                tv.setScaleX(toScale);
                tv.setScaleY(toScale);
                isInAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {
                isInAnimation = true;
                adjustLifeSize(false);
            }
        });
        return scale;
    }

    private Animation getScaleAnimation(final TextView tv, final float toScale, final int type) {
        Animation scale = new ScaleAnimation(tv.getScaleX(), toScale, tv.getScaleX(), toScale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(type == Constants.EXPAND ? getResources().getInteger(R.integer.animation_expand_text_duration) : getResources().getInteger(R.integer.animation_collapse_text_duration));
        scale.setStartOffset(type == Constants.EXPAND ? getResources().getInteger(R.integer.animation_expand_text_delay) : 0);
        scale.setInterpolator(this, android.R.anim.overshoot_interpolator);
        scale.setFillAfter(false);
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                tv.clearAnimation();
                tv.setScaleX(toScale);
                tv.setScaleY(toScale);
                isInAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {
                isInAnimation = true;
            }
        });
        return scale;
    }

    private void goToScene(int goTo) {
        isInAnimation = true;
        if (goTo == this.currentSceneOverview) {
            int auxActivePlayer = 0;
            if (currentScene == this.currentScene11) {
                currentScaleLife = layout11_life.getScaleX();
                auxActivePlayer = 0;
            } else if (currentScene == this.currentScene12) {
                currentScaleLife = layout12_life.getScaleX();
                auxActivePlayer = 1;
            } else if (currentScene == this.currentScene21) {
                currentScaleLife = layout21_life.getScaleX();
                auxActivePlayer = 2;
            } else if (currentScene == this.currentScene22) {
                currentScaleLife = layout22_life.getScaleX();
                auxActivePlayer = 3;
            }
            mTransitionManager.transitionTo(overview_scene);
            previousScene = currentScene;
            currentScene = this.currentSceneOverview;
            createGameLayout();
            updateLayout();
            setActionBarColor(ContextCompat.getColor(MainActivity.this, R.color.primary_color));
            animateLifeValue(auxActivePlayer, Constants.COLLAPSE);
            animateEDHValue(Constants.COLLAPSE);
            animateName(Constants.COLLAPSE);
        } else if (goTo == this.currentScene11) {
            currentScaleLife = layout11_life.getScaleX();
            mTransitionManager.transitionTo(big11_scene);
            previousScene = currentScene;
            currentScene = this.currentScene11;
            createLayout11();
            updateLayout11();
            setActionBarColor(activePlayer1.getPlayerDeck().getDeckShieldColor()[0]);
            animateLifeValue(activePlayer1.getPlayerTag(), Constants.EXPAND);
            animateEDHValue(Constants.EXPAND);
            animateName(Constants.EXPAND);
        } else if (goTo == this.currentScene12) {
            currentScaleLife = layout12_life.getScaleX();
            mTransitionManager.transitionTo(big12_scene);
            previousScene = currentScene;
            currentScene = this.currentScene12;
            createLayout12();
            updateLayout12();
            setActionBarColor(activePlayer2.getPlayerDeck().getDeckShieldColor()[0]);
            animateLifeValue(activePlayer2.getPlayerTag(), Constants.EXPAND);
            animateEDHValue(Constants.EXPAND);
            animateName(Constants.EXPAND);
        } else if (goTo == this.currentScene21) {
            currentScaleLife = layout21_life.getScaleX();
            mTransitionManager.transitionTo(big21_scene);
            previousScene = currentScene;
            currentScene = this.currentScene21;
            createLayout21();
            updateLayout21();
            setActionBarColor(activePlayer3.getPlayerDeck().getDeckShieldColor()[0]);
            animateLifeValue(activePlayer3.getPlayerTag(), Constants.EXPAND);
            animateEDHValue(Constants.EXPAND);
            animateName(Constants.EXPAND);
        } else if (goTo == this.currentScene22) {
            currentScaleLife = layout22_life.getScaleX();
            mTransitionManager.transitionTo(big22_scene);
            previousScene = currentScene;
            currentScene = this.currentScene22;
            createLayout22();
            updateLayout22();
            setActionBarColor(activePlayer4.getPlayerDeck().getDeckShieldColor()[0]);
            animateLifeValue(activePlayer4.getPlayerTag(), Constants.EXPAND);
            animateEDHValue(Constants.EXPAND);
            animateName(Constants.EXPAND);
        }

        setupOptionMenu();
    }

    private void historyHandler(final ActivePlayer player) {
        Thread threadLife;
        final int playerTag;

        if (player.getPlayerTag() == 0) {
            threadLife = mThreadLife1;
            playerTag = 0;
        } else if (player.getPlayerTag() == 1) {
            threadLife = mThreadLife2;
            playerTag = 1;
        } else if (player.getPlayerTag() == 2) {
            threadLife = mThreadLife3;
            playerTag = 2;
        } else if (player.getPlayerTag() == 3) {
            threadLife = mThreadLife4;
            playerTag = 3;
        } else {
            threadLife = null;
            playerTag = -1;
        }

        if (threadLife != null) {
            threadLife.interrupt();
        }

        threadLife = new Thread(
                new Runnable() {
                    @SuppressWarnings("ConstantConditions")
                    public void run() {
                        try {
                            int latestSavedLife;
                            String latestSavedLifePreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.PLAYER_HISTORY_LIFE + playerTag, Constants.INITIAL_PLAYER_LIFE);
                            if (!latestSavedLifePreferences.isEmpty()) {
                                String[] latestSavedLifeArray = latestSavedLifePreferences.split("_");
                                latestSavedLife = Integer.valueOf(latestSavedLifeArray[latestSavedLifeArray.length - 1]);
                            } else {
                                latestSavedLife = player.getPlayerLife();
                            }

                            String latestSavedEDH;
                            String latestSavedEDHPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.PLAYER_EDH_PREFIX + playerTag, "0@0@0@0");
                            if (!latestSavedEDHPreferences.isEmpty()) {
                                String[] latestSavedLifeArray = latestSavedEDHPreferences.split("_");
                                latestSavedEDH = latestSavedLifeArray[latestSavedLifeArray.length - 1];
                            } else {
                                latestSavedEDH = player.getPlayerEDH1() + "@" + player.getPlayerEDH2() + "@" + player.getPlayerEDH3() + "@" + player.getPlayerEDH4();
                            }

                            Thread.sleep(2000);

                            int currentLife = player.getPlayerLife();
                            String currentEdh = player.getPlayerEDH1() + "@" + player.getPlayerEDH2() + "@" + player.getPlayerEDH3() + "@" + player.getPlayerEDH4();

                            if ((currentLife - latestSavedLife) != 0 || !currentEdh.equalsIgnoreCase(latestSavedEDH)) {
                                String lifeToBeSaved = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getString(Constants.PLAYER_HISTORY_LIFE + playerTag, Constants.INITIAL_PLAYER_LIFE);
                                lifeToBeSaved = lifeToBeSaved + "_" + currentLife;
                                getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_HISTORY_LIFE + playerTag, lifeToBeSaved).apply();

                                String edhToBeSaved = latestSavedEDHPreferences + "_" + currentEdh;
                                getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putString(Constants.PLAYER_EDH_PREFIX + playerTag, edhToBeSaved).apply();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        threadLife.start();

        if (player.getPlayerTag() == 0)
            mThreadLife1 = threadLife;
        else if (player.getPlayerTag() == 1)
            mThreadLife2 = threadLife;
        else if (player.getPlayerTag() == 2)
            mThreadLife3 = threadLife;
        else if (player.getPlayerTag() == 3)
            mThreadLife4 = threadLife;
    }

    private boolean isPlayerActiveAndAlive(int playerTag) {
        if (totalPlayers <= playerTag)
            return false;
        else {
            switch (playerTag) {
                case 0:
                    return activePlayer1.getPlayerIsAlive();
                case 1:
                    return activePlayer2.getPlayerIsAlive();
                case 2:
                    return activePlayer3.getPlayerIsAlive();
                case 3:
                    return activePlayer4.getPlayerIsAlive();
                default:
                    return true;
            }
        }
    }

    private boolean isValidGame() {
        boolean isValid = true;
        DecksDataAccessObject deckDb = new DecksDataAccessObject(this);
        PlayersDataAccessObject playersDB = new PlayersDataAccessObject(this);

        deckDb.open();
        playersDB.open();

        for (int i = 0; i < totalPlayers; i++) {
            ActivePlayer auxPlayer = Utils.loadPlayerFromSharedPreferences(this, i);
            if (!deckDb.hasDeck(auxPlayer.getPlayerDeck()) || !playersDB.hasPlayer(auxPlayer.getPlayerDeck().getDeckOwnerName()))
                isValid = false;
            if (auxPlayer.getPlayerDeck().getDeckOwnerName().equalsIgnoreCase("") || auxPlayer.getPlayerDeck().getDeckName().equalsIgnoreCase(""))
                isValid = false;
            try {
                if (auxPlayer.getPlayerDeck().getDeckShieldColor()[0] != deckDb.getDeck(auxPlayer.getPlayerDeck().getDeckOwnerName(), auxPlayer.getPlayerDeck().getDeckName()).getDeckShieldColor()[0])
                    isValid = false;
            } catch (NullPointerException e) {
                isValid = false;
            }
        }

        if (totalPlayers < 2)
            isValid = false;

        playersDB.close();
        deckDb.close();
        return isValid;
    }

    private void setActionBarColor(int color) {
        mActionBar.setBackgroundDrawable(new ColorDrawable(color));
        statusBarBackground.setBackgroundColor(color);
    }

    private void setupOptionMenu() {
        if (optionMenu != null) {
            if (currentScene == this.currentSceneOverview) {
                optionMenu.getItem(Integer.valueOf(getString(R.string.menu_main_history_index))).setEnabled(false);
                optionMenu.getItem(Integer.valueOf(getString(R.string.menu_main_history_index))).setVisible(false);
            } else {
                optionMenu.getItem(Integer.valueOf(getString(R.string.menu_main_history_index))).setEnabled(true);
                optionMenu.getItem(Integer.valueOf(getString(R.string.menu_main_history_index))).setVisible(true);
            }
        }
    }

    private void showDiceDialog() {
        @SuppressLint("InflateParams")
        View logView = LayoutInflater.from(this).inflate(R.layout.dialog_roll_a_dice, null);

        TextView dummyView = (TextView) logView.findViewById(R.id.dummyView);
        dummyView.requestFocus();

        final NumberPicker mNumberPicker1 = (NumberPicker) logView.findViewById(R.id.numberPicker1);
        mNumberPicker1.setMinValue(0);
        mNumberPicker1.setMaxValue(99);
        mNumberPicker1.clearFocus();

        final NumberPicker mNumberPicker2 = (NumberPicker) logView.findViewById(R.id.numberPicker2);
        mNumberPicker2.setMinValue(0);
        mNumberPicker2.setMaxValue(99);
        mNumberPicker2.setValue(99);
        mNumberPicker2.clearFocus();

        dummyView.requestFocus();

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("Roll a dice");
        alertDialogBuilder.setMessage("Choose the limits: ");
        alertDialogBuilder.setPositiveButton(R.string.roll,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        final int minValue;
                        final int maxValue;

                        if (mNumberPicker1.getValue() <= mNumberPicker2.getValue()) {
                            minValue = mNumberPicker1.getValue();
                            maxValue = mNumberPicker2.getValue();
                        } else {
                            minValue = mNumberPicker2.getValue();
                            maxValue = mNumberPicker1.getValue();
                        }

                        @SuppressLint("InflateParams")
                        View logView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_roll_a_dice_result, null);
                        textViewDiceResult = (TextView) logView.findViewById(R.id.textViewDiceResult);
                        textViewDiceResult.setText(MessageFormat.format("{0}", Utils.getRandomInt(minValue, maxValue)));

                        progressRandomBar = (ProgressBar) logView.findViewById(R.id.randomProgress);
                        progressRandomBar.setVisibility(View.INVISIBLE);

                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                        alertDialogBuilder.setView(logView);
                        alertDialogBuilder.setTitle("Roll a dice");
                        alertDialogBuilder.setMessage("Dice result [" + minValue + ", " + maxValue + "]:");
                        alertDialogBuilder.setPositiveButton(R.string.random,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });

                        alertDialogBuilder.setNeutralButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        alertDialogBuilder.setNegativeButton(R.string.back,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        showDiceDialog();
                                    }
                                });

                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new RandomDiceTask().execute(minValue, maxValue);
                            }
                        });
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showRandomPlayerDialog() {

        @SuppressLint("InflateParams")
        View logView = LayoutInflater.from(this).inflate(R.layout.dialog_roll_a_dice_result, null);
        textViewResult = (TextView) logView.findViewById(R.id.textViewDiceResult);
        textViewResult.setTextSize(42);
        textViewResult.setText("...");
        textViewResult.setVisibility(View.VISIBLE);

        progressRandomBar = (ProgressBar) logView.findViewById(R.id.randomProgress);
        progressRandomBar.setVisibility(View.INVISIBLE);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("Random Player:");
        alertDialogBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setPositiveButton(R.string.random,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RandomPlayerTask().execute();
            }
        });
    }

    private void startTimerCounter() {
        final long[] countUpTotal = {mSharedPreferences.getInt(Constants.CURRENT_GAME_TIMER, 0)};
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometerTotal);
        if (chronometer != null) {
            chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                @Override
                public void onChronometerTick(Chronometer arg0) {
                    countUpTotal[0]++;
                    String minute = String.valueOf(countUpTotal[0] / 60);
                    if (minute.length() == 1)
                        minute = "0" + minute;
                    String second = String.valueOf(countUpTotal[0] % 60);
                    if (second.length() == 1)
                        second = "0" + second;

                    if (isValidGame())
                        mActionBar.setTitle(minute + ":" + second);
                    else
                        mActionBar.setTitle(getString(R.string.app_name));

                    getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE).edit().putInt(Constants.CURRENT_GAME_TIMER, (int) countUpTotal[0]).apply();
                }
            });
            chronometer.start();
        }
    }

    private void updateDethroneIcon() {
        layout11_throne.setVisibility(View.INVISIBLE);
        layout12_throne.setVisibility(View.INVISIBLE);
        if (totalPlayers >= 3)
            layout21_throne.setVisibility(View.INVISIBLE);
        if (totalPlayers >= 4)
            layout22_throne.setVisibility(View.INVISIBLE);

        //P1
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
            layout11_throne.setVisibility(View.VISIBLE);

        //P2
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
            layout12_throne.setVisibility(View.VISIBLE);

        //P3
        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
            layout21_throne.setVisibility(View.VISIBLE);

        //P4
        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3))
            layout22_throne.setVisibility(View.VISIBLE);

        //P1 and P2
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife())
                layout11_throne.setVisibility(View.VISIBLE);
            if (activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife())
                layout12_throne.setVisibility(View.VISIBLE);
        }

        //P1 and P3
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife())
                layout11_throne.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife())
                layout21_throne.setVisibility(View.VISIBLE);
        }

        //P1 and P4
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout11_throne.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife())
                layout22_throne.setVisibility(View.VISIBLE);
        }

        //P2 and P3
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife())
                layout12_throne.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife())
                layout21_throne.setVisibility(View.VISIBLE);
        }

        //P2 and P4
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout12_throne.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
                layout22_throne.setVisibility(View.VISIBLE);
        }

        //P3 and P4
        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout21_throne.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
                layout22_throne.setVisibility(View.VISIBLE);
        }

        //P1, P2 and P3
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife())
                layout11_throne.setVisibility(View.VISIBLE);
            if (activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife())
                layout12_throne.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife())
                layout21_throne.setVisibility(View.VISIBLE);
        }

        //P1, P2 and P4
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout11_throne.setVisibility(View.VISIBLE);
            if (activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout12_throne.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
                layout22_throne.setVisibility(View.VISIBLE);
        }

        //P1, P3 and P4
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout11_throne.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout21_throne.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
                layout22_throne.setVisibility(View.VISIBLE);
        }

        //P2, P3 and P4
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout12_throne.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout21_throne.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
                layout22_throne.setVisibility(View.VISIBLE);
        }

        //P1, P2, P3 and P4
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout11_throne.setVisibility(View.VISIBLE);
            if (activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout12_throne.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                layout21_throne.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
                layout22_throne.setVisibility(View.VISIBLE);
        }
    }

    private void updateLayout() {
        updateLayout11();
        updateLayout12();
        updateLayout21();
        updateLayout22();
    }

    private void updateLayout11() {
        updateDethroneIcon();
        layout11_name.setText(activePlayer1.getPlayerDeck().getDeckOwnerName());
        layout11_deck.setText(activePlayer1.getPlayerDeck().getDeckName());
        layout11_life.setText(String.format(Locale.US, "%d", activePlayer1.getPlayerLife()));
        layout11_life_positive.setColorFilter(activePlayer1.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
        layout11_life_negative.setColorFilter(activePlayer1.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);

        if (currentScene != this.currentSceneOverview) {
            layout11_positive_EDH1.setColorFilter(activePlayer1.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            layout11_negative_EDH1.setColorFilter(activePlayer1.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            layout11_positive_EDH2.setColorFilter(activePlayer1.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            layout11_negative_EDH2.setColorFilter(activePlayer1.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            if (totalPlayers >= 3) {
                layout11_positive_EDH3.setColorFilter(activePlayer1.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout11_negative_EDH3.setColorFilter(activePlayer1.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            }
            if (totalPlayers == 4) {
                layout11_positive_EDH4.setColorFilter(activePlayer1.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout11_negative_EDH4.setColorFilter(activePlayer1.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            }
        }

        layout11_edh1.setText(String.format(Locale.US, "%d", activePlayer1.getPlayerEDH1()));
        layout11_edh2.setText(String.format(Locale.US, "%d", activePlayer1.getPlayerEDH2()));

        if (currentScene != this.currentSceneOverview) {
            layout11_edh1_name.setText(activePlayer1.getPlayerDeck().getDeckOwnerName());
            layout11_edh2_name.setText(activePlayer2.getPlayerDeck().getDeckOwnerName());
        }

        if (totalPlayers >= 3) {
            layout11_edh3.setText(String.format(Locale.US, "%d", activePlayer1.getPlayerEDH3()));
            if (currentScene != this.currentSceneOverview)
                layout11_edh3_name.setText(activePlayer3.getPlayerDeck().getDeckOwnerName());
        }

        if (totalPlayers == 4) {
            layout11_edh4.setText(String.format(Locale.US, "%d", activePlayer1.getPlayerEDH4()));
            if (currentScene != this.currentSceneOverview)
                layout11_edh4_name.setText(activePlayer4.getPlayerDeck().getDeckOwnerName());
        }
    }

    private void updateLayout12() {
        updateDethroneIcon();
        layout12_name.setText(activePlayer2.getPlayerDeck().getDeckOwnerName());
        layout12_deck.setText(activePlayer2.getPlayerDeck().getDeckName());
        layout12_life.setText(String.format(Locale.US, "%d", activePlayer2.getPlayerLife()));
        layout12_life_positive.setColorFilter(activePlayer2.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
        layout12_life_negative.setColorFilter(activePlayer2.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);

        if (currentScene != this.currentSceneOverview) {
            layout12_positive_EDH1.setColorFilter(activePlayer2.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            layout12_negative_EDH1.setColorFilter(activePlayer2.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            layout12_positive_EDH2.setColorFilter(activePlayer2.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            layout12_negative_EDH2.setColorFilter(activePlayer2.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            if (totalPlayers >= 3) {
                layout12_positive_EDH3.setColorFilter(activePlayer2.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout12_negative_EDH3.setColorFilter(activePlayer2.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            }
            if (totalPlayers == 4) {
                layout12_positive_EDH4.setColorFilter(activePlayer2.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout12_negative_EDH4.setColorFilter(activePlayer2.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            }
        }

        layout12_edh1.setText(String.format(Locale.US, "%d", activePlayer2.getPlayerEDH1()));
        layout12_edh2.setText(String.format(Locale.US, "%d", activePlayer2.getPlayerEDH2()));

        if (currentScene != this.currentSceneOverview) {
            layout12_edh1_name.setText(activePlayer1.getPlayerDeck().getDeckOwnerName());
            layout12_edh2_name.setText(activePlayer2.getPlayerDeck().getDeckOwnerName());
        }

        if (totalPlayers >= 3) {
            layout12_edh3.setText(String.format(Locale.US, "%d", activePlayer2.getPlayerEDH3()));
            if (currentScene != this.currentSceneOverview) {
                layout12_edh3_name.setText(activePlayer3.getPlayerDeck().getDeckOwnerName());
            }
        }
        if (totalPlayers == 4) {
            layout12_edh4.setText(String.format(Locale.US, "%d", activePlayer2.getPlayerEDH4()));
            if (currentScene != this.currentSceneOverview) {
                layout12_edh4_name.setText(activePlayer4.getPlayerDeck().getDeckOwnerName());
            }
        }
    }

    private void updateLayout21() {
        if (totalPlayers >= 3) {
            updateDethroneIcon();
            layout21_name.setText(activePlayer3.getPlayerDeck().getDeckOwnerName());
            layout21_deck.setText(activePlayer3.getPlayerDeck().getDeckName());
            layout21_life.setText(String.format(Locale.US, "%d", activePlayer3.getPlayerLife()));
            layout21_life_positive.setColorFilter(activePlayer3.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            layout21_life_negative.setColorFilter(activePlayer3.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);

            if (currentScene != this.currentSceneOverview) {
                layout21_positive_EDH1.setColorFilter(activePlayer3.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout21_negative_EDH1.setColorFilter(activePlayer3.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout21_positive_EDH2.setColorFilter(activePlayer3.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout21_negative_EDH2.setColorFilter(activePlayer3.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout21_positive_EDH3.setColorFilter(activePlayer3.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout21_negative_EDH3.setColorFilter(activePlayer3.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                if (totalPlayers == 4) {
                    layout21_positive_EDH4.setColorFilter(activePlayer3.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                    layout21_negative_EDH4.setColorFilter(activePlayer3.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                }
            }

            layout21_edh1.setText(String.format(Locale.US, "%d", activePlayer3.getPlayerEDH1()));
            layout21_edh2.setText(String.format(Locale.US, "%d", activePlayer3.getPlayerEDH2()));
            layout21_edh3.setText(String.format(Locale.US, "%d", activePlayer3.getPlayerEDH3()));

            if (currentScene != this.currentSceneOverview) {
                layout21_edh1_name.setText(activePlayer1.getPlayerDeck().getDeckOwnerName());
                layout21_edh2_name.setText(activePlayer2.getPlayerDeck().getDeckOwnerName());
                layout21_edh3_name.setText(activePlayer3.getPlayerDeck().getDeckOwnerName());
            }

            if (totalPlayers == 4) {
                layout21_edh4.setText(String.format(Locale.US, "%d", activePlayer3.getPlayerEDH4()));
                if (currentScene != this.currentSceneOverview) {
                    layout21_edh4_name.setText(activePlayer4.getPlayerDeck().getDeckOwnerName());
                }
            }
        }
    }

    private void updateLayout22() {
        if (totalPlayers >= 4) {
            updateDethroneIcon();
            layout22_name.setText(activePlayer4.getPlayerDeck().getDeckOwnerName());
            layout22_deck.setText(activePlayer4.getPlayerDeck().getDeckName());
            layout22_life.setText(String.format(Locale.US, "%d", activePlayer4.getPlayerLife()));
            layout22_life_positive.setColorFilter(activePlayer4.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            layout22_life_negative.setColorFilter(activePlayer4.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);

            if (currentScene != this.currentSceneOverview) {
                layout22_positive_EDH1.setColorFilter(activePlayer4.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout22_negative_EDH1.setColorFilter(activePlayer4.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout22_positive_EDH2.setColorFilter(activePlayer4.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout22_negative_EDH2.setColorFilter(activePlayer4.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout22_positive_EDH3.setColorFilter(activePlayer4.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout22_negative_EDH3.setColorFilter(activePlayer4.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout22_positive_EDH4.setColorFilter(activePlayer4.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
                layout22_negative_EDH4.setColorFilter(activePlayer4.getPlayerDeck().getDeckShieldColor()[0], PorterDuff.Mode.SRC_IN);
            }

            layout22_edh1.setText(String.format(Locale.US, "%d", activePlayer4.getPlayerEDH1()));
            layout22_edh2.setText(String.format(Locale.US, "%d", activePlayer4.getPlayerEDH2()));
            layout22_edh3.setText(String.format(Locale.US, "%d", activePlayer4.getPlayerEDH3()));
            layout22_edh4.setText(String.format(Locale.US, "%d", activePlayer4.getPlayerEDH4()));

            if (currentScene != this.currentSceneOverview) {
                layout22_edh1_name.setText(activePlayer1.getPlayerDeck().getDeckOwnerName());
                layout22_edh2_name.setText(activePlayer2.getPlayerDeck().getDeckOwnerName());
                layout22_edh3_name.setText(activePlayer3.getPlayerDeck().getDeckOwnerName());
                layout22_edh4_name.setText(activePlayer4.getPlayerDeck().getDeckOwnerName());
            }
        }
    }

    class RandomDiceTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final int minValue = params[0];
            final int maxValue = params[1];
            return MessageFormat.format("{0}", Utils.getRandomInt(minValue, maxValue));
        }

        @Override
        protected void onPostExecute(String result) {
            progressRandomBar.setVisibility(View.INVISIBLE);
            textViewDiceResult.setVisibility(View.VISIBLE);
            textViewDiceResult.setText(result);
        }

        @Override
        protected void onPreExecute() {
            progressRandomBar.setVisibility(View.VISIBLE);
            textViewDiceResult.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }
    }

    class RandomPlayerTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final int minValue = 0;
            final int maxValue = totalPlayers - 1;
            return MessageFormat.format("{0}", getActivePlayer(Utils.getRandomInt(minValue, maxValue)).getPlayerDeck().getDeckOwnerName());
        }

        @Override
        protected void onPostExecute(String result) {
            progressRandomBar.setVisibility(View.INVISIBLE);
            textViewResult.setVisibility(View.VISIBLE);
            textViewResult.setText(result);
        }

        @Override
        protected void onPreExecute() {
            progressRandomBar.setVisibility(View.VISIBLE);
            textViewResult.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }
    }
}

//    private boolean isPlayerOnThrone(int playerTag) {
//        ActivePlayer auxPlayer = new ActivePlayer();
//        switch (playerTag) {
//            case 0:
//                auxPlayer = activePlayer1;
//                break;
//            case 1:
//                auxPlayer = activePlayer2;
//                break;
//            case 2:
//                auxPlayer = activePlayer3;
//                break;
//            case 3:
//                auxPlayer = activePlayer4;
//                break;
//        }
//
//        //P1
//        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
//            if (auxPlayer.getPlayerTag() == 0)
//                return true;
//
//        //P2
//        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
//            if (auxPlayer.getPlayerTag() == 1)
//                return true;
//
//        //P3
//        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
//            if (auxPlayer.getPlayerTag() == 2)
//                return true;
//
//        //P4
//        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3))
//            if (auxPlayer.getPlayerTag() == 3)
//                return true;
//
//        //P1 and P2
//        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
//            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife())
//                return true;
//        }
//
//        //P1 and P3
//        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
//            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife())
//                return true;
//        }
//
//        //P1 and P4
//        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
//            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife())
//                return true;
//        }
//
//        //P2 and P3
//        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
//            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife())
//                return true;
//        }
//
//        //P2 and P4
//        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
//            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
//                return true;
//        }
//
//        //P3 and P4
//        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
//            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
//                return true;
//        }
//
//        //P1, P2 and P3
//        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
//            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife())
//                return true;
//        }
//
//        //P1, P2 and P4
//        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
//            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
//                return true;
//        }
//
//        //P1, P3 and P4
//        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
//            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
//                return true;
//        }
//
//        //P2, P3 and P4
//        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
//            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
//                return true;
//        }
//
//        //P1, P2, P3 and P4
//        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
//            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
//                return true;
//            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
//                return true;
//        }
//
//        return false;
//    }