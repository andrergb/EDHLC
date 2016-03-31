package com.android.argb.edhlc.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
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
import android.widget.ListView;

import com.android.argb.edhlc.Constants;
import com.android.argb.edhlc.NewGameAdapter;
import com.android.argb.edhlc.R;
import com.android.argb.edhlc.Utils;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.database.player.PlayersDataAccessObject;
import com.android.argb.edhlc.objects.ActivePlayerNew;
import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Player;

import java.util.ArrayList;
import java.util.List;

public class NewGameActivity extends AppCompatActivity {

    private ActionBar mActionBar;
    private Menu optionMenu;
    private View statusBarBackground;

    private ListView listViewNewGame;

    private ArrayList<String[]> playersList; // 0: type - 1: item - 2: check
    private NewGameAdapter mPlayersAdapter;

    private DecksDataAccessObject decksDb;
    private PlayersDataAccessObject playersDb;

    @Override
    public void onBackPressed() {
        Intent intentHome = new Intent(this, MainActivityNew.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentHome);
        this.finish();

        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionMenu = menu;
        getMenuInflater().inflate(R.menu.menu_new_game, menu);
        if (getCurrentTotalPlayers() >= 2)
            optionMenu.getItem(0).setEnabled(true);
        else
            optionMenu.getItem(0).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Option menu
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent(this, MainActivityNew.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;

            case R.id.actions_start_game:

                int index = -1;
                String player = "";
                String deck = "";
                for (int i = 0; i < playersList.size(); i++) {

                    if (mPlayersAdapter.isSelected(i) && mPlayersAdapter.isPlayer(i))
                        player = playersList.get(i)[1];
                    else if (mPlayersAdapter.isSelected(i) && mPlayersAdapter.isDeck(i))
                        deck = playersList.get(i)[1];

                    if (!player.equalsIgnoreCase("") && !deck.equalsIgnoreCase("")) {
                        index++;
                        Utils.savePlayerInSharedPreferences(this, new ActivePlayerNew(decksDb.getDeck(player, deck), true, 40, 0, 0, 0, 0, index));
                        player = "";
                        deck = "";
                    }
                }
                //Total players
                getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE).edit().putInt(Constants.CURRENT_GAME_TOTAL_PLAYERS, index + 1).apply();
                //INITIAL TAB
                getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE).edit().putInt(Constants.CURRENT_VIEW_TAB, 0).apply();

                Intent intentHome = new Intent(this, MainActivityNew.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
                this.finish();

                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);


        playersDb = new PlayersDataAccessObject(this);
        decksDb = new DecksDataAccessObject(this);
        playersDb.open();
        decksDb.open();

        createStatusBar();
        createToolbar();
        createLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        decksDb.close();
        playersDb.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLayout();
    }


    private void createLayout() {
        playersList = new ArrayList<>();
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
                        //TODO snack
                        Log.d("dezao", "limit reached - 4 players");
                    }

                    mPlayersAdapter.notifyDataSetChanged();

                    if (getCurrentTotalPlayers() >= 2)
                        optionMenu.getItem(0).setEnabled(true);
                    else
                        optionMenu.getItem(0).setEnabled(false);
                }
            }
        });
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
        mActionBar.setTitle("New Game - " + getCurrentTotalPlayers());
    }

    private int getCurrentTotalPlayers() {
        int ret = 0;
        if (playersList == null)
            return ret;

        for (int i = 0; i < playersList.size(); i++)
            if (playersList.get(i)[0].equalsIgnoreCase("DECK"))
                if (playersList.get(i)[2].equalsIgnoreCase("TRUE"))
                    ret++;

        return ret;
    }

    private boolean isPlayerSelected(int position) {
        for (int i = position; i >= 0; i--)
            if (mPlayersAdapter.isPlayer(i))
                return playersList.get(i)[2].equalsIgnoreCase("TRUE");
        return false;
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
        List<Player> allPlayers = playersDb.getAllPlayers();
        for (int i = 0; i < allPlayers.size(); i++) {
            String currentPlayer = allPlayers.get(i).getPlayerName();
            String playerCheck = "FALSE";

            Intent intent = getIntent();
            boolean isValid = intent.getBooleanExtra("NEW_GAME_IS_VALID", false);

            if (isValid) {
                int totalPlayers = intent.getIntExtra("NEW_GAME_TOTAL_PLAYER", 0);

                String player1 = intent.getStringExtra("NEW_GAME_PLAYER_1");
                String player2 = intent.getStringExtra("NEW_GAME_PLAYER_2");
                String player3 = totalPlayers >= 3 ? intent.getStringExtra("NEW_GAME_PLAYER_3") : "";
                String player4 = totalPlayers >= 4 ? intent.getStringExtra("NEW_GAME_PLAYER_4") : "";

                if (currentPlayer.equalsIgnoreCase(player1))
                    playerCheck = "TRUE";
                if (currentPlayer.equalsIgnoreCase(player2))
                    playerCheck = "TRUE";
                if (totalPlayers >= 3)
                    if (currentPlayer.equalsIgnoreCase(player3))
                        playerCheck = "TRUE";
                if (totalPlayers >= 4)
                    if (currentPlayer.equalsIgnoreCase(player4))
                        playerCheck = "TRUE";

            }

            playersList.add(new String[]{"PLAYER", currentPlayer, playerCheck});

            List<Deck> allDeckCurrentPlayer = decksDb.getAllDeckByPlayerName(allPlayers.get(i).getPlayerName());
            for (int j = 0; j < allDeckCurrentPlayer.size(); j++) {

                String currentDeck = allDeckCurrentPlayer.get(j).getDeckName();
                String deckCheck = "FALSE";

                if (isValid) {
                    int totalPlayers = intent.getIntExtra("NEW_GAME_TOTAL_PLAYER", 0);

                    String deck1 = intent.getStringExtra("NEW_GAME_DECK_1");
                    String deck2 = intent.getStringExtra("NEW_GAME_DECK_2");
                    String deck3 = totalPlayers >= 3 ? intent.getStringExtra("NEW_GAME_DECK_3") : "";
                    String deck4 = totalPlayers >= 4 ? intent.getStringExtra("NEW_GAME_DECK_4") : "";

                    if (currentDeck.equalsIgnoreCase(deck1))
                        deckCheck = "TRUE";
                    if (currentDeck.equalsIgnoreCase(deck2))
                        deckCheck = "TRUE";
                    if (totalPlayers >= 3)
                        if (currentDeck.equalsIgnoreCase(deck3))
                            deckCheck = "TRUE";
                    if (totalPlayers >= 4)
                        if (currentDeck.equalsIgnoreCase(deck4))
                            deckCheck = "TRUE";

                }

                playersList.add(new String[]{"DECK", currentDeck, deckCheck});
            }
        }
        mPlayersAdapter.notifyDataSetChanged();
    }
}
