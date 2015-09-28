package elementstcg;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Maarten on 28-9-2015.
 */
public class PlayerTest extends TestCase {

    private Player player = new Player(20, "player");

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
        playerThree.modifyHp(-20);

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
}