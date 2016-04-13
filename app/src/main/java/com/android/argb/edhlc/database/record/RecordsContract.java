package com.android.argb.edhlc.database.record;

import android.provider.BaseColumns;

/* Created by ARGB */
public class RecordsContract {

    public RecordsContract() {
    }

    public static abstract class RecordsEntry implements BaseColumns {
        public static final String TABLE_NAME = "records";
        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_DATE = "logdate";

        public static final String COLUMN_TOTAL_PLAYERS = "totalplayers";

        public static final String COLUMN_FIRST_PLAYER_NAME = "firstplayername";
        public static final String COLUMN_FIRST_PLAYER_DECK = "firstplayerdeck";

        public static final String COLUMN_SECOND_PLAYER_NAME = "secondplayername";
        public static final String COLUMN_SECOND_PLAYER_DECK = "secondplayerdeck";

        public static final String COLUMN_THIRD_PLAYER_NAME = "thirdplayername";
        public static final String COLUMN_THIRD_PLAYER_DECK = "thirdplayerdeck";

        public static final String COLUMN_FOURTH_PLAYER_NAME = "fourthplayername";
        public static final String COLUMN_FOURTH_PLAYER_DECK = "fourthplayerdeck";
    }
}
