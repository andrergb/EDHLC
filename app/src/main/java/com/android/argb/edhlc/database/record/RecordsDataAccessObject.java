package com.android.argb.edhlc.database.record;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agbarros on 05/11/2015.
 */
public class RecordsDataAccessObject {

    private SQLiteDatabase database;
    private RecordsDbHelper recordsDBHelper;

    public RecordsDataAccessObject(Context context) {
        recordsDBHelper = new RecordsDbHelper(context);
    }

    public void open() {
        database = recordsDBHelper.getWritableDatabase();
    }

    public void close() {
        recordsDBHelper.close();
    }


    public long createRecord(Record record) {
        ContentValues values = new ContentValues();

        values.put(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME, record.getFistPlace().getPlayerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK, record.getFistPlace().getDeckName());
        values.put(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME, record.getSecondtPlace().getPlayerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK, record.getSecondtPlace().getDeckName());
        values.put(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME, record.getThirdPlace().getPlayerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK, record.getThirdPlace().getDeckName());
        values.put(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME, record.getFourthPlace().getPlayerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK, record.getFourthPlace().getDeckName());

        return database.insert(RecordsContract.RecordsEntry.TABLE_NAME, null, values);
    }

    public List<Record> getAllRecords() {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(RecordsContract.RecordsEntry.TABLE_NAME, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

    public List<Record> getAllRecordsByPlayerName(String playerName) {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(
                RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + " LIKE ? OR "
                        + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + " LIKE ? OR "
                        + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + " LIKE ? OR "
                        + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + " LIKE ?",
                new String[]{playerName, playerName, playerName, playerName},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

    public List<Record> getAllFirstPlaceRecordsByPlayerName(String playerName) {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(
                RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + " LIKE ?",
                new String[]{playerName},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

    public List<Record> getAllFirstPlaceRecordsByDeck(Deck deck) {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(
                RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + " LIKE ? AND "
                        + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK + " LIKE ?",
                new String[]{deck.getPlayerName(), deck.getDeckName()},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

    public List<Record> getAllSecondPlaceRecordsByPlayerName(String playerName) {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(
                RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + " LIKE ?",
                new String[]{playerName},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

    public List<Record> getAllSecondPlaceRecordsByDeck(Deck deck) {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(
                RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + " LIKE ? AND "
                        + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK + " LIKE ?",
                new String[]{deck.getPlayerName(), deck.getDeckName()},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

    public List<Record> getAllThirdPlaceRecordsByPlayerName(String playerName) {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(
                RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + " LIKE ?",
                new String[]{playerName},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

    public List<Record> getAllThirdPlaceRecordsByDeck(Deck deck) {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(
                RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + " LIKE ? AND "
                        + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK + " LIKE ?",
                new String[]{deck.getPlayerName(), deck.getDeckName()},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

    public List<Record> getAllFourthPlaceRecordsByPlayerName(String playerName) {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(
                RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + " LIKE ?",
                new String[]{playerName},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

    public List<Record> getAllFourthPlaceRecordsByDeck(Deck deck) {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(
                RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + " LIKE ? AND "
                        + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK + " LIKE ?",
                new String[]{deck.getPlayerName(), deck.getDeckName()},
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

    public void deleteRecord(Record record) {
        database.delete(
                RecordsContract.RecordsEntry.TABLE_NAME,
                RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + " LIKE ? AND "
                        + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK + " LIKE ? AND "

                        + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + " LIKE ? AND "
                        + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK + " LIKE ? AND "

                        + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + " LIKE ? AND "
                        + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK + " LIKE ? AND "

                        + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + " LIKE ? AND"
                        + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK + " LIKE ?",
                new String[]{
                        record.getFistPlace().getPlayerName(), record.getFistPlace().getDeckName(),
                        record.getSecondtPlace().getPlayerName(), record.getSecondtPlace().getDeckName(),
                        record.getThirdPlace().getPlayerName(), record.getThirdPlace().getDeckName(),
                        record.getFourthPlace().getPlayerName(), record.getFourthPlace().getDeckName(),
                }
        );
    }

    private Record cursorToRecord(Cursor cursor) {
        Record record = new Record();

        record.setFistPlace(
                new Deck(
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK))
                )
        );

        record.setSecondtPlace(
                new Deck(
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK))
                )
        );

        record.setThirdPlace(
                new Deck(
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK))
                )
        );

        record.setFourthPlace(
                new Deck(
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK))
                )
        );

        return record;
    }

}
