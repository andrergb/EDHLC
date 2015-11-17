package com.android.argb.edhlc;

/**
 * -Created by agbarros on 05/11/2015.
 */
public class Constants {

    public static int initialPlayerLife = 40;
    public static int minPlayerLife = -99;
    public static int maxPlayerLife = 99;

    public static int initiPlayerEDHDamage = 0;
    public static int minEDHDamage = 0;
    public static int maxEDHDamage = 21;

    public static String PREFERENCE_NAME = "PREF_EDH_LC";
    public static String PLAYER_NAME = "PLAYER_NAME";
    public static String PLAYER_DECK = "PLAYER_DECK";
    public static String PLAYER_LIFE = "PLAYER_LIFE";
    public static String PLAYER_EDH1 = "PLAYER_EDH1";
    public static String PLAYER_EDH2 = "PLAYER_EDH2";
    public static String PLAYER_EDH3 = "PLAYER_EDH3";
    public static String PLAYER_EDH4 = "PLAYER_EDH4";
    public static String PLAYER_COLOR1 = "PLAYER_COLOR1";
    public static String PLAYER_COLOR2 = "PLAYER_COLOR2";

    public static String BROADCAST_INTENT_FILTER_NEW_GAME = "broadcast_intent_filter_new_game";
    public static String BROADCAST_INTENT_FILTER_DECK_ADDED_OR_REMOVED = "broadcast_intent_filter_deck_added_or_removed";
    public static String BROADCAST_INTENT_FILTER_PLAYER_ADDED_OR_REMOVED = "broadcast_intent_filter_player_added_or_removed";

    public static String BROADCAST_MESSAGE_NEW_GAME_OPTION = "broadcast_message_new_game_option";
    public static String BROADCAST_MESSAGE_NEW_GAME_OPTION_YES = "broadcast_message_new_game_option_yes";
    public static String BROADCAST_MESSAGE_NEW_GAME_OPTION_NO = "broadcast_message_new_game_option_no";
    public static String BROADCAST_MESSAGE_NEW_GAME_PLAYERS = "broadcast_message_new_game_players";
    public static String BROADCAST_MESSAGE_NEW_GAME_DECKS = "broadcast_message_new_game_decks";
}
