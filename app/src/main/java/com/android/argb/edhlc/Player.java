package com.android.argb.edhlc;

import android.app.Activity;
import android.content.SharedPreferences;


public class Player {

    protected String playerName;
    protected int playerLife;
    protected int playerEDH1;
    protected int playerEDH2;
    protected int playerEDH3;
    protected int playerEDH4;
    protected int[] playerColor;
    protected int playerTag;

    protected static String PREFNAME = "PREF_EDHLC";

    protected static String PName = "PNAME";
    protected static String PLife = "PLIFE";
    protected static String PEDH1 = "PEDH1";
    protected static String PEDH2 = "PEDH2";
    protected static String PEDH3 = "PEDH3";
    protected static String PEDH4 = "PEDH4";
    protected static String PCOLOR1 = "PCOLOR1";
    protected static String PCOLOR2 = "PCOLOR2";

    public Player() {
    }

    public Player(String Name, int life, int edh1, int edh2, int edh3, int edh4, int[] pColor, int tag) {
        this.playerName = Name;
        this.playerLife = life;
        this.playerEDH1 = edh1;
        this.playerEDH2 = edh2;
        this.playerEDH3 = edh3;
        this.playerEDH4 = edh4;
        this.playerColor = pColor;
        this.playerTag = tag;
    }

    public static void savePlayerSharedPreferences(Activity activity, Player player) {
        SharedPreferences.Editor editor = activity.getSharedPreferences(PREFNAME, Activity.MODE_PRIVATE).edit();
        editor.putString(player.getPlayerTag() + PName, player.getPlayerName());
        editor.putInt(player.getPlayerTag() + PLife, player.getPlayerLife());
        editor.putInt(player.getPlayerTag() + PEDH1, player.getPlayerEDH1());
        editor.putInt(player.getPlayerTag() + PEDH2, player.getPlayerEDH2());
        editor.putInt(player.getPlayerTag() + PEDH3, player.getPlayerEDH3());
        editor.putInt(player.getPlayerTag() + PEDH4, player.getPlayerEDH4());
        editor.putInt(player.getPlayerTag() + PCOLOR1, player.getPlayerColor()[0]);
        editor.putInt(player.getPlayerTag() + PCOLOR2, player.getPlayerColor()[1]);
        editor.commit();
    }

    public static Player loadPlayerSharedPreferences(Activity activity, int tag) {
        SharedPreferences prefs = activity.getSharedPreferences(PREFNAME, Activity.MODE_PRIVATE);
        String pName = prefs.getString(tag + PName, "Player " + tag);
        int pLife = prefs.getInt(tag + PLife, 40);
        int pEDH1 = prefs.getInt(tag + PEDH1, 0);
        int pEDH2 = prefs.getInt(tag + PEDH2, 0);
        int pEDH3 = prefs.getInt(tag + PEDH3, 0);
        int pEDH4 = prefs.getInt(tag + PEDH4, 0);
        int[] pColor = {prefs.getInt(tag + PCOLOR1, 0), prefs.getInt(tag + PCOLOR2, 0)};

        return new Player(pName, pLife, pEDH1, pEDH2, pEDH3, pEDH4, pColor, tag);
    }

    public String getPlayerName() {
        return this.playerName;
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

