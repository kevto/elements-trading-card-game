package elementstcg;

import elementstcg.util.CustomException.ExceedCapacityException;
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
    Deck deck2;
    ArrayList<Card> cards;
    ArrayList<Card> cards2;
    private Card cardOne = new Card(Element.Air, 1, 1,"AirCard" , 1);
    private Card cardTwo = new Card(Element.Earth, 1, 1,"EarthCard" , 1);
    private Card cardThree = new Card(Element.Fire, 1, 1,"FireCard" , 1);
    private Card cardFour = new Card(Element.Thunder, 1, 1,"ThunderCard" , 1);
    private Card cardFive = new Card(Element.Water, 1, 1,"WaterCard" , 1);
    private Card cardSix = new Card(Element.Air, 1, 1, "testCard", 1);
    private Card cardSeven = new Card(Element.Water, 1, 1, "testCaaaaaard", 1);

    @Before
    public void setUp() {
        cards = new ArrayList<Card>();
        cards2 = new ArrayList<Card>();
        cards.add(cardOne);
        cards.add(cardTwo);
        cards.add(cardThree);
        cards.add(cardFour);
        cards.add(cardFive);
        deck = new Deck(cards);
        cards2.add(cardSix);
        cards2.add(cardSeven);
        deck2 = new Deck(cards2);
    }

    @Test
    public void testGetRandomCard() throws Exception {
        assertTrue("No card was returned", deck.getRandomCard() instanceof Card);
        assertEquals("Draw card was not removed", 4, deck.getAmountCards());

        deck.getRandomCard();
        deck.getRandomCard();
        deck.getRandomCard();
        deck.getRandomCard();

        Card c = deck.getRandomCard();
        assertNull("No more cards in deck, return should be null", c);
    }

    @Test
    public void testGetAmountCards() throws Exception {
        assertEquals("Wrong amount of cards returned", 5, deck.getAmountCards());
    }

    @Test
    public void testGetCards() throws Exception {
        assertEquals("Wrong card object returned", cards, deck.getCards());

        assertEquals("A card was illegally added", false, deck.addCard(new Card(Element.Air, 1, 1, "ILLEGAL_CARD", 1)));
    }

    @Test
    public void testAddCard() throws Exception {
        int i = deck2.getAmountCards();
        Card c = new Card(Element.Air, 1, 1, "test Card please ignore", 1);
        deck2.addCard(c);
        assertTrue("Card did not get added to deck", deck2.getAmountCards() > i);
    }
}