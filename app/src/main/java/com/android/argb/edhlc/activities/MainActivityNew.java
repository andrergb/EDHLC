package com.android.argb.edhlc.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
                startActivity(new Intent(this, OverviewActivity.class));
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
        totalPlayers = mSharedPreferences.getInt(Constants.CURRENT_GAME_TOTAL_PLAYERS, 0);

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
            setActionBarColor(getResources().getIntArray(R.array.edh_default)[0]);
            viewNewGame.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        } else {
            int color = getActivePlayer(tabLayout.getSelectedTabPosition()).getPlayerDeck().getDeckShieldColor()[0];
            if (color == 0)
                color = getResources().getIntArray(R.array.edh_default)[0];
            setActionBarColor(color);
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
        viewNewGame = (RelativeLayout) findViewById(R.id.viewPagerMainNewGame);
        viewPager = (ViewPager) findViewById(R.id.viewPagerMain);
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
}
