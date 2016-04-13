package com.android.argb.edhlc.database.player;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* Created by ARGB */
public class PlayersDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Players.db";
    public static final int DATABASE_VERSION = 7;

    private static final String DATABASE_CREATE = "create table "
            + PlayersContract.PlayersEntry.TABLE_NAME + "("
            + PlayersContract.PlayersEntry.COLUMN_ID + " integer primary key autoincrement, "
            + PlayersContract.PlayersEntry.COLUMN_PLAYER_NAME + " text not null, "
            + PlayersContract.PlayersEntry.COLUMN_PLAYER_DATE + " text not null);";

    public PlayersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlayersContract.PlayersEntry.TABLE_NAME);
        onCreate(db);
    }

}


