package com.android.argb.edhlc.objects;

import java.util.List;

/* Created by ARGB */
public class Record {

    protected Deck emptyDeck = new Deck("", "");

    protected int totalPlayers;
    protected Deck fistPlace;
    protected Deck secondPlace;
    protected Deck thirdPlace;
    protected Deck fourthPlace;
    private String date;

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
    }

//    public static boolean isValidRecord(List<Deck> deckList, Deck analyzed) {
//        List<Deck> aux = new ArrayList<>(deckList);
//        aux.add(analyzed);
//        for (int j = 0; j <= aux.size() - 2; j++) {
//            for (int i = j + 1; i <= aux.size() - 1; i++) {
//                if (aux.get(j).isEqualDeck(aux.get(i))) {
//                    aux.clear();
//                    return false;
//                }
//            }
//        }
//        aux.clear();
//        return true;
//    }

    public String getDate() {
        return date;
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
