package elementstcg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {

    private List<Card> cards;
    public static int MAX_CARDS = 0;

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
        cards = cardList;
    }

    /**
     * Get a random Card object from the deck and remove it from the deck
     * @author Maarten Verboogen
     * @return the randomly selected Card from the deck
     */
    public Card getRandomCard() {
        Random random = new Random();
        int index =  (random.nextInt() * getAmountCards());
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
}
