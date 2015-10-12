package elementstcg;

import elementstcg.util.CustomException.EmptyFieldException;
import elementstcg.util.CustomException.ExceedCapacityException;
import elementstcg.util.CustomException.OccupiedFieldException;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private int initialHp = 20;
    private boolean playerTurn;
    public static int MAX_CAP_POINTS;

    private Player player;
    private Player enemy;

    private List<Card> playerField;
    private List<Card> enemyField;

    private Account account = Account.getInstance();

    /**
     * Gets the opponent and sets him up/adds him to the board.
     * @param enemyName
     */
    public void setupPlayer(String enemyName){
        enemy = new Player(initialHp, enemyName);
    }

    /**
     * Updates the players HP with the given value. And checks if the game is over
     * @param hp
     */
    public void updatePlayerHP(int hp){
        player.modifyHp(hp);

        isGameOver();
    }

    /**
     * Updates the enemy's HP with the given value. And checks if the game is over
     * @param hp
     */
    public void updateEnemyHP(int hp){
        enemy.modifyHp(hp);

        isGameOver();
    }

    /**
     * Checks if the game is over (presumably by checking if either players HP is <= 0).
     * @return if the game is over.
     */
    public boolean isGameOver(){

        if(player.getHp() <= 0) {
            //TODO: Implement Player LOST
            return true;
        }

        if(enemy.getHp() <= 0) {
            //TODO: Implement Player WIN
            return true;
        }

        return false;
    }

    /**
     * This method gets called when the turn advances to the other player.
     */
    public void nextTurn(){
        playerTurn = !playerTurn;
    }

    /**
     * This method places a card from the players hand onto the board.
     * @param point Location of the card on the board where it gets placed.
     * @param card Which card gets placed.
     */
    public void putCardPlayer(int point, Card card) throws OccupiedFieldException, ExceedCapacityException {
        int cap = 0;

        for (Card c : playerField) {
            if(c != null) {
                cap = c.getCapacityPoints();
            }
        }

        if(cap + card.getCapacityPoints() <= MAX_CAP_POINTS){

            Card fieldCard = playerField.get(point);

            if(fieldCard == null){
                playerField.add(point, card);
            }
            else {
                throw new OccupiedFieldException("There is already a card on this field");
            }
        }
        else {
            throw new ExceedCapacityException("Card cannot be played, total capacity points exceed the maximum (" + (cap + card.getCapacityPoints()) + "/" + MAX_CAP_POINTS + ")");
        }
    }

    /**
     * This method places a card from the enemys hand onto the board.
     * @param point Location on the board where the card gets placed.
     * @param card Which card gets placed.
     */
    public void putCardEnemy(int point, Card card){
        enemyField.add(point, card);
    }

    /**
     * Method that gets called when the player attacks an enemy's card.
     * @param card The card that attacks.
     * @param point Location of the card that gets attacked.
     */
    public void attackEnemyCard(Card card, int point) throws EmptyFieldException {
        Card fieldCard = enemyField.get(point);

        if(fieldCard != null) {
            fieldCard.modifyHP(card.getAttack());
            fieldCard.setAttacked(true);

            if(fieldCard.getHP() <= 0) {
                enemyField.remove(point);

                //TODO: Implement notifying enemy to remove card from field
            }
        }
        else {
            throw new EmptyFieldException("The field you selected didn't contain a card");
        }
    }

    /**
     * Constructor with the enemy player.
     * @param enemyName
     */
    public Board(String enemyName){
        playerField = new ArrayList<Card>();
        enemyField = new ArrayList<Card>();

        player = new Player(initialHp, account.getUserName());
        setupPlayer(enemyName);
    }

    /**
     * Constructor without the enemy player.
     */
    public Board(){
        playerField = new ArrayList<Card>();
        enemyField = new ArrayList<Card>();

        player = new Player(initialHp, account.getUserName());
        setupPlayer("Enemy");
    }
}
