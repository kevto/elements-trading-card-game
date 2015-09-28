package elementstcg;

/**
 * Created by Maarten on 28-9-2015.
 */
public class Deck {

    private List<Card> cards;
    private final int MAX_CARDS = 0;

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
        //TODO: implement getRandomCard
        return null;
    }

    /**
     * Get the size of the cards object
     * @author Maarten Verboogen
     * @return an int of the size of the cards list object
     */
    public int getAmountCards() {
        //TODO: implement getAmountCards
        return 0;
    }

    /**
     * Get all the Card objects in the cards list object
     * @author Maarten Verboogen
     * @return all Card objects in Deck
     */
    public ArrayList<Card> getCards() {
        //TODO: implement getCards
        return null;
    }
}
