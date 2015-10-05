package elementstcg;

import junit.framework.TestCase;
import org.junit.Before;

public class BoardTest extends TestCase {

    public void testSetupPlayer() throws Exception {

    }

    public void testUpdatePlayerHP() throws Exception {

    }

    public void testUpdateEnemyHP() throws Exception {


    }

    public void testIsGameOver() throws Exception {

    }

    public void testNextTurn() throws Exception {
        Board turnBoard = new Board();

        assertFalse("Turn didnt start as false", turnBoard.getTurn());
        turnBoard.nextTurn();
        assertTrue("Turn was not changed", turnBoard.getTurn());
    }

    public void testPutCardPlayer() throws Exception {

    }

    public void testPutCardEnemy() throws Exception {

    }

    public void testAttackEnemyCard() throws Exception {

    }
}