package com.android.argb.edhlc.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.PlayerListAdapter;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Player;

import java.util.ArrayList;
import java.util.List;

//Crop: https://github.com/lvillani/android-cropimage
public class PlayerListActivity extends AppCompatActivity {
    private DecksDataAccessObject decksDB;
    private RecordsDataAccessObject recordsDB;
    private PlayersDataAccessObject playersDB;

    //Card - Deck list
    private ListView listPlayer;
    private List<String[]> playerList;  // 0 title - 1 subTitle - 2 selection
    private PlayerListAdapter mPlayerListAdapter;

    ///Drawer
    private DrawerLayout mPlayerDrawerLayout;
    private LinearLayout mPlayerDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Switch switchScreen;

    private boolean mIsInEditMode;
    private ActionBar mActionBar;
    private Menu optionMenu;
    private View statusBarBackground;

    @Override
    public void onBackPressed() {
        if (mPlayerDrawerLayout.isDrawerOpen(mPlayerDrawer))
            mPlayerDrawerLayout.closeDrawers();
        else if (mIsInEditMode) {
            mIsInEditMode = false;
            updateEditMode();
        } else {
            super.onBackPressed();
        }
    }

    //TODO card general info
    public void onClickCardExpansion(View v) {
//        switch (v.getId()) {
//            case R.id.relativeCardTitlePlayerInfo:
//                Utils.toggleCardExpansion(this, cardViewPlayerInfo, textTitlePlayerInfo, iconIndicatorPlayerInfo, relativeTitlePlayerInfo.getHeight(), mCardViewFullHeightPlayerInfo);
//                break;
//        }
    }

    public void onClickCheckBoxEditDeckList(View view) {
        optionMenu.findItem(R.id.action_edit_player).setVisible(mPlayerListAdapter.getTotalDataChecked() < 2);
        mPlayerListAdapter.notifyDataSetChanged();
    }

    public void onClickDrawerItem(View view) {
        switch (view.getId()) {
            case R.id.drawerItemHome:
                mPlayerDrawerLayout.closeDrawers();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

            case R.id.drawerItemPlayers:
                mPlayerDrawerLayout.closeDrawers();
                startActivity(new Intent(this, PlayerListActivityOLD.class));
                finish();
                break;

            case R.id.drawerItemRecords:
                mPlayerDrawerLayout.closeDrawers();
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
                mPlayerDrawerLayout.closeDrawers();
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                break;

            case R.id.drawerItemAbout:
                mPlayerDrawerLayout.closeDrawers();
                //TODO
                break;
        }
    }

    public void onClickFabButton(View view) {
        switch (view.getId()) {
            case R.id.fabAddPlayerToList:
                dialogAddPlayer();
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
        getMenuInflater().inflate(R.menu.menu_player_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //Option menu
        switch (item.getItemId()) {
            case R.id.action_edit_player:
                for (int i = 0; i < playerList.size(); i++)
                    if (playerList.get(i)[2].equalsIgnoreCase("true"))
                        dialogEditPlayer(playerList.get(i)[0]);
                return true;
            case R.id.action_delete_player:
                dialogRemovePlayer();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playerlist);

        decksDB = new DecksDataAccessObject(this);
        decksDB.open();

        recordsDB = new RecordsDataAccessObject(this);
        recordsDB.open();

        playersDB = new PlayersDataAccessObject(this);
        playersDB.open();

        createStatusBar();
        createToolbar();
        createDrawer();
        createLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        decksDB.close();
        recordsDB.close();
        playersDB.close();
    }

    @Override
    protected void onPause() {
        if (mIsInEditMode) {
            mIsInEditMode = false;
            updateEditMode();
        }

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
        mPlayerDrawer = (LinearLayout) findViewById(R.id.player_list_drawer);
        mPlayerDrawerLayout = (DrawerLayout) findViewById(R.id.player_list_drawer_layout);
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

        LinearLayout drawerItemPlayers = (LinearLayout) findViewById(R.id.drawerItemPlayers);
        ImageView drawerItemIconPlayers = (ImageView) findViewById(R.id.drawerItemIconPlayers);
        TextView drawerItemTextPlayers = (TextView) findViewById(R.id.drawerItemTextPlayers);
        drawerItemPlayers.setBackgroundColor(ContextCompat.getColor(this, R.color.gray200));
        drawerItemIconPlayers.setColorFilter(ContextCompat.getColor(this, R.color.accent_color));
        drawerItemTextPlayers.setTextColor(ContextCompat.getColor(this, R.color.accent_color));
    }

    private void createLayout() {
        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        nestedScrollView.smoothScrollBy(0, 0);

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

        //FloatingActionButton
        //FloatingActionButton fabAddPlayerToList = (FloatingActionButton) findViewById(R.id.fabAddPlayerToList);

        //Card playerList
        listPlayer = (ListView) findViewById(R.id.listPlayer);
        listPlayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mIsInEditMode) {
                    Intent intent = new Intent(PlayerListActivity.this, PlayerActivity.class);
                    intent.putExtra("PLAYER_NAME", playerList.get(position)[0]);
                    startActivity(intent);
                } else {
                    mPlayerListAdapter.checkBoxSetSelection(position, mPlayerListAdapter.checkBoxGetSelection(position) ? "false" : "true");
                    optionMenu.findItem(R.id.action_edit_player).setVisible(mPlayerListAdapter.getTotalDataChecked() < 2);
                    mPlayerListAdapter.notifyDataSetChanged();
                }
            }
        });
        listPlayer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPlayerListAdapter.checkBoxSetSelection(position, "true");
                mIsInEditMode = true;
                updateEditMode();
                return true;
            }
        });

        playerList = new ArrayList<>();
        mPlayerListAdapter = new PlayerListAdapter(this, playerList);
        listPlayer.setAdapter(mPlayerListAdapter);
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
        mActionBar.setTitle("Player list");
    }

    private void dialogAddPlayer() {
        View playerNameView = LayoutInflater.from(PlayerListActivity.this).inflate(R.layout.dialog_player_name, null);
        final EditText userInput = (EditText) playerNameView.findViewById(R.id.editTextPlayerName);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlayerListActivity.this);
        alertDialogBuilder.setView(playerNameView);
        alertDialogBuilder.setTitle("Add new Player");
        alertDialogBuilder.setMessage("Player name:");
        alertDialogBuilder.setPositiveButton("ADD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        alertDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();

        //Override POSITIVE button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempName = userInput.getText().toString();
                if (!tempName.equalsIgnoreCase("")) {
                    if (playersDB.addPlayer(tempName, Utils.getCurrentDate()) != -1) {
                        Toast.makeText(PlayerListActivity.this, tempName + " added", Toast.LENGTH_SHORT).show();
                        updateLayout();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(PlayerListActivity.this, "Fail: Player " + tempName + " already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PlayerListActivity.this, "Insert a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dialogEditPlayer(final String mPlayerName) {
        View playerNameView = LayoutInflater.from(PlayerListActivity.this).inflate(R.layout.dialog_player_name, null);
        final EditText userInput = (EditText) playerNameView.findViewById(R.id.editTextPlayerName);
        userInput.setText(mPlayerName);
        userInput.setSelection(userInput.getText().length());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlayerListActivity.this);
        alertDialogBuilder.setView(playerNameView);
        alertDialogBuilder.setTitle("Edit " + mPlayerName);
        alertDialogBuilder.setPositiveButton("EDIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        alertDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();

        //Override POSITIVE button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = userInput.getText().toString();
                if (!newName.equalsIgnoreCase("")) {
                    long result = playersDB.updatePlayer(mPlayerName, newName);
                    if (result != -1) {
                        decksDB.updateDeckOwner(mPlayerName, newName);
                        recordsDB.updateRecord(mPlayerName, newName);
                        updateLayout();

                        mIsInEditMode = false;
                        updateEditMode();

                        Toast.makeText(PlayerListActivity.this, newName + " edited", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(PlayerListActivity.this, "Player " + newName + " already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PlayerListActivity.this, "Insert a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void dialogRemovePlayer() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlayerListActivity.this);
        alertDialogBuilder.setTitle("Delete player");
        alertDialogBuilder.setMessage("Are you sure to delete these players?");
        alertDialogBuilder.setPositiveButton("DELETE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        for (int i = 0; i < playerList.size(); i++)
                            if (playerList.get(i)[2].equalsIgnoreCase("true"))
                                playersDB.deletePlayer(playerList.get(i)[0]);

                        updateLayout();
                        mIsInEditMode = false;
                        updateEditMode();

                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void updateEditMode() {
        int color = mIsInEditMode ? ContextCompat.getColor(this, R.color.secondary_text) : ContextCompat.getColor(this, R.color.primary_color);

        statusBarBackground.setBackgroundColor(color);
        mActionBar.setBackgroundDrawable(new ColorDrawable(color));

        optionMenu.findItem(R.id.action_edit_player).setVisible(mIsInEditMode);
        optionMenu.findItem(R.id.action_delete_player).setVisible(mIsInEditMode);

        if (!mIsInEditMode)
            mPlayerListAdapter.checkBoxClearAllSelections();
        mPlayerListAdapter.setIsInEditMode(mIsInEditMode);
        mPlayerListAdapter.notifyDataSetChanged();
    }

    private void updateLayout() {

        //Card playerList
        List<Player> allPlayers = playersDB.getAllPlayers();

        if (playerList == null)
            playerList = new ArrayList<>();
        else
            playerList.clear();

        for (int i = 0; i < allPlayers.size(); i++) {
            Player auxPlayer = allPlayers.get(i);

            String title = auxPlayer.getPlayerName();

            int totalDecks = decksDB.getAllDeckByPlayerName(title).size();
            int totalGames = recordsDB.getAllRecordsByPlayerName(title).size();
            String subTitle = totalDecks + (totalDecks == 1 ? " deck" : " decks") + " and " + totalGames + (totalGames == 1 ? " game" : " games");

            playerList.add(new String[]{title, subTitle, "false"});
        }
        mPlayerListAdapter.notifyDataSetChanged();
        Utils.justifyListViewHeightBasedOnChildren(listPlayer);
    }
}
