package com.android.argb.edhlc.database.deck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.argb.edhlc.objects.Deck;

import java.util.ArrayList;
import java.util.List;

/* Created by ARGB */
public class DecksDataAccessObject {

    private SQLiteDatabase database;
    private DecksDbHelper decksDBHelper;

    public DecksDataAccessObject(Context context) {
        decksDBHelper = new DecksDbHelper(context);
    }

    public long addDeck(Deck deck) {
        if (!isDeckAdded(deck)) {
            ContentValues values = new ContentValues();
            values.put(DecksContract.DecksEntry.COLUMN_PLAYER_NAME, deck.getDeckOwnerName());
            values.put(DecksContract.DecksEntry.COLUMN_DECK_NAME, deck.getDeckName());
            values.put(DecksContract.DecksEntry.COLUMN_DECK_COLOR, String.valueOf(deck.getDeckShieldColor()[0] + System.getProperty("path.separator") + deck.getDeckShieldColor()[1]));
            values.put(DecksContract.DecksEntry.COLUMN_DECK_IDENTITY, deck.getDeckIdentity());
            values.put(DecksContract.DecksEntry.COLUMN_DECK_CREATION_DATE, deck.getDeckCreationDate());

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
                null,
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
        Cursor cursor = database.query(
                DecksContract.DecksEntry.TABLE_NAME,
                null,
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND " + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                new String[]{playerName, deckName},
                null,
                null,
                null
        );

        Deck aux = new Deck();
        if (cursor.moveToFirst())
            aux = cursorToDeck(cursor);

        cursor.close();
        return aux;
    }

    public boolean hasDeck(Deck deck) {
        Cursor c = database.query(
                DecksContract.DecksEntry.TABLE_NAME,
                null,
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND " + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                new String[]{deck.getDeckOwnerName(), deck.getDeckName()},
                null,
                null,
                null
        );

        if (c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    public boolean isDeckAdded(Deck deck) {
        Cursor cursor = database.query(
                DecksContract.DecksEntry.TABLE_NAME,
                new String[]{DecksContract.DecksEntry.COLUMN_PLAYER_NAME, DecksContract.DecksEntry.COLUMN_DECK_NAME},
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND " + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                new String[]{deck.getDeckOwnerName(), deck.getDeckName()}, null, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean isOpen() {
        return database.isOpen();
    }

    public void open() {
        database = decksDBHelper.getWritableDatabase();
    }

    public long removeDeck(Deck deck) {
        return database.delete(
                DecksContract.DecksEntry.TABLE_NAME,
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND "
                        + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                new String[]{deck.getDeckOwnerName(), deck.getDeckName()}
        );
    }

    public int updateDeckIdentity(Deck deck, String identity) {
        ContentValues values = new ContentValues();
        values.put(DecksContract.DecksEntry.COLUMN_DECK_IDENTITY, identity);

        return database.update(DecksContract.DecksEntry.TABLE_NAME,
                values,
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND " + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                new String[]{deck.getDeckOwnerName(), deck.getDeckName()});
    }

    public int updateDeckName(Deck deck, String deckName) {
        if (!isDeckAdded(new Deck(deck.getDeckOwnerName(), deckName))) {
            ContentValues values = new ContentValues();
            values.put(DecksContract.DecksEntry.COLUMN_DECK_NAME, deckName);

            return database.update(DecksContract.DecksEntry.TABLE_NAME,
                    values,
                    DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND " + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                    new String[]{deck.getDeckOwnerName(), deck.getDeckName()});
        } else {
            return -1;
        }
    }

    public long updateDeckOwner(String oldOwnerName, String newOwnerName) {
        ContentValues values = new ContentValues();
        values.put(DecksContract.DecksEntry.COLUMN_PLAYER_NAME, newOwnerName);

        return database.update(DecksContract.DecksEntry.TABLE_NAME,
                values,
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ?",
                new String[]{oldOwnerName});

    }

    public int updateDeckShieldColor(Deck deck, int[] colors) {
        ContentValues values = new ContentValues();
        values.put(DecksContract.DecksEntry.COLUMN_DECK_COLOR, String.valueOf(colors[0]) + System.getProperty("path.separator") + String.valueOf(colors[1]));

        return database.update(DecksContract.DecksEntry.TABLE_NAME,
                values,
                DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " LIKE ? AND " + DecksContract.DecksEntry.COLUMN_DECK_NAME + " LIKE ?",
                new String[]{deck.getDeckOwnerName(), deck.getDeckName()});
    }

    private Deck cursorToDeck(Cursor cursor) {
        Deck deck = new Deck();
        deck.setDeckOwnerName(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_PLAYER_NAME)));
        deck.setDeckName(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_DECK_NAME)));
        deck.setDeckShieldColor(
                new int[]{Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_DECK_COLOR)).split(System.getProperty("path.separator"))[0]),
                        Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_DECK_COLOR)).split(System.getProperty("path.separator"))[1])});
        deck.setDeckIdentity(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_DECK_IDENTITY)));
        deck.setDeckCreationDate(cursor.getString(cursor.getColumnIndexOrThrow(DecksContract.DecksEntry.COLUMN_DECK_CREATION_DATE)));
        return deck;
    }

}
