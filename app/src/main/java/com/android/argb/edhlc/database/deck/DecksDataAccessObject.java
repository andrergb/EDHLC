package com.android.argb.edhlc.database.deck;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.argb.edhlc.objects.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agbarros on 05/11/2015.
 */
public class DecksDataAccessObject {

    private SQLiteDatabase database;
    private DecksDbHelper decksDBHelper;

    public DecksDataAccessObject(Context context) {
        decksDBHelper = new DecksDbHelper(context);
    }

    public void open() {
        database = decksDBHelper.getWritableDatabase();
    }

    public void close() {
        decksDBHelper.close();
    }


    public long createDeck(Deck deck) {
        if (!isDeckAdded(deck)) {
            ContentValues values = new ContentValues();
            values.put(DecksContract.DecksEntry.COLUMN_PLAYER_NAME, deck.getPlayerName());
            values.put(DecksContract.DecksEntry.COLUMN_DECK_NAME, deck.getDeckName());

            return database.insert(DecksContract.DecksEntry.TABLE_NAME, null, values);
        } else {
            return -1;
        }
    }

    public boolean isDeckAdded(Deck deck) {
        Cursor cursor = database.query(
                DecksContract.DecksEntry.TABLE_NAME,
                new String[]{DecksContract.DecksEntry.COLUMN_PLAYER_NAME, DecksContract.DecksEntry.COLUMN_DECK_NAME},
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND " + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                new String[]{deck.getPlayerName(), deck.getDeckName()}, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public List<Deck> getAllDecks() {
        List<Deck> deckList = new ArrayList<>();
        Cursor cursor = database.query(DecksContract.DecksEntry.TABLE_NAME, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            deckList.add(cursorToDeck(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return deckList;
    }

    public List<Deck> getAllDeckByPlayerName(String playerName) {
        List<Deck> deckList = new ArrayList<>();
        Cursor cursor = database.query(
                DecksContract.DecksEntry.TABLE_NAME,
                new String[]{DecksContract.DecksEntry.COLUMN_PLAYER_NAME, DecksContract.DecksEntry.COLUMN_DECK_NAME},
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ?",
                new String[]{playerName},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            deckList.add(cursorToDeck(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return deckList;
    }

    public long deleteDeck(Deck deck) {
        return database.delete(
                DecksContract.DecksEntry.TABLE_NAME,
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND "
                        + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                new String[]{deck.getPlayerName(), deck.getDeckName()}
        );
    }

    // TODO
    public void updateDeckByPlayerName() {
    }

    private Deck cursorToDeck(Cursor cursor) {
        Deck deck = new Deck();
        deck.setPlayerName(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_PLAYER_NAME)));
        deck.setDeckName(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_DECK_NAME)));
        return deck;
    }

}
