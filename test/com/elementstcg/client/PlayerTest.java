package com.elementstcg.client;

import com.elementstcg.shared.trait.Card;
import com.elementstcg.shared.trait.Element;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Maarten on 28-9-2015.
 */
public class PlayerTest extends TestCase {

    private Player player = null;

    @Before
    public void setUp() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(Element.Air, 1, 1,"AirCard" , 1));
        cards.add(new Card(Element.Earth, 1, 1,"EarthCard" , 1));
        cards.add(new Card(Element.Fire, 1, 1,"FireCard" , 1));
        cards.add(new Card(Element.Thunder, 1, 1,"ThunderCard" , 1));
        cards.add(new Card(Element.Water, 1, 1,"WaterCard" , 1));
        Deck deck = new Deck(cards);
        player = new Player(20, "player", deck);
    }

    @Test
    public void testDrawCard() throws Exception {
        assertEquals("No card object was returned", true, player.drawCard() instanceof Card);
    }

    @Test
    public void testModifyHp() throws Exception {
        Player playerOne = new Player(20, "player");
        Player playerTwo = new Player(20, "player");
        Player playerThree = new Player(10, "player");

        playerOne.modifyHp(5);
        playerTwo.modifyHp(-5);
        playerThree.modifyHp(21);

        assertEquals("Player did not receive damage", 15, playerOne.getHp());
        assertEquals("Player was not healed", 25, playerTwo.getHp());
        assertEquals("HP should not be able to go lower than 0", 0, playerThree.getHp());
    }

    @Test
    public void testGetHp() throws Exception {
        assertEquals("Did not return the proper HP", 20, player.getHp());
    }

    @Test
    public void testGetHand() throws Exception {

        assertNotNull("Hand should not be null", player.getHand());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("The wrong username was returned", "player", player.getName());
    }

    @Test
    public void testSetDeck() throws Exception
    {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(Element.Air, 1, 1, "asdf", 1));

        Deck deck = new Deck(cards);
        Player p = new Player(10, "asdf");
        p.setDeck(deck);

        assertEquals("Wrong deck returned", deck.getAmountCards(), p.getDeck().getAmountCards());
    }
}