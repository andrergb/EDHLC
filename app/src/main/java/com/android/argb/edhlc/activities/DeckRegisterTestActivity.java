package com.android.argb.edhlc.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.argb.edhlc.R;
import com.android.argb.edhlc.database.deck.DecksDataAccessObject;
import com.android.argb.edhlc.objects.Deck;

import java.util.List;

public class DeckRegisterTestActivity extends ActionBarActivity {

    private int numPlayers;

    private EditText editTextPlayerName;
    private EditText editTextDeckName;
    private Button buttonOk;

    private DecksDataAccessObject db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decktest);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.edh_default_secondary));
        }

        db = new DecksDataAccessObject(this);
        db.open();

        //TODO
        createLayout(this.findViewById(android.R.id.content));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            editTextPlayerName = (EditText) findViewById(R.id.decktestPlayerName);
            editTextDeckName = (EditText) findViewById(R.id.decktestDeckName);
            buttonOk = (Button) findViewById(R.id.decktestButtonOk);
        }
    }

    public void onClickSaveDeck(View view) {
        Log.d("deckTest", "Inserting: " + editTextPlayerName.getText().toString() + " - " + editTextDeckName.getText().toString());
        db.createDeck(new Deck(editTextPlayerName.getText().toString(), editTextDeckName.getText().toString()));
    }

    public void onClickLog(View view) {
        List<Deck> listDecks = db.getAllDecks();

        Log.d("deckTest", "====================== Size: " + listDecks.size());

        for (int i = 0; i < listDecks.size(); i++)
            Log.d("deckTest", listDecks.get(i).getPlayerName() + " - " + listDecks.get(i).getDeckName());

        Log.d("deckTest", "======================");
    }
}
