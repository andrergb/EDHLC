package com.android.argb.edhlc.objects.Drawer;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.activities.MainActivity;
import com.android.argb.edhlc.activities.PlayerListActivity;
import com.android.argb.edhlc.activities.RecordsActivity;
import com.android.argb.edhlc.activities.SettingsActivity;
import com.android.argb.edhlc.activities.TurnActivity;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.listviewdragginganimation.DynamicListView;
import com.android.argb.edhlc.listviewdragginganimation.StableArrayAdapter;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Record;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * -Created by agbarros on 05/11/2015.
 */
public class DrawerMain {

    private AlertDialog newGameConfirmationDialog;
    private AlertDialog newGameSelectPlayersDialog;
    private AlertDialog logGameConfirmationDialog;
    private AlertDialog logGameSelectPlayersDialog;

    private ArrayList<String> mNewGameDecks;
    private ArrayList<String> mLogGameDecks;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LinearLayout linearLayoutDrawer;

    private Activity parentActivity;

    private PlayersDataAccessObject playersDB;
    private DecksDataAccessObject decksDB;

    public DrawerMain(final Activity parentActivity, List<String[]> options) {
        this.parentActivity = parentActivity;

        linearLayoutDrawer = (LinearLayout) parentActivity.findViewById(R.id.drawer);
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

    public void dismiss() {
        mDrawerLayout.closeDrawers();
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    public boolean isDrawerOpen() {
        return (mDrawerLayout.isDrawerOpen(linearLayoutDrawer));
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

    private void createDiceRangeDialog(final View view) {
        View logView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_roll_a_dice, null);

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

    private void createDiceResultDialog(final View view, final int minValue, final int maxValue) {
        final Random r = new Random();
        final int[] dice = {r.nextInt(maxValue - minValue + 1) + minValue};

        View logView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_roll_a_dice_result, null);
        final TextView textViewDiceResult = (TextView) logView.findViewById(R.id.textViewDiceResult);
        textViewDiceResult.setText(MessageFormat.format("{0}", dice[0]));

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

        alertDialogBuilder.setNeutralButton("Random",
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
                dice[0] = r.nextInt((maxValue - minValue) + 1) + minValue;
                textViewDiceResult.setText(MessageFormat.format("{0}", dice[0]));
            }
        });
    }

    private void createLogGameConfirmationDialog(final View view) {
        View logView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_log_confirmation, null);
        StableArrayAdapter adapter = new StableArrayAdapter(parentActivity, R.layout.row_two_item_list_border, R.id.text1, mLogGameDecks);
        final DynamicListView listView = (DynamicListView) logView.findViewById(R.id.listViewConfirmation);
        listView.setDynamicArrayListContent(mLogGameDecks);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("Log Game");
        alertDialogBuilder.setMessage("Check the players and its decks: ");
        alertDialogBuilder.setPositiveButton("SAVE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        alertDialogBuilder.setNeutralButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        playersDB.close();
                        decksDB.close();
                    }
                });

        logGameConfirmationDialog = alertDialogBuilder.create();
        logGameConfirmationDialog.show();

        //Override POSITIVE button
        logGameConfirmationDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> mDynamicArrayListContent = listView.getDynamicArrayListContent();
                ArrayList<Deck> mDecksListToBeRecorded = new ArrayList<>();
                for (int i = 0; i < mDynamicArrayListContent.size(); i++) {
                    String auxPlayerName = mDynamicArrayListContent.get(i).split(System.getProperty("line.separator"))[0];
                    String auxPlayerDeck = mDynamicArrayListContent.get(i).split(System.getProperty("line.separator"))[1];

                    Deck currentDeck = new Deck(auxPlayerName, auxPlayerDeck);
                    if (!currentDeck.isEqualDeck(new Deck(parentActivity.getResources().getString(R.string.default_player_1), parentActivity.getResources().getString(R.string.default_deck_1)))
                            && !currentDeck.isEqualDeck(new Deck(parentActivity.getResources().getString(R.string.default_player_2), parentActivity.getResources().getString(R.string.default_deck_2)))
                            && !currentDeck.isEqualDeck(new Deck(parentActivity.getResources().getString(R.string.default_player_3), parentActivity.getResources().getString(R.string.default_deck_3)))
                            && !currentDeck.isEqualDeck(new Deck(parentActivity.getResources().getString(R.string.default_player_4), parentActivity.getResources().getString(R.string.default_deck_4)))) {
                        mDecksListToBeRecorded.add(currentDeck);
                    }
                }
                if (mDecksListToBeRecorded.size() >= 2) {
                    RecordsDataAccessObject recordDB = new RecordsDataAccessObject(parentActivity);
                    recordDB.open();

                    Calendar c = Calendar.getInstance();
                    String date = Constants.MONTH[c.get(Calendar.MONTH)]
                            + " " + String.valueOf(c.get(Calendar.DAY_OF_MONTH))
                            + ", " + String.valueOf(c.get(Calendar.YEAR));

                    long result = recordDB.addRecord(new Record(mDecksListToBeRecorded, date));
                    recordDB.close();

                    if (result != -1)
                        Toast.makeText(view.getContext(), "Game saved", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(view.getContext(), "Error inserting this record", Toast.LENGTH_SHORT).show();
                    logGameConfirmationDialog.dismiss();
                } else {
                    Toast.makeText(view.getContext(), "At least 2 players are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Deck aux = null;
                if (!(mLogGameDecks.get(position).contains(parentActivity.getResources().getString(R.string.default_player_1) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_1)))
                        && !(mLogGameDecks.get(position).contains(parentActivity.getResources().getString(R.string.default_player_2) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_2)))
                        && !(mLogGameDecks.get(position).contains(parentActivity.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_3)))
                        && !(mLogGameDecks.get(position).contains(parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4)))) {
                    aux = new Deck(mLogGameDecks.get(position).split(System.getProperty("line.separator"))[0], mLogGameDecks.get(position).split(System.getProperty("line.separator"))[1]);
                }

                createLogGameSelectPlayersDialog(view, position, aux);
                logGameConfirmationDialog.dismiss();
            }
        });
    }

    private void createLogGameSelectPlayersDialog(final View view, final int position, final Deck selectedPlayer) {
        View NewGamePlayersChoiceView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_two_spinners, null);

        final Spinner spinnerPlayerName = (Spinner) NewGamePlayersChoiceView.findViewById(R.id.spinner1);
        final Spinner spinnerPlayerDeck = (Spinner) NewGamePlayersChoiceView.findViewById(R.id.spinner2);
        handleSpinners(spinnerPlayerName, spinnerPlayerDeck, selectedPlayer);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(NewGamePlayersChoiceView);
        alertDialogBuilder.setTitle("Log Game");
        alertDialogBuilder.setMessage("Choose a player and its deck: ");
        alertDialogBuilder.setPositiveButton(selectedPlayer == null ? "ADD" : "UPDATE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        alertDialogBuilder.setNeutralButton("BACK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        createLogGameConfirmationDialog(view);
                        dialog.cancel();
                    }
                });
        if (selectedPlayer != null) {
            alertDialogBuilder.setNegativeButton("REMOVE",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mLogGameDecks.remove(position);
                            if (!mLogGameDecks.contains(parentActivity.getResources().getString(R.string.default_player_1) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_1)))
                                mLogGameDecks.add(position, parentActivity.getResources().getString(R.string.default_player_1) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_1));
                            else if (!mLogGameDecks.contains(parentActivity.getResources().getString(R.string.default_player_2) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_2)))
                                mLogGameDecks.add(position, parentActivity.getResources().getString(R.string.default_player_2) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_2));
                            else if (!mLogGameDecks.contains(parentActivity.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_3)))
                                mLogGameDecks.add(position, parentActivity.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_3));
                            else if (!mLogGameDecks.contains(parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4)))
                                mLogGameDecks.add(position, parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4));

                            createLogGameConfirmationDialog(view);
                            dialog.cancel();
                        }
                    });
        }
        logGameSelectPlayersDialog = alertDialogBuilder.create();
        logGameSelectPlayersDialog.show();

        //Override POSITIVE button - ADD
        logGameSelectPlayersDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auxPlayerName = spinnerPlayerName.getSelectedItem().toString();
                String tempPlayerDeck = spinnerPlayerDeck.getSelectedItem().toString();

                if (selectedPlayer != null && selectedPlayer.isEqualDeck(new Deck(auxPlayerName, tempPlayerDeck))) {
                    createLogGameConfirmationDialog(v);
                    logGameSelectPlayersDialog.dismiss();
                } else {
                    List<Deck> mDeckListAux = new ArrayList<>();
                    mDeckListAux.add(new Deck(mLogGameDecks.get(0).split(System.getProperty("line.separator"))[0], mLogGameDecks.get(0).split(System.getProperty("line.separator"))[1]));
                    mDeckListAux.add(new Deck(mLogGameDecks.get(1).split(System.getProperty("line.separator"))[0], mLogGameDecks.get(1).split(System.getProperty("line.separator"))[1]));
                    mDeckListAux.add(new Deck(mLogGameDecks.get(2).split(System.getProperty("line.separator"))[0], mLogGameDecks.get(2).split(System.getProperty("line.separator"))[1]));
                    mDeckListAux.add(new Deck(mLogGameDecks.get(3).split(System.getProperty("line.separator"))[0], mLogGameDecks.get(3).split(System.getProperty("line.separator"))[1]));

                    if (!auxPlayerName.equalsIgnoreCase(parentActivity.getString(R.string.edh_spinner_player_hint)) && !tempPlayerDeck.equalsIgnoreCase(parentActivity.getString(R.string.edh_spinner_deck_hint))) {
                        if (Record.isValidRecord(mDeckListAux, new Deck(auxPlayerName, tempPlayerDeck))) {
                            mLogGameDecks.remove(position);
                            mLogGameDecks.add(position, auxPlayerName + System.getProperty("line.separator") + tempPlayerDeck);
                            logGameSelectPlayersDialog.dismiss();
                            createLogGameConfirmationDialog(v);
                        } else {
                            Toast.makeText(view.getContext(), "ERROR: Player/Deck already added!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "ERROR: invalid Player/Deck!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void createNewGameConfirmationDialog(final View view) {
        View logView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_log_confirmation, null);
        StableArrayAdapter adapter = new StableArrayAdapter(parentActivity, R.layout.row_two_item_list_border, R.id.text1, mNewGameDecks);
        final DynamicListView listView = (DynamicListView) logView.findViewById(R.id.listViewConfirmation);
        listView.setDynamicArrayListContent(mNewGameDecks);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(logView);
        alertDialogBuilder.setTitle("New Game");
        alertDialogBuilder.setMessage("Check the players and its decks: ");
        alertDialogBuilder.setPositiveButton("START",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        alertDialogBuilder.setNeutralButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        newGameConfirmationDialog = alertDialogBuilder.create();
        newGameConfirmationDialog.show();

        //Override POSITIVE button
        newGameConfirmationDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> players = new ArrayList<>();
                ArrayList<String> decks = new ArrayList<>();
                ArrayList<String> color = new ArrayList<>();

                for (int i = 0; i < mNewGameDecks.size(); i++) {
                    if (!(mNewGameDecks.get(i).contains(parentActivity.getResources().getString(R.string.default_player_1) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_1)))
                            && !(mNewGameDecks.get(i).contains(parentActivity.getResources().getString(R.string.default_player_2) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_2)))
                            && !(mNewGameDecks.get(i).contains(parentActivity.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_3)))
                            && !(mNewGameDecks.get(i).contains(parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4)))) {

                        String[] auxNewGameDecks = mNewGameDecks.get(i).split(System.getProperty("line.separator"));
                        players.add(auxNewGameDecks[0]);
                        decks.add(auxNewGameDecks[1]);
                        color.add(auxNewGameDecks[2]);
                    }
                }

                if (players.size() >= 2) {
                    final Intent intent = new Intent(Constants.BROADCAST_INTENT);
                    intent.putExtra(Constants.BROADCAST_MESSAGE_NEW_GAME_OPTION, Constants.BROADCAST_MESSAGE_NEW_GAME_OPTION);
                    intent.putStringArrayListExtra(Constants.BROADCAST_MESSAGE_NEW_GAME_PLAYERS, players);
                    intent.putStringArrayListExtra(Constants.BROADCAST_MESSAGE_NEW_GAME_DECKS, decks);
                    intent.putStringArrayListExtra(Constants.BROADCAST_MESSAGE_NEW_GAME_COLOR, color);
                    LocalBroadcastManager.getInstance(parentActivity).sendBroadcast(intent);
                    newGameConfirmationDialog.dismiss();
                } else {
                    Toast.makeText(view.getContext(), "At least 2 players are required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Deck aux = null;
                if (!(mNewGameDecks.get(position).contains(parentActivity.getResources().getString(R.string.default_player_1) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_1)))
                        && !(mNewGameDecks.get(position).contains(parentActivity.getResources().getString(R.string.default_player_2) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_2)))
                        && !(mNewGameDecks.get(position).contains(parentActivity.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_3)))
                        && !(mNewGameDecks.get(position).contains(parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4)))) {
                    aux = new Deck(mNewGameDecks.get(position).split(System.getProperty("line.separator"))[0], mNewGameDecks.get(position).split(System.getProperty("line.separator"))[1]);
                }

                createNewGameSelectPlayersDialog(view, position, aux);
                newGameConfirmationDialog.dismiss();
            }
        });
    }

    private void createNewGameSelectPlayersDialog(final View view, final int position, final Deck selectedPlayer) {
        View NewGamePlayersChoiceView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_two_spinners, null);

        final Spinner spinnerPlayerName = (Spinner) NewGamePlayersChoiceView.findViewById(R.id.spinner1);
        final Spinner spinnerPlayerDeck = (Spinner) NewGamePlayersChoiceView.findViewById(R.id.spinner2);
        handleSpinners(spinnerPlayerName, spinnerPlayerDeck, selectedPlayer);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(NewGamePlayersChoiceView);
        alertDialogBuilder.setTitle("New Game");
        alertDialogBuilder.setMessage("Choose a player and its deck: ");
        alertDialogBuilder.setPositiveButton(selectedPlayer == null ? "ADD" : "UPDATE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Overridden at 'alertDialog.getButton' to avoid dismiss every time
                    }
                });
        alertDialogBuilder.setNeutralButton("BACK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        createNewGameConfirmationDialog(view);
                        dialog.cancel();
                    }
                });
        if (selectedPlayer != null) {
            alertDialogBuilder.setNegativeButton("REMOVE",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mNewGameDecks.remove(position);
                            String defaultColor = parentActivity.getResources().getColor(R.color.primary_color) + System.getProperty("path.separator") + parentActivity.getResources().getColor(R.color.secondary_color);
                            if (!mNewGameDecks.contains(parentActivity.getResources().getString(R.string.default_player_1) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_1)))
                                mNewGameDecks.add(position, parentActivity.getResources().getString(R.string.default_player_1) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_1) + System.getProperty("line.separator") + defaultColor);
                            else if (!mNewGameDecks.contains(parentActivity.getResources().getString(R.string.default_player_2) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_2)))
                                mNewGameDecks.add(position, parentActivity.getResources().getString(R.string.default_player_2) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_2) + System.getProperty("line.separator") + defaultColor);
                            else if (!mNewGameDecks.contains(parentActivity.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_3)))
                                mNewGameDecks.add(position, parentActivity.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_3) + System.getProperty("line.separator") + defaultColor);
                            else if (!mNewGameDecks.contains(parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4)))
                                mNewGameDecks.add(position, parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4) + System.getProperty("line.separator") + defaultColor);

                            createNewGameConfirmationDialog(view);
                            dialog.cancel();
                        }
                    });
        }
        newGameSelectPlayersDialog = alertDialogBuilder.create();
        newGameSelectPlayersDialog.show();

        //Override POSITIVE button - ADD
        newGameSelectPlayersDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String auxPlayerName = spinnerPlayerName.getSelectedItem().toString();
                String tempPlayerDeck = spinnerPlayerDeck.getSelectedItem().toString();

                if (selectedPlayer != null && selectedPlayer.isEqualDeck(new Deck(auxPlayerName, tempPlayerDeck))) {
                    createNewGameConfirmationDialog(v);
                    newGameSelectPlayersDialog.dismiss();
                } else {
                    List<Deck> mDeckListAux = new ArrayList<>();
                    mDeckListAux.add(new Deck(mNewGameDecks.get(0).split(System.getProperty("line.separator"))[0], mNewGameDecks.get(0).split(System.getProperty("line.separator"))[1]));
                    mDeckListAux.add(new Deck(mNewGameDecks.get(1).split(System.getProperty("line.separator"))[0], mNewGameDecks.get(1).split(System.getProperty("line.separator"))[1]));
                    mDeckListAux.add(new Deck(mNewGameDecks.get(2).split(System.getProperty("line.separator"))[0], mNewGameDecks.get(2).split(System.getProperty("line.separator"))[1]));
                    mDeckListAux.add(new Deck(mNewGameDecks.get(3).split(System.getProperty("line.separator"))[0], mNewGameDecks.get(3).split(System.getProperty("line.separator"))[1]));

                    if (!auxPlayerName.equalsIgnoreCase(parentActivity.getString(R.string.edh_spinner_player_hint)) && !tempPlayerDeck.equalsIgnoreCase(parentActivity.getString(R.string.edh_spinner_deck_hint))) {
                        if (Record.isValidRecord(mDeckListAux, new Deck(auxPlayerName, tempPlayerDeck))) {
                            mNewGameDecks.remove(position);
                            mNewGameDecks.add(position, auxPlayerName + System.getProperty("line.separator") + tempPlayerDeck + System.getProperty("line.separator") + decksDB.getDeck(auxPlayerName, tempPlayerDeck).getDeckShieldColor()[0] + System.getProperty("path.separator") + decksDB.getDeck(auxPlayerName, tempPlayerDeck).getDeckShieldColor()[1]);
                            newGameSelectPlayersDialog.dismiss();
                            createNewGameConfirmationDialog(v);
                        } else {
                            Toast.makeText(view.getContext(), "ERROR: Player/Deck already added!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "ERROR: invalid Player/Deck!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void handleSpinners(final Spinner spinnerName, final Spinner spinnerDeck, final Deck selectedPlayerPreviously) {
        playersDB.open();
        decksDB.open();

        final ArrayList<String> players = (ArrayList<String>) playersDB.getAllPlayersName();
        players.add(0, parentActivity.getString(R.string.edh_spinner_player_hint));

        final ArrayList<String> decks;
        decks = new ArrayList<>();

        final ArrayAdapter<String> playersNameAdapter;
        playersNameAdapter = new ArrayAdapter<>(parentActivity, R.layout.row_spinner_selected, players);
        playersNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(playersNameAdapter);

        final ArrayAdapter<String> playersDeckAdapter;
        playersDeckAdapter = new ArrayAdapter<>(parentActivity, R.layout.row_spinner_selected, decks);
        playersDeckAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDeck.setAdapter(playersDeckAdapter);

        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Deck> aux = decksDB.getAllDeckByPlayerName(players.get(position));
                decks.clear();
                decks.add(0, parentActivity.getString(R.string.edh_spinner_deck_hint));
                for (int i = 0; i < aux.size(); i++) {
                    decks.add(aux.get(i).getDeckName());
                }

                if (selectedPlayerPreviously != null) {
                    //Set previously selection
                    String comparePlayerDeck = selectedPlayerPreviously.getDeckName();
                    if (!comparePlayerDeck.equals(null)) {
                        int spinnerPosition = playersDeckAdapter.getPosition(comparePlayerDeck);
                        spinnerDeck.setSelection(spinnerPosition);
                    }
                }

                playersDeckAdapter.notifyDataSetChanged();

                if (selectedPlayerPreviously != null) {
                    //Set REMOVE button enabled or not
                    if (spinnerName.getSelectedItem() != null && spinnerDeck.getSelectedItem() != null) {
                        if (selectedPlayerPreviously.isEqualDeck(new Deck(spinnerName.getSelectedItem().toString(), spinnerDeck.getSelectedItem().toString()))) {
                            if (newGameSelectPlayersDialog != null && newGameSelectPlayersDialog.isShowing())
                                newGameSelectPlayersDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
                            else if (logGameSelectPlayersDialog != null && logGameSelectPlayersDialog.isShowing())
                                logGameSelectPlayersDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
                        } else {
                            if (newGameSelectPlayersDialog != null && newGameSelectPlayersDialog.isShowing())
                                newGameSelectPlayersDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                            else if (logGameSelectPlayersDialog != null && logGameSelectPlayersDialog.isShowing())
                                logGameSelectPlayersDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerDeck.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (selectedPlayerPreviously != null) {
                    //Set REMOVE button enabled or not
                    if (spinnerName.getSelectedItem() != null && spinnerDeck.getSelectedItem() != null) {
                        if (selectedPlayerPreviously.isEqualDeck(new Deck(spinnerName.getSelectedItem().toString(), spinnerDeck.getSelectedItem().toString()))) {
                            if (newGameSelectPlayersDialog != null && newGameSelectPlayersDialog.isShowing())
                                newGameSelectPlayersDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
                            else if (logGameSelectPlayersDialog != null && logGameSelectPlayersDialog.isShowing())
                                logGameSelectPlayersDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
                        } else {
                            if (newGameSelectPlayersDialog != null && newGameSelectPlayersDialog.isShowing())
                                newGameSelectPlayersDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                            else if (logGameSelectPlayersDialog != null && logGameSelectPlayersDialog.isShowing())
                                logGameSelectPlayersDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (selectedPlayerPreviously != null) {
            String comparePlayerName = selectedPlayerPreviously.getDeckOwnerName();
            if (!comparePlayerName.equals(null)) {
                int spinnerPosition = playersNameAdapter.getPosition(comparePlayerName);
                spinnerName.setSelection(spinnerPosition);
            }
        }
    }

    private class DrawerItemClickListener1 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: //NewGame
                    mDrawerLayout.closeDrawers();

                    mNewGameDecks = new ArrayList<>();
                    String defaultColor = parentActivity.getResources().getColor(R.color.primary_color) + System.getProperty("path.separator") + parentActivity.getResources().getColor(R.color.secondary_color);
                    mNewGameDecks.add(parentActivity.getResources().getString(R.string.default_player_1) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_1) + System.getProperty("line.separator") + defaultColor);
                    mNewGameDecks.add(parentActivity.getResources().getString(R.string.default_player_2) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_2) + System.getProperty("line.separator") + defaultColor);
                    mNewGameDecks.add(parentActivity.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_3) + System.getProperty("line.separator") + defaultColor);
                    mNewGameDecks.add(parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4) + System.getProperty("line.separator") + defaultColor);

                    List<Deck> mNewGameDecksActive = MainActivity.getActiveDecksList();
                    for (int i = 0; i < mNewGameDecksActive.size(); i++) {
                        mNewGameDecks.remove(i);
                        mNewGameDecks.add(i,
                                mNewGameDecksActive.get(i).getDeckOwnerName()
                                        + System.getProperty("line.separator")
                                        + mNewGameDecksActive.get(i).getDeckName()
                                        + System.getProperty("line.separator")
                                        + String.valueOf(mNewGameDecksActive.get(i).getDeckShieldColor()[0] + System.getProperty("path.separator") + mNewGameDecksActive.get(i).getDeckShieldColor()[1]));
                    }

                    createNewGameConfirmationDialog(view);
                    break;

                case 1: //Log Game
                    mDrawerLayout.closeDrawers();

                    mLogGameDecks = new ArrayList<>();

                    ArrayList<Deck> mDeadDecksList = MainActivity.getDeadDecksList();
                    if (mDeadDecksList != null)
                        for (int i = 0; i < mDeadDecksList.size(); i++)
                            mLogGameDecks.add(mDeadDecksList.get(i).getDeckOwnerName() + System.getProperty("line.separator") + mDeadDecksList.get(i).getDeckName());

                    ArrayList<Deck> mAliveDecksList = MainActivity.getAliveDecksList();
                    if (mAliveDecksList != null)
                        for (int i = 0; i < mAliveDecksList.size(); i++)
                            mLogGameDecks.add(mAliveDecksList.get(i).getDeckOwnerName() + System.getProperty("line.separator") + mAliveDecksList.get(i).getDeckName());

                    switch (mLogGameDecks.size()) {
                        case 0:
                            mLogGameDecks.add(parentActivity.getResources().getString(R.string.default_player_1) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_1));
                            mLogGameDecks.add(parentActivity.getResources().getString(R.string.default_player_2) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_2));
                            mLogGameDecks.add(parentActivity.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_3));
                            mLogGameDecks.add(parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4));
                            break;
                        case 1:
                            mLogGameDecks.add(parentActivity.getResources().getString(R.string.default_player_2) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_2));
                            mLogGameDecks.add(parentActivity.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_3));
                            mLogGameDecks.add(parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4));
                            break;
                        case 2:
                            mLogGameDecks.add(parentActivity.getResources().getString(R.string.default_player_3) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_3));
                            mLogGameDecks.add(parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4));
                            break;
                        case 3:
                            mLogGameDecks.add(parentActivity.getResources().getString(R.string.default_player_4) + System.getProperty("line.separator") + parentActivity.getResources().getString(R.string.default_deck_4));
                            break;
                    }
                    Collections.reverse(mLogGameDecks);

                    createLogGameConfirmationDialog(view);
                    break;
                case 2: //Roll a dice
                    mDrawerLayout.closeDrawers();
                    createDiceRangeDialog(view);
                    break;
                case 3: //Random a player
                    mDrawerLayout.closeDrawers();
                    Intent intent = new Intent(Constants.BROADCAST_INTENT);
                    intent.putExtra(Constants.BROADCAST_MESSAGE_RANDOM_PLAYER_OPTION, Constants.BROADCAST_MESSAGE_RANDOM_PLAYER_OPTION);
                    LocalBroadcastManager.getInstance(parentActivity).sendBroadcast(intent);
                    break;
                case 4: //Turns
                    parentActivity.startActivity(new Intent(parentActivity, TurnActivity.class));
                    break;
            }
        }
    }

    private class DrawerItemClickListener2 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: //Home
                    mDrawerLayout.closeDrawers();
                    break;
                case 1: //Players
                    mDrawerLayout.closeDrawers();
                    parentActivity.startActivity(new Intent(parentActivity, PlayerListActivity.class));
                    break;
                case 2: //All Records
                    mDrawerLayout.closeDrawers();
                    parentActivity.startActivity(new Intent(parentActivity, RecordsActivity.class));
                    break;
                case 3: //Settings
                    mDrawerLayout.closeDrawers();
                    parentActivity.startActivity(new Intent(parentActivity, SettingsActivity.class));
                    break;
                case 4: //About
                    mDrawerLayout.closeDrawers();
                    createAboutDialog(view);
                    break;
            }
        }
    }

}