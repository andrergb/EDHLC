package com.android.argb.edhlc.objects;

/* Created by ARGB */
public class Deck {

    private String deckOwnerName;
    private String deckName;
    private int[] deckShieldColor;
    private String deckIdentity;
    private String deckCreationDate;

    public Deck() {
    }

    public Deck(String deckOwnerName, String deckName) {
        this.deckOwnerName = deckOwnerName;
        this.deckName = deckName;
    }

    public Deck(String deckOwnerName, String deckName, int[] deckShieldColor) {
        this.deckOwnerName = deckOwnerName;
        this.deckName = deckName;
        this.deckShieldColor = deckShieldColor;
    }

    public Deck(String deckOwnerName, String deckName, int[] deckShieldColor, String deckIdentity, String deckCreationDate) {
        this.deckOwnerName = deckOwnerName;
        this.deckName = deckName;
        this.deckShieldColor = deckShieldColor;
        this.deckIdentity = deckIdentity;
        this.deckCreationDate = deckCreationDate;
    }

    public String getDeckCreationDate() {
        return deckCreationDate;
    }

    public void setDeckCreationDate(String deckCreationDate) {
        this.deckCreationDate = deckCreationDate;
    }

    public String getDeckIdentity() {
        return deckIdentity;
    }

    public void setDeckIdentity(String identity) {
        this.deckIdentity = identity;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getDeckOwnerName() {
        return deckOwnerName;
    }

    public void setDeckOwnerName(String deckOwnerName) {
        this.deckOwnerName = deckOwnerName;
    }

    public int[] getDeckShieldColor() {
        return deckShieldColor;
    }

    public void setDeckShieldColor(int[] deckShieldColor) {
        this.deckShieldColor = deckShieldColor;
    }

    public boolean isEqualDeck(Deck b) {
        return (this.getDeckOwnerName().equalsIgnoreCase(b.getDeckOwnerName()) && this.getDeckName().equalsIgnoreCase(b.getDeckName()));
    }

}
