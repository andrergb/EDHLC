package com.android.argb.edhlc.database.player;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.argb.edhlc.objects.Player;

import java.util.ArrayList;
import java.util.List;

/* Created by ARGB */
public class PlayersDataAccessObject {

    private SQLiteDatabase database;
    private PlayersDbHelper playersDBHelper;

    public PlayersDataAccessObject(Context context) {
        playersDBHelper = new PlayersDbHelper(context);
    }

    public long addPlayer(String playerName, String creationDate) {
        if (!isPlayerAdded(playerName)) {
            ContentValues values = new ContentValues();
            values.put(PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME, playerName);
            values.put(PlayersContract.PlayersEntry.COLUMN_PLAYER_DATE, creationDate);
            return database.insert(PlayersContract.PlayersEntry.TABLE_NAME, null, values);
        } else {
            return -1;
        }
    }

    public void close() {
        playersDBHelper.close();
    }

    public int deletePlayer(String player) {
        return database.delete(
                PlayersContract.PlayersEntry.TABLE_NAME,
                PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME + " LIKE ?",
                new String[]{player}
        );
    }

    public List<Player> getAllPlayers() {
        List<Player> playerList = new ArrayList<>();
        Cursor cursor = database.query(PlayersContract.PlayersEntry.TABLE_NAME, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            playerList.add(new Player(cursor.getString(cursor.getColumnIndexOrThrow(PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(PlayersContract.PlayersEntry.COLUMN_PLAYER_DATE))));
            cursor.moveToNext();
        }
        cursor.close();
        return playerList;
    }

    public Player getPlayer(String playerName) {
        Cursor cursor = database.query(
                PlayersContract.PlayersEntry.TABLE_NAME,
                null,
                PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME + " LIKE ?",
                new String[]{playerName},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        Player playerList = new Player(cursor.getString(cursor.getColumnIndexOrThrow(PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(PlayersContract.PlayersEntry.COLUMN_PLAYER_DATE)));
        cursor.close();
        return playerList;
    }

    public boolean hasPlayer(String playerName) {
        Cursor c = database.query(
                PlayersContract.PlayersEntry.TABLE_NAME,
                null,
                PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME + " LIKE ?",
                new String[]{playerName},
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

    public boolean isOpen() {
        return database.isOpen();
    }

    public boolean isPlayerAdded(String playerName) {
        Cursor cursor = database.query(
                PlayersContract.PlayersEntry.TABLE_NAME,
                new String[]{PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME},
                PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME + " LIKE ?",
                new String[]{playerName}, null, null, null);

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
        database = playersDBHelper.getWritableDatabase();
    }

    public long updatePlayer(String oldName, String newName) {
        if (!isPlayerAdded(newName)) {
            ContentValues values = new ContentValues();
            values.put(PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME, newName);
            return database.update(PlayersContract.PlayersEntry.TABLE_NAME, values, PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME + " =?", new String[]{oldName});
        } else {
            return -1;
        }
    }
}
