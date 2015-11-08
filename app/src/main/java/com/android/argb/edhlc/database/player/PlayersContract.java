package com.android.argb.edhlc.database.player;

import android.provider.BaseColumns;

/**
 * Created by agbarros on 05/11/2015.
 */
public class PlayersContract {

    public PlayersContract() {
    }

    public static abstract class PlayersEntry implements BaseColumns {
        public static final String TABLE_NAME = "decks";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_PLAYER_NAME = "playername";
    }
}
