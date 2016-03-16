package com.android.argb.edhlc.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.NewGameAdapter;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;

import java.util.ArrayList;

//Crop: https://github.com/lvillani/android-cropimage
public class NewGameActivity extends AppCompatActivity {

    ///Drawer
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Switch switchScreen;

    private ActionBar mActionBar;
    private Menu optionMenu;
    private View statusBarBackground;

    private ListView listViewNewGame;

    private ArrayList<String[]> playersList; // 0: type - 1: item
    private NewGameAdapter mPlayersAdapter;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawer))
            mDrawerLayout.closeDrawers();

        super.onBackPressed();
    }

    public void onClickDrawerItem(View view) {
        switch (view.getId()) {
            case R.id.drawerItemHome:
                mDrawerLayout.closeDrawers();
                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                this.finish();
                break;

            case R.id.drawerItemPlayers:
                mDrawerLayout.closeDrawers();
                break;

            case R.id.drawerItemRecords:
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, RecordsActivity.class));
                finish();
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
                mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                break;

            case R.id.drawerItemAbout:
                mDrawerLayout.closeDrawers();
                //TODO
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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        createStatusBar();
        createToolbar();
        createDrawer();
        createLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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

        updateLayout();
    }

    private void createDrawer() {
        mDrawer = (LinearLayout) findViewById(R.id.new_game_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.new_game_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //TODO
//        LinearLayout drawerItemPlayers = (LinearLayout) findViewById(R.id.drawerItemPlayers);
//        ImageView drawerItemIconPlayers = (ImageView) findViewById(R.id.drawerItemIconPlayers);
//        TextView drawerItemTextPlayers = (TextView) findViewById(R.id.drawerItemTextPlayers);
//        drawerItemPlayers.setBackgroundColor(ContextCompat.getColor(this, R.color.gray200));
//        drawerItemIconPlayers.setColorFilter(ContextCompat.getColor(this, R.color.accent_color));
//        drawerItemTextPlayers.setTextColor(ContextCompat.getColor(this, R.color.accent_color));
    }

    private void createLayout() {
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


        //TODO
        playersList = new ArrayList<>();
        playersList.add(new String[]{"PLAYER", "Dezao", "FALSE"});
        playersList.add(new String[]{"deck", "animar", "TRUE"});
        playersList.add(new String[]{"deck", "zur", "FALSE"});
        playersList.add(new String[]{"PLAYER", "Monkey", "FALSE"});
        playersList.add(new String[]{"deck", "zurgo", "FALSE"});
        playersList.add(new String[]{"deck", "olivia", "FALSE"});
        playersList.add(new String[]{"deck", "olivia2", "FALSE"});
        playersList.add(new String[]{"deck", "olivia3", "FALSE"});
        playersList.add(new String[]{"PLAYER", "a1", "FALSE"});
        playersList.add(new String[]{"deck", "a2", "FALSE"});
        playersList.add(new String[]{"PLAYER", "v2", "FALSE"});
        playersList.add(new String[]{"deck", "b2", "FALSE"});
        playersList.add(new String[]{"PLAYER", "d2", "FALSE"});
        playersList.add(new String[]{"deck", "g2", "FALSE"});

        mPlayersAdapter = new NewGameAdapter(this, playersList);
        listViewNewGame = (ListView) findViewById(R.id.new_game_list);
        listViewNewGame.setAdapter(mPlayersAdapter);
        listViewNewGame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPlayersAdapter.isDeck(position)) {

                    String currentSelection = playersList.get(position)[2];

                    if (getCurrentTotalPlayers() < 4) {
                        setPlayerSelected(position, currentSelection.equalsIgnoreCase("TRUE") ? "FALSE" : "TRUE");
                        mActionBar.setTitle("New Game - " + getCurrentTotalPlayers());
                    } else if (isPlayerSelected(position)) {
                        setPlayerSelected(position, currentSelection.equalsIgnoreCase("TRUE") ? "FALSE" : "TRUE");
                        mActionBar.setTitle("New Game - " + getCurrentTotalPlayers());
                    } else if (currentSelection.equalsIgnoreCase("TRUE")) {
                        playersList.get(position)[2] = "FALSE";
                    } else {
                        Log.d("dezao", "limit reached - 4 players");
                    }

                    mPlayersAdapter.notifyDataSetChanged();
                }
            }
        });
    }


    private boolean isPlayerSelected(int position) {
        for (int i = position; i >= 0; i--)
            if (mPlayersAdapter.isPlayer(i))
                return playersList.get(i)[2].equalsIgnoreCase("TRUE");
        return false;
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
        mActionBar.setTitle("New Game");
    }

    private int getCurrentTotalPlayers() {
        int ret = 0;
        for (int i = 0; i < playersList.size(); i++)
            if (playersList.get(i)[0].equalsIgnoreCase("DECK"))
                if (playersList.get(i)[2].equalsIgnoreCase("TRUE"))
                    ret++;
        return ret;
    }

    private void setPlayerSelected(int position, String state) {
        if (state.equalsIgnoreCase("TRUE")) {
            for (int i = position - 1; i >= 0; i--) {
                if (mPlayersAdapter.isDeck(i)) {
                    if (mPlayersAdapter.isSelected(i)) {
                        playersList.get(i)[2] = "FALSE";
                    }
                } else {
                    break;
                }
            }
            for (int i = position + 1; i < playersList.size(); i++) {
                if (mPlayersAdapter.isDeck(i)) {
                    if (mPlayersAdapter.isSelected(i)) {
                        playersList.get(i)[2] = "FALSE";
                    }
                } else {
                    break;
                }
            }
        }

        playersList.get(position)[2] = state;

        for (int i = position; i >= 0; i--) {
            if (mPlayersAdapter.isPlayer(i)) {
                playersList.get(i)[2] = state;
                return;
            }
        }
    }

    private void updateLayout() {
        //TODO
    }
}
