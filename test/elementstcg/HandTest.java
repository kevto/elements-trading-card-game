package elementstcg;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

/**
 * Created by Maarten on 28-9-2015.
 */
public class HandTest extends TestCase {

    private Hand hand;
    private Card cardOne = new Card(Element.Air, 1, 1,"AirCard" , 1);
    private Card cardTwo = new Card(Element.Earth, 1, 1,"EarthCard" , 1);
    private Card cardThree = new Card(Element.Fire, 1, 1,"FireCard" , 1);
    private Card cardFour = new Card(Element.Thunder, 1, 1,"ThunderCard" , 1);
    private Card cardFive = new Card(Element.Water, 1, 1,"WaterCard" , 1);

    @Before
    public void setup() {
        hand = new Hand();
        hand.addCard(cardOne);
        hand.addCard(cardTwo);
        hand.addCard(cardThree);
        hand.addCard(cardFour);
        hand.addCard(cardFive);
    }

    @Test
    public void testAddCard() throws Exception {
        Card card = new Card(Element.Air, 1, 1,"TEMPCARD" , 1);
        hand.addCard(card);

        assertEquals("The card was not added", 6, hand.getAmountCards());
        assertTrue("The card was not added", hand.getCard(5).equals(card));

        try{
            hand.addCard(null);
            fail("An card that was null has been added");
        }
        catch(IllegalArgumentException IAE){
        }

        try {
            hand.addCard(cardFive);
            fail("A duplicate object has been added to the list");
        }
        catch(IllegalArgumentException IAE){
        }
    }

    @Test
    public void testGetCard() throws Exception {
        assertEquals("The wrong card was returned", cardOne, hand.getCard(0));
        assertEquals("The wrong card was returned", cardFive, hand.getCard(4));
        assertNull("No card should be returned (out of index)", hand.getCard(10));
        assertNull("No card should be returned (out of index)", hand.getCard(-1));
    }

    @Test
    public void testGetAmountCards() throws Exception {
        assertEquals("The wrong amount was returned", 5, hand.getAmountCards());

        Card card = new Card(Element.Air, 1, 1,"TEMPCARD" , 1);
        hand.addCard(card);

        assertEquals("The wrong amount was returned", 6, hand.getAmountCards());

        hand.playCard(2);

        assertEquals("The wrong amount was returned", 5, hand.getAmountCards());
    }

    @Test
    public void testPlayCard() throws Exception {
        assertEquals("The wrong Card has been played", cardOne, hand.playCard(0));
        assertEquals("The card was not removed", 4, hand.getAmountCards());
        assertNotEquals("The card was not removed", cardOne, hand.getCard(0));

        assertNull("An non existing card has been played", hand.playCard(100));
        assertNull("An non existing card has been played", hand.playCard(-1));
    }
}