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

    private static Player enemyPlayer;

    public static Card getEnemyCard(){
        Hand currentHand;
        currentHand = enemyPlayer.getHand();
        int cardsHolding = currentHand.getAmountCards();
        Random rand = new Random();
        int randomNum = rand.nextInt(cardsHolding);
        Card retrievedCard = currentHand.getCard(randomNum);


        return retrievedCard;

    }

    public static void setPlayer(Player p){
        enemyPlayer = p;

    }

    public static Card attackPlayer(){
        Hand currentHand;
        currentHand = enemyPlayer.getHand();
        int cardsHolding = currentHand.getAmountCards();
        Random rand = new Random();
        int randomNum = rand.nextInt(cardsHolding);
        Card retrievedCard = currentHand.getCard(randomNum);


        return retrievedCard;
    }
}
