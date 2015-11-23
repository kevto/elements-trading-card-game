/**
 * @Author Maarten Verboogen, Rick van Duijnhoven
 */

package com.elementstcg.server.game;


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

    @Before
    public void setUp()
    {
        player = new Player(20, "Rick");
        enemy = new Player(20, "Maarten");
        board = new Board(player.getName(), enemy.getName());
        card = new Card(Element.Air, 3, 10, "TestCard", 2);
    }

    @Test
    public void testSetupPlayer() {
        Board b = new Board("Rick", "Lol");
        b.setupPlayer("Kevin");
        assertEquals("Enemy didn't match", b.getEnemy().getName(), "Kevin");
    }

    @Test
    public void testUpdatePlayerHP(){
        board.updatePlayerHP(10);
        assertEquals("HP wasn't 35", board.getPlayer().getHp(), 35);
        board.updatePlayerHP(-20);
        assertEquals("HP should be 55 now.", board.getPlayer().getHp(), 55);
    }

    @Test
    public void testUpdateEnemyHP(){
        board.updateEnemyHP(10);
        assertEquals("HP wasn't 35", board.getEnemy().getHp(), 35);
        board.updateEnemyHP(-20);
        assertEquals("HP should be 55 now.", board.getEnemy().getHp(), 55);
    }

    @Test
    public void testIsGameOver(){
        assertFalse("Game should not be over.", board.isGameOver());
        board.updatePlayerHP(100);
        assertTrue("Should return true, player hp", board.isGameOver());
        board.updatePlayerHP(-100);
        board.updateEnemyHP(100);
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
            board.putCardPlayer(1, card);
        } catch (OccupiedFieldException e) {
            System.out.println(e.toString());
        } catch (ExceedCapacityException e) {
            System.out.println(e.toString());
        }

        HashMap<Integer, Card> testCards = board.getPlayerField();

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
        board.putCardEnemy(1, card);
        HashMap<Integer, Card> testCards = board.getEnemyField();

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
        board.putCardEnemy(1, card);
        Card testCard = new Card(Element.Air, 4, 10, "alakazam", 3);
        board.attackCard(testCard, 1, board.getEnemyField(), null);
        HashMap<Integer, Card> testcards = board.getEnemyField();

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
        Board b = new Board("testName", "enemy");
        assertEquals("Board failed to create", b.getEnemy().getName(), "enemy");

        try {
            Board failBoard = new Board("", "");
            fail("EnemyName in the board constructor can NOT be empty!");
        } catch(IllegalArgumentException ex) {}
    }
}