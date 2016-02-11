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
 * -Created by agbarros on 05/11/2015.
 */
public class RecordsDataAccessObject {

    private SQLiteDatabase database;
    private RecordsDbHelper recordsDBHelper;

    public RecordsDataAccessObject(Context context) {
        recordsDBHelper = new RecordsDbHelper(context);
    }

    public long addRecord(Record record) {
        ContentValues values = new ContentValues();

        values.put(RecordsContract.RecordsEntry.COLUMN_DATE, record.getDate());

        values.put(RecordsContract.RecordsEntry.COLUMN_TOTAL_PLAYERS, record.getTotalPlayers());

        values.put(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME, record.getFirstPlace().getPlayerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK, record.getFirstPlace().getDeckName());

        values.put(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME, record.getSecondPlace().getPlayerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK, record.getSecondPlace().getDeckName());

        values.put(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME, record.getThirdPlace().getPlayerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK, record.getThirdPlace().getDeckName());

        values.put(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME, record.getFourthPlace().getPlayerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK, record.getFourthPlace().getDeckName());

        return database.insert(RecordsContract.RecordsEntry.TABLE_NAME, null, values);
    }

    public void close() {
        recordsDBHelper.close();
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
                        record.getFirstPlace().getPlayerName(), record.getFirstPlace().getDeckName(),
                        record.getSecondPlace().getPlayerName(), record.getSecondPlace().getDeckName(),
                        record.getThirdPlace().getPlayerName(), record.getThirdPlace().getDeckName(),
                        record.getFourthPlace().getPlayerName(), record.getFourthPlace().getDeckName(),
                }
        );
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

//    public List<Record> getAllFirstPlaceRecordsByPlayerName(String playerName) {
//        List<Record> recordList = new ArrayList<>();
//        Cursor cursor = database.query(
//                RecordsContract.RecordsEntry.TABLE_NAME,
//                null,
//                RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + " LIKE ?",
//                new String[]{playerName},
//                null,
//                null,
//                null
//        );
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            recordList.add(cursorToRecord(cursor));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return recordList;
//    }
//
//    public List<Record> getAllFourthPlaceRecordsByDeck(Deck deck) {
//        List<Record> recordList = new ArrayList<>();
//        Cursor cursor = database.query(
//                RecordsContract.RecordsEntry.TABLE_NAME,
//                null,
//                RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + " LIKE ? AND "
//                        + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK + " LIKE ?",
//                new String[]{deck.getPlayerName(), deck.getDeckName()},
//                null,
//                null,
//                null
//        );
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            recordList.add(cursorToRecord(cursor));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return recordList;
//    }
//
//    public List<Record> getAllFourthPlaceRecordsByPlayerName(String playerName) {
//        List<Record> recordList = new ArrayList<>();
//        Cursor cursor = database.query(
//                RecordsContract.RecordsEntry.TABLE_NAME,
//                null,
//                RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + " LIKE ?",
//                new String[]{playerName},
//                null,
//                null,
//                null
//        );
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            recordList.add(cursorToRecord(cursor));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return recordList;
//    }

    public List<Record> getAllRecordsByDeck(Deck deck) {
        List<Record> recordList = new ArrayList<>();

        if (deck.getDeckName().equalsIgnoreCase("") && deck.getPlayerName().equalsIgnoreCase("")) {
            recordList = getAllRecords();
        } else {
            Cursor cursor = database.query(
                    RecordsContract.RecordsEntry.TABLE_NAME,
                    null,
                    "(" + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + " LIKE ? AND "
                            + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK + " LIKE ?"
                            + ") OR ("
                            + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + " LIKE ? AND "
                            + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK + " LIKE ?"
                            + ") OR ("
                            + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + " LIKE ? AND "
                            + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK + " LIKE ?"
                            + ") OR ("
                            + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + " LIKE ? AND "
                            + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK + " LIKE ?)",
                    new String[]{deck.getPlayerName(),
                            deck.getDeckName(),
                            deck.getPlayerName(),
                            deck.getDeckName(),
                            deck.getPlayerName(),
                            deck.getDeckName(),
                            deck.getPlayerName(),
                            deck.getDeckName()},
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
        }
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

    public List<Record> getRecordsByPosition(Deck deck, int position, int totalPlayers) {
        String selection = "";
        switch (position) {
            case 1:
                selection = RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME
                        + " LIKE ? AND "
                        + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK
                        + " LIKE ?";
                break;
            case 2:
                selection = RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME
                        + " LIKE ? AND "
                        + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK
                        + " LIKE ?";
                break;
            case 3:
                selection = RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME
                        + " LIKE ? AND "
                        + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK
                        + " LIKE ?";
                break;
            case 4:
                selection = RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME
                        + " LIKE ? AND "
                        + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK
                        + " LIKE ?";
                break;
        }

        selection = selection + " AND " + RecordsContract.RecordsEntry.COLUMN_TOTAL_PLAYERS + " LIKE ?";
        String[] selectionArgs = new String[]{deck.getPlayerName(), deck.getDeckName(), String.valueOf(totalPlayers)};

        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(RecordsContract.RecordsEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

//    public List<Record> getAllSecondPlaceRecordsByDeck(Deck deck) {
//        List<Record> recordList = new ArrayList<>();
//        Cursor cursor = database.query(
//                RecordsContract.RecordsEntry.TABLE_NAME,
//                null,
//                RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + " LIKE ? AND "
//                        + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK + " LIKE ?",
//                new String[]{deck.getPlayerName(), deck.getDeckName()},
//                null,
//                null,
//                null
//        );
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            recordList.add(cursorToRecord(cursor));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return recordList;
//    }
//
//    public List<Record> getAllSecondPlaceRecordsByPlayerName(String playerName) {
//        List<Record> recordList = new ArrayList<>();
//        Cursor cursor = database.query(
//                RecordsContract.RecordsEntry.TABLE_NAME,
//                null,
//                RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + " LIKE ?",
//                new String[]{playerName},
//                null,
//                null,
//                null
//        );
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            recordList.add(cursorToRecord(cursor));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return recordList;
//    }
//
//    public List<Record> getAllThirdPlaceRecordsByDeck(Deck deck) {
//        List<Record> recordList = new ArrayList<>();
//        Cursor cursor = database.query(
//                RecordsContract.RecordsEntry.TABLE_NAME,
//                null,
//                RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + " LIKE ? AND "
//                        + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK + " LIKE ?",
//                new String[]{deck.getPlayerName(), deck.getDeckName()},
//                null,
//                null,
//                null
//        );
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            recordList.add(cursorToRecord(cursor));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return recordList;
//    }
//
//    public List<Record> getAllThirdPlaceRecordsByPlayerName(String playerName) {
//        List<Record> recordList = new ArrayList<>();
//        Cursor cursor = database.query(
//                RecordsContract.RecordsEntry.TABLE_NAME,
//                null,
//                RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + " LIKE ?",
//                new String[]{playerName},
//                null,
//                null,
//                null
//        );
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            recordList.add(cursorToRecord(cursor));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        return recordList;
//    }

    public void open() {
        database = recordsDBHelper.getWritableDatabase();
    }

    public long updateRecord(String oldPlayerName, String newPlayerName) {
        long totalUpdate = 0;

        ContentValues values1 = new ContentValues();
        values1.put(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME, newPlayerName);
        totalUpdate += database.update(RecordsContract.RecordsEntry.TABLE_NAME, values1, RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + "=?", new String[]{oldPlayerName});

        ContentValues values2 = new ContentValues();
        values2.put(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME, newPlayerName);
        totalUpdate += database.update(RecordsContract.RecordsEntry.TABLE_NAME, values2, RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + "=?", new String[]{oldPlayerName});

        ContentValues values3 = new ContentValues();
        values3.put(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME, newPlayerName);
        totalUpdate += database.update(RecordsContract.RecordsEntry.TABLE_NAME, values3, RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + "=?", new String[]{oldPlayerName});

        ContentValues values4 = new ContentValues();
        values4.put(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME, newPlayerName);
        totalUpdate += database.update(RecordsContract.RecordsEntry.TABLE_NAME, values4, RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + "=?", new String[]{oldPlayerName});

        return totalUpdate;
    }

    public long updateRecord(Deck oldDeck, Deck newDeck) {
        long totalUpdate = 0;

        ContentValues values1 = new ContentValues();
        values1.put(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME, newDeck.getPlayerName());
        values1.put(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK, newDeck.getDeckName());
        totalUpdate += database.update(
                RecordsContract.RecordsEntry.TABLE_NAME,
                values1,
                RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + "=? AND " + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK + "=?",
                new String[]{oldDeck.getPlayerName(), oldDeck.getDeckName()}
        );

        ContentValues values2 = new ContentValues();
        values2.put(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME, newDeck.getPlayerName());
        values2.put(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK, newDeck.getDeckName());
        totalUpdate += database.update(
                RecordsContract.RecordsEntry.TABLE_NAME,
                values2,
                RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + "=? AND " + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK + "=?",
                new String[]{oldDeck.getPlayerName(), oldDeck.getDeckName()}
        );

        ContentValues values3 = new ContentValues();
        values3.put(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME, newDeck.getPlayerName());
        values3.put(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK, newDeck.getDeckName());
        totalUpdate += database.update(
                RecordsContract.RecordsEntry.TABLE_NAME,
                values3,
                RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + "=? AND " + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK + "=?",
                new String[]{oldDeck.getPlayerName(), oldDeck.getDeckName()}
        );

        ContentValues values4 = new ContentValues();
        values4.put(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME, newDeck.getPlayerName());
        values4.put(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK, newDeck.getDeckName());
        totalUpdate += database.update(
                RecordsContract.RecordsEntry.TABLE_NAME,
                values4,
                RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + "=? AND " + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK + "=?",
                new String[]{oldDeck.getPlayerName(), oldDeck.getDeckName()}
        );

        return totalUpdate;
    }

    private Record cursorToRecord(Cursor cursor) {
        Record record = new Record();

        record.setDate(cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_DATE)));

        record.setTotalPlayers(Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_TOTAL_PLAYERS))));

        record.setFistPlace(
                new Deck(
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK))
                )
        );

        record.setSecondPlace(
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
