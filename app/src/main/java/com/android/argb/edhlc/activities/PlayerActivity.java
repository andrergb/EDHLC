package com.android.argb.edhlc.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Drawer.DrawerPlayer;
import com.android.argb.edhlc.objects.Record;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends ActionBarActivity {

    private static CheckBox checkBox;
    BroadcastReceiver mBroadcastReceiver;
    private TextView textViewPlayerName;
    private TextView textViewPlayerTotalDecks;
    private TextView textViewPlayerGames;
    private ListView listViewPlayerDecks;
    private String mPlayerName;
    private DecksDataAccessObject decksDB;
    private RecordsDataAccessObject recordsDB;
    private DrawerPlayer mDrawerPlayer;

    public void createLayout(View view) {
        if (view != null) {
            checkBox = (CheckBox) findViewById(R.id.checkBoxKeepScreenOn);
            textViewPlayerName = (TextView) findViewById(R.id.textViewPlayerName);
            textViewPlayerTotalDecks = (TextView) findViewById(R.id.textViewPlayerTotalDecks);
            textViewPlayerGames = (TextView) findViewById(R.id.textViewPlayerGames);

            listViewPlayerDecks = (ListView) findViewById(R.id.listViewPlayerDecks);
            listViewPlayerDecks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String playerDeck = decksDB.getAllDeckByPlayerName(mPlayerName).get(position).getDeckName();
                    if (recordsDB.getAllRecordsByDeck(new Deck(mPlayerName, playerDeck)).size() > 0) {
                        Intent intent = new Intent(PlayerActivity.this, RecordsActivity.class);
                        intent.putExtra("RECORDS_PLAYER_NAME", mPlayerName);
                        intent.putExtra("RECORDS_DECK_NAME", playerDeck);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PlayerActivity.this, "No records to show", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerPlayer.isDrawerOpen()) {
            mDrawerPlayer.dismiss();
        } else {
            Intent intent = new Intent(PlayerActivity.this, PlayerListActivity.class);
            startActivity(intent);
            this.finish();
            super.onBackPressed();
        }
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
        mDrawerPlayer.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //DrawerMain menu
        if (mDrawerPlayer.getDrawerToggle().onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.dark_primary_color));
        }

        decksDB = new DecksDataAccessObject(this);
        recordsDB = new RecordsDataAccessObject(this);

        Intent intent = getIntent();
        mPlayerName = intent.getStringExtra("PLAYERNAME");

        //DrawerMain menu
        List<String[]> drawerLists = new ArrayList<>();
        drawerLists.add(getResources().getStringArray(R.array.string_menu_player_1));
        drawerLists.add(getResources().getStringArray(R.array.string_menu_player_2));

        assert getSupportActionBar() != null;
        mDrawerPlayer = new DrawerPlayer(this, drawerLists, mPlayerName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateLayout();
            }
        };

        createLayout(this.findViewById(android.R.id.content));
    }

    @Override
    protected void onPause() {
        super.onPause();
        decksDB.close();
        recordsDB.close();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerPlayer.getDrawerToggle().syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mBroadcastReceiver,
                new IntentFilter(Constants.BROADCAST_INTENT_FILTER_DECK_CRUD)
        );

        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        decksDB.open();
        recordsDB.open();

        updateLayout();
    }

    private void updateLayout() {
//        if (getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE).getInt(Constants.SCREEN_ON, 0) == 1) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            checkBox.setChecked(true);
//        } else {
//            checkBox.setChecked(false);
//            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        }
//
//        /*ActivePlayer's info - for all decks*/
//        List<Record> recordsAllPositions = recordsDB.getAllRecordsByPlayerName(mPlayerName);
//        List<Record> recordsFirst = recordsDB.getAllFirstPlaceRecordsByPlayerName(mPlayerName);
//        List<Record> recordsSecond = recordsDB.getAllSecondPlaceRecordsByPlayerName(mPlayerName);
//        List<Record> recordsThird = recordsDB.getAllThirdPlaceRecordsByPlayerName(mPlayerName);
//        List<Record> recordsFourth = recordsDB.getAllFourthPlaceRecordsByPlayerName(mPlayerName);
//
//        textViewPlayerName.setText(mPlayerName);
//        textViewPlayerGames.setText(getString(R.string.player_games,
//                recordsAllPositions.size(),
//                recordsFirst.size(),
//                recordsSecond.size(),
//                recordsThird.size(),
//                recordsFourth.size()));
//
//        /*Information for each deck*/
//        List<Deck> decks = decksDB.getAllDeckByPlayerName(mPlayerName);
//        textViewPlayerTotalDecks.setText(getString(R.string.player_decks, decks.size()));
//
//        /*
//        * List<Record> => All records in 1st/2nd/3rd/4th for a specific deck
//        * List < List<Record>> => each position is a list from a specific deck
//        * */
//        List<List<Record>> recordsFirstPlaceByDeck = new ArrayList<>();
//        List<List<Record>> recordsSecondPlaceByDeck = new ArrayList<>();
//        List<List<Record>> recordsThirdPlaceByDeck = new ArrayList<>();
//        List<List<Record>> recordsFourthPlaceByDeck = new ArrayList<>();
//
//        /*
//        * Get all 1st/2nd/3rd/4th records for each active player's deck
//        * */
//        for (int i = 0; i < decks.size(); i++) {
//            recordsFirstPlaceByDeck.add(recordsDB.getAllFirstPlaceRecordsByDeck(decks.get(i)));
//            recordsSecondPlaceByDeck.add(recordsDB.getAllSecondPlaceRecordsByDeck(decks.get(i)));
//            recordsThirdPlaceByDeck.add(recordsDB.getAllThirdPlaceRecordsByDeck(decks.get(i)));
//            recordsFourthPlaceByDeck.add(recordsDB.getAllFourthPlaceRecordsByDeck(decks.get(i)));
//        }
//
//
//        listViewPlayerDecks.setAdapter(new customDeckListViewAdapter(
//                this.getBaseContext(),
//                decks,
//                recordsFirstPlaceByDeck,
//                recordsSecondPlaceByDeck,
//                recordsThirdPlaceByDeck,
//                recordsFourthPlaceByDeck));
    }

    class customDeckListViewAdapter extends BaseAdapter {

        Context context;
        List<Deck> dataDeckList;
        List<List<Record>> dataFirstPlaceByDeckRecords;
        List<List<Record>> dataSecondPlaceByDeckRecords;
        List<List<Record>> dataThirdPlaceByDeckRecords;
        List<List<Record>> dataFourthPlaceByDeckRecords;

        private LayoutInflater inflater = null;

        public customDeckListViewAdapter(Context context, List<Deck> dataDeckList,
                                         List<List<Record>> dataFirstPlaceByDeckRecords,
                                         List<List<Record>> dataSecondPlaceByDeckRecords,
                                         List<List<Record>> dataThirdPlaceByDeckRecords,
                                         List<List<Record>> dataFourthPlaceByDeckRecords) {

            this.context = context;
            this.dataDeckList = dataDeckList;
            this.dataFirstPlaceByDeckRecords = dataFirstPlaceByDeckRecords;
            this.dataSecondPlaceByDeckRecords = dataSecondPlaceByDeckRecords;
            this.dataThirdPlaceByDeckRecords = dataThirdPlaceByDeckRecords;
            this.dataFourthPlaceByDeckRecords = dataFourthPlaceByDeckRecords;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return dataDeckList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataDeckList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.row_deck, null);

            TextView textViewRowDeckName = (TextView) vi.findViewById(R.id.textViewRowDeckName);
            TextView textViewRowDeckGames = (TextView) vi.findViewById(R.id.textViewRowDeckGames);

            int totalFirstPlace = dataFirstPlaceByDeckRecords.get(position).size();
            int totalSecondPlace = dataSecondPlaceByDeckRecords.get(position).size();
            int totalThirdPlace = dataThirdPlaceByDeckRecords.get(position).size();
            int totalFourthPlace = dataFourthPlaceByDeckRecords.get(position).size();
            int totalGames = totalFirstPlace + totalSecondPlace + totalThirdPlace + totalFourthPlace;

            textViewRowDeckName.setText(dataDeckList.get(position).getDeckName());
            textViewRowDeckGames.setText(getString(R.string.player_deck_games,
                    totalGames,
                    totalFirstPlace,
                    totalSecondPlace,
                    totalThirdPlace,
                    totalFourthPlace));

            return vi;
        }
    }
}
