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


        switch (Enemy_c.getElement()){
            case Fire:
                if (Player_c.getElement() == Element.Air){
                    multiplier = 2;
                }
                else {
                    multiplier = 1;
                }
                break;
            case Earth:
                if (Player_c.getElement() == Element.Water){
                    multiplier = 2;
                }
                else {
                    multiplier = 1;
                }
                break;
            case Water:
                if (Player_c.getElement() == Element.Fire){
                    multiplier = 2;
                }
                else {
                    multiplier = 1;
                }
                break;
            case Air:
                if (Player_c.getElement() == Element.Thunder){
                    multiplier = 2;
                }
                else {
                    multiplier = 1;
                }
                break;
            case Thunder:
                if (Player_c.getElement() == Element.Earth){
                    multiplier = 2;
                }
                else {
                    multiplier = 1;
                }
                break;
        }




        return multiplier;
    }

}
