package com.android.argb.edhlc.objects;

/* Created by ARGB */
public class ActivePlayer {

    protected Deck deck;

    protected boolean playerIsAlive;

    protected int playerLife;

    protected int playerEDH1;
    protected int playerEDH2;
    protected int playerEDH3;
    protected int playerEDH4;

    protected int playerTag;

    public ActivePlayer() {
    }

    public ActivePlayer(Deck deck, boolean playerIsAlive, int life, int edh1, int edh2, int edh3, int edh4, int playerTag) {
        this.deck = deck;
        this.playerIsAlive = playerIsAlive;
        this.playerLife = life;
        this.playerEDH1 = edh1;
        this.playerEDH2 = edh2;
        this.playerEDH3 = edh3;
        this.playerEDH4 = edh4;
        this.playerTag = playerTag;
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

    public int getPlayerTag() {
        return playerTag;
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

}

