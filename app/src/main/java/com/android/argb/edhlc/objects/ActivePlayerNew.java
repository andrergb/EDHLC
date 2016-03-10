package com.android.argb.edhlc.objects;

import android.app.Activity;
import android.content.SharedPreferences;

import com.android.argb.edhlc.Constants;


public class ActivePlayerNew {

    protected Deck deck;

    protected boolean playerIsAlive;

    protected int playerLife;

    protected int playerEDH1;
    protected int playerEDH2;
    protected int playerEDH3;
    protected int playerEDH4;

    protected int playerTag;

    public ActivePlayerNew() {
    }

    public ActivePlayerNew(Deck deck, boolean playerIsAlive, int life, int edh1, int edh2, int edh3, int edh4, int playerTag) {
        this.deck = deck;
        this.playerIsAlive = playerIsAlive;
        this.playerLife = life;
        this.playerEDH1 = edh1;
        this.playerEDH2 = edh2;
        this.playerEDH3 = edh3;
        this.playerEDH4 = edh4;
        this.playerTag = playerTag;
    }

    public static void savePlayerSharedPreferences(Activity activity, ActivePlayerNew activePlayer) {
//        SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE).edit();
//        editor.putString(activePlayer.getPlayerTag() + Constants.PLAYER_NAME, activePlayer.getPlayerName());
//        editor.putString(activePlayer.getPlayerTag() + Constants.PLAYER_DECK, activePlayer.getPlayerDeck());
//        editor.putBoolean(activePlayer.getPlayerTag() + Constants.PLAYER_IS_ALIVE, activePlayer.getPlayerIsAlive());
//        editor.putInt(activePlayer.getPlayerTag() + Constants.PLAYER_LIFE, activePlayer.getPlayerLife());
//        editor.putInt(activePlayer.getPlayerTag() + Constants.PLAYER_EDH1, activePlayer.getPlayerEDH1());
//        editor.putInt(activePlayer.getPlayerTag() + Constants.PLAYER_EDH2, activePlayer.getPlayerEDH2());
//        editor.putInt(activePlayer.getPlayerTag() + Constants.PLAYER_EDH3, activePlayer.getPlayerEDH3());
//        editor.putInt(activePlayer.getPlayerTag() + Constants.PLAYER_EDH4, activePlayer.getPlayerEDH4());
//        editor.putInt(activePlayer.getPlayerTag() + Constants.PLAYER_COLOR1, activePlayer.getPlayerColor()[0]);
//        editor.putInt(activePlayer.getPlayerTag() + Constants.PLAYER_COLOR2, activePlayer.getPlayerColor()[1]);
//        editor.commit();
    }

    public static ActivePlayerNew loadPlayerSharedPreferences(Activity activity, int tag) {
//        SharedPreferences prefs = activity.getSharedPreferences(Constants.PREFERENCE_NAME, Activity.MODE_PRIVATE);
//        String pName = prefs.getString(tag + Constants.PLAYER_NAME, "ActivePlayer " + tag);
//        String pPlayerDeck = prefs.getString(tag + Constants.PLAYER_DECK, "Deck" + tag);
//        boolean pIsAlive = prefs.getBoolean(tag + Constants.PLAYER_IS_ALIVE, true);
//        int pLife = prefs.getInt(tag + Constants.PLAYER_LIFE, 40);
//        int pEDH1 = prefs.getInt(tag + Constants.PLAYER_EDH1, 0);
//        int pEDH2 = prefs.getInt(tag + Constants.PLAYER_EDH2, 0);
//        int pEDH3 = prefs.getInt(tag + Constants.PLAYER_EDH3, 0);
//        int pEDH4 = prefs.getInt(tag + Constants.PLAYER_EDH4, 0);
//        int[] pColor = {prefs.getInt(tag + Constants.PLAYER_COLOR1, 0), prefs.getInt(tag + Constants.PLAYER_COLOR2, 0)};
//
//        return new ActivePlayerNew(pName, pPlayerDeck, pIsAlive, pLife, pEDH1, pEDH2, pEDH3, pEDH4, pColor, tag);
        return new ActivePlayerNew();
    }


    public Deck getPlayerDeck() {
        return deck;
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


    public void setPlayerDeck(Deck deck) {
        this.deck = deck;
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
}

