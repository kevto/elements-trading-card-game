package com.elementstcg.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {

    private List<Card> cards;
    private int cardsLeft;
    public static int MAX_CARDS = 50;

    /**
     * Initialize an Deck object with an empty ArrayList<Card>()
     * @author Maarten Verboogen
     */
    public Deck(){
        cards = new ArrayList<Card>();
    }

    /**
     * Initialize an Deck object with the given ArrayList<Card>()
     * @author Maarten Verboogen
     * @param cardList the list of cards Deck should be initialized with
     */
    public Deck(ArrayList<Card> cardList) {

        if (cardList.size() > MAX_CARDS) {
            throw new IllegalArgumentException("You're trying to add too many cards.");
        }

        cards = cardList;
    }

    /**
     * Get a random Card object from the deck and remove it from the deck
     * @author Maarten Verboogen
     * @return the randomly selected Card from the deck
     */
    public Card getRandomCard() {
        if (cards.size() <= 0) {
            return null;
        }

        Random random = new Random();
        int index =  (random.nextInt(cards.size()));
        Card card = cards.get(index);
        cards.remove(card);

        return card;
    }

    /**
     * Get the size of the cards object
     * @author Maarten Verboogen
     * @return an int of the size of the cards list object
     */
    public int getAmountCards() {
        return cards.size();
    }

    /**
     * Get all the Card objects in the cards list object
     * @author Maarten Verboogen
     * @return all Card objects in Deck
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Adds the given card to the cardlist of the deck.
     * @param c the given card.
     * @return true or false, depending on if it succeeded or not.
     */
    public boolean addCard(Card c) {
        if (cards.size() < MAX_CARDS && c != null) {
            cards.add(c);
            return true;
        } else {
            return false;
        }
    }

    public void setRemainingCards(int amount){
        cardsLeft = amount;
    }


}
