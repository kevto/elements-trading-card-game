package elementstcg;

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
        //TODO : implement setupPlayer()

    }

    /**
     * Updates the players HP with the given value.
     * @param hp
     */
    public void updatePlayerHP(int hp){
        player.modifyHp(hp);

        isGameOver();
    }

    /**
     * Updates the enemy's HP with the given value.
     * @param hp
     */
    public void updateEnemyHP(int hp){
        enemy.modifyHp(hp);

        isGameOver();
    }

    /**
     * Checks if the game is over (presumably by checking if either players HP is <= 0).
     */
    public void isGameOver(){

        if(player.getHp() <= 0) {
            //TODO: Implement Player LOST
        }

        if(enemy.getHp() <= 0) {
            //TODO: Implement Player WIN
        }
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
    public void putCardPlayer(int point, Card card){
        Card fieldCard = playerField.get(point);

        if(fieldCard == null){
            playerField.add(point, card);
        }
        else {
            if(!card.getAttacked()) {
                //TODO: Show options (Switch cards/ Do nothing)
            }
            else {
                //TODO: Notify player no action avialable
            }
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
     * @param point Location of the card that gets attacked.
     * @param parameter Amount of damage that will be dealt to the card.
     */
    public void attackEnemyCard(int point, int parameter){
        Card card = enemyField.get(point);

        if(card != null) {
            card.modifyHP(parameter);
        }
    }

    /**
     * Constructor with the enemy player.
     * @param enemyname
     */
    public Board(String enemyname){
        playerField = new ArrayList<Card>();
        enemyField = new ArrayList<Card>();

        player = new Player(initialHp, account.getUserName());
        enemy = new Player(initialHp, enemyname);
    }

    /**
     * Constructor without the enemy player.
     */
    public Board(){
        playerField = new ArrayList<Card>();
        enemyField = new ArrayList<Card>();

        player = new Player(initialHp, account.getUserName());
        enemy = new Player(initialHp, "Enemy");
    }



}
