package com.android.argb.edhlc.objects;

/**
 * -Created by agbarros on 05/11/2015.
 */
public class Deck {

    protected String playerName;
    protected String deckName;

    //TODO Deck Improvement
    //protected String general;
    //protected String colors;
    //protected String type;
    //protected String deckList;

    public Deck() {
    }

    public Deck(String playerName, String deckName) {
        this.playerName = playerName;
        this.deckName = deckName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getDeckName() {
        return deckName;
    }

    public boolean isEqualDeck(Deck b) {
        return (this.getPlayerName().equalsIgnoreCase(b.getPlayerName()) && this.getDeckName().equalsIgnoreCase(b.getDeckName()));
    }
}
