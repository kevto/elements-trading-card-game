package elementstcg.util;

import elementstcg.Card;
import elementstcg.Element;

/**
 * Created by Danny ter Haar on 12/10/15.
 * Fire, Earth, Water, Air, Thunder
 *
 * Earth storm and fire hear my call.
 */
public class CalculateMultiplier {




    public static double calculatedMultplier(Card Enemy_c, Card Player_c) {

        double multiplier = 0;
        double extraDamageValue = 2;
        double normalDamageValue = 1;
        double lowDamageValue;

        switch (Enemy_c.getElement()){
            case Fire:
                if (Player_c.getElement() == Element.Air){
                    multiplier = extraDamageValue;
                }
                else {
                    multiplier = normalDamageValue;
                }
                break;
            case Earth:
                if (Player_c.getElement() == Element.Water){
                    multiplier = extraDamageValue;
                }
                else {
                    multiplier = normalDamageValue;
                }
                break;
            case Water:
                if (Player_c.getElement() == Element.Fire){
                    multiplier = extraDamageValue;
                }
                else {
                    multiplier = normalDamageValue;
                }
                break;
            case Air:
                if (Player_c.getElement() == Element.Thunder){
                    multiplier = extraDamageValue;
                }
                else {
                    multiplier = normalDamageValue;
                }
                break;
            case Thunder:
                if (Player_c.getElement() == Element.Earth){
                    multiplier = extraDamageValue;
                }
                else {
                    multiplier = normalDamageValue;
                }
                break;
        }




        return multiplier;
    }

}
