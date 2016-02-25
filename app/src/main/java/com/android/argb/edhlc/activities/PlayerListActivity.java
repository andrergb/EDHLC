package com.android.argb.edhlc.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PlayerListActivity extends AppCompatActivity {

    private static CheckBox checkBox;
    ExpandableListAdapter mExpandableListAdapter;
    ExpandableListView mExpandableListView;
    List<String[]> listDataHeader;
    HashMap<String, List<String[]>> listDataChild;
    Activity activity;

    private PlayersDataAccessObject playersDB;
    private DecksDataAccessObject decksDB;
    private RecordsDataAccessObject recordsDB;

    private DrawerPlayerList mDrawerPlayerList;
    private CheckBox checkBoxManaWhite;
    private CheckBox checkBoxManaBlue;
    private CheckBox checkBoxManaBlack;
    private CheckBox checkBoxManaRed;
    private CheckBox checkBoxManaGreen;
    private CheckBox checkBoxManaColorless;

    public void createLayout(View view) {
        if (view != null) {
            checkBox = (CheckBox) findViewById(R.id.checkBoxKeepScreenOn);

            mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);

            mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition)[0] + " : " + listDataChild.get(listDataHeader.get(groupPosition)[0]).get(childPosition)[0], Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(PlayerListActivity.this, DeckActivity.class);
                    intent.putExtra("PLAYERNAME", listDataHeader.get(groupPosition)[0]);
                    intent.putExtra("DECKNAME", listDataChild.get(listDataHeader.get(groupPosition)[0]).get(childPosition)[0]);
                    intent.putExtra("DECKIDENTITY", listDataChild.get(listDataHeader.get(groupPosition)[0]).get(childPosition)[1]);
                    startActivity(intent);

                    PlayerListActivity.this.finish();
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
                    //TODO test playerActivity
                    Intent intent = new Intent(PlayerListActivity.this, PlayerActivity.class);
                    intent.putExtra("PLAYERNAME", listDataHeader.get(groupPosition)[0]);
                    startActivity(intent);
                }
            });
        }

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        prepareListData();

        mExpandableListAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        mExpandableListView.setAdapter(mExpandableListAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerPlayerList.isDrawerOpen())
            mDrawerPlayerList.dismiss();
        else
            super.onBackPressed();
    }

    public void onClickButtonMoreHeader(final String player, final int position) {
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
                createAddDeckDialog(v, player, position);
                alertDialog.dismiss();
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
                createDeletePlayerDialog(v, player);
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

    public void onClickManaCheckBox(View view) {
        if (checkBoxManaWhite.isChecked() ||
                checkBoxManaBlue.isChecked() ||
                checkBoxManaBlack.isChecked() ||
                checkBoxManaRed.isChecked() ||
                checkBoxManaGreen.isChecked())
            checkBoxManaColorless.setChecked(false);
        else
            checkBoxManaColorless.setChecked(true);
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
            window.setStatusBarColor(this.getResources().getColor(R.color.secondary_color));
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

        createLayout(this.findViewById(android.R.id.content));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private void createAddDeckDialog(final View view, final String player, final int position) {
        View playerNameView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_deck, null);
        final EditText userInput = (EditText) playerNameView.findViewById(R.id.editTextEditDeckName);
        checkBoxManaWhite = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_white);
        checkBoxManaBlue = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_blue);
        checkBoxManaBlack = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_black);
        checkBoxManaRed = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_red);
        checkBoxManaGreen = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_green);
        checkBoxManaColorless = (CheckBox) playerNameView.findViewById(R.id.checkbox_mana_colorless);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(playerNameView);
        alertDialogBuilder.setTitle("Add new Deck for " + player);
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

                    String colorIdentity = "";
                    colorIdentity = checkBoxManaWhite.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaBlue.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaBlack.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaRed.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaGreen.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");
                    colorIdentity = checkBoxManaColorless.isChecked() ? colorIdentity.concat("1") : colorIdentity.concat("0");

                    Calendar c = Calendar.getInstance();
                    String date = Constants.MONTH[c.get(Calendar.MONTH)]
                            + " " + String.valueOf(c.get(Calendar.DAY_OF_MONTH))
                            + ", " + String.valueOf(c.get(Calendar.YEAR));

                    if (decksDB.addDeck(new Deck(player, tempName, new int[]{PlayerListActivity.this.getResources().getColor(R.color.primary_color), PlayerListActivity.this.getResources().getColor(R.color.secondary_color)}, colorIdentity, date)) != -1) {
                        Toast.makeText(view.getContext(), tempName + " added", Toast.LENGTH_SHORT).show();
                        updateLayout();
                        alertDialog.dismiss();
                        mExpandableListView.expandGroup(position);
                    } else {
                        Toast.makeText(view.getContext(), "Fail: Deck " + tempName + " already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Insert a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createDeletePlayerDialog(final View view, final String player) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setTitle("Delete " + player);
        alertDialogBuilder.setPositiveButton("DELETE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (playersDB.deletePlayer(player) != 0) {
                            updateLayout();
                            Toast.makeText(view.getContext(), player + " removed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
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

    private void createEditPlayerDialog(final View view, final String oldName) {
        View playerNameView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_player_name, null);
        final EditText userInput = (EditText) playerNameView.findViewById(R.id.editTextPlayerName);
        userInput.setText(oldName);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(playerNameView);
        alertDialogBuilder.setTitle("Edit " + oldName);
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
                        decksDB.updateDeckOwner(oldName, newName);
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

    private void prepareListData() {
        if (listDataHeader == null)
            listDataHeader = new ArrayList<>();
        else
            listDataHeader.clear();

        if (listDataChild == null)
            listDataChild = new HashMap<>();
        else
            listDataChild.clear();


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

            List<String[]> auxDecks = new ArrayList<>();
            for (int j = 0; j < decks.size(); j++) {
                auxDecks.add(new String[]{decks.get(j).getDeckName(), decks.get(j).getDeckIdentity()});
            }

            listDataChild.put(listDataHeader.get(i)[0], auxDecks);
        }
    }

    private void updateLayout() {
        prepareListData();

        mExpandableListAdapter.notifyDataSetChanged();

        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}
