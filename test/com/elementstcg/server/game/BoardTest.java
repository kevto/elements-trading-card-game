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
    Account acc;
    Account acc2;

    @Before
    public void setUp()
    {
        //TODO: make actual sessions to test with.
        enemy = new Player(20, "Maarten", null);
        acc = new Account("rickvd", "testpw", "testmail@test.com");
        acc2 = new Account("maarten", "nietmaarten", "test@maarten.com");
        s1 = new Session("Rick", null, acc);
        s2 = new Session("Maarten", null, acc2);
        board = new Board("Rick", s1, s2);
        player = board.getPlayerOne();
        card = new Card(Element.Air, 3, 10, "TestCard", 2);
        board.forcePutCardPlayer(5, card);
    }

    @Test
    public void testConstructor()
    {
        try{
            Board b = new Board(null, s1, s2);
            fail("Board without key created");
        } catch(IllegalArgumentException ex) {}
        try{
            Board b = new Board("testkey", null, s2);
            fail("Board without valid session for player one created.");
        } catch(IllegalArgumentException ex) {}
        try{
            Board b = new Board("testkey", s1, null);
            fail("Board without valid session for player two created");
        } catch(IllegalArgumentException ex) {}

        Board b = new Board("testKey", s1, s2);

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
    public void testAttackCard() throws EmptyFieldException, OccupiedFieldException, ExceedCapacityException {
        //TODO: add runnable for attackCard
        board.putCardPlayerTwo(1, card);
        Card testCard = new Card(Element.Air, 4, 10, "alakazam", 3);
        board.putCardPlayer(1, testCard, player);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("Test runnable, no worries.");
            }
        };
        board.attackCard(player, 1, 1, r);
        HashMap<Integer, Card> testCards = board.getPlayerTwoField();

        if (testCards == null) {
            fail("No cards found");
        }

        Card c = testCards.get(1);

        if (c == null) {
            fail("No card found");
        }

        assertEquals("Attack didn't go through.", c.getHP(), 6);
    }

    /**
     * Works exactly the same as GetPlayerTwoCardPoint, so only one requires testing.
     */
    @Test
    public void testGetPlayerOneCardPoint() {
        Card testCard = new Card(Element.Air, 3, 3, "asdf", 1);
        assertEquals("-1 Was not returned, even though the card shouldn't be there", -1, board.getPlayerOneCardPoint(testCard));
        assertEquals("Wrong point was returned.", 5, board.getPlayerOneCardPoint(card));
    }

    @Test
    public void testGetCurrentPlayer() {
        assertEquals("Wrong player returned, no turns have been had so should be playerOne.", player, board.getCurrentPlayer());
    }

    @Test
    public void testRemovePlayerOneCard() {
        board.removePlayerOneCard(5);
        assertNull("Returned card/number wasn't null", board.getPlayerOneField().get(5));
    }

}