/**
 * @Author Maarten Verboogen, Rick van Duijnhoven
 */

package com.elementstcg.client;

import com.elementstcg.client.Board;
import com.elementstcg.client.Card;
import com.elementstcg.client.Element;
import com.elementstcg.client.Player;
import com.elementstcg.client.util.CustomException.EmptyFieldException;
import com.elementstcg.client.util.CustomException.ExceedCapacityException;
import com.elementstcg.client.util.CustomException.OccupiedFieldException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

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
        board = new Board(enemy.getName());
        card = new Card(Element.Air, 3, 10, "TestCard", 2);
    }

    @Test
    public void testSetupPlayer() {
        Board b = new Board();
        b.setupPlayer("Kevin");
        assertEquals("Enemy didn't match", b.getEnemy().getName(), "Kevin");
    }

    @Test
    public void testUpdatePlayerHP(){
        board.updatePlayerHP(10);
        assertEquals("HP wasn't 10", board.getPlayer().getHp(), 10);
        board.updatePlayerHP(-20);
        assertEquals("HP should be 30 now.", board.getPlayer().getHp(), 30);
    }

    @Test
    public void testUpdateEnemyHP(){
        board.updateEnemyHP(10);
        assertEquals("HP wasn't 10", board.getEnemy().getHp(), 10);
        board.updateEnemyHP(-20);
        assertEquals("HP should be 30 now.", board.getEnemy().getHp(), 30);
    }

    @Test
    public void testIsGameOver(){
        assertFalse("Game should not be over.", board.isGameOver());
        board.updatePlayerHP(30);
        assertTrue("Should return true, player hp", board.isGameOver());
        board.updatePlayerHP(-30);
        board.updateEnemyHP(30);
        assertTrue("Should be true, enemy hp", board.isGameOver());
    }

    @Test
    public void testNextTurn(){
        Board turnBoard = new Board();

        assertFalse("Turn didn't start as false", turnBoard.getTurn());
        turnBoard.nextTurn();
        assertTrue("Turn was not changed", turnBoard.getTurn());
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
        Board b = new Board("testName");
        assertEquals("Board failed to create", b.getEnemy().getName(), "testName");

        try {
            Board failBoard = new Board("");
            fail("EnemyName in the board constructor can NOT be empty!");
        } catch(IllegalArgumentException ex) {}
    }
}