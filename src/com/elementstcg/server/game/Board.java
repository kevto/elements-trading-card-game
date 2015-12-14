package com.elementstcg.server.game;

import com.elementstcg.server.game.util.CalculateMultiplier;
import com.elementstcg.server.game.util.CustomException.ExceedCapacityException;
import com.elementstcg.server.game.util.CustomException.EmptyFieldException;
import com.elementstcg.server.game.util.CustomException.OccupiedFieldException;
import com.elementstcg.server.handlers.Session;
import com.elementstcg.shared.trait.Card;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.Map;

public class Board {

    private boolean playerOneTurn = true;
    private boolean firstTurn = true;
    public static final int MAX_CAP_POINTS = 15;
    public static final int INITIAL_HP = 45;
    private Player playerOne;
    private Player playerTwo;
    private String sessionKey;

    private HashMap<Integer, Card> playerOneField;
    private HashMap<Integer, Card> playerTwoField;

    /**
     * Constructor with the enemy player.
     * @param key is the session key of the board.
     * @param playerOne session object of the first player
     * @param playerTwo session object of the second player
     */
    public Board(String key, Session playerOne, Session playerTwo){
        sessionKey = key;
        playerOneField = new HashMap<>();
        playerTwoField = new HashMap<>();

        this.playerOne = new Player(INITIAL_HP, playerOne.getAccount().getUserName(), playerOne);
        this.playerTwo = new Player(INITIAL_HP, playerTwo.getAccount().getUserName(), playerTwo);

        this.playerOne.setDeck(new Deck(Deck.getDeckOne()));
        this.playerTwo.setDeck(new Deck(Deck.getDeckOne()));
    }


    /**
     * Updates the players HP with the given value. And checks if the game is over
     * @param hp updating the player HP by input. Can be negative as of positive value.
     */
    public void updatePlayerOneHP(int hp){
        playerOne.modifyHp(hp);

        isGameOver();
    }

    /**
     * Updates the enemy's HP with the given value. And checks if the game is over
     * @param hp updating the enemy HP by input. Can be negative as of positive value.
     */
    public void updatePlayerTwoHP(int hp){
        playerTwo.modifyHp(hp);

        isGameOver();
    }

    /**
     * Checks if the game is over (presumably by checking if either players HP is <= 0).
     * @return if the game is over.
     */
    public boolean isGameOver(){
        if(playerOne.getHp() <= 0) {
            return true;
        } else if(playerTwo.getHp() <= 0) {
            return true;
        } else
            return false;
    }

    /**
     * Get the value of the playerTurn field
     * @return value of playerTurn
     */
    public boolean getTurn() {
        return playerOneTurn;
    }

    /**
     * This method gets called when the turn advances to the other player.
     */
    public void nextTurn(){
        playerOneTurn ^= true;
        if(firstTurn) {
            firstTurn = false;
        }

        for(Map.Entry<Integer, Card> entry : playerOneField.entrySet())
            entry.getValue().setAttacked(false);

        for(Map.Entry<Integer, Card> entry : playerTwoField.entrySet())
            entry.getValue().setAttacked(false);
    }

    /**
     * This method places a card from the players hand onto the board.
     * @param point Location of the card on the board where it gets placed.
     * @param card Which card gets placed.
     */
    public void putCardPlayer(int point, Card card, Player p) throws OccupiedFieldException, ExceedCapacityException {
        int cap = 0;

        if (p == playerOne) {

            for (Card c : playerOneField.values()) {
                if (c != null) {
                    cap += c.getCapacityPoints();
                }
            }

            if(cap + card.getCapacityPoints() <= MAX_CAP_POINTS) {
                Card fieldCard = playerOneField.get(point);

                if(fieldCard == null){
                    playerOneField.put(point, card);
                } else {
                    throw new OccupiedFieldException("There is already a card on this field");
                }
            } else {
                throw new ExceedCapacityException("Card cannot be played, total capacity points exceed the maximum (" + (cap + card.getCapacityPoints()) + "/" + MAX_CAP_POINTS + ")");
            }
        }
        else
        {
            for (Card c : playerTwoField.values()) {
                if (c != null) {
                    cap += c.getCapacityPoints();
                }
            }

            if(cap + card.getCapacityPoints() <= MAX_CAP_POINTS) {
                Card fieldCard = playerTwoField.get(point);

                if(fieldCard == null){
                    playerTwoField.put(point, card);
                } else {
                    throw new OccupiedFieldException("There is already a card on this field");
                }
            } else {
                throw new ExceedCapacityException("Card cannot be played, total capacity points exceed the maximum (" + (cap + card.getCapacityPoints()) + "/" + MAX_CAP_POINTS + ")");
            }
        }


    }

    /**
     * This method places a card from the players hand onto the board. Forcefully does that.
     * @param point Location of the card on the board where it gets placed.
     * @param card Which card gets placed.
     */
    public void forcePutCardPlayer(int point, Card card) {
        if(playerOneField.containsKey(point))
            playerOneField.remove(point);
        playerOneField.put(point, card);
    }

    /**
     * This method places a card from the enemys hand onto the board.
     * @param point Location on the board where the card gets placed.
     * @param card Which card gets placed.
     */
    public void putCardPlayerTwo(int point, Card card){
        playerTwoField.put(point, card);
    }

    /**
     * Search for the point of the card in the player field.
     * @param card to search on.
     * @return point of the card that was being searched for.
     */
    public int getPlayerOneCardPoint(Card card) {
        for(Map.Entry<Integer, Card> entry : playerOneField.entrySet())
            if(entry.getValue().equals(card))
                return entry.getKey();
        return -1;
    }

    /**
     * Search for the point of the card in the enemy field.
     * @param card to search on.
     * @return point of the card that was being searched for.
     */
    public int getPlayerTwoCardPoint(Card card) {
        for(Map.Entry<Integer, Card> entry : playerTwoField.entrySet())
            if(entry.getValue().equals(card))
                return entry.getKey();
        return -1;
    }

    /**
     * Removes a player card from the field.
     * @param point key of the card to remove.
     */
    public void removePlayerOneCard(int point) {
        playerOneField.remove(point);
    }

    /**
     * Removes an enemy card from the field.
     * @param point key of the card to remove.
     */
    public void removePlayerTwoCard(int point) {
        playerTwoField.remove(point);
    }

    /**
     * * Method that gets called when the player or enemy attacks an opponent's card.
     * @param player Player that is attacking.
     * @param point Location of the card that gets attacked.
     * @param selected
     * @param removeCard Runnable that will run on the JavaFX thread to remove the card.
     * @throws EmptyFieldException happens when the selected enemy playerfield doesn't contain
     * a card in it.
     */
    public void attackCard(Player player, int selected, int point, Runnable removeCard) throws EmptyFieldException{
        HashMap<Integer, Card> defender = (player.equals(playerOne) ? this.playerTwoField : this.playerOneField);
        HashMap<Integer, Card> attacker = (player.equals(playerOne) ? this.playerOneField : this.playerTwoField);
        Card fieldCard = defender.get(point);
        Card attackCard = attacker.get(selected);

        double totalDamage = 0;
        if(fieldCard != null) {
            totalDamage = attackCard.getAttack() * CalculateMultiplier.calculatedMultplier(fieldCard, attackCard);

            // Checking if there's a defender card infront of the card that
            // the persons wishes to attack. Attack that one if true.
            //if(point < 10) {
            //    if (defenderField.containsKey(point + 10) != false) {
            //        totalDamage = card.getAttack() * CalculateMultiplier.calculatedMultplier(defenderField.get(point + 10), card);
            //        defenderField.get(point + 10).modifyHP((int) totalDamage);
            //        fieldCard = defenderField.get(point + 10);
            //    } else {
                    fieldCard.modifyHP((int) totalDamage);
            //    }
            //} else {
            //    fieldCard.modifyHP((int) totalDamage);
            //}

            attackCard.setAttacked(true);

            if(fieldCard.getHP() <= 0) {
                int keyToRemove = -1;

                for(Map.Entry<Integer, Card> entry : defender.entrySet())
                    if(entry.getValue().equals(fieldCard))
                        keyToRemove = entry.getKey();

                if(keyToRemove != -1)
                    defender.remove(keyToRemove);
                if(removeCard != null) {
                    removeCard.run();
                }
            }
        }
        else {
            throw new EmptyFieldException("The field you selected didn't contain a card");
        }
    }


    /**
     * Gets the enemy Player instance
     * @return enemy player instance.
     */
    public Player getPlayerTwo() {
        return playerTwo;
    }

    /**
     * Getter for the player Player instance
     * @return player Player instance.
     */
    public Player getPlayerOne() {
        return playerOne;
    }

    /**
     * Getter to get all the cards on the play field of the player.
     * @return List of cards of the player field
     */
    public HashMap<Integer, Card> getPlayerOneField() {
        return playerOneField;
    }

    /**
     * Getter to get all the cards on the play field of the enemy.
     * @return List of cards of the enemy field
     */
    public HashMap<Integer, Card> getPlayerTwoField() {
        return playerTwoField;
    }


    public Player getCurrentPlayer() {
        return playerOneTurn == true ? playerOne : playerTwo;
    }
    /**
     * Gets the session key.
     * @return session key in String.
     */
    public String getSessionKey() {
        return sessionKey;
    }

    /**
     * Returns boolean whether it's the board's first turn.
     * @return boolean.
     */
    public boolean isFirstTurn() {
        return firstTurn;
    }

    public Card getCard(int point) { return playerOneField.get(point); }
}
