package elementstcg;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by Maarten on 28-9-2015.
 */
public class DeckTest extends TestCase {

    Deck deck;
    ArrayList<Card> cards;
    private Card cardOne = new Card(Element.Air, 1, 1,"AirCard" , 1);
    private Card cardTwo = new Card(Element.Earth, 1, 1,"EarthCard" , 1);
    private Card cardThree = new Card(Element.Fire, 1, 1,"FireCard" , 1);
    private Card cardFour = new Card(Element.Thunder, 1, 1,"ThunderCard" , 1);
    private Card cardFive = new Card(Element.Water, 1, 1,"WaterCard" , 1);

    @Before
    public void setup() {
        cards = new ArrayList<Card>();
        cards.add(cardOne);
        cards.add(cardTwo);
        cards.add(cardThree);
        cards.add(cardFour);
        cards.add(cardFive);
        deck = new Deck(cards);
    }

    @Test
    public void testGetRandomCard() throws Exception {
        assertTrue("No card was returned", deck.getRandomCard() instanceof Card);
        assertEquals("Draw card was not removed", 4, deck.getAmountCards());

        deck.getRandomCard();
        deck.getRandomCard();
        deck.getRandomCard();
        deck.getRandomCard();

        assertNull("No more cards in deck, return should be null", deck.getRandomCard());
    }

    @Test
    public void testGetAmountCards() throws Exception {
        assertEquals("Wrong amount of cards returned", 5, deck.getAmountCards());
    }

    @Test
    public void testGetCards() throws Exception {
        assertEquals("Wrong card object returned", cards, deck.getCards());

        List<Card> tempList = deck.getCards();
        tempList.add(new Card(Element.Air, 1, 1,"ILLEGAL_CARD" , 1));

        assertEquals("An card was illegaly added", 5, deck.getAmountCards());
    }
}