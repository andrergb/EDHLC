package com.android.argb.edhlc.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.record.RecordsDataAccessObject;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Player;
import com.android.argb.edhlc.objects.Record;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends ActionBarActivity {

    private TextView textViewPlayerName;
    private TextView textViewPlayerTotalDecks;
    private TextView textViewPlayerGames;
    private ListView listViewPlayerDecks;

    private String playerName;

    private DecksDataAccessObject decksDB;
    private RecordsDataAccessObject recordsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.edh_default_dark));
        }

        decksDB = new DecksDataAccessObject(this);
        recordsDB = new RecordsDataAccessObject(this);

        //TODO
        playerName = "trofino";

//        populateDeckDB();
//        populateRecordDB();
        createLayout(this.findViewById(android.R.id.content));

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (getSharedPreferences(Player.PREFNAME, MODE_PRIVATE).getInt("SCREEN_ON", 0) == 1)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        decksDB.open();
        recordsDB.open();

        updateLayout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        decksDB.close();
        recordsDB.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void createLayout(View view) {
        if (view != null) {
            textViewPlayerName = (TextView) findViewById(R.id.textViewPlayerName);
            textViewPlayerTotalDecks = (TextView) findViewById(R.id.textViewPlayerTotalDecks);
            textViewPlayerGames = (TextView) findViewById(R.id.textViewPlayerGames);

            listViewPlayerDecks = (ListView) findViewById(R.id.listViewPlayerDecks);
        }
    }

    private void updateLayout() {
        /*Player's info - for all decks*/
        List<Record> recordsAllPositions = recordsDB.getAllRecordsByPlayerName(playerName);
        List<Record> recordsFirst = recordsDB.getAllFirstPlaceRecordsByPlayerName(playerName);
        List<Record> recordsSecond = recordsDB.getAllSecondPlaceRecordsByPlayerName(playerName);
        List<Record> recordsThird = recordsDB.getAllThirdPlaceRecordsByPlayerName(playerName);
        List<Record> recordsFourth = recordsDB.getAllFourthPlaceRecordsByPlayerName(playerName);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "MAGIC.TTF");
        textViewPlayerName.setTypeface(typeFace);
        textViewPlayerName.setText(playerName);

        textViewPlayerGames.setText(getString(R.string.player_games,
                recordsAllPositions.size(),
                recordsFirst.size(),
                recordsSecond.size(),
                recordsThird.size(),
                recordsFourth.size()));

        /*Information for each deck*/
        // TODO check if exists
        List<Deck> decks = decksDB.getAllDeckByPlayerName(playerName);
        textViewPlayerTotalDecks.setText(getString(R.string.player_decks, decks.size()));

        /*
        * List<Record> => All records in 1st/2nd/3rd/4th for a specific deck
        * List < List<Record>> => each position is a list from a specific deck
        * */
        List<List<Record>> recordsFirstPlaceByDeck = new ArrayList<>();
        List<List<Record>> recordsSecondPlaceByDeck = new ArrayList<>();
        List<List<Record>> recordsThirdPlaceByDeck = new ArrayList<>();
        List<List<Record>> recordsFourhPlaceByDeck = new ArrayList<>();

        /*
        * Get all 1st/2nd/3rd/4th records for each active player's deck
        * */
        for (int i = 0; i < decks.size(); i++) {
            recordsFirstPlaceByDeck.add(recordsDB.getAllFirstPlaceRecordsByDeck(decks.get(i)));
            recordsSecondPlaceByDeck.add(recordsDB.getAllSecondPlaceRecordsByDeck(decks.get(i)));
            recordsThirdPlaceByDeck.add(recordsDB.getAllThirdPlaceRecordsByDeck(decks.get(i)));
            recordsFourhPlaceByDeck.add(recordsDB.getAllFourthPlaceRecordsByDeck(decks.get(i)));
        }


        listViewPlayerDecks.setAdapter(new customDeckListViewAdapter(
                this.getBaseContext(),
                decks,
                recordsFirstPlaceByDeck,
                recordsSecondPlaceByDeck,
                recordsThirdPlaceByDeck,
                recordsFourhPlaceByDeck));
    }

    //TODO
    public void onClickAddDeck(View view) {

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

    private void populateDeckDB() {
        decksDB.createDeck(new Deck("trofino", "Deck1"));
        decksDB.createDeck(new Deck("trofino", "Deck2"));
        decksDB.createDeck(new Deck("Dezao", "DeckD"));
        decksDB.createDeck(new Deck("Dezao", "DeckDe"));
        decksDB.createDeck(new Deck("Ant", "DeckA"));
        decksDB.createDeck(new Deck("Marcos", "DeckM"));
    }

    // TODO REMOVE
    private void populateRecordDB() {

        /*
        * Deck1 == 6
        * first == 3
        * second = 1
        * third = 1
        * fourth = 1
        * */
        long inserted = recordsDB.createRecord(new Record(
                new Deck("trofino", "Deck1"),
                new Deck("Dezao", "DeckD"),
                new Deck("Marcos", "DeckM"),
                new Deck("Ant", "DeckA")));
        recordsDB.createRecord(new Record(
                new Deck("trofino", "Deck1"),
                new Deck("Dezao", "DeckD"),
                new Deck("Marcos", "DeckM"),
                new Deck("Ant", "DeckA")));
        recordsDB.createRecord(new Record(
                new Deck("trofino", "Deck1"),
                new Deck("Dezao", "DeckD"),
                new Deck("Marcos", "DeckM"),
                new Deck("Ant", "DeckA")));
        recordsDB.createRecord(new Record(
                new Deck("Dezao", "DeckD"),
                new Deck("trofino", "Deck1"),
                new Deck("Marcos", "DeckM"),
                new Deck("Ant", "DeckA")));
        recordsDB.createRecord(new Record(
                new Deck("Dezao", "DeckD"),
                new Deck("Marcos", "DeckM"),
                new Deck("trofino", "Deck1"),
                new Deck("Ant", "DeckA")));
        recordsDB.createRecord(new Record(
                new Deck("Dezao", "DeckD"),
                new Deck("Marcos", "DeckM"),
                new Deck("Ant", "DeckA"),
                new Deck("trofino", "Deck1")));


        /*
        * Deck2 == 8
        * first == 1
        * second = 2
        * third = 2
        * fourth = 3
        * */
        recordsDB.createRecord(new Record(
                new Deck("trofino", "Deck2"),
                new Deck("Dezao", "DeckD"),
                new Deck("Marcos", "DeckM"),
                new Deck("Ant", "DeckA")));
        recordsDB.createRecord(new Record(
                new Deck("Dezao", "DeckD"),
                new Deck("trofino", "Deck2"),
                new Deck("Marcos", "DeckM"),
                new Deck("Ant", "DeckA")));
        recordsDB.createRecord(new Record(
                new Deck("Dezao", "DeckD"),
                new Deck("trofino", "Deck2"),
                new Deck("Marcos", "DeckM"),
                new Deck("Ant", "DeckA")));
        recordsDB.createRecord(new Record(
                new Deck("Dezao", "DeckD"),
                new Deck("Marcos", "DeckM"),
                new Deck("trofino", "Deck2"),
                new Deck("Ant", "DeckA")));
        recordsDB.createRecord(new Record(
                new Deck("Dezao", "DeckD"),
                new Deck("Marcos", "DeckM"),
                new Deck("trofino", "Deck2"),
                new Deck("Ant", "DeckA")));
        recordsDB.createRecord(new Record(
                new Deck("Dezao", "DeckD"),
                new Deck("Marcos", "DeckM"),
                new Deck("Ant", "DeckA"),
                new Deck("trofino", "Deck2")));
        recordsDB.createRecord(new Record(
                new Deck("Dezao", "DeckD"),
                new Deck("Marcos", "DeckM"),
                new Deck("Ant", "DeckA"),
                new Deck("trofino", "Deck2")));
        recordsDB.createRecord(new Record(
                new Deck("Dezao", "DeckD"),
                new Deck("Marcos", "DeckM"),
                new Deck("Ant", "DeckA"),
                new Deck("trofino", "Deck2")));
    }
}
