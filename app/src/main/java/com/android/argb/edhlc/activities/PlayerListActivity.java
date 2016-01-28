package com.android.argb.edhlc.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.ExpandableListAdapter;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Drawer.DrawerPlayerList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerListActivity extends ActionBarActivity {

    private static CheckBox checkBox;
    BroadcastReceiver mPlayerCRUDBroadcastReceiver;
    ExpandableListAdapter mExpandableListAdapter;
    ExpandableListView mExpandableListView;
    List<String[]> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    Activity activity;

    private PlayersDataAccessObject playersDB;
    private DecksDataAccessObject decksDB;
    private RecordsDataAccessObject recordsDB;

    private DrawerPlayerList mDrawerPlayerList;

    public void createLayout(View view) {
        if (view != null) {
            checkBox = (CheckBox) findViewById(R.id.checkBoxKeepScreenOn);

            // get the listview
            mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

            mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Toast.makeText(
                            getApplicationContext(),
                            listDataHeader.get(groupPosition)[0]
                                    + " : "
                                    + listDataChild.get(
                                    listDataHeader.get(groupPosition)[0]).get(
                                    childPosition), Toast.LENGTH_SHORT)
                            .show();


//                    Intent intent = new Intent(PlayerListActivity.this, RecordsActivity.class);
//                    intent.putExtra("RECORDS_PLAYER_NAME", listDataHeader.get(groupPosition)[0]);
//                    intent.putExtra("RECORDS_DECK_NAME", listDataChild.get(
//                            listDataHeader.get(groupPosition)[0]).get(
//                            childPosition));
//                    startActivity(intent);

                    Intent intent = new Intent(PlayerListActivity.this, PlayerActivity.class);
                    intent.putExtra("PLAYERNAME", listDataHeader.get(groupPosition)[0]);
                    startActivity(intent);

                    return false;
                }
            });

            mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                }
            });

            mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerPlayerList.isDrawerOpen())
            mDrawerPlayerList.dismiss();
        else
            super.onBackPressed();
    }

    public void onClickButtonMoreHeader(final String player) {
        View viewMoreHeader = LayoutInflater.from(this).inflate(R.layout.dialog_header_options, null);
        RelativeLayout layoutAddDeck = (RelativeLayout) viewMoreHeader.findViewById(R.id.layoutAddDeck);
        RelativeLayout layoutEditPlayer = (RelativeLayout) viewMoreHeader.findViewById(R.id.layoutEditPlayer);
        RelativeLayout layoutRemovePlayer = (RelativeLayout) viewMoreHeader.findViewById(R.id.layoutDeletePlayer);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(viewMoreHeader);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();

        layoutAddDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        layoutEditPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditPlayerDialog(v, player);
                alertDialog.dismiss();
            }
        });
        layoutRemovePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playersDB.deletePlayer(player) != 0) {
                    updateLayout();
                    Toast.makeText(v.getContext(), player + " removed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });
    }

    public void onClickKeepScreenOn(View view) {
        if (!checkBox.isChecked()) {
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 0).commit();
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).edit().putInt(Constants.SCREEN_ON, 1).commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerPlayerList.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //DrawerMain menu
        if (mDrawerPlayerList.getDrawerToggle().onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playerlist);
        activity = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.edh_default_secondary));
        }

        recordsDB = new RecordsDataAccessObject(this);
        recordsDB.open();
        decksDB = new DecksDataAccessObject(this);
        decksDB.open();
        playersDB = new PlayersDataAccessObject(this);
        playersDB.open();

        //DrawerMain menu
        List<String[]> drawerLists = new ArrayList<>();
        drawerLists.add(getResources().getStringArray(R.array.string_menu_player_list_1));
        drawerLists.add(getResources().getStringArray(R.array.string_menu_player_list_2));
        assert getSupportActionBar() != null;
        mDrawerPlayerList = new DrawerPlayerList(this, drawerLists);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Broadcast
        mPlayerCRUDBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateLayout();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mPlayerCRUDBroadcastReceiver,
                new IntentFilter(Constants.BROADCAST_INTENT_FILTER_PLAYER_CRUD)
        );

        createLayout(this.findViewById(android.R.id.content));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlayerCRUDBroadcastReceiver);
        playersDB.close();
        recordsDB.close();
        decksDB.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerPlayerList.getDrawerToggle().syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        updateLayout();
    }

    private void createEditPlayerDialog(final View view, final String oldName) {
        View playerNameView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_player_name, null);
        final EditText userInput = (EditText) playerNameView.findViewById(R.id.editTextPlayerName);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(playerNameView);
        alertDialogBuilder.setTitle("Edit Player");
        alertDialogBuilder.setMessage("Choose a new name for " + oldName + ": ");
        alertDialogBuilder.setPositiveButton("EDIT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
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
                    long result = playersDB.updatePlayer(oldName, newName);
                    if (result != -1) {
                        decksDB.updateDeck(oldName, newName);
                        recordsDB.updateRecord(oldName, newName);
                        updateLayout();
                        Toast.makeText(view.getContext(), newName + " edited", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(view.getContext(), "Fail: Player " + newName + " already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Insert a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createRemovePlayerDialog(final View view, final String player) {

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        List<String> auxAllPlayers = new ArrayList<>(playersDB.getAllPlayers());
        for (int i = 0; i < auxAllPlayers.size(); i++) {
            String playerName = auxAllPlayers.get(i);
            int totalGames = recordsDB.getAllRecordsByPlayerName(playerName).size();
            int totalDecks = decksDB.getAllDeckByPlayerName(playerName).size();

            //TODO plural
            listDataHeader.add(new String[]{playerName, totalDecks + " decks, " + totalGames + " games"});
        }

        for (int i = 0; i < listDataHeader.size(); i++) {
            List<Deck> decks = decksDB.getAllDeckByPlayerName(listDataHeader.get(i)[0]);

            List<String> auxDecks = new ArrayList<>();
            for (int j = 0; j < decks.size(); j++)
                auxDecks.add(decks.get(j).getDeckName());

            listDataChild.put(listDataHeader.get(i)[0], auxDecks);
        }
    }

    private void updateLayout() {
        prepareListData();
        mExpandableListAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        mExpandableListView.setAdapter(mExpandableListAdapter);

        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}
