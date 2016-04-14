package com.android.argb.edhlc.objects;

import java.util.List;

/* Created by ARGB */
public class Record {

    protected Deck emptyDeck = new Deck("", "");

    private int id;
    protected int totalPlayers;
    protected Deck fistPlace;
    protected Deck secondPlace;
    protected Deck thirdPlace;
    protected Deck fourthPlace;
    private String date;
    private boolean selected;

    public Record() {
    }

    public Record(List<Deck> listDeck, String date) {
        this.date = date;

        this.totalPlayers = listDeck.size();

        this.fistPlace = emptyDeck;
        this.secondPlace = emptyDeck;
        this.thirdPlace = emptyDeck;
        this.fourthPlace = emptyDeck;

        if (listDeck.size() >= 1)
            this.fistPlace = listDeck.get(0);
        if (listDeck.size() >= 2)
            this.secondPlace = listDeck.get(1);
        if (listDeck.size() >= 3)
            this.thirdPlace = listDeck.get(2);
        if (listDeck.size() == 4)
            this.fourthPlace = listDeck.get(3);

        this.selected = false;
    }

//    public Record(List<Deck> listDeck, String date, boolean isSelected) {
//        this.date = date;
//
//        this.totalPlayers = listDeck.size();
//
//        this.fistPlace = emptyDeck;
//        this.secondPlace = emptyDeck;
//        this.thirdPlace = emptyDeck;
//        this.fourthPlace = emptyDeck;
//
//        if (listDeck.size() >= 1)
//            this.fistPlace = listDeck.get(0);
//        if (listDeck.size() >= 2)
//            this.secondPlace = listDeck.get(1);
//        if (listDeck.size() >= 3)
//            this.thirdPlace = listDeck.get(2);
//        if (listDeck.size() == 4)
//            this.fourthPlace = listDeck.get(3);
//
//        this.selected = isSelected;
//    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Deck getFirstPlace() {
        return fistPlace;
    }

    public Deck getFourthPlace() {
        return fourthPlace;
    }

    public void setFourthPlace(Deck fourthPlace) {
        this.fourthPlace = fourthPlace;
    }

    public Deck getSecondPlace() {
        return secondPlace;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSecondPlace(Deck secondPlace) {
        this.secondPlace = secondPlace;
    }

    public Deck getThirdPlace() {
        return thirdPlace;
    }

    public void setThirdPlace(Deck thirdPlace) {
        this.thirdPlace = thirdPlace;
    }

    public int getTotalPlayers() {
        return this.totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setFistPlace(Deck fistPlace) {
        this.fistPlace = fistPlace;
    }

    public int size() {
        if (this.fistPlace.isEqualDeck(emptyDeck))
            return 0;
        else if (this.secondPlace.isEqualDeck(emptyDeck))
            return 1;
        else if (this.thirdPlace.isEqualDeck(emptyDeck))
            return 2;
        else if (this.fourthPlace.isEqualDeck(emptyDeck))
            return 3;
        else
            return 4;
    }
}
