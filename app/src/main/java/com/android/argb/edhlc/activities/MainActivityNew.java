package com.android.argb.edhlc.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;
import com.android.argb.edhlc.objects.ActivePlayerNew;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivityNew extends AppCompatActivity implements MainFragment.OnUpdateData {

    private static final int MODE_NEW_GAME = 0;
    private static final int MODE_OVERVIEW = 1;
    private static final int MODE_PLAYING = 2;

    ///LAYOUT OVERVIEW
    private static ImageView overview_ImageViewThroneP1;
    private static ImageView overview_ImageViewThroneP2;
    private static ImageView overview_ImageViewThroneP3;
    private static ImageView overview_ImageViewThroneP4;
    private static TextView overview_TextViewP1Name;
    private static TextView overview_TextViewP1Life;
    private static ImageView overview_lifePositiveP1;
    private static ImageView overview_lifeNegativeP1;
    private static TextView overview_TextViewP1EDH1;
    private static TextView overview_TextViewP1EDH2;
    private static TextView overview_TextViewP1EDH3;
    private static TextView overview_TextViewP1EDH4;
    private static TextView overview_TextViewP2Name;
    private static TextView overview_TextViewP2Life;
    private static ImageView overview_lifePositiveP2;
    private static ImageView overview_lifeNegativeP2;
    private static TextView overview_TextViewP2EDH1;
    private static TextView overview_TextViewP2EDH2;
    private static TextView overview_TextViewP2EDH3;
    private static TextView overview_TextViewP2EDH4;
    private static TextView overview_TextViewP3Name;
    private static TextView overview_TextViewP3Life;
    private static ImageView overview_lifePositiveP3;
    private static ImageView overview_lifeNegativeP3;
    private static TextView overview_TextViewP3EDH1;
    private static TextView overview_TextViewP3EDH2;
    private static TextView overview_TextViewP3EDH3;
    private static TextView overview_TextViewP3EDH4;
    private static TextView overview_TextViewP4Name;
    private static TextView overview_TextViewP4Life;
    private static ImageView overview_lifePositiveP4;
    private static ImageView overview_lifeNegativeP4;
    private static TextView overview_TextViewP4EDH1;
    private static TextView overview_TextViewP4EDH2;
    private static TextView overview_TextViewP4EDH3;
    private static TextView overview_TextViewP4EDH4;

    ///Drawer
    private DrawerLayout mPlayerDrawerLayout;
    private LinearLayout mPlayerDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Switch switchScreen;
    private SharedPreferences mSharedPreferences;
    private ActionBar mActionBar;
    private Menu optionMenu;
    private View statusBarBackground;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RelativeLayout viewNewGame;
    private RelativeLayout viewOverview;
    private List<MainFragment> fragments;
    private MainPagerAdapter mPagerAdapter;
    //Players
    private int totalPlayers;
    private ActivePlayerNew activePlayer1;
    private ActivePlayerNew activePlayer2;
    private ActivePlayerNew activePlayer3;
    private ActivePlayerNew activePlayer4;

    @Override
    public void iUpdateActivePlayer(ActivePlayerNew activePlayer) {
        switch (activePlayer.getPlayerTag()) {
            case 0:
                activePlayer1 = activePlayer;
                break;
            case 1:
                activePlayer2 = activePlayer;
                break;
            case 2:
                activePlayer3 = activePlayer;
                break;
            case 3:
                activePlayer4 = activePlayer;
                break;
        }
    }

    @Override
    public void iUpdateDethrone() {
        for (int i = 0; i < fragments.size(); i++)
            fragments.get(i).updateFragmentDethrone(isPlayerOnThrone(i));
    }

    public boolean isValidGame() {
        boolean isValid = true;
        DecksDataAccessObject deckDb = new DecksDataAccessObject(this);
        PlayersDataAccessObject playersDB = new PlayersDataAccessObject(this);

        deckDb.open();
        playersDB.open();

        for (int i = 0; i < totalPlayers; i++) {
            ActivePlayerNew auxPlayer = Utils.loadPlayerFromSharedPreferences(this, i);
            if (!deckDb.hasDeck(auxPlayer.getPlayerDeck()) || !playersDB.hasPlayer(auxPlayer.getPlayerDeck().getDeckOwnerName()))
                isValid = false;
            if (auxPlayer.getPlayerDeck().getDeckOwnerName().equalsIgnoreCase("") || auxPlayer.getPlayerDeck().getDeckName().equalsIgnoreCase(""))
                isValid = false;
            if (auxPlayer.getPlayerDeck().getDeckShieldColor()[0] !=
                    deckDb.getDeck(auxPlayer.getPlayerDeck().getDeckOwnerName(), auxPlayer.getPlayerDeck().getDeckName()).getDeckShieldColor()[0])
                isValid = false;
        }

        if (totalPlayers < 2)
            isValid = false;

        playersDB.close();
        deckDb.close();
        return isValid;
    }

    @Override
    public void onBackPressed() {
        if (mPlayerDrawerLayout.isDrawerOpen(mPlayerDrawer))
            mPlayerDrawerLayout.closeDrawers();
        else if (getCurrentMode() != MODE_PLAYING && isValidGame())
            setMode(MODE_PLAYING);
        else
            super.onBackPressed();
    }

    public void onClickDrawerItem(View view) {
        switch (view.getId()) {
            case R.id.drawerItemHome:
                mPlayerDrawerLayout.closeDrawers();
                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                break;

            case R.id.drawerItemPlayers:
                mPlayerDrawerLayout.closeDrawers();
                Intent intentPlayerList = new Intent(this, PlayerListActivity.class);
                intentPlayerList.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentPlayerList);
                break;

            case R.id.drawerItemRecords:
                mPlayerDrawerLayout.closeDrawers();
                startActivity(new Intent(this, RecordsActivity.class));
                break;

            case R.id.drawerItemScreen:
                switchScreen.setChecked(!switchScreen.isChecked());
                if (!switchScreen.isChecked()) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).commit();
                } else {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).commit();
                }
                break;

            case R.id.drawerItemSettings:
                mPlayerDrawerLayout.closeDrawers();
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.drawerItemAbout:
                mPlayerDrawerLayout.closeDrawers();
                break;
        }
    }

    public void onClickNewGame(View view) {
        startActivity(new Intent(this, NewGameActivity.class));
        this.finish();
    }

    public void onClickOverview(View view) {
        switch (view.getId()) {
            case R.id.nameP1:
                break;
            case R.id.textViewOverviewP1Life:
                break;
            case R.id.lifePositiveP1:
                activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() + 1);
                overview_TextViewP1Life.setText(String.valueOf(activePlayer1.getPlayerLife()));
                updateOverviewDethroneIcon();
                break;
            case R.id.lifeNegativeP1:
                activePlayer1.setPlayerLife(activePlayer1.getPlayerLife() - 1);
                overview_TextViewP1Life.setText(String.valueOf(activePlayer1.getPlayerLife()));
                updateOverviewDethroneIcon();
                break;

            case R.id.nameP2:
                break;
            case R.id.textViewOverviewP2Life:
                break;
            case R.id.lifePositiveP2:
                activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() + 1);
                overview_TextViewP2Life.setText(String.valueOf(activePlayer2.getPlayerLife()));
                updateOverviewDethroneIcon();
                break;
            case R.id.lifeNegativeP2:
                activePlayer2.setPlayerLife(activePlayer2.getPlayerLife() - 1);
                overview_TextViewP2Life.setText(String.valueOf(activePlayer2.getPlayerLife()));
                updateOverviewDethroneIcon();
                break;

            case R.id.nameP3:
                break;
            case R.id.textViewOverviewP3Life:
                break;
            case R.id.lifePositiveP3:
                activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() + 1);
                overview_TextViewP3Life.setText(String.valueOf(activePlayer3.getPlayerLife()));
                updateOverviewDethroneIcon();
                break;
            case R.id.lifeNegativeP3:
                activePlayer3.setPlayerLife(activePlayer3.getPlayerLife() - 1);
                overview_TextViewP3Life.setText(String.valueOf(activePlayer3.getPlayerLife()));
                updateOverviewDethroneIcon();
                break;

            case R.id.nameP4:
                break;
            case R.id.textViewOverviewP4Life:
                break;
            case R.id.lifePositiveP4:
                activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() + 1);
                overview_TextViewP4Life.setText(String.valueOf(activePlayer4.getPlayerLife()));
                updateOverviewDethroneIcon();
                break;
            case R.id.lifeNegativeP4:
                activePlayer4.setPlayerLife(activePlayer4.getPlayerLife() - 1);
                overview_TextViewP4Life.setText(String.valueOf(activePlayer4.getPlayerLife()));
                updateOverviewDethroneIcon();
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //Option menu
        switch (item.getItemId()) {
            case R.id.action_overview:
                if (getCurrentMode() == MODE_PLAYING)
                    setMode(MODE_OVERVIEW);
                else if (getCurrentMode() == MODE_OVERVIEW)
                    setMode(MODE_PLAYING);
                break;

            case R.id.action_history:
                startActivity(new Intent(this, HistoryActivity.class));
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

            case R.id.actions_roll_dice:
                showDiceDialog();
                break;

            case R.id.actions_random_player:
                showRandomPlayerDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tabLayout != null)
            getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE).edit().putInt(Constants.CURRENT_VIEW_TAB, tabLayout.getSelectedTabPosition()).apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        createStatusBar();
        createToolbar();
        createDrawer();
        createLayout();

        mSharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE);
        totalPlayers = mSharedPreferences.getInt(Constants.CURRENT_GAME_TOTAL_PLAYERS, 0);

        createOverviewLayout();

        if (isValidGame()) {
            activePlayer1 = Utils.loadPlayerFromSharedPreferences(this, 0);
            activePlayer2 = Utils.loadPlayerFromSharedPreferences(this, 1);
            if (totalPlayers >= 3)
                activePlayer3 = Utils.loadPlayerFromSharedPreferences(this, 2);
            if (totalPlayers >= 4)
                activePlayer4 = Utils.loadPlayerFromSharedPreferences(this, 3);

            List<String> tabTitles = new ArrayList<>();
            fragments = new ArrayList<>();
            fragments.add(MainFragment.newInstance(activePlayer1, totalPlayers));
            tabTitles.add(activePlayer1.getPlayerDeck().getDeckOwnerName());
            fragments.add(MainFragment.newInstance(activePlayer2, totalPlayers));
            tabTitles.add(activePlayer2.getPlayerDeck().getDeckOwnerName());
            if (totalPlayers >= 3) {
                fragments.add(MainFragment.newInstance(activePlayer3, totalPlayers));
                tabTitles.add(activePlayer3.getPlayerDeck().getDeckOwnerName());
            }
            if (totalPlayers >= 4) {
                fragments.add(MainFragment.newInstance(activePlayer4, totalPlayers));
                tabTitles.add(activePlayer4.getPlayerDeck().getDeckOwnerName());
            }
            mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), MainActivityNew.this, fragments, tabTitles);

            viewPager.setAdapter(mPagerAdapter);
            viewPager.setOffscreenPageLimit(fragments.size());

            tabLayout.setupWithViewPager(viewPager);

            setMode(MODE_PLAYING);
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
        if (viewPager != null)
            viewPager.setCurrentItem(mSharedPreferences.getInt(Constants.CURRENT_VIEW_TAB, 0));

        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            switchScreen.setChecked(true);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            switchScreen.setChecked(false);
        }

        if (!isValidGame())
            setMode(MODE_NEW_GAME);
        else
            setMode(MODE_PLAYING);
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

    private void createDrawer() {
        mPlayerDrawer = (LinearLayout) findViewById(R.id.main_drawer);
        mPlayerDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mPlayerDrawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mPlayerDrawerLayout.setDrawerListener(mDrawerToggle);

        LinearLayout drawerItemHome = (LinearLayout) findViewById(R.id.drawerItemHome);
        ImageView drawerItemIconHome = (ImageView) findViewById(R.id.drawerItemIconHome);
        TextView drawerItemTextHome = (TextView) findViewById(R.id.drawerItemTextHome);
        drawerItemHome.setBackgroundColor(ContextCompat.getColor(this, R.color.gray200));
        drawerItemIconHome.setColorFilter(ContextCompat.getColor(this, R.color.accent_color));
        drawerItemTextHome.setTextColor(ContextCompat.getColor(this, R.color.accent_color));
    }

    private void createLayout() {
        //TODO enhance layout
        viewNewGame = (RelativeLayout) findViewById(R.id.fragmentNewGame);
        viewOverview = (RelativeLayout) findViewById(R.id.fragmentOverview);

        viewPager = (ViewPager) findViewById(R.id.fragmentViewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int color = getActivePlayer(position).getPlayerDeck().getDeckShieldColor()[0];
                if (color == 0)
                    color = getResources().getIntArray(R.array.edh_default)[0];
                setActionBarColor(color);
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        //Drawer
        switchScreen = (Switch) findViewById(R.id.switchScreen);
        switchScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switchScreen.isChecked()) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).commit();
                } else {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).commit();
                }
            }
        });
    }

    private void createOverviewLayout() {
        FrameLayout overview = (FrameLayout) findViewById(R.id.fragmentOverviewFrameLayout);
        if (totalPlayers == 2)
            getLayoutInflater().inflate(R.layout.activity_overview_new_content_2p, overview, true);
        else if (totalPlayers == 3)
            getLayoutInflater().inflate(R.layout.activity_overview_new_content_3p, overview, true);
        else if (totalPlayers == 4)
            getLayoutInflater().inflate(R.layout.activity_overview_new_content_4p, overview, true);

        if (totalPlayers >= 1) {
            overview_ImageViewThroneP1 = (ImageView) findViewById(R.id.imageViewThroneP1);
            overview_ImageViewThroneP2 = (ImageView) findViewById(R.id.imageViewThroneP2);
            overview_ImageViewThroneP3 = (ImageView) findViewById(R.id.imageViewThroneP3);
            overview_ImageViewThroneP4 = (ImageView) findViewById(R.id.imageViewThroneP4);
        }

        if (totalPlayers >= 1) {
            overview_TextViewP1Name = (TextView) findViewById(R.id.textViewOverviewP1Name);
            overview_TextViewP1Life = (TextView) findViewById(R.id.textViewOverviewP1Life);
            LinearLayout mLinearEDH1 = (LinearLayout) findViewById(R.id.linearEDH1);
            mLinearEDH1.setWeightSum(totalPlayers);
            overview_lifePositiveP1 = (ImageView) findViewById(R.id.lifePositiveP1);
            overview_lifeNegativeP1 = (ImageView) findViewById(R.id.lifeNegativeP1);
            overview_TextViewP1EDH1 = (TextView) findViewById(R.id.textViewOverviewP1EDH1);
            overview_TextViewP1EDH2 = (TextView) findViewById(R.id.textViewOverviewP1EDH2);
            if (totalPlayers >= 3)
                overview_TextViewP1EDH3 = (TextView) findViewById(R.id.textViewOverviewP1EDH3);
            if (totalPlayers >= 4)
                overview_TextViewP1EDH4 = (TextView) findViewById(R.id.textViewOverviewP1EDH4);
        }

        if (totalPlayers >= 2) {
            overview_TextViewP2Name = (TextView) findViewById(R.id.textViewOverviewP2Name);
            overview_TextViewP2Life = (TextView) findViewById(R.id.textViewOverviewP2Life);
            LinearLayout mLinearEDH2 = (LinearLayout) findViewById(R.id.linearEDH2);
            mLinearEDH2.setWeightSum(totalPlayers);
            overview_lifePositiveP2 = (ImageView) findViewById(R.id.lifePositiveP2);
            overview_lifeNegativeP2 = (ImageView) findViewById(R.id.lifeNegativeP2);
            overview_TextViewP2EDH1 = (TextView) findViewById(R.id.textViewOverviewP2EDH1);
            overview_TextViewP2EDH2 = (TextView) findViewById(R.id.textViewOverviewP2EDH2);
            if (totalPlayers >= 3)
                overview_TextViewP2EDH3 = (TextView) findViewById(R.id.textViewOverviewP2EDH3);
            if (totalPlayers >= 4)
                overview_TextViewP2EDH4 = (TextView) findViewById(R.id.textViewOverviewP2EDH4);
        }

        if (totalPlayers >= 3) {
            overview_TextViewP3Name = (TextView) findViewById(R.id.textViewOverviewP3Name);
            overview_TextViewP3Life = (TextView) findViewById(R.id.textViewOverviewP3Life);
            LinearLayout mLinearEDH3 = (LinearLayout) findViewById(R.id.linearEDH3);
            mLinearEDH3.setWeightSum(totalPlayers);
            overview_lifePositiveP3 = (ImageView) findViewById(R.id.lifePositiveP3);
            overview_lifeNegativeP3 = (ImageView) findViewById(R.id.lifeNegativeP3);
            overview_TextViewP3EDH1 = (TextView) findViewById(R.id.textViewOverviewP3EDH1);
            overview_TextViewP3EDH2 = (TextView) findViewById(R.id.textViewOverviewP3EDH2);
            overview_TextViewP3EDH3 = (TextView) findViewById(R.id.textViewOverviewP3EDH3);
            if (totalPlayers >= 4)
                overview_TextViewP3EDH4 = (TextView) findViewById(R.id.textViewOverviewP3EDH4);
        }

        if (totalPlayers >= 4) {
            overview_TextViewP4Name = (TextView) findViewById(R.id.textViewOverviewP4Name);
            overview_TextViewP4Life = (TextView) findViewById(R.id.textViewOverviewP4Life);
            LinearLayout mLinearEDH4 = (LinearLayout) findViewById(R.id.linearEDH4);
            mLinearEDH4.setWeightSum(totalPlayers);
            overview_lifePositiveP4 = (ImageView) findViewById(R.id.lifePositiveP4);
            overview_lifeNegativeP4 = (ImageView) findViewById(R.id.lifeNegativeP4);
            overview_TextViewP4EDH1 = (TextView) findViewById(R.id.textViewOverviewP4EDH1);
            overview_TextViewP4EDH2 = (TextView) findViewById(R.id.textViewOverviewP4EDH2);
            overview_TextViewP4EDH3 = (TextView) findViewById(R.id.textViewOverviewP4EDH3);
            overview_TextViewP4EDH4 = (TextView) findViewById(R.id.textViewOverviewP4EDH4);
        }

    }

    private void createStatusBar() {
        statusBarBackground = findViewById(R.id.statusBarBackground);
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
        mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.primary_color)));
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
    }

    private ActivePlayerNew getActivePlayer(int position) {
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
                return new ActivePlayerNew();
        }
    }

    private int getCurrentMode() {
        if (viewNewGame.getVisibility() == View.VISIBLE)
            return MODE_NEW_GAME;
        else if (viewOverview.getVisibility() == View.VISIBLE)
            return MODE_OVERVIEW;
        else if (viewPager.getVisibility() == View.VISIBLE)
            return MODE_PLAYING;
        return -1;
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

    private boolean isPlayerOnThrone(int playerTag) {
        ActivePlayerNew auxPlayer = new ActivePlayerNew();
        switch (playerTag) {
            case 0:
                auxPlayer = activePlayer1;
                break;
            case 1:
                auxPlayer = activePlayer2;
                break;
            case 2:
                auxPlayer = activePlayer3;
                break;
            case 3:
                auxPlayer = activePlayer4;
                break;
        }

        //P1
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
            if (auxPlayer.getPlayerTag() == 0)
                return true;

        //P2
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
            if (auxPlayer.getPlayerTag() == 1)
                return true;

        //P3
        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
            if (auxPlayer.getPlayerTag() == 2)
                return true;

        //P4
        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3))
            if (auxPlayer.getPlayerTag() == 3)
                return true;

        //P1 and P2
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife())
                return true;
        }

        //P1 and P3
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife())
                return true;
        }

        //P1 and P4
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife())
                return true;
        }

        //P2 and P3
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife())
                return true;
        }

        //P2 and P4
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
                return true;
        }

        //P3 and P4
        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
                return true;
        }

        //P1, P2 and P3
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife())
                return true;
        }

        //P1, P2 and P4
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
                return true;
        }

        //P1, P3 and P4
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
                return true;
        }

        //P2, P3 and P4
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
                return true;
        }

        //P1, P2, P3 and P4
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (auxPlayer.getPlayerTag() == 0 && activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 1 && activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 2 && activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                return true;
            if (auxPlayer.getPlayerTag() == 3 && activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
                return true;
        }

        return false;
    }

    private void setActionBarColor(int color) {
        mActionBar.setBackgroundDrawable(new ColorDrawable(color));
        statusBarBackground.setBackgroundColor(color);
    }

    //TODO handle action bar
    private void setMode(int mode) {
        switch (mode) {
            case MODE_NEW_GAME:
                setActionBarColor(getResources().getIntArray(R.array.edh_default)[0]);

                viewNewGame.setVisibility(View.VISIBLE);
                viewOverview.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                break;

            case MODE_OVERVIEW:
                setActionBarColor(getResources().getIntArray(R.array.edh_default)[0]);

                updateOverviewLayout();
                viewNewGame.setVisibility(View.GONE);
                viewOverview.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                break;

            case MODE_PLAYING:
                int color = getActivePlayer(tabLayout.getSelectedTabPosition()).getPlayerDeck().getDeckShieldColor()[0];
                if (color == 0)
                    color = getResources().getIntArray(R.array.edh_default)[0];
                setActionBarColor(color);

                for (int i = 0; i < fragments.size(); i++) {
                    if (i == 0)
                        fragments.get(0).updateLife(activePlayer1.getPlayerLife());
                    else if (i == 1)
                        fragments.get(1).updateLife(activePlayer2.getPlayerLife());
                    else if (i == 2)
                        fragments.get(2).updateLife(activePlayer3.getPlayerLife());
                    else if (i == 3)
                        fragments.get(3).updateLife(activePlayer4.getPlayerLife());
                }

                viewNewGame.setVisibility(View.GONE);
                viewOverview.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showDiceDialog() {
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
        alertDialogBuilder.setPositiveButton("ROLL",
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

                        final Random r = new Random();
                        final int[] dice = {r.nextInt(maxValue - minValue + 1) + minValue};

                        View logView = LayoutInflater.from(MainActivityNew.this).inflate(R.layout.dialog_roll_a_dice_result, null);
                        final TextView textViewDiceResult = (TextView) logView.findViewById(R.id.textViewDiceResult);
                        textViewDiceResult.setText(MessageFormat.format("{0}", dice[0]));

                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivityNew.this);
                        alertDialogBuilder.setView(logView);
                        alertDialogBuilder.setTitle("Roll a dice");
                        alertDialogBuilder.setMessage("Dice result [" + minValue + ", " + maxValue + "]:");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        alertDialogBuilder.setNegativeButton("BACK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        showDiceDialog();
                                    }
                                });

                        alertDialogBuilder.setNeutralButton("REPEAT",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });

                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dice[0] = r.nextInt((maxValue - minValue) + 1) + minValue;
                                textViewDiceResult.setText(MessageFormat.format("{0}", dice[0]));
                            }
                        });
                    }
                });
        alertDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showRandomPlayerDialog() {
        final int minValue = 0;
        final int maxValue = totalPlayers - 1;
        final Random r = new Random();
        final int[] randomResult = {r.nextInt(maxValue - minValue + 1) + minValue};

        View logView = LayoutInflater.from(this).inflate(R.layout.dialog_roll_a_dice_result, null);
        final TextView textViewResult = (TextView) logView.findViewById(R.id.textViewDiceResult);
        textViewResult.setTextSize(42);
        textViewResult.setText(MessageFormat.format("{0}", getActivePlayer(randomResult[0]).getPlayerDeck().getDeckOwnerName()));

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("Random Player:");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setNeutralButton("REPEAT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomResult[0] = r.nextInt(maxValue - minValue + 1) + minValue;
                textViewResult.setText(MessageFormat.format("{0}", getActivePlayer(randomResult[0]).getPlayerDeck().getDeckOwnerName()));
            }
        });
    }

    private void updateOverviewDethroneIcon() {
        overview_ImageViewThroneP1.setVisibility(View.INVISIBLE);
        overview_ImageViewThroneP2.setVisibility(View.INVISIBLE);
        if (totalPlayers >= 3)
            overview_ImageViewThroneP3.setVisibility(View.INVISIBLE);
        if (totalPlayers >= 4)
            overview_ImageViewThroneP4.setVisibility(View.INVISIBLE);

        //P1
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
            overview_ImageViewThroneP1.setVisibility(View.VISIBLE);

        //P2
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
            overview_ImageViewThroneP2.setVisibility(View.VISIBLE);

        //P3
        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3))
            overview_ImageViewThroneP3.setVisibility(View.VISIBLE);

        //P4
        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3))
            overview_ImageViewThroneP4.setVisibility(View.VISIBLE);

        //P1 and P2
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife())
                overview_ImageViewThroneP1.setVisibility(View.VISIBLE);
            if (activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife())
                overview_ImageViewThroneP2.setVisibility(View.VISIBLE);
        }

        //P1 and P3
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife())
                overview_ImageViewThroneP1.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife())
                overview_ImageViewThroneP3.setVisibility(View.VISIBLE);
        }

        //P1 and P4
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP1.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife())
                overview_ImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P2 and P3
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife())
                overview_ImageViewThroneP2.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife())
                overview_ImageViewThroneP3.setVisibility(View.VISIBLE);
        }

        //P2 and P4
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP2.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
                overview_ImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P3 and P4
        if (!isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP3.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
                overview_ImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P1, P2 and P3
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && !isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife())
                overview_ImageViewThroneP1.setVisibility(View.VISIBLE);
            if (activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife())
                overview_ImageViewThroneP2.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife())
                overview_ImageViewThroneP3.setVisibility(View.VISIBLE);
        }

        //P1, P2 and P4
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && !isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP1.setVisibility(View.VISIBLE);
            if (activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP2.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
                overview_ImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P1, P3 and P4
        if (isPlayerActiveAndAlive(0) && !isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP1.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP3.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
                overview_ImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P2, P3 and P4
        if (!isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP2.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP3.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife())
                overview_ImageViewThroneP4.setVisibility(View.VISIBLE);
        }

        //P1, P2, P3 and P4
        if (isPlayerActiveAndAlive(0) && isPlayerActiveAndAlive(1) && isPlayerActiveAndAlive(2) && isPlayerActiveAndAlive(3)) {
            if (activePlayer1.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer1.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP1.setVisibility(View.VISIBLE);
            if (activePlayer2.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer3.getPlayerLife() && activePlayer2.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP2.setVisibility(View.VISIBLE);
            if (activePlayer3.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer3.getPlayerLife() >= activePlayer4.getPlayerLife())
                overview_ImageViewThroneP3.setVisibility(View.VISIBLE);
            if (activePlayer4.getPlayerLife() >= activePlayer1.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer2.getPlayerLife() && activePlayer4.getPlayerLife() >= activePlayer3.getPlayerLife())
                overview_ImageViewThroneP4.setVisibility(View.VISIBLE);
        }
    }

    private void updateOverviewLayout() {
        if (totalPlayers >= 1)
            updateOverviewDethroneIcon();

        if (totalPlayers >= 1) {
            overview_TextViewP1Name.setText(activePlayer1.getPlayerDeck().getDeckOwnerName());
            overview_TextViewP1Name.setEnabled(activePlayer1.getPlayerIsAlive());
            overview_TextViewP1Name.setTextColor(activePlayer1.getPlayerIsAlive() ? activePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_TextViewP1Life.setText(String.valueOf(activePlayer1.getPlayerLife()));
            overview_TextViewP1Life.setTextSize(TypedValue.COMPLEX_UNIT_SP, (activePlayer1.getPlayerLife() > 99 || activePlayer1.getPlayerLife() < -99) ? (totalPlayers == 2 ? 100 : 70) : (totalPlayers == 2 ? 120 : 100));
            overview_TextViewP1Life.setEnabled(activePlayer1.getPlayerIsAlive());
            overview_TextViewP1Life.setTextColor(activePlayer1.getPlayerIsAlive() ? activePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_lifePositiveP1.setColorFilter(activePlayer1.getPlayerIsAlive() ? activePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_lifeNegativeP1.setColorFilter(activePlayer1.getPlayerIsAlive() ? activePlayer1.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_TextViewP1EDH1.setText(String.valueOf(activePlayer1.getPlayerEDH1()));
            overview_TextViewP1EDH1.setEnabled(activePlayer1.getPlayerIsAlive());
            overview_TextViewP1EDH2.setText(String.valueOf(activePlayer1.getPlayerEDH2()));
            overview_TextViewP1EDH2.setEnabled(activePlayer1.getPlayerIsAlive());
            if (totalPlayers >= 3) {
                overview_TextViewP1EDH3.setText(String.valueOf(activePlayer1.getPlayerEDH3()));
                overview_TextViewP1EDH3.setEnabled(activePlayer1.getPlayerIsAlive());
            }
            if (totalPlayers >= 4) {
                overview_TextViewP1EDH4.setText(String.valueOf(activePlayer1.getPlayerEDH4()));
                overview_TextViewP1EDH4.setEnabled(activePlayer1.getPlayerIsAlive());
            }
        }

        if (totalPlayers >= 2) {
            overview_TextViewP2Name.setText(activePlayer2.getPlayerDeck().getDeckOwnerName());
            overview_TextViewP2Name.setEnabled(activePlayer2.getPlayerIsAlive());
            overview_TextViewP2Name.setTextColor(activePlayer2.getPlayerIsAlive() ? activePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_TextViewP2Life.setText(String.valueOf(activePlayer2.getPlayerLife()));
            overview_TextViewP2Life.setTextSize(TypedValue.COMPLEX_UNIT_SP, (activePlayer2.getPlayerLife() > 99 || activePlayer2.getPlayerLife() < -99) ? (totalPlayers == 2 ? 100 : 70) : (totalPlayers == 2 ? 120 : 100));
            overview_TextViewP2Life.setEnabled(activePlayer2.getPlayerIsAlive());
            overview_TextViewP2Life.setTextColor(activePlayer2.getPlayerIsAlive() ? activePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_lifePositiveP2.setColorFilter(activePlayer1.getPlayerIsAlive() ? activePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_lifeNegativeP2.setColorFilter(activePlayer1.getPlayerIsAlive() ? activePlayer2.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_TextViewP2EDH1.setText(String.valueOf(activePlayer2.getPlayerEDH1()));
            overview_TextViewP2EDH1.setEnabled(activePlayer2.getPlayerIsAlive());
            overview_TextViewP2EDH2.setText(String.valueOf(activePlayer2.getPlayerEDH2()));
            overview_TextViewP2EDH2.setEnabled(activePlayer2.getPlayerIsAlive());
            if (totalPlayers >= 3) {
                overview_TextViewP2EDH3.setText(String.valueOf(activePlayer2.getPlayerEDH3()));
                overview_TextViewP2EDH3.setEnabled(activePlayer2.getPlayerIsAlive());
            }
            if (totalPlayers >= 4) {
                overview_TextViewP2EDH4.setText(String.valueOf(activePlayer2.getPlayerEDH4()));
                overview_TextViewP2EDH4.setEnabled(activePlayer2.getPlayerIsAlive());
            }
        }

        if (totalPlayers >= 3) {
            overview_TextViewP3Name.setText(activePlayer3.getPlayerDeck().getDeckOwnerName());
            overview_TextViewP3Name.setEnabled(activePlayer3.getPlayerIsAlive());
            overview_TextViewP3Name.setTextColor(activePlayer3.getPlayerIsAlive() ? activePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_TextViewP3Life.setText(String.valueOf(activePlayer3.getPlayerLife()));
            overview_TextViewP3Life.setTextSize(TypedValue.COMPLEX_UNIT_SP, (activePlayer3.getPlayerLife() > 99 || activePlayer3.getPlayerLife() < -99) ? (totalPlayers == 3 ? 100 : 70) : (totalPlayers == 3 ? 120 : 100));
            overview_TextViewP3Life.setEnabled(activePlayer3.getPlayerIsAlive());
            overview_TextViewP3Life.setTextColor(activePlayer3.getPlayerIsAlive() ? activePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_lifePositiveP3.setColorFilter(activePlayer1.getPlayerIsAlive() ? activePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_lifeNegativeP3.setColorFilter(activePlayer1.getPlayerIsAlive() ? activePlayer3.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_TextViewP3EDH1.setText(String.valueOf(activePlayer3.getPlayerEDH1()));
            overview_TextViewP3EDH1.setEnabled(activePlayer3.getPlayerIsAlive());
            overview_TextViewP3EDH2.setText(String.valueOf(activePlayer3.getPlayerEDH2()));
            overview_TextViewP3EDH2.setEnabled(activePlayer3.getPlayerIsAlive());
            overview_TextViewP3EDH3.setText(String.valueOf(activePlayer3.getPlayerEDH3()));
            overview_TextViewP3EDH3.setEnabled(activePlayer3.getPlayerIsAlive());
            if (totalPlayers >= 4) {
                overview_TextViewP3EDH4.setText(String.valueOf(activePlayer3.getPlayerEDH4()));
                overview_TextViewP3EDH4.setEnabled(activePlayer3.getPlayerIsAlive());
            }
        }

        if (totalPlayers >= 4) {
            overview_TextViewP4Name.setText(activePlayer4.getPlayerDeck().getDeckOwnerName());
            overview_TextViewP4Name.setEnabled(activePlayer4.getPlayerIsAlive());
            overview_TextViewP4Name.setTextColor(activePlayer4.getPlayerIsAlive() ? activePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_TextViewP4Life.setText(String.valueOf(activePlayer4.getPlayerLife()));
            overview_TextViewP4Life.setTextSize(TypedValue.COMPLEX_UNIT_SP, (activePlayer4.getPlayerLife() > 99 || activePlayer4.getPlayerLife() < -99) ? 70 : 100);
            overview_TextViewP4Life.setEnabled(activePlayer4.getPlayerIsAlive());
            overview_TextViewP4Life.setTextColor(activePlayer4.getPlayerIsAlive() ? activePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_lifePositiveP4.setColorFilter(activePlayer1.getPlayerIsAlive() ? activePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_lifeNegativeP4.setColorFilter(activePlayer1.getPlayerIsAlive() ? activePlayer4.getPlayerDeck().getDeckShieldColor()[0] : Color.LTGRAY);
            overview_TextViewP4EDH1.setText(String.valueOf(activePlayer4.getPlayerEDH1()));
            overview_TextViewP4EDH1.setEnabled(activePlayer4.getPlayerIsAlive());
            overview_TextViewP4EDH2.setText(String.valueOf(activePlayer4.getPlayerEDH2()));
            overview_TextViewP4EDH2.setEnabled(activePlayer4.getPlayerIsAlive());
            overview_TextViewP4EDH3.setText(String.valueOf(activePlayer4.getPlayerEDH3()));
            overview_TextViewP4EDH3.setEnabled(activePlayer4.getPlayerIsAlive());
            overview_TextViewP4EDH4.setText(String.valueOf(activePlayer4.getPlayerEDH4()));
            overview_TextViewP4EDH4.setEnabled(activePlayer4.getPlayerIsAlive());
        }
    }
}
