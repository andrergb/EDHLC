package com.android.argb.edhlc.database.record;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.argb.edhlc.objects.Deck;
import com.android.argb.edhlc.objects.Record;

import java.util.ArrayList;
import java.util.List;

/* Created by ARGB */
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

        values.put(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME, record.getFirstPlace().getDeckOwnerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK, record.getFirstPlace().getDeckName());

        values.put(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME, record.getSecondPlace().getDeckOwnerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK, record.getSecondPlace().getDeckName());

        values.put(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME, record.getThirdPlace().getDeckOwnerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK, record.getThirdPlace().getDeckName());

        values.put(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME, record.getFourthPlace().getDeckOwnerName());
        values.put(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK, record.getFourthPlace().getDeckName());

        return database.insert(RecordsContract.RecordsEntry.TABLE_NAME, null, values);
    }

    public void close() {
        recordsDBHelper.close();
    }

    public void deleteRecord(Record record) {

        String whereClause = RecordsContract.RecordsEntry.COLUMN_ID + " LIKE ? AND "
                + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + " LIKE ? AND "
                + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK + " LIKE ? AND "

                + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + " LIKE ? AND "
                + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK + " LIKE ?";

        ArrayList<String> whereArgs = new ArrayList<>();

        whereArgs.add(String.valueOf(record.getId()));

        whereArgs.add(record.getFirstPlace().getDeckOwnerName());
        whereArgs.add(record.getFirstPlace().getDeckName());

        whereArgs.add(record.getSecondPlace().getDeckOwnerName());
        whereArgs.add(record.getSecondPlace().getDeckName());

        if (record.getTotalPlayers() >= 3) {
            whereClause = whereClause + " AND "
                    + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + " LIKE ? AND "
                    + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK + " LIKE ?";
            whereArgs.add(record.getThirdPlace().getDeckOwnerName());
            whereArgs.add(record.getThirdPlace().getDeckName());
        }

        if (record.getTotalPlayers() >= 4) {
            whereClause = whereClause + " AND "
                    + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + " LIKE ? AND "
                    + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK + " LIKE ?";
            whereArgs.add(record.getFourthPlace().getDeckOwnerName());
            whereArgs.add(record.getFourthPlace().getDeckName());
        }

        database.delete(
                RecordsContract.RecordsEntry.TABLE_NAME,
                whereClause,
                whereArgs.toArray(new String[whereArgs.size()])
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

    public List<Record> getAllRecords(int totalPlayers) {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(RecordsContract.RecordsEntry.TABLE_NAME, null, RecordsContract.RecordsEntry.COLUMN_TOTAL_PLAYERS + " LIKE ?", new String[]{String.valueOf(totalPlayers)}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recordList.add(cursorToRecord(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return recordList;
    }

    public List<Record> getAllRecordsByDeck(Deck deck) {
        List<Record> recordList = new ArrayList<>();

        if (deck.getDeckName().equalsIgnoreCase("") && deck.getDeckOwnerName().equalsIgnoreCase("")) {
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
                    new String[]{deck.getDeckOwnerName(),
                            deck.getDeckName(),
                            deck.getDeckOwnerName(),
                            deck.getDeckName(),
                            deck.getDeckOwnerName(),
                            deck.getDeckName(),
                            deck.getDeckOwnerName(),
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


    public List<Record> getAllRecordsByDeck(Deck deck, int totalPlayers) {
        List<Record> recordList = new ArrayList<>();

        if (deck.getDeckName().equalsIgnoreCase("") && deck.getDeckOwnerName().equalsIgnoreCase("")) {
            recordList = getAllRecords();
        } else {
            Cursor cursor = database.query(
                    RecordsContract.RecordsEntry.TABLE_NAME,
                    null,
                    "(" +
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
                            + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK + " LIKE ?)"
                            + ") AND "
                            + RecordsContract.RecordsEntry.COLUMN_TOTAL_PLAYERS + " LIKE ?",
                    new String[]{deck.getDeckOwnerName(),
                            deck.getDeckName(),
                            deck.getDeckOwnerName(),
                            deck.getDeckName(),
                            deck.getDeckOwnerName(),
                            deck.getDeckName(),
                            deck.getDeckOwnerName(),
                            deck.getDeckName(),
                            String.valueOf(totalPlayers)},
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

    public List<Record> getAllRecordsByPlayerName(String playerName, int totalPlayers) {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = database.query(
                RecordsContract.RecordsEntry.TABLE_NAME,
                null,
                "(" + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + " LIKE ? OR "
                        + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + " LIKE ? OR "
                        + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + " LIKE ? OR "
                        + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + " LIKE ?)"
                        + " AND " + RecordsContract.RecordsEntry.COLUMN_TOTAL_PLAYERS + " LIKE ?",
                new String[]{playerName, playerName, playerName, playerName, String.valueOf(totalPlayers)},
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

    public List<Deck> getLeastUsedDecks(List<Deck> decks) {
        int min = Integer.MAX_VALUE;
        List<Deck> leastUsedDecks = new ArrayList<>();
        for (int i = 0; i < decks.size(); i++) {
            int aux = getAllRecordsByDeck(decks.get(i)).size();
            if (aux < min)
                leastUsedDecks.clear();
            if (aux <= min) {
                leastUsedDecks.add(decks.get(i));
                min = aux;
            }
        }

        return leastUsedDecks;
    }

    public List<Deck> getMostUsedDecks(List<Deck> decks) {
        int max = 0;
        List<Deck> mostUsedDecks = new ArrayList<>();
        for (int i = 0; i < decks.size(); i++) {
            int aux = getAllRecordsByDeck(decks.get(i)).size();
            if (aux > max)
                mostUsedDecks.clear();
            if (aux >= max) {
                mostUsedDecks.add(decks.get(i));
                max = aux;
            }
        }

        return mostUsedDecks;
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
        String[] selectionArgs = new String[]{deck.getDeckOwnerName(), deck.getDeckName(), String.valueOf(totalPlayers)};

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

    public List<Record> getRecordsByPosition(String playerName, int position, int totalPlayers) {
        String selection = "";
        switch (position) {
            case 1:
                selection = RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME
                        + " LIKE ?";
                break;
            case 2:
                selection = RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME
                        + " LIKE ?";
                break;
            case 3:
                selection = RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME
                        + " LIKE ?";
                break;
            case 4:
                selection = RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME
                        + " LIKE ?";
                break;
        }

        selection = selection + " AND " + RecordsContract.RecordsEntry.COLUMN_TOTAL_PLAYERS + " LIKE ?";
        String[] selectionArgs = new String[]{playerName, String.valueOf(totalPlayers)};

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

    public List<Record> getRecordsByPosition(String playerName, int position) {
        String selection = "";
        switch (position) {
            case 1:
                selection = RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME
                        + " LIKE ?";
                break;
            case 2:
                selection = RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME
                        + " LIKE ?";
                break;
            case 3:
                selection = RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME
                        + " LIKE ?";
                break;
            case 4:
                selection = RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME
                        + " LIKE ?";
                break;
        }

        String[] selectionArgs = new String[]{playerName};

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

    public void open() {
        database = recordsDBHelper.getWritableDatabase();
    }

    public long updateDeckNameRecord(Deck deck, String newDeckName) {
        long totalUpdate = 0;

        ContentValues values1 = new ContentValues();
        values1.put(RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK, newDeckName);
        totalUpdate += database.update(
                RecordsContract.RecordsEntry.TABLE_NAME,
                values1,
                RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_NAME + "=? AND " + RecordsContract.RecordsEntry.COLUMN_FIRST_PLAYER_DECK + "=?",
                new String[]{deck.getDeckOwnerName(), deck.getDeckName()}
        );

        ContentValues values2 = new ContentValues();
        values2.put(RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK, newDeckName);
        totalUpdate += database.update(
                RecordsContract.RecordsEntry.TABLE_NAME,
                values2,
                RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_NAME + "=? AND " + RecordsContract.RecordsEntry.COLUMN_SECOND_PLAYER_DECK + "=?",
                new String[]{deck.getDeckOwnerName(), deck.getDeckName()}
        );

        ContentValues values3 = new ContentValues();
        values3.put(RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK, newDeckName);
        totalUpdate += database.update(
                RecordsContract.RecordsEntry.TABLE_NAME,
                values3,
                RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_NAME + "=? AND " + RecordsContract.RecordsEntry.COLUMN_THIRD_PLAYER_DECK + "=?",
                new String[]{deck.getDeckOwnerName(), deck.getDeckName()}
        );

        ContentValues values4 = new ContentValues();
        values4.put(RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK, newDeckName);
        totalUpdate += database.update(
                RecordsContract.RecordsEntry.TABLE_NAME,
                values4,
                RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_NAME + "=? AND " + RecordsContract.RecordsEntry.COLUMN_FOURTH_PLAYER_DECK + "=?",
                new String[]{deck.getDeckOwnerName(), deck.getDeckName()}
        );

        return totalUpdate;
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

    private Record cursorToRecord(Cursor cursor) {
        Record record = new Record();

        record.setId(cursor.getInt(cursor.getColumnIndexOrThrow(RecordsContract.RecordsEntry.COLUMN_ID)));

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
