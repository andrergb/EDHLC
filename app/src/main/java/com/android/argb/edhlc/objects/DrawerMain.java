package com.android.argb.edhlc.objects;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.activities.PlayerListActivity;
import com.android.argb.edhlc.activities.RecordsActivity;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * -Created by agbarros on 05/11/2015.
 */
public class DrawerMain {

    public static String BROADCAST_INTENTFILTER_NEWGAME = "broadcast_intentfilter_newgame";
    public static String BROADCAST_MESSAGE_NEWGAME_OPTION = "brodcast_message_newgame_option";
    public static String BROADCAST_MESSAGE_NEWGAME_OPTION_YES = "yes";
    public static String BROADCAST_MESSAGE_NEWGAME_OPTION_NO = "no";
    public static String BROADCAST_MESSAGE_NEWGAME_PLAYERS = "brodcast_message_newgame_players";
    public static String BROADCAST_MESSAGE_NEWGAME_DECKS = "brodcast_message_newgame_decks";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LinearLayout linearLayoutDrawer;

    private Activity parentActivity;

    private PlayersDataAccessObject playersDB;
    private DecksDataAccessObject decksDB;

    public DrawerMain(final Activity parentActivity, List<String[]> options) {
        this.parentActivity = parentActivity;

        linearLayoutDrawer = (LinearLayout) parentActivity.findViewById(R.id.linearLayoutDrawer);
        mDrawerLayout = (DrawerLayout) parentActivity.findViewById(R.id.drawer_layout);

        ListView mDrawerList1 = (ListView) parentActivity.findViewById(R.id.listViewDrawer1);
        mDrawerList1.setAdapter(new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, options.get(0)));
        mDrawerList1.setOnItemClickListener(new DrawerItemClickListener1());

        ListView mDrawerList2 = (ListView) parentActivity.findViewById(R.id.listViewDrawer2);
        mDrawerList2.setAdapter(new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, options.get(1)));
        mDrawerList2.setOnItemClickListener(new DrawerItemClickListener2());

        mDrawerToggle = new ActionBarDrawerToggle(parentActivity, mDrawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                parentActivity.invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                parentActivity.invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        playersDB = new PlayersDataAccessObject(parentActivity);
        decksDB = new DecksDataAccessObject(parentActivity);
    }


    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    public boolean isDrawerOpen() {
        return (mDrawerLayout.isDrawerOpen(linearLayoutDrawer));
    }

    public void dismiss() {
        mDrawerLayout.closeDrawers();
    }

    private class DrawerItemClickListener1 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: //NewGame
                    mDrawerLayout.closeDrawers();
                    createNewGameDialog(view);
                    break;
                case 1: //Log Game
                    mDrawerLayout.closeDrawers();
                    createLogGameTotalDialog(view);
                    break;
                case 2: //Roll a dice
                    mDrawerLayout.closeDrawers();
                    createDiceRangeDialog(view);
                    break;
            }
        }
    }

    private class DrawerItemClickListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: //Players
                    mDrawerLayout.closeDrawers();
                    parentActivity.startActivity(new Intent(parentActivity, PlayerListActivity.class));
                    break;
                case 1: //All Records
                    mDrawerLayout.closeDrawers();
                    Intent intent = new Intent(parentActivity, RecordsActivity.class);
                    intent.putExtra("RECORDS_PLAYER_NAME", "");
                    intent.putExtra("RECORDS_DECK_NAME", "");
                    parentActivity.startActivity(intent);
                    break;
                case 2: //Settings
                    // parentActivity.startActivity(new Intent(parentActivity, SettingsActivity.class));
                    break;
                case 3: //About
                    mDrawerLayout.closeDrawers();
                    createAboutDialog(view);
                    break;
            }
        }
    }

    public void createNewGameDialog(final View view) {
        final Intent intent = new Intent(DrawerMain.BROADCAST_INTENTFILTER_NEWGAME);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setTitle("New Game");
        alertDialogBuilder.setMessage("Do you want to keep the current names?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        intent.putExtra(DrawerMain.BROADCAST_MESSAGE_NEWGAME_OPTION, DrawerMain.BROADCAST_MESSAGE_NEWGAME_OPTION_YES);
                        LocalBroadcastManager.getInstance(parentActivity).sendBroadcast(intent);
                    }
                });
        alertDialogBuilder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        createNewGameTotalDialog(view);
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createNewGameTotalDialog(final View view) {
        listDeck = new ArrayList<>();
        View newGameNewPlayersView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_single_spinner, null);
        final Spinner spinnerTotalPlayers = (Spinner) newGameNewPlayersView.findViewById(R.id.spinner1);

        final ArrayAdapter<String> newGameNewPlayersAdapter = new ArrayAdapter<>(parentActivity, R.layout.row_spinner_selected, new String[]{"2", "3", "4"});
        newGameNewPlayersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTotalPlayers.setAdapter(newGameNewPlayersAdapter);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(newGameNewPlayersView);
        alertDialogBuilder.setTitle("New Game");
        alertDialogBuilder.setMessage("Choose the total players: ");
        alertDialogBuilder.setPositiveButton("Next",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        createNewGamePlayersDialog(view, Integer.parseInt(spinnerTotalPlayers.getSelectedItem().toString()), 1);
                    }
                });
        alertDialogBuilder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createNewGamePlayersDialog(final View view, final int totalPlayers, final int steps) {
        View NewGamePlayersChoiceView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_two_spinners, null);

        final Spinner spinnerPlayerName = (Spinner) NewGamePlayersChoiceView.findViewById(R.id.spinner1);
        final Spinner spinnerPlayerDeck = (Spinner) NewGamePlayersChoiceView.findViewById(R.id.spinner2);
        handleSpinners(spinnerPlayerName, spinnerPlayerDeck);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(NewGamePlayersChoiceView);
        alertDialogBuilder.setTitle("New Game - Player " + steps + "/" + totalPlayers);
        final String[] positions = new String[]{"1st", "2nd", "3rd", "4th"};
        alertDialogBuilder.setMessage("Choose the " + positions[steps - 1] + " Player and Deck:");
        alertDialogBuilder.setPositiveButton("Next",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        alertDialogBuilder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        playersDB.close();
                        decksDB.close();
                    }
                });
        alertDialogBuilder.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (steps == 1) {
                            createNewGameTotalDialog(view);
                        } else {
                            listDeck.remove(listDeck.size() - 1);
                            createNewGamePlayersDialog(view, totalPlayers, steps - 1);
                        }
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //Override POSITIVE button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auxPlayerName = spinnerPlayerName.getSelectedItem().toString();
                String tempPlayerDeck = spinnerPlayerDeck.getSelectedItem().toString();

                if (!auxPlayerName.equalsIgnoreCase(parentActivity.getString(R.string.edh_spinner_player_hint))
                        && !tempPlayerDeck.equalsIgnoreCase(parentActivity.getString(R.string.edh_spinner_deck_hint))) {

                    listDeck.add(new Deck(auxPlayerName, tempPlayerDeck));
                    if (Record.isValidRecord(listDeck)) {
                        alertDialog.dismiss();
                        if (steps != totalPlayers) {
                            createNewGamePlayersDialog(view, totalPlayers, steps + 1);
                        } else {
                            createNewGameConfirmationDialog(view);
                            playersDB.close();
                            decksDB.close();
                        }
                    } else {
                        listDeck.remove(listDeck.size() - 1);
                        Toast.makeText(view.getContext(), "ERROR: Player/Deck already added!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "ERROR: invalid Player/Deck!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNewGameConfirmationDialog(final View view) {
        final Intent intent = new Intent(DrawerMain.BROADCAST_INTENTFILTER_NEWGAME);

        View logView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_log_confirmation, null);

        final List<Map<String, String>> listMapDecks = new ArrayList<>();
        for (int i = 0; i < listDeck.size(); i++) {
            Map<String, String> auxMap = new HashMap<>(2);
            auxMap.put("playerName", listDeck.get(i).getPlayerName());
            auxMap.put("deck", listDeck.get(i).getDeckName());
            listMapDecks.add(auxMap);
        }

        final ListView listViewConfirmation = (ListView) logView.findViewById(R.id.listViewConfirmation);
        SimpleAdapter adapter = new SimpleAdapter(parentActivity,
                listMapDecks,
                R.layout.row_two_item_list,
                new String[]{"playerName", "deck"},
                new int[]{R.id.text1, R.id.text2});
        listViewConfirmation.setAdapter(adapter);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("New Game - Overview");
        alertDialogBuilder.setMessage("Check the Player and Decks: ");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList<String> players = new ArrayList<>();
                        ArrayList<String> decks = new ArrayList<>();
                        for (int i = 0; i < listDeck.size(); i++) {
                            players.add(listDeck.get(i).getPlayerName());
                            decks.add(listDeck.get(i).getDeckName());
                        }
                        intent.putExtra(DrawerMain.BROADCAST_MESSAGE_NEWGAME_OPTION, DrawerMain.BROADCAST_MESSAGE_NEWGAME_OPTION_NO);
                        intent.putStringArrayListExtra(DrawerMain.BROADCAST_MESSAGE_NEWGAME_PLAYERS, players);
                        intent.putStringArrayListExtra(DrawerMain.BROADCAST_MESSAGE_NEWGAME_DECKS, decks);
                        LocalBroadcastManager.getInstance(parentActivity).sendBroadcast(intent);
                    }
                });
        alertDialogBuilder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        playersDB.close();
                        decksDB.close();
                    }
                });
        alertDialogBuilder.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listDeck.remove(listDeck.size() - 1);
                        createNewGamePlayersDialog(view, listDeck.size() + 1, listDeck.size() + 1);
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createAboutDialog(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setTitle("About " + parentActivity.getResources().getString(R.string.app_name));
        String message;
        try {
            message = "\nVersion: " + parentActivity.getPackageManager().getPackageInfo(parentActivity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            message = "";
        }
        alertDialogBuilder.setMessage("Created by ARGB" + message);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private List<Deck> listDeck;

    private void createDiceRangeDialog(final View view) {
        View logView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_roll_a_dice, null);

        final NumberPicker mNumberPicker1 = (NumberPicker) logView.findViewById(R.id.numberPicker1);
        mNumberPicker1.setMinValue(0);
        mNumberPicker1.setMaxValue(99);

        final NumberPicker mNumberPicker2 = (NumberPicker) logView.findViewById(R.id.numberPicker2);
        mNumberPicker2.setMinValue(0);
        mNumberPicker2.setMaxValue(99);
        mNumberPicker2.setValue(99);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("Roll a dice");
        alertDialogBuilder.setMessage("Choose the limits: ");
        alertDialogBuilder.setPositiveButton("Roll",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        int minValue;
                        int maxValue;
                        if (mNumberPicker1.getValue() <= mNumberPicker2.getValue()) {
                            minValue = mNumberPicker1.getValue();
                            maxValue = mNumberPicker2.getValue();
                        } else {
                            minValue = mNumberPicker2.getValue();
                            maxValue = mNumberPicker1.getValue();
                        }

                        createDiceResultDialog(view, minValue, maxValue);
                    }
                });
        alertDialogBuilder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createDiceResultDialog(final View view, int minValue, int maxValue) {
        Random r = new Random();
        int dice = r.nextInt(maxValue - minValue + 1) + minValue;

        View logView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_roll_a_dice_result, null);
        final TextView textViewDiceResult = (TextView) logView.findViewById(R.id.textViewDiceResult);
        textViewDiceResult.setText("" + dice);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("Roll a dice");
        alertDialogBuilder.setMessage("Dice result [" + minValue + ", " + maxValue + "]:");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createLogGameTotalDialog(final View view) {
        listDeck = new ArrayList<>();
        View logView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_single_spinner, null);
        final Spinner spinnerTotalPlayers = (Spinner) logView.findViewById(R.id.spinner1);

        final ArrayAdapter<String> playersNameAdapter = new ArrayAdapter<>(parentActivity, R.layout.row_spinner_selected, new String[]{"2", "3", "4"});
        playersNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTotalPlayers.setAdapter(playersNameAdapter);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("Log Game");
        alertDialogBuilder.setMessage("Choose the total players: ");
        alertDialogBuilder.setPositiveButton("Next",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        createLogGamePlayersDialog(view, Integer.parseInt(spinnerTotalPlayers.getSelectedItem().toString()), 1);
                    }
                });
        alertDialogBuilder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createLogGamePlayersDialog(final View view, final int totalPlayers, final int steps) {
        View logView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_two_spinners, null);

        final Spinner spinnerFirstPlaceName = (Spinner) logView.findViewById(R.id.spinner1);
        final Spinner spinnerFirstPlaceDeck = (Spinner) logView.findViewById(R.id.spinner2);
        handleSpinners(spinnerFirstPlaceName, spinnerFirstPlaceDeck);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("Log Game - Player " + steps + "/" + totalPlayers);
        final String[] positions = new String[]{"1st", "2nd", "3rd", "4th"};
        alertDialogBuilder.setMessage("Choose the " + positions[steps - 1] + " Player and Deck:");
        alertDialogBuilder.setPositiveButton("Next",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        alertDialogBuilder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        playersDB.close();
                        decksDB.close();
                    }
                });
        alertDialogBuilder.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (steps == 1) {
                            createLogGameTotalDialog(view);
                        } else {
                            listDeck.remove(listDeck.size() - 1);
                            createLogGamePlayersDialog(view, totalPlayers, steps - 1);
                        }
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //Override POSITIVE button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auxPlayerName = spinnerFirstPlaceName.getSelectedItem().toString();
                String tempPlayerDeck = spinnerFirstPlaceDeck.getSelectedItem().toString();

                if (!auxPlayerName.equalsIgnoreCase(parentActivity.getString(R.string.edh_spinner_player_hint))
                        && !tempPlayerDeck.equalsIgnoreCase(parentActivity.getString(R.string.edh_spinner_deck_hint))) {

                    listDeck.add(new Deck(auxPlayerName, tempPlayerDeck));
                    if (Record.isValidRecord(listDeck)) {
                        alertDialog.dismiss();
                        if (steps != totalPlayers) {
                            createLogGamePlayersDialog(view, totalPlayers, steps + 1);
                        } else {
                            createLogGameConfirmationDialog(view);
                            playersDB.close();
                            decksDB.close();
                        }
                    } else {
                        listDeck.remove(listDeck.size() - 1);
                        Toast.makeText(view.getContext(), "ERROR: Player/Deck already added!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "ERROR: invalid Player/Deck!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createLogGameConfirmationDialog(final View view) {
        View logView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_log_confirmation, null);

        final List<Map<String, String>> listMapDecks = new ArrayList<>();
        for (int i = 0; i < listDeck.size(); i++) {
            Map<String, String> auxMap = new HashMap<>(2);
            auxMap.put("playerName", listDeck.get(i).getPlayerName());
            auxMap.put("deck", listDeck.get(i).getDeckName());
            listMapDecks.add(auxMap);
        }

        final ListView listViewConfirmation = (ListView) logView.findViewById(R.id.listViewConfirmation);
        SimpleAdapter adapter = new SimpleAdapter(parentActivity,
                listMapDecks,
                R.layout.row_two_item_list,
                new String[]{"playerName", "deck"},
                new int[]{R.id.text1, R.id.text2});
        listViewConfirmation.setAdapter(adapter);


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("Log Game - Overview");
        alertDialogBuilder.setMessage("Check the Player and Decks positions: ");
        alertDialogBuilder.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RecordsDataAccessObject recordDB = new RecordsDataAccessObject(parentActivity);
                        recordDB.open();
                        long result = recordDB.createRecord(new Record(listDeck));
                        recordDB.close();
                        if (result != -1)
                            Toast.makeText(view.getContext(), "Game SAVED!!!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(view.getContext(), "ERROR inserting this record", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialogBuilder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        playersDB.close();
                        decksDB.close();
                    }
                });
        alertDialogBuilder.setNegativeButton("Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listDeck.remove(listDeck.size() - 1);
                        createLogGamePlayersDialog(view, listDeck.size() + 1, listDeck.size() + 1);
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void handleSpinners(Spinner spinnerName, Spinner spinnerDeck) {
        playersDB.open();
        decksDB.open();

        final ArrayList<String> players = (ArrayList<String>) playersDB.getAllPlayers();
        players.add(0, parentActivity.getString(R.string.edh_spinner_player_hint));

        final ArrayList<String> decks;
        decks = new ArrayList<>();

        final ArrayAdapter<String> playersNameAdapter;
        playersNameAdapter = new ArrayAdapter<>(parentActivity, R.layout.row_spinner_selected, players);
        playersNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<String> playersDeckAdapter;
        playersDeckAdapter = new ArrayAdapter<>(parentActivity, R.layout.row_spinner_selected, decks);
        playersDeckAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerName.setAdapter(playersNameAdapter);
        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Deck> aux = decksDB.getAllDeckByPlayerName(players.get(position));
                decks.clear();
                decks.add(0, parentActivity.getString(R.string.edh_spinner_deck_hint));
                for (int i = 0; i < aux.size(); i++) {
                    decks.add(aux.get(i).getDeckName());
                }
                playersDeckAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerDeck.setAdapter(playersDeckAdapter);
    }

}
