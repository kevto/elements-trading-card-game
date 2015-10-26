package elementstcg;

import elementstcg.gui.BoardController;
import elementstcg.util.AIEnemy;
import elementstcg.util.CalculateMultiplier;
import elementstcg.util.CustomException.EmptyFieldException;
import elementstcg.util.CustomException.ExceedCapacityException;
import elementstcg.util.CustomException.OccupiedFieldException;

import javax.lang.model.type.NullType;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class Board {

    private int initialHp = 20;
    private boolean playerTurn;
    public static int MAX_CAP_POINTS;
    private Player player;
    private Player enemy;

    private HashMap<Integer, Card> playerField;
    private HashMap<Integer, Card> enemyField;

    /**
     * Constructor with the enemy player.
     * @param enemyName name of the enemy player.
     */
    public Board(String enemyName){
        playerField = new HashMap<>();
        enemyField = new HashMap<>();

        if (enemyName == null || enemyName == "")
        {
            throw new IllegalArgumentException("enemyName cannot be empty in this constructor.");
        }

        player = new Player(initialHp, (Account.getInstance() != null ? Account.getInstance().getUserName() : "Player"));
        setupPlayer(enemyName);
    }

    /**
     * Constructor without the enemy player.
     */
    public Board(){
        playerField = new HashMap<>();
        enemyField = new HashMap<>();

        player = new Player(initialHp, (Account.getInstance() != null ? Account.getInstance().getUserName() : "Player"));
        setupPlayer("Enemy");
    }

    /**
     * Gets the opponent and sets him up/adds him to the board.
     * @param enemyName name of the enemy player. Enemy name may not be null nor empty
     */
    public void setupPlayer(String enemyName){
        enemy = new Player(initialHp, enemyName);
    }

    /**
     * Updates the players HP with the given value. And checks if the game is over
     * @param hp updating the player HP by input. Can be negative as of positive value.
     */
    public void updatePlayerHP(int hp){
        player.modifyHp(hp);

        isGameOver();
    }

    /**
     * Updates the enemy's HP with the given value. And checks if the game is over
     * @param hp updating the enemy HP by input. Can be negative as of positive value.
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
            return true;
        } else if(enemy.getHp() <= 0) {
            return true;
        } else
            return false;
    }

    /**
     * Get the value of the playerTurn field
     * @return value of playerTurn
     */
    public boolean getTurn() {
        return playerTurn;
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

        for (Card c : playerField.values()) {
            if(c != null) {
                cap += c.getCapacityPoints();
            }
        }

        if(cap + card.getCapacityPoints() <= MAX_CAP_POINTS) {
            Card fieldCard = playerField.get(point);

            if(fieldCard == null){
                playerField.put(point, card);
            } else {
                throw new OccupiedFieldException("There is already a card on this field");
            }
        } else {
            throw new ExceedCapacityException("Card cannot be played, total capacity points exceed the maximum (" + (cap + card.getCapacityPoints()) + "/" + MAX_CAP_POINTS + ")");
        }
    }

    /**
     * This method places a card from the enemys hand onto the board.
     * @param point Location on the board where the card gets placed.
     * @param card Which card gets placed.
     */
    public void putCardEnemy(int point, Card card){
        enemyField.put(point, card);
    }

    /**
     * * Method that gets called when the player or enemy attacks an opponent's card.
     * @param card The card that attacks.
     * @param point Location of the card that gets attacked.
     * @param defenderField Dictionary of cards that are on the player field will be the defender.
     * @param removeCard Runnable that will run on the JavaFX thread to remove the card.
     * @throws EmptyFieldException happens when the selected enemy playerfield doesn't contain
     * a card in it.
     */
    public void attackCard(Card card, int point, HashMap<Integer, Card> defenderField, Runnable removeCard) throws EmptyFieldException{
        Card fieldCard = defenderField.get(point);

        double totalDamage = 0;
        if(fieldCard != null) {
            totalDamage = card.getAttack() * CalculateMultiplier.calculatedMultplier(fieldCard, card);

            // Checking if there's a defender card infront of the card that
            // the persons whishes to attack. Attack that one if true.
            if(point < 10) {
                if (defenderField.containsKey(point + 10) != false) {
                    totalDamage = card.getAttack() * CalculateMultiplier.calculatedMultplier(defenderField.get(point + 10), card);
                    defenderField.get(point + 10).modifyHP((int) totalDamage);
                    fieldCard = defenderField.get(point + 10);
                } else {
                    fieldCard.modifyHP((int) totalDamage);
                }
            } else {
                fieldCard.modifyHP((int) totalDamage);
            }

            // TODO Need to find a way to set all the cards to -> setAttacked(false)
            card.setAttacked(true);

            if(fieldCard.getHP() <= 0) {
                // TODO Not sure whether this works or not. Requires some testing to be done.
                for(Map.Entry<Integer, Card> entry : defenderField.entrySet())
                    if(entry.getValue().equals(fieldCard))
                        defenderField.remove(entry.getKey());

                //TODO: Implement notifying enemy to remove card from field (MEANT FOR BoardController)
                if(removeCard != null)
                    removeCard.run();
            }
        }
        else {
            throw new EmptyFieldException("The field you selected didn't contain a card");
        }
    }

    /**
     * Generates attack points for the enemy.
     * @param attackingAttackCards boolean whether the AI attacks a player card or the player self.
     * @return generated attack points.
     */
    private int generateAttackPointForAI(Boolean attackingAttackCards){
        int generatedPoint = 0;
        Random rand = new Random();
        Card fieldCard = null;

        while (fieldCard == null){
            //Bron: http://www.mkyong.com/java/java-generate-random-integers-in-a-range/
            if (attackingAttackCards == true) {
                generatedPoint = rand.nextInt((10 - 16) + 1) + 16;
            } else {
                generatedPoint = rand.nextInt((0 - 6) + 1) + 6;
            }

            fieldCard = enemyField.get(generatedPoint);
        }
        return generatedPoint;
    }

    /**
     * This either will redraw a card from the field, or attack an enemy player
     * There is 35% percent chance the enemy will withdraw a card.
     * If the enemy player cannot find a card to withdraw the AI will
     * attack an enemy player instead.
     */
    public void doMove(){
        Random random = new Random();
        int withdrawOrAttack;
        withdrawOrAttack = random.nextInt((100 - 0) + 1) + 0;
        if (withdrawOrAttack <= 35){
            Collection<Card> possibleCards = enemyField.values();
            int randomCardInt = random.nextInt((possibleCards.size() - 0) + 1) + 0;
            Card chosenCard = (Card) possibleCards.toArray()[randomCardInt];
            int attempts = 0;
            while (chosenCard.getAttacked() == true || attempts <= 10){
                if (chosenCard.getAttacked() != true){
                    enemyField.remove(chosenCard);
                    enemy.getHand().addCard(chosenCard);
                }
                attempts++;
            }
            if (attempts <= 10){
                AttackPlayerCard();
            }
        } else {
            AttackPlayerCard();
        }

    }

    /**
     * This will randomly choose a player card to attack with a randomly chosen card
     */
    public void AttackPlayerCard(){
        Random random = new Random();
        Card retrievedCard = AIEnemy.attackPlayer();
        Card fieldCard = null;
        int pointer = 0;
        while (fieldCard == null) {

            pointer = generateAttackPointForAI(random.nextBoolean());
            fieldCard = playerField.get(pointer);
        }
        try {
            attackCard(retrievedCard, pointer, this.playerField, null);
        } catch (EmptyFieldException e) {
            e.printStackTrace();
        }
    }

    public void PlaceAICardOnField(){
        Card card = AIEnemy.GetCardFromHand();
        Random rand = new Random();
        int cap = 0;
        boolean canPlaceCard = false;
        int point = 0;
        point = rand.nextInt((20 - 0) + 1) + 1;

        for (Card c : enemyField.values()) {
            if(c != null) {
                cap += c.getCapacityPoints();
            }
        }

        if(cap + card.getCapacityPoints() <= MAX_CAP_POINTS) {

            Card fieldCard = enemyField.get(point);
            while (canPlaceCard == false) {
                if (fieldCard == null) {
                    enemyField.put(point, card);
                    canPlaceCard = true;
                }
                else{
                    point = rand.nextInt((20 - 0) + 1) + 1;
                }
            }
        }
    }
    /**
     * Gets the enemy Player instance
     * @return enemy player instance.
     */
    public Player getEnemy() {
        return enemy;
    }

    /**
     * Getter for the player Player instance
     * @return player Player instance.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter to get all the cards on the play field of the player.
     * @return List of cards of the player field
     */
    public HashMap<Integer, Card> getPlayerField() {
        return playerField;
    }

    /**
     * Getter to get all the cards on the play field of the enemy.
     * @return List of cards of the enemy field
     */
    public HashMap<Integer, Card> getEnemyField() {
        return enemyField;
    }
}
