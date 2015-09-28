package elementstcg;

/**
 * Created by Maarten on 28-9-2015.
 */
public class Hand {

    private List<Card> cards;

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
        //TODO: implement addCard
    }

    /**
     * Get the card at the specified index of the cards
     * @author Maarten Verboogen
     * @param index the index of the card
     * @return the Card object at the specified index
     */
    public Card getCard(int index){
        //TODO: implement getCard methode
        return null;
    }

    /**
     * Get the size of the cards object
     * @author Maarten Verboogen
     * @return get the size of the cards object
     */
    public int getAmountCards(){
        //TODO: implement getAmountCards
        return 0;
    }

    /**
     * Get the card at the specified index of cards, and remove it from the list
     * @author Maarten Verboogen
     * @param index the index of the card
     * @return the Card object at the specified index
     */
    public Card playCard(int index) {
        //TODO: implement playCard methode
        return null;
    }
}
