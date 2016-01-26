package com.elementstcg.server.game.util;

import com.elementstcg.shared.trait.Card;
import com.elementstcg.shared.trait.Element;

/**
 * Created by Danny ter Haar on 12/10/15.
 * Fire, Earth, Water, Air, Thunder
 *
 * Earth storm and fire hear my call.
 */
public class CalculateMultiplier {

    public static double calculatedMultplier(Card Enemy_c, Card Player_c) {

        double extraDamageValue = 2;
        double normalDamageValue = 1;

        switch (Player_c.getElement()){
            case Fire:
                if (Enemy_c.getElement() == Element.Air){
                    return extraDamageValue;
                }
                break;
            case Earth:
                if (Enemy_c.getElement() == Element.Water){
                    return extraDamageValue;
                }
                break;
            case Water:
                if (Enemy_c.getElement() == Element.Fire){
                    return extraDamageValue;
                }
                break;
            case Air:
                if (Enemy_c.getElement() == Element.Thunder){
                    return extraDamageValue;
                }
                break;
            case Thunder:
                if (Enemy_c.getElement() == Element.Earth){
                    return extraDamageValue;
                }
                break;
        }
        return normalDamageValue;
    }
}
