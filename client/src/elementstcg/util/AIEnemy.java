package elementstcg.util;

import elementstcg.*;
import java.util.Random;
/**
 * Created by Danny ter Haar on 19-10-15.
 * This class is a static class, where we will decide how the AI will play the game.
 * It's not really an artificial intelligence, but more of an algoritme
 *
 */
public class AIEnemy {


    public static Card getEnemyCard(Player enemyP){
        return enemyP.drawCard();
    }

    public static void setPlayer(Player p, Player enemyP){
        enemyP = p;

    }

    public static Card attackPlayer(Player enemyP){
        return enemyP.drawCard();
    }

    public static void DrawCard(Player enemyP){
        enemyP.drawCard();
    }

    public static Card GetCardFromHand(Player enemyP){
        Card card;
        Random rand = new Random();
        int cardIndex = rand.nextInt((enemyP.getHand().getAmountCards() - 0) + 1) + 0;
        card = enemyP.getHand().getCard(cardIndex);
        return card;
    }


}
