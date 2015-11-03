package elementstcg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maarten on 28-9-2015.
 */
public class Hand {

    private List<Card> cards;
    public static int MAX_CARDS = 0;

    /**
     * Initializes the Hand object
     * @author Maarten Verboogen
     */
    public Hand() {
        cards = new ArrayList<Card>();
    }

    /**
     * Add an card to the list cards
     * @author Maarten Verboogen
     * @param card
     */
    public void addCard(Card card) {
        if (card != null && !cards.contains(card))
        {
            cards.add(card);
        }
        else
        {
            throw new IllegalArgumentException("Card cannot be null");
        }
    }

    /**
     * Get the card at the specified index of the cards
     * Can return null if out of bounds.
     * @author Maarten Verboogen
     * @param index the index of the card
     * @return the Card object at the specified index
     */
    public Card getCard(int index){
        if ((index <= cards.size()-1) && (index >= 0)){
            return cards.get(index);
        }
        else{
            return null;
        }

    }

    /**
     * Get the size of the cards object
     * @author Maarten Verboogen
     * @return get the size of the cards object
     */
    public int getAmountCards(){ return cards.size(); }

    /**
     * Get the card at the specified index of cards, and remove it from the list
     * Can return null if index out of bounds.
     * @author Maarten Verboogen
     * @param index the index of the card
     * @return the Card object at the specified index
     */
    public Card playCard(int index) {
        if ((index <= cards.size()-1) && (index >= 0)){
            Card PlayingCard = cards.get(index);
            cards.remove(index);
            return PlayingCard;
        }
        else{
            return null;
        }
    }

    /**
     * Gets a list of cards of this hand object.
     * @return list of cards.
     */
    public List<Card> getCards() {
        return cards;
    }
}
