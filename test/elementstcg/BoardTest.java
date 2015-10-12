/**
 * @Author Maarten Verboogen, Rick van Duijnhoven
 */

package elementstcg;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

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
        assertEquals("HP wasn't 10", board.getPlayer().getHp(), 10);
        board.updateEnemyHP(-20);
        assertEquals("HP should be 30 now.", board.getPlayer().getHp(), 30);

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

    }

    @Test
    public void testPutCardEnemy() throws Exception {

    }

    @Test
    public void testAttackEnemyCard() throws Exception {

    }
}