package com.android.argb.edhlc.objects;

import java.util.List;

/**
 * -Created by agbarros on 05/11/2015.
 */
public class Record {

    protected Deck emptyDeck= new Deck("", "");

    protected Deck fistPlace;
    protected Deck secondPlace;
    protected Deck thirdPlace;
    protected Deck fourthPlace;

    public Record() {
    }

    public Record(Deck fistPlace, Deck secondPlace, Deck thirdPlace, Deck fourthPlace) {
        this.fistPlace = fistPlace;
        this.secondPlace = secondPlace;
        this.thirdPlace = thirdPlace;
        this.fourthPlace = fourthPlace;

    }

    public Record(List<Deck> listDeck) {
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

    public static boolean isValidRecord(List<Deck> deckList) {
        for (int j = 0; j <= deckList.size() - 2; j++) {
            for (int i = j + 1; i <= deckList.size() - 1; i++) {
                if (deckList.get(j).isEqualDeck(deckList.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setFistPlace(Deck fistPlace) {
        this.fistPlace = fistPlace;
    }

    public void setSecondPlace(Deck secondPlace) {
        this.secondPlace = secondPlace;
    }

    public void setThirdPlace(Deck thirdPlace) {
        this.thirdPlace = thirdPlace;
    }

    public void setFourthPlace(Deck fourthPlace) {
        this.fourthPlace = fourthPlace;
    }

    public Deck getFirstPlace() {
        return fistPlace;
    }

    public Deck getSecondPlace() {
        return secondPlace;
    }

    public Deck getThirdPlace() {
        return thirdPlace;
    }

    public Deck getFourthPlace() {
        return fourthPlace;
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
