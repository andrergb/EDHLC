package com.android.argb.edhlc.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;
import com.android.argb.edhlc.objects.ActivePlayerNew;
import com.android.argb.edhlc.objects.Deck;

import java.util.ArrayList;
import java.util.List;

public class MainActivityNew extends AppCompatActivity implements MainFragment.OnUpdateData {

    ///Drawer
    private DrawerLayout mPlayerDrawerLayout;
    private LinearLayout mPlayerDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Switch switchScreen;

    private ActionBar mActionBar;
    private Menu optionMenu;
    private View statusBarBackground;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RelativeLayout viewNewGame;
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

    @Override
    public void onBackPressed() {
        if (mPlayerDrawerLayout.isDrawerOpen(mPlayerDrawer))
            mPlayerDrawerLayout.closeDrawers();
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionMenu = menu;
        getMenuInflater().inflate(R.menu.menu_player_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tabLayout != null)
            outState.putInt("TAB_POSITION", tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        createStatusBar();
        createToolbar();
        createDrawer();
        createLayout();

        SharedPreferences mSharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE);
        totalPlayers = mSharedPreferences.getInt(Constants.CURRENT_GAME_TOTAL_PLAYERS, 4);

        if (isValidGame()) {
            viewNewGame.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);

            activePlayer1 = Utils.loadPlayerFromSharedPreferences(this, 0);
            activePlayer2 = Utils.loadPlayerFromSharedPreferences(this, 1);
            if (totalPlayers >= 3)
                activePlayer3 = Utils.loadPlayerFromSharedPreferences(this, 2);
            if (totalPlayers >= 4)
                activePlayer4 = Utils.loadPlayerFromSharedPreferences(this, 3);

            fragments = new ArrayList<>();
            fragments.add(MainFragment.newInstance(activePlayer1, totalPlayers));
            fragments.add(MainFragment.newInstance(activePlayer2, totalPlayers));
            if (totalPlayers >= 3)
                fragments.add(MainFragment.newInstance(activePlayer3, totalPlayers));
            if (totalPlayers >= 4)
                fragments.add(MainFragment.newInstance(activePlayer4, totalPlayers));
            mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), MainActivityNew.this, fragments);

            viewPager.setAdapter(mPagerAdapter);
            viewPager.setOffscreenPageLimit(fragments.size());

            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (viewPager != null)
            viewPager.setCurrentItem(savedInstanceState.getInt("TAB_POSITION"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            switchScreen.setChecked(true);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            switchScreen.setChecked(false);
        }

        if (!isValidGame()) {
            viewNewGame.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        }
    }

    public void onClickNewGame(View view) {
        //TODO just for test -> populate shared on new game activity
        Utils.savePlayerInSharedPreferences(this, new ActivePlayerNew(new Deck("Player 0", "Deck 0", new int[]{0xFFFF0000}), true, 41, 0, 0, 0, 0, 0));
        Utils.savePlayerInSharedPreferences(this, new ActivePlayerNew(new Deck("Player 1", "Deck 1", new int[]{0xFFFF0000}), true, 41, 0, 0, 0, 0, 1));
        Utils.savePlayerInSharedPreferences(this, new ActivePlayerNew(new Deck("Player 2", "Deck 2", new int[]{0xFFFF0000}), true, 41, 0, 0, 0, 0, 2));
        Utils.savePlayerInSharedPreferences(this, new ActivePlayerNew(new Deck("Player 3", "Deck 3", new int[]{0xFFFF0000}), true, 41, 0, 0, 0, 0, 3));

        //TODO new game activity
        startActivity(new Intent(this, NewGameActivity.class));
        this.finish();
    }

    public boolean isValidGame() {
        boolean isValid = true;
        DecksDataAccessObject deckDb = new DecksDataAccessObject(this);
        PlayersDataAccessObject playersDB = new PlayersDataAccessObject(this);

        deckDb.open();
        playersDB.open();

        for (int i = 0; i < totalPlayers; i++) {
            ActivePlayerNew auxPlayer = Utils.loadPlayerFromSharedPreferences(this, i);
            if (!deckDb.hasDeck(auxPlayer.getPlayerDeck()) || !playersDB.hasPlayer(auxPlayer.getPlayerDeck().getDeckOwnerName())) {
                isValid = false;
            }
        }

        playersDB.close();
        deckDb.close();
        return isValid;
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

//        LinearLayout drawerItemPlayers = (LinearLayout) findViewById(R.id.drawerItemPlayers);
//        ImageView drawerItemIconPlayers = (ImageView) findViewById(R.id.drawerItemIconPlayers);
//        TextView drawerItemTextPlayers = (TextView) findViewById(R.id.drawerItemTextPlayers);
//        drawerItemPlayers.setBackgroundColor(ContextCompat.getColor(this, R.color.gray200));
//        drawerItemIconPlayers.setColorFilter(ContextCompat.getColor(this, R.color.accent_color));
//        drawerItemTextPlayers.setTextColor(ContextCompat.getColor(this, R.color.accent_color));
    }

    private void createLayout() {
        //TODO enhance layout
        viewNewGame = (RelativeLayout) findViewById(R.id.viewPagerMainNewGame);
        viewPager = (ViewPager) findViewById(R.id.viewPagerMain);
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

    private boolean isPlayerActiveAndAlive(int playerTag) {
        if (totalPlayers < playerTag)
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

}
