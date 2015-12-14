package com.elementstcg.server.game;

import com.elementstcg.shared.trait.Card;
import com.elementstcg.shared.trait.Element;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by Maarten on 28-9-2015.
 */
public class HandTest extends TestCase {

    private Hand hand;
    private Card cardOne;
    private Card cardTwo;
    private Card cardThree;
    private Card cardFour;
    private Card cardFive;

    @Before
    public void setUp() {
        hand = new Hand();
        cardOne = new Card(Element.Air, 1, 1,"AirCard" , 1);
        cardTwo = new Card(Element.Earth, 1, 1,"EarthCard" , 1);
        cardThree = new Card(Element.Fire, 1, 1,"FireCard" , 1);
        cardFour = new Card(Element.Thunder, 1, 1,"ThunderCard" , 1);
        cardFive = new Card(Element.Water, 1, 1,"WaterCard" , 1);
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
            fail("A card that was null has been added");
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
        int i = hand.getAmountCards();
        System.out.println(i);

        assertEquals("The wrong Card has been played", cardOne, hand.playCard(0));
        assertEquals("The card was not removed", 4, hand.getAmountCards());
        assertNotEquals("The card was not removed", cardOne, hand.getCard(0));

        assertNull("A non existing card has been played", hand.playCard(100));
        assertNull("A non existing card has been played", hand.playCard(-1));
    }
}