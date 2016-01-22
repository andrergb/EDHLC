package com.android.argb.edhlc.objects;

/**
 * -Created by agbarros on 05/11/2015.
 */
public class Deck {

    protected String playerName;
    protected String deckName;
    protected int[] deckColor;

    //TODO Deck Improvement
    //protected String general;
    //protected String type;
    //protected String deckList;

    public Deck() {
    }

    public Deck(String playerName, String deckName) {
        this.playerName = playerName;
        this.deckName = deckName;
    }

    public Deck(String playerName, String deckName, int[] deckColor) {
        this.playerName = playerName;
        this.deckName = deckName;
        this.deckColor = deckColor;
    }

    public int[] getDeckColor() {
        return deckColor;
    }

    public void setDeckColor(int[] deckColor) {
        this.deckColor = deckColor;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isEqualDeck(Deck b) {
        return (this.getPlayerName().equalsIgnoreCase(b.getPlayerName()) && this.getDeckName().equalsIgnoreCase(b.getDeckName()));
    }

}
