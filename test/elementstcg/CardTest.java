package elementstcg;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by rick on 9/28/2015.
 * This testclass will be sed to test the class Card.
 */
public class CardTest extends TestCase {

    private Card card = new Card(Element.Fire, 10, 15, "testcard", 4);

    @Test
    public void testGetHP() throws Exception {
        assertNotNull("Card is null", card);
        assertEquals("HP is not correct", 15, card.getHP());
        assertNotEquals("HP shouldn't be equal now", 10, card.getHP());
        assertNotNull("HP can't be null", card.getHP());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("Name isn't equal", "testcard", card.getName());
        assertNotNull("Name can't be null", card.getName());
    }

    @Test
    public void testGetAttacked() throws Exception {
        assertFalse("Attacked isn't false", card.getAttacked());
    }

    @Test
    public void testSetAttacked() throws Exception {
        card.setAttacked(true);
        assertTrue("Attacked isn't true", card.getAttacked());
        card.setAttacked(false);
    }

    @Test
    public void testGetElement() throws Exception {
        assertEquals("Element isn't correct", "Fire", card.getElement());
        assertNotNull("Element can't be null", card.getElement());
    }

    @Test
    public void testGetCapacityPoints() throws Exception {
        assertEquals("CP isn't correct", 4, card.getCapacityPoints());
        assertNotNull("CP can't be null", card.getCapacityPoints());
    }

    @Test
    public void testModifyHP() throws Exception {
        card.modifyHP(5);
        assertEquals("HP hasn't been modified", 10, card.getHP());
        card.modifyHP(-10);
        assertEquals("HP didn't go up", 20, card.getHP());
    }

    @Test
    public void testGetAttack() throws Exception {
        assertNotNull("Attack cannot be null", card.getAttack());
        assertEquals("Attack is not correct", 10, card.getAttack());
    }
}