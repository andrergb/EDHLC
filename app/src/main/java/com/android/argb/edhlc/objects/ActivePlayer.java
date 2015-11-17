package com.android.argb.edhlc.objects;

import android.app.Activity;
import android.content.SharedPreferences;


public class ActivePlayer {

    protected String playerName;
    protected String playerDeck;
    protected int playerLife;
    protected int playerEDH1;
    protected int playerEDH2;
    protected int playerEDH3;
    protected int playerEDH4;
    protected int[] playerColor;
    protected int playerTag;

    public static String PREFNAME = "PREF_EDHLC";
    public static String PName = "PNAME";
    public static String PDeck = "PDECK";
    public static String PLife = "PLIFE";
    public static String PEDH1 = "PEDH1";
    public static String PEDH2 = "PEDH2";
    public static String PEDH3 = "PEDH3";
    public static String PEDH4 = "PEDH4";
    public static String PCOLOR1 = "PCOLOR1";
    public static String PCOLOR2 = "PCOLOR2";

    public ActivePlayer() {
    }

    public ActivePlayer(String Name, String playerDeck, int life, int edh1, int edh2, int edh3, int edh4, int[] pColor, int tag) {
        this.playerName = Name;
        this.playerDeck = playerDeck;
        this.playerLife = life;
        this.playerEDH1 = edh1;
        this.playerEDH2 = edh2;
        this.playerEDH3 = edh3;
        this.playerEDH4 = edh4;
        this.playerColor = pColor;
        this.playerTag = tag;
    }

    public static void savePlayerSharedPreferences(Activity activity, ActivePlayer activePlayer) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(PREFNAME, Activity.MODE_PRIVATE).edit();
        editor.putString(activePlayer.getPlayerTag() + PName, activePlayer.getPlayerName());
        editor.putString(activePlayer.getPlayerTag() + PDeck, activePlayer.getPlayerDeck());
        editor.putInt(activePlayer.getPlayerTag() + PLife, activePlayer.getPlayerLife());
        editor.putInt(activePlayer.getPlayerTag() + PEDH1, activePlayer.getPlayerEDH1());
        editor.putInt(activePlayer.getPlayerTag() + PEDH2, activePlayer.getPlayerEDH2());
        editor.putInt(activePlayer.getPlayerTag() + PEDH3, activePlayer.getPlayerEDH3());
        editor.putInt(activePlayer.getPlayerTag() + PEDH4, activePlayer.getPlayerEDH4());
        editor.putInt(activePlayer.getPlayerTag() + PCOLOR1, activePlayer.getPlayerColor()[0]);
        editor.putInt(activePlayer.getPlayerTag() + PCOLOR2, activePlayer.getPlayerColor()[1]);
        editor.commit();
    }

    public static ActivePlayer loadPlayerSharedPreferences(Activity activity, int tag) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFNAME, Activity.MODE_PRIVATE);
        String pName = prefs.getString(tag + PName, "ActivePlayer " + tag);
        String pPlayerDeck = prefs.getString(tag + PDeck, "Deck" + tag);
        int pLife = prefs.getInt(tag + PLife, 40);
        int pEDH1 = prefs.getInt(tag + PEDH1, 0);
        int pEDH2 = prefs.getInt(tag + PEDH2, 0);
        int pEDH3 = prefs.getInt(tag + PEDH3, 0);
        int pEDH4 = prefs.getInt(tag + PEDH4, 0);
        int[] pColor = {prefs.getInt(tag + PCOLOR1, 0), prefs.getInt(tag + PCOLOR2, 0)};

        return new ActivePlayer(pName, pPlayerDeck, pLife, pEDH1, pEDH2, pEDH3, pEDH4, pColor, tag);
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getPlayerDeck() {
        return playerDeck;
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

