package com.elementstcg.server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import com.elementstcg.shared.trait.Element;

public class Deck {

    private List<Card> cards;
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

    public static ArrayList<Card> getDeckOne() {
        ArrayList<Card> cards = new ArrayList<>();

        cards.add(new Card(Element.Fire, 3, 10, "Fire 1", 1));
        cards.add(new Card(Element.Fire, 4, 10, "Fire 2", 1));
        cards.add(new Card(Element.Fire, 5, 9, "Fire 3",  2));
        cards.add(new Card(Element.Fire, 6, 8, "Fire 4",  2));
        cards.add(new Card(Element.Fire, 8, 8, "Fire 5",  2));
        cards.add(new Card(Element.Fire, 9, 9, "Fire 6",  3));
        cards.add(new Card(Element.Fire, 11, 8, "Fire 7", 3));
        cards.add(new Card(Element.Fire, 13, 8, "Fire 8", 3));
        cards.add(new Card(Element.Air, 3, 10, "Air 1", 1));
        cards.add(new Card(Element.Air, 4, 10, "Air 2", 1));
        cards.add(new Card(Element.Air, 5, 9,  "Air 3", 2));
        cards.add(new Card(Element.Air, 6, 8,  "Air 4", 2));
        cards.add(new Card(Element.Air, 8, 8,  "Air 5", 2));
        cards.add(new Card(Element.Air, 9, 9,  "Air 6", 3));
        cards.add(new Card(Element.Air, 11, 8, "Air 7", 3));
        cards.add(new Card(Element.Air, 13, 8, "Air 8", 3));
        cards.add(new Card(Element.Earth, 3, 10, "Earth 1", 1));
        cards.add(new Card(Element.Earth, 4, 10, "Earth 2", 1));
        cards.add(new Card(Element.Earth, 5, 9,  "Earth 3", 2));
        cards.add(new Card(Element.Earth, 6, 8,  "Earth 4", 2));
        cards.add(new Card(Element.Earth, 8, 8,  "Earth 5", 2));
        cards.add(new Card(Element.Earth, 9, 9,  "Earth 6", 3));
        cards.add(new Card(Element.Earth, 11, 8, "Earth 7", 3));
        cards.add(new Card(Element.Earth, 13, 8, "Earth 8", 3));
        cards.add(new Card(Element.Thunder, 3, 10, "Thunder 1", 1));
        cards.add(new Card(Element.Thunder, 4, 10, "Thunder 2", 1));
        cards.add(new Card(Element.Thunder, 5, 9,  "Thunder 3", 2));
        cards.add(new Card(Element.Thunder, 6, 8,  "Thunder 4", 2));
        cards.add(new Card(Element.Thunder, 8, 8,  "Thunder 5", 2));
        cards.add(new Card(Element.Thunder, 9, 9,  "Thunder 6", 3));
        cards.add(new Card(Element.Thunder, 11, 8, "Thunder 7", 3));
        cards.add(new Card(Element.Thunder, 13, 8, "Thunder 8", 3));
        cards.add(new Card(Element.Water, 3, 10, "Water 1", 1));
        cards.add(new Card(Element.Water, 4, 10, "Water 2", 1));
        cards.add(new Card(Element.Water, 5, 9,  "Water 3", 2));
        cards.add(new Card(Element.Water, 6, 8,  "Water 4", 2));
        cards.add(new Card(Element.Water, 8, 8,  "Water 5", 2));
        cards.add(new Card(Element.Water, 9, 9,  "Water 6", 3));
        cards.add(new Card(Element.Water, 11, 8, "Water 7", 3));
        cards.add(new Card(Element.Water, 13, 8, "Water 8", 3));

        Collections.shuffle(cards);

        return cards;
    }
}
