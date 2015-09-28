package elementstcg;

/**
 * Created by Mick on 28-9-2015.
 */
public class Board {

    private boolean playerTurn;

    public static int MAX_CAP_POINTS;

    /**
     * Gets the opponent and sets him up/adds him to the board.
     * @param enemyName
     */
    public void setupPlayer(String enemyName){
        //TODO : implement setupPlayer()

    }

    /**
     * Updates the players HP with the given value.
     * @param hp
     */
    public void updatePlayerHP(int hp){
        //TODO
    }

    /**
     * Updates the enemy's HP with the given value.
     * @param hp
     */
    public void updateEnemyHP(int hp){
        //TODO
    }

    /**
     * Checks if the game is over (presumably by checking if either players HP is <= 0).
     */
    public void isGameOver(){
        //TODO
    }

    /**
     * This method gets called when the turn advances to the other player.
     */
    public void nextTurn(){
        //TODO
    }

    /**
     * This method places a card from the players hand onto the board.
     * @param point Location of the card on the board where it gets placed.
     * @param card Which card gets placed.
     */
    public void putCardPlayer(int point, Card card){
        //TODO
    }

    /**
     * This method places a card from the enemys hand onto the board.
     * @param point Location on the board where the card gets placed.
     * @param card Which card gets placed.
     */
    public void putCardEnemy(int point, Card card){
        //TODO
    }

    /**
     * Method that gets called when the player attacks an enemy's card.
     * @param point Location of the card that gets attacked.
     * @param parameter Amount of damage that will be dealt to the card.
     */
    public void attackEnemyCard(int point, int parameter){
        //TODO
    }

    /**
     * Constructor with the enemy player.
     * @param enemyname
     */
    public Board(String enemyname){
        //TODO
    }

    /**
     * Constructor without the enemy player.
     */
    public Board(){
        //TODO
    }



}
