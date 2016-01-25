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

    public long addDeck(Deck deck) {
        if (!isDeckAdded(deck)) {
            ContentValues values = new ContentValues();
            values.put(DecksContract.DecksEntry.COLUMN_PLAYER_NAME, deck.getPlayerName());
            values.put(DecksContract.DecksEntry.COLUMN_DECK_NAME, deck.getDeckName());
            values.put(DecksContract.DecksEntry.COLUMN_DECK_COLOR, String.valueOf(deck.getDeckColor()[0] + System.getProperty("path.separator") + deck.getDeckColor()[1]));

            return database.insert(DecksContract.DecksEntry.TABLE_NAME, null, values);
        } else {
            return -1;
        }
    }

    public void close() {
        decksDBHelper.close();
    }

    public List<Deck> getAllDeckByPlayerName(String playerName) {
        List<Deck> deckList = new ArrayList<>();
        Cursor cursor = database.query(
                DecksContract.DecksEntry.TABLE_NAME,
                new String[]{DecksContract.DecksEntry.COLUMN_PLAYER_NAME, DecksContract.DecksEntry.COLUMN_DECK_NAME, DecksContract.DecksEntry.COLUMN_DECK_COLOR},
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

    public Deck getDeck(String playerName, String deckName) {
        List<Deck> deckList = new ArrayList<>();
        Cursor cursor = database.query(
                DecksContract.DecksEntry.TABLE_NAME,
                new String[]{DecksContract.DecksEntry.COLUMN_PLAYER_NAME, DecksContract.DecksEntry.COLUMN_DECK_NAME, DecksContract.DecksEntry.COLUMN_DECK_COLOR},
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND " + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                new String[]{playerName, deckName},
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
        return deckList.get(0);
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

    public void open() {
        database = decksDBHelper.getWritableDatabase();
    }

    public long removeDeck(Deck deck) {
        return database.delete(
                DecksContract.DecksEntry.TABLE_NAME,
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND "
                        + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                new String[]{deck.getPlayerName(), deck.getDeckName()}
        );
    }

    public long updateDeck(Deck oldDeck, Deck newDeck) {
        if (!isDeckAdded(newDeck)) {

            ContentValues values = new ContentValues();
            values.put(DecksContract.DecksEntry.COLUMN_PLAYER_NAME, newDeck.getPlayerName());
            values.put(DecksContract.DecksEntry.COLUMN_DECK_NAME, newDeck.getDeckName());
            if (newDeck.getDeckColor() != null) {
                values.put(DecksContract.DecksEntry.COLUMN_DECK_COLOR, String.valueOf(newDeck.getDeckColor()[0] + System.getProperty("path.separator") + newDeck.getDeckColor()[1]));
            }

            return database.update(DecksContract.DecksEntry.TABLE_NAME,
                    values,
                    DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND " + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                    new String[]{oldDeck.getPlayerName(), oldDeck.getDeckName()});
        } else {
            return -1;
        }
    }

    public long updateDeck(String playerOldName, String playerNewName) {
        ContentValues values = new ContentValues();
        values.put(DecksContract.DecksEntry.COLUMN_PLAYER_NAME, playerNewName);
        return database.update(DecksContract.DecksEntry.TABLE_NAME,
                values,
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ?",
                new String[]{playerOldName});

    }

    public int updateDeckColor(Deck deck) {
        ContentValues values = new ContentValues();
        values.put(DecksContract.DecksEntry.COLUMN_DECK_COLOR, String.valueOf(String.valueOf(deck.getDeckColor()[0] + System.getProperty("path.separator") + deck.getDeckColor()[1])));

        return database.update(DecksContract.DecksEntry.TABLE_NAME,
                values,
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND " + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                new String[]{deck.getPlayerName(), deck.getDeckName()});
    }

    private Deck cursorToDeck(Cursor cursor) {
        Deck deck = new Deck();
        deck.setPlayerName(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_PLAYER_NAME)));
        deck.setDeckName(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_DECK_NAME)));
        deck.setDeckColor(
                new int[]{Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_DECK_COLOR)).split(System.getProperty("path.separator"))[0]),
                        Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_DECK_COLOR)).split(System.getProperty("path.separator"))[1])});
        return deck;
    }

}
