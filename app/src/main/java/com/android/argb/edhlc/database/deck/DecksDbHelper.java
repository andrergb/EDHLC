package com.android.argb.edhlc.database.deck;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by agbarros on 05/11/2015.
 */
public class DecksDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Decks.db";
    public static final int DATABASE_VERSION = 13;

    private static final String DATABASE_CREATE = "create table "
            + DecksContract.DecksEntry.TABLE_NAME + "("
            + DecksContract.DecksEntry.COLUMN_ID + " integer primary key autoincrement, "
            + DecksContract.DecksEntry.COLUMN_PLAYER_NAME + " text not null, "
            + DecksContract.DecksEntry.COLUMN_DECK_NAME + " text not null, "
            + DecksContract.DecksEntry.COLUMN_DECK_COLOR + " text not null, "
            + DecksContract.DecksEntry.COLUMN_DECK_IDENTITY + " text not null);";

    public DecksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DecksContract.DecksEntry.TABLE_NAME);
        onCreate(db);
    }

}


