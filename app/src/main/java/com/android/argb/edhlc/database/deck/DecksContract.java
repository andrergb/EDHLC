package com.android.argb.edhlc.database.deck;

import android.provider.BaseColumns;

/**
 * Created by agbarros on 05/11/2015.
 */
public class DecksContract {

    public DecksContract() {
    }

    public static abstract class DecksEntry implements BaseColumns {
        public static final String TABLE_NAME = "decks";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_PLAYER_NAME = "playername";
        public static final String COLUMN_DECK_NAME = "deckname";
        public static final String COLUMN_DECK_COLOR = "deckcolor";
        public static final String COLUMN_DECK_IDENTITY = "deckidentity";
    }
}
