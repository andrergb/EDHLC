package com.android.argb.edhlc.objects;

public class Player {

    protected String playerName;
    protected String playerDate;

    public Player() {
    }

    public Player(String playerName, String playerDate) {
        this.playerName = playerName;
        this.playerDate = playerDate;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerDate() {
        return playerDate;
    }

    public void setPlayerDate(String playerDate) {
        this.playerDate = playerDate;
    }

}

