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
import com.android.argb.edhlc.activities.RecordsActivity;
import com.android.argb.edhlc.activities.SettingsActivity;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;

import java.util.List;

/**
 * -Created by agbarros on 05/11/2015.
 */
public class DrawerPlayerList {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private LinearLayout linearLayoutDrawer;

    private Activity parentActivity;

    private PlayersDataAccessObject playersDB;

    public DrawerPlayerList(final Activity parentActivity, List<String[]> options) {
        this.parentActivity = parentActivity;
        playersDB = new PlayersDataAccessObject(parentActivity);

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
                case 0: //AddPlayer
                    mDrawerLayout.closeDrawers();
                    createAddPlayerDialog(view);
                    break;
                case 1: //EditPlayer
                    break;
                case 2: //RemovePlayer
                    mDrawerLayout.closeDrawers();
                    createRemovePlayerDialog(view);
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
                    // parentActivity.startActivity(new Intent(parentActivity, PlayerListActivity.class));
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

    private void createAddPlayerDialog(final View view) {
        View playerNameView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_player_name, null);
        final EditText userInput = (EditText) playerNameView.findViewById(R.id.editTextPlayerName);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(playerNameView);
        alertDialogBuilder.setTitle("Add new Player");
        alertDialogBuilder.setMessage("Player name:");
        alertDialogBuilder.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String tempName = userInput.getText().toString();
                        if (!tempName.equalsIgnoreCase("")) {
                            playersDB.open();
                            long result = playersDB.createPlayer(tempName);
                            playersDB.close();
                            if (result != -1) {
                                Toast.makeText(view.getContext(), tempName + " added!", Toast.LENGTH_SHORT).show();
                                LocalBroadcastManager.getInstance(parentActivity).sendBroadcast(new Intent(Constants.BROADCAST_INTENT_FILTER_PLAYER_ADDED_OR_REMOVED));
                            } else {
                                Toast.makeText(view.getContext(), "ERROR: Player already added!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();
    }

    private void createRemovePlayerDialog(final View view) {
        playersDB.open();
        List<String> allPlayers = playersDB.getAllPlayers();
        allPlayers.add(0, parentActivity.getString(R.string.edh_spinner_player_hint));
        playersDB.close();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setTitle("Remove a Player");

        if (allPlayers.size() > 1) {
            View removePlayerView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_single_spinner, null);
            final Spinner spinnerTotalPlayers = (Spinner) removePlayerView.findViewById(R.id.spinner1);

            final ArrayAdapter<String> playersNameAdapter = new ArrayAdapter<>(parentActivity, R.layout.row_spinner_selected, allPlayers);
            playersNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTotalPlayers.setAdapter(playersNameAdapter);

            alertDialogBuilder.setView(removePlayerView);
            alertDialogBuilder.setMessage("Choose a player to remove:");
            alertDialogBuilder.setPositiveButton("Remove",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String tempName = spinnerTotalPlayers.getSelectedItem().toString();
                            if (!tempName.equalsIgnoreCase("")) {
                                playersDB.open();
                                int result = playersDB.deletePlayer(tempName);
                                playersDB.close();
                                if (result != 0) {
                                    LocalBroadcastManager.getInstance(parentActivity).sendBroadcast(new Intent(Constants.BROADCAST_INTENT_FILTER_PLAYER_ADDED_OR_REMOVED));
                                    Toast.makeText(view.getContext(), tempName + " removed!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(view.getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            alertDialog.show();
        } else {
            alertDialogBuilder.setMessage("There is no player to remove");
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

}
