package com.elementstcg.server.game;

import com.elementstcg.server.game.util.CalculateMultiplier;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by Danny ter Haar on 10/12/15.
 *
 * Element element, int attack, int hp, String name, int cPoints
 */
public class Multipliertest extends TestCase {

    @Test
    public void testMultiplier() throws Exception {

        Card enemyCard1 = new Card(Element.Fire, 100, 200, "Test Monster 1", 10);
        Card playerCard1 = new Card(Element.Air, 100, 200, "Test Monster 1", 10);
        Card enemyCard2 = new Card(Element.Earth, 100, 200, "Test Monster 1", 10);
        Card playerCard2 = new Card(Element.Thunder, 100, 200, "Test Monster 1", 10);
        Card enemyCard3 = new Card(Element.Water, 100, 200, "Test Monster 1", 10);
        Card playerCard3 = new Card(Element.Thunder, 100, 200, "Test Monster 1", 10);


        assertEquals(2.0, CalculateMultiplier.calculatedMultplier(enemyCard1, playerCard1));
        assertEquals(1.0, CalculateMultiplier.calculatedMultplier(enemyCard2, playerCard2));
        assertEquals(1.0, CalculateMultiplier.calculatedMultplier(enemyCard3, playerCard3));

    }
}
