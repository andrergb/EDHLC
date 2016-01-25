package com.android.argb.edhlc.objects.Drawer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.activities.PlayerListActivity;
import com.android.argb.edhlc.activities.RecordsActivity;
import com.android.argb.edhlc.activities.SettingsActivity;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.objects.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * -Created by agbarros on 05/11/2015.
 */
public class DrawerPlayer {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LinearLayout linearLayoutDrawer;

    private Activity parentActivity;

    private String currentPlayer;

//    private PlayersDataAccessObject playersDB;
//    private DecksDataAccessObject decksDB;

    public DrawerPlayer(final Activity parentActivity, List<String[]> options, String currentPlayer) {
        this.parentActivity = parentActivity;
        this.currentPlayer = currentPlayer;

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

                DecksDataAccessObject decksDB = new DecksDataAccessObject(parentActivity);
                decksDB.open();
                List<Deck> a = decksDB.getAllDecks();
                decksDB.close();
                for (int i = 0; i < a.size(); i++) {
                    Log.d("dezao", a.get(i).getPlayerName() + " - " + a.get(i).getDeckName());
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

//        playersDB = new PlayersDataAccessObject(parentActivity);
//        decksDB = new DecksDataAccessObject(parentActivity);
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

    private void createAddDeckDialog(final View view) {
        View playerNameView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_deck_name, null);
        final EditText userInput = (EditText) playerNameView.findViewById(R.id.editTextDeckName);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(playerNameView);
        alertDialogBuilder.setTitle("Add new Deck");
        alertDialogBuilder.setMessage("Deck name:");
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
                    DecksDataAccessObject decksDB = new DecksDataAccessObject(parentActivity);
                    decksDB.open();
                    long result = decksDB.addDeck(new Deck(currentPlayer, tempName, new int[]{parentActivity.getResources().getColor(R.color.edh_default_primary), parentActivity.getResources().getColor(R.color.edh_default_secondary)}));
                    decksDB.close();
                    if (result != -1) {
                        Toast.makeText(view.getContext(), tempName + " added", Toast.LENGTH_SHORT).show();
                        LocalBroadcastManager.getInstance(parentActivity).sendBroadcast(new Intent(Constants.BROADCAST_INTENT_FILTER_DECK_CRUD));
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(view.getContext(), "Fail: Deck " + tempName + " already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Inset a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createEditDeckDialog(final View view) {
        DecksDataAccessObject decksDb = new DecksDataAccessObject(parentActivity);
        decksDb.open();
        List<Deck> deckList = decksDb.getAllDeckByPlayerName(currentPlayer);
        decksDb.close();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setTitle("Edit deck");

        if (deckList.size() > 0) {
            ArrayList<String> aux = new ArrayList<>();
            aux.add(0, parentActivity.getResources().getString(R.string.edh_spinner_deck_hint));
            for (int i = 0; i < deckList.size(); i++)
                aux.add(deckList.get(i).getDeckName());

            View spinnerView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_single_spinner, null);
            final Spinner spinnerDecks = (Spinner) spinnerView.findViewById(R.id.spinner1);

            final ArrayAdapter<String> decksNameAdapter = new ArrayAdapter<>(parentActivity, R.layout.row_spinner_selected, aux);
            decksNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDecks.setAdapter(decksNameAdapter);

            alertDialogBuilder.setView(spinnerView);
            alertDialogBuilder.setMessage("Choose a deck to edit: ");
            alertDialogBuilder.setPositiveButton("EDIT",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            alertDialogBuilder.setNeutralButton("CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });


            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            //Override POSITIVE button
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tempName = spinnerDecks.getSelectedItem().toString();
                    if (!tempName.equalsIgnoreCase(parentActivity.getResources().getString(R.string.edh_spinner_deck_hint))) {
                        createEditDeckDialog(view, new Deck(currentPlayer, tempName));
                        alertDialog.cancel();
                    } else {
                        Toast.makeText(view.getContext(), "Choose a deck", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            alertDialogBuilder.setMessage("There is no deck to edit");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void createEditDeckDialog(final View view, final Deck oldDeck) {
        View playerNameView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_deck_name, null);
        final EditText userInput = (EditText) playerNameView.findViewById(R.id.editTextDeckName);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(playerNameView);
        alertDialogBuilder.setTitle("Edit Deck");
        alertDialogBuilder.setMessage("Choose a new name for " + oldDeck.getDeckName() + ": ");
        alertDialogBuilder.setPositiveButton("EDIT",
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
                String tempDeckName = userInput.getText().toString();
                if (!tempDeckName.equalsIgnoreCase("")) {
                    DecksDataAccessObject decksDB = new DecksDataAccessObject(parentActivity);
                    decksDB.open();
                    long result = decksDB.updateDeck(oldDeck, new Deck(currentPlayer, tempDeckName));
                    decksDB.close();
                    if (result != -1) {
                        Toast.makeText(view.getContext(), tempDeckName + " edited", Toast.LENGTH_SHORT).show();
                        LocalBroadcastManager.getInstance(parentActivity).sendBroadcast(new Intent(Constants.BROADCAST_INTENT_FILTER_DECK_CRUD));
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(view.getContext(), "Fail: Deck " + tempDeckName + " already exists", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Insert a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createRemoveDeckDialog(final View view) {
        DecksDataAccessObject decksDb = new DecksDataAccessObject(parentActivity);
        decksDb.open();
        List<Deck> deckList = decksDb.getAllDeckByPlayerName(currentPlayer);
        decksDb.close();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setTitle("Remove deck");

        if (deckList.size() > 0) {
            ArrayList<String> aux = new ArrayList<>();
            aux.add(0, parentActivity.getResources().getString(R.string.edh_spinner_deck_hint));
            for (int i = 0; i < deckList.size(); i++)
                aux.add(deckList.get(i).getDeckName());

            View spinnerView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_single_spinner, null);
            final Spinner spinnerDecks = (Spinner) spinnerView.findViewById(R.id.spinner1);

            final ArrayAdapter<String> decksNameAdapter = new ArrayAdapter<>(parentActivity, R.layout.row_spinner_selected, aux);
            decksNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDecks.setAdapter(decksNameAdapter);

            alertDialogBuilder.setView(spinnerView);
            alertDialogBuilder.setMessage("Choose a deck to remove: ");
            alertDialogBuilder.setPositiveButton("Remove",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            alertDialogBuilder.setNeutralButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            //Override POSITIVE button
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tempName = spinnerDecks.getSelectedItem().toString();
                    if (!tempName.equalsIgnoreCase(parentActivity.getResources().getString(R.string.edh_spinner_deck_hint))) {
                        DecksDataAccessObject decksDb = new DecksDataAccessObject(parentActivity);
                        decksDb.open();
                        long result = decksDb.removeDeck(new Deck(currentPlayer, tempName));
                        decksDb.close();
                        if (result != 0) {
                            LocalBroadcastManager.getInstance(parentActivity).sendBroadcast(new Intent(Constants.BROADCAST_INTENT_FILTER_DECK_CRUD));
                            Toast.makeText(view.getContext(), tempName + " removed", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(view.getContext(), "Fail", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "Choose a Deck", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            alertDialogBuilder.setMessage("There is no deck to remove");
            alertDialogBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private class DrawerItemClickListener1 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0: //AddDeck
                    createAddDeckDialog(view);
                    mDrawerLayout.closeDrawers();
                    break;
                case 1: //EditDeck
                    createEditDeckDialog(view);
                    mDrawerLayout.closeDrawers();
                    break;
                case 2: //RemoveDeck
                    createRemoveDeckDialog(view);
                    mDrawerLayout.closeDrawers();
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
                    parentActivity.finish();
                    break;
                case 1: //Players
                    mDrawerLayout.closeDrawers();
                    parentActivity.startActivity(new Intent(parentActivity, PlayerListActivity.class));
                    parentActivity.finish();
                    break;
                case 2: //All Records
                    mDrawerLayout.closeDrawers();
                    Intent intent = new Intent(parentActivity, RecordsActivity.class);
                    intent.putExtra("RECORDS_PLAYER_NAME", "");
                    intent.putExtra("RECORDS_DECK_NAME", "");
                    parentActivity.startActivity(intent);
                    parentActivity.finish();
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
