package elementstcg;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by rick on 9/28/2015.
 * This testclass will be sed to test the class Card.
 */
public class CardTest extends TestCase {

    private Card card;

    @Before
    public void setUp() {
        card = new Card(Element.Fire, 10, 15, "testcard", 4);
    }

    /**
     * Tests for the getHp() method.
     * @throws Exception
     */
    @Test
    public void testGetHP() throws Exception {
        assertNotNull("Card is null", card);
        assertEquals("HP is not correct", 15, card.getHP());
        assertNotEquals("HP shouldn't be equal now", 10, card.getHP());
        assertNotNull("HP can't be null", card.getHP());
    }

    /**
     * Tests for the getName() method;
     * @throws Exception
     */
    @Test
    public void testGetName() throws Exception {
        assertEquals("Name isn't equal", "testcard", card.getName());
        assertNotNull("Name can't be null", card.getName());
    }

    /**
     * Tests for the getAttacked() method.
     * @throws Exception
     */
    @Test
    public void testGetAttacked() throws Exception {
        assertFalse("Attacked isn't false", card.getAttacked());
    }

    /**
     * Tests for the setAttacked method.
     * @throws Exception
     */
    @Test
    public void testSetAttacked() throws Exception {
        card.setAttacked(true);
        assertTrue("Attacked isn't true", card.getAttacked());
        card.setAttacked(false);
    }

    /**
     * Tests the method getElement();
     * @throws Exception
     */
    @Test
    public void testGetElement() throws Exception {
        assertEquals("Element isn't correct", Element.Fire, card.getElement());
        assertNotNull("Element can't be null", card.getElement());
    }

    /**
     * Test the getCapacityPoints() method.
     * @throws Exception
     */
    @Test
    public void testGetCapacityPoints() throws Exception {
        assertEquals("CP isn't correct", 4, card.getCapacityPoints());
        assertNotNull("CP can't be null", card.getCapacityPoints());
    }

    /**
     * Tests the modifyHp() method.
     * @throws Exception
     */
    @Test
    public void testModifyHP() throws Exception {
        Card healthCard = new Card(Element.Fire, 10, 15, "testcard", 4);

        healthCard.modifyHP(-5);
        assertEquals("HP hasn't been modified", 10, healthCard.getHP());
        healthCard.modifyHP(10);
        assertEquals("HP didn't go up", 20, healthCard.getHP());
    }

    /**
     * Tests the getAttack() method.
     * @throws Exception
     */
    @Test
    public void testGetAttack() throws Exception {
        assertNotNull("Attack cannot be null", card.getAttack());
        assertEquals("Attack is not correct", 10, card.getAttack());
    }
}