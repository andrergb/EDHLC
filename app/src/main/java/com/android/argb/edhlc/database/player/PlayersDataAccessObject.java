package com.android.argb.edhlc.database.player;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * -Created by agbarros on 05/11/2015.
 */
public class PlayersDataAccessObject {

    private SQLiteDatabase database;
    private PlayersDbHelper playersDBHelper;

    public PlayersDataAccessObject(Context context) {
        playersDBHelper = new PlayersDbHelper(context);
    }

    public void open() {
        database = playersDBHelper.getWritableDatabase();
    }

    public void close() {
        playersDBHelper.close();
    }


    public long createPlayer(String playerName) {
        ContentValues values = new ContentValues();
        values.put(PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME, playerName);

        return database.insert(PlayersContract.PlayersEntry.TABLE_NAME, null, values);
    }

    public List<String> getAllPlayers() {
        List<String> playerList = new ArrayList<>();
        Cursor cursor = database.query(PlayersContract.PlayersEntry.TABLE_NAME, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            playerList.add(cursor.getString(cursor.getColumnIndexOrThrow(PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME)));
            cursor.moveToNext();
        }
        cursor.close();
        return playerList;
    }

    public int deletePlayer(String player) {
        return database.delete(
                PlayersContract.PlayersEntry.TABLE_NAME,
                PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME + " LIKE ?",
                new String[]{player}
        );
    }

    // TODO
    public void updateDeckByPlayerName() {
    }
}
