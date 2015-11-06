package com.android.argb.edhlc.database.record;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by agbarros on 05/11/2015.
 */
public class RecordsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Records.db";
    public static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table "
            + RecordsContract.RecordsEntry.TABLE_NAME + "("
            + RecordsContract.RecordsEntry.COLUMN_ID + " integer primary key autoincrement, "

            + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + " text not null, "
            + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK + " text not null, "

            + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + " text not null, "
            + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK + " text not null, "

            + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + " text, "
            + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK + " text, "

            + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + " text, "
            + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK + " textl);";

    public RecordsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RecordsContract.RecordsEntry.TABLE_NAME);
        onCreate(db);
    }

}


