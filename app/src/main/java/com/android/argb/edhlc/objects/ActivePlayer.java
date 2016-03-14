package com.android.argb.edhlc.objects;

import android.app.Activity;
import android.content.SharedPreferences;

import com.android.argb.edhlc.Constants;


public class ActivePlayer {

    protected String playerName;
    protected String playerDeck;
    protected boolean playerIsAlive;
    protected int playerLife;
    protected int playerEDH1;
    protected int playerEDH2;
    protected int playerEDH3;
    protected int playerEDH4;
    protected int[] playerColor;
    protected int playerTag;

    public ActivePlayer() {
    }

    public ActivePlayer(String Name, String playerDeck, boolean playerIsAlive, int life, int edh1, int edh2, int edh3, int edh4, int[] pColor, int tag) {
        this.playerName = Name;
        this.playerDeck = playerDeck;
        this.playerIsAlive = playerIsAlive;
        this.playerLife = life;
        this.playerEDH1 = edh1;
        this.playerEDH2 = edh2;
        this.playerEDH3 = edh3;
        this.playerEDH4 = edh4;
        this.playerColor = pColor;
        this.playerTag = tag;
    }

    public static void savePlayerSharedPreferences(Activity activity, ActivePlayer activePlayer) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE).edit();
        editor.putString(activePlayer.getPlayerTag() + Constants.CURRENT_GAME_PLAYER_NAME, activePlayer.getPlayerName());
        editor.putString(activePlayer.getPlayerTag() + Constants.CURRENT_GAME_PLAYER_DECK, activePlayer.getPlayerDeck());
        editor.putBoolean(activePlayer.getPlayerTag() + Constants.CURRENT_GAME_PLAYER_IS_ALIVE, activePlayer.getPlayerIsAlive());
        editor.putInt(activePlayer.getPlayerTag() + Constants.CURRENT_GAME_PLAYER_LIFE, activePlayer.getPlayerLife());
        editor.putInt(activePlayer.getPlayerTag() + Constants.CURRENT_GAME_PLAYER_EDH1, activePlayer.getPlayerEDH1());
        editor.putInt(activePlayer.getPlayerTag() + Constants.CURRENT_GAME_PLAYER_EDH2, activePlayer.getPlayerEDH2());
        editor.putInt(activePlayer.getPlayerTag() + Constants.CURRENT_GAME_PLAYER_EDH3, activePlayer.getPlayerEDH3());
        editor.putInt(activePlayer.getPlayerTag() + Constants.CURRENT_GAME_PLAYER_EDH4, activePlayer.getPlayerEDH4());
        editor.putInt(activePlayer.getPlayerTag() + Constants.CURRENT_GAME_PLAYER_COLOR1, activePlayer.getPlayerColor()[0]);
        editor.putInt(activePlayer.getPlayerTag() + Constants.PLAYER_COLOR2, activePlayer.getPlayerColor()[1]);
        editor.commit();
    }

    public static ActivePlayer loadPlayerSharedPreferences(Activity activity, int tag) {
        SharedPreferences prefs = activity.getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE);
        String pName = prefs.getString(tag + Constants.CURRENT_GAME_PLAYER_NAME, "ActivePlayer " + tag);
        String pPlayerDeck = prefs.getString(tag + Constants.CURRENT_GAME_PLAYER_DECK, "Deck" + tag);
        boolean pIsAlive = prefs.getBoolean(tag + Constants.CURRENT_GAME_PLAYER_IS_ALIVE, true);
        int pLife = prefs.getInt(tag + Constants.CURRENT_GAME_PLAYER_LIFE, 40);
        int pEDH1 = prefs.getInt(tag + Constants.CURRENT_GAME_PLAYER_EDH1, 0);
        int pEDH2 = prefs.getInt(tag + Constants.CURRENT_GAME_PLAYER_EDH2, 0);
        int pEDH3 = prefs.getInt(tag + Constants.CURRENT_GAME_PLAYER_EDH3, 0);
        int pEDH4 = prefs.getInt(tag + Constants.CURRENT_GAME_PLAYER_EDH4, 0);
        int[] pColor = {prefs.getInt(tag + Constants.CURRENT_GAME_PLAYER_COLOR1, 0), prefs.getInt(tag + Constants.PLAYER_COLOR2, 0)};

        return new ActivePlayer(pName, pPlayerDeck, pIsAlive, pLife, pEDH1, pEDH2, pEDH3, pEDH4, pColor, tag);
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getPlayerDeck() {
        return playerDeck;
    }

    public boolean getPlayerIsAlive() {
        return playerIsAlive;
    }

    public int getPlayerLife() {
        return this.playerLife;
    }

    public int getPlayerEDH1() {
        return this.playerEDH1;
    }

    public int getPlayerEDH2() {
        return this.playerEDH2;
    }

    public int getPlayerEDH3() {
        return this.playerEDH3;
    }

    public int getPlayerEDH4() {
        return this.playerEDH4;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerDeck(String playerDeck) {
        this.playerDeck = playerDeck;
    }

    public void setPlayerIsAlive(boolean playerIsAlive) {
        this.playerIsAlive = playerIsAlive;
    }

    public void setPlayerLife(int playerLife) {
        this.playerLife = playerLife;
    }

    public void setPlayerEDH1(int playerEDH1) {
        this.playerEDH1 = playerEDH1;
    }

    public void setPlayerEDH2(int playerEDH2) {
        this.playerEDH2 = playerEDH2;
    }

    public void setPlayerEDH3(int playerEDH3) {
        this.playerEDH3 = playerEDH3;
    }

    public void setPlayerEDH4(int playerEDH4) {
        this.playerEDH4 = playerEDH4;
    }

    public void setPlayerTag(int playerTag) {
        this.playerTag = playerTag;
    }

    public int getPlayerTag() {
        return playerTag;
    }

    public int[] getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(int[] playerColor) {
        this.playerColor = playerColor;
    }
}

