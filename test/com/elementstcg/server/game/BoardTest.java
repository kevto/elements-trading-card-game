/**
 * @Author Maarten Verboogen, Rick van Duijnhoven
 */

package com.elementstcg.server.game;


import com.elementstcg.server.handlers.Session;
import com.elementstcg.shared.trait.Card;
import com.elementstcg.shared.trait.Element;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import com.elementstcg.server.game.util.CustomException.EmptyFieldException;
import com.elementstcg.server.game.util.CustomException.ExceedCapacityException;
import com.elementstcg.server.game.util.CustomException.OccupiedFieldException;

import java.util.HashMap;

/**
 * Test class made for unit tests of the class Board.
 */
public class BoardTest extends TestCase {

    Player player;
    Board board;
    Player enemy;
    Card card;
    Session s1;
    Session s2;

    @Before
    public void setUp()
    {
        //TODO: make actual sessions to test with.
        player = new Player(20, "Rick", null);
        enemy = new Player(20, "Maarten", null);
        board = new Board("Rick", null, null);
        card = new Card(Element.Air, 3, 10, "TestCard", 2);
        s1 = new Session("Rick", null, null);
    }

    @Test
    public void testUpdatePlayerOneHP(){
        board.updatePlayerOneHP(10);
        assertEquals("HP wasn't 35", board.getPlayerOne().getHp(), 35);
        board.updatePlayerOneHP(-20);
        assertEquals("HP should be 55 now.", board.getPlayerOne().getHp(), 55);
    }

    @Test
    public void testUpdateEnemyHP(){
        board.updatePlayerTwoHP(10);
        assertEquals("HP wasn't 35", board.getPlayerTwo().getHp(), 35);
        board.updatePlayerTwoHP(-20);
        assertEquals("HP should be 55 now.", board.getPlayerTwo().getHp(), 55);
    }

    @Test
    public void testIsGameOver(){
        assertFalse("Game should not be over.", board.isGameOver());
        board.updatePlayerOneHP(100);
        assertTrue("Should return true, player hp", board.isGameOver());
        board.updatePlayerOneHP(-100);
        board.updatePlayerTwoHP(100);
        assertTrue("Should be true, enemy hp", board.isGameOver());
    }

    @Test
    public void testNextTurn(){
        assertTrue("Turn didn't start as false", board.getTurn());
        board.nextTurn();
        assertFalse("Turn was not changed", board.getTurn());
    }

    @Test
    public void testPutCardPlayer(){
        try {
            board.putCardPlayer(1, card, player);
        } catch (OccupiedFieldException e) {
            System.out.println(e.toString());
        } catch (ExceedCapacityException e) {
            System.out.println(e.toString());
        }

        HashMap<Integer, Card> testCards = board.getPlayerOneField();

        if (testCards.isEmpty()) {
            fail("Card was not entered into player field");
        }

        Card testCard = testCards.get(1);

        if (testCard == null) {
            fail("No card returned");
        }

        assertEquals("The wrong card was returned.", testCard.getName(), card.getName());
    }

    @Test
    public void testPutCardEnemy() throws Exception {
        board.putCardPlayerTwo(1, card);
        HashMap<Integer, Card> testCards = board.getPlayerTwoField();

        if (testCards.isEmpty()) {
            fail("Card was not entered into player field");
        }

        Card testCard = testCards.get(1);

        if (testCard == null) {
            fail("No card returned");
        }

        assertEquals("The wrong card was returned.", testCard.getName(), card.getName());
    }

    @Test
    public void testAttackCard() throws EmptyFieldException {
        //TODO: add runnable for attackCard
        board.putCardPlayerTwo(1, card);
        Card testCard = new Card(Element.Air, 4, 10, "alakazam", 3);
        board.attackCard(player, 1, 1, null);
        HashMap<Integer, Card> testcards = board.getPlayerTwoField();

        if (testcards == null) {
            fail("No cards found");
        }

        Card c = testcards.get(1);

        if (c == null) {
            fail("No card found");
        }

        assertEquals("Attack didn't go through.", c.getHP(), 6);
    }

    @Test
    public void testConstructor()
    {
        Board b = new Board("testName", null, null);
        assertEquals("Board failed to create", b.getPlayerTwo().getName(), "enemy");

        try {
            Board failBoard = new Board("", null, null);
            fail("EnemyName in the board constructor can NOT be empty!");
        } catch(IllegalArgumentException ex) {}
    }
}