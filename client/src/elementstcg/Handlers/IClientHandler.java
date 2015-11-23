
import elementstcg.*;

/**
 * THIS INTERFACE IS USED BY THE SERVER TO COMMUNICATE WITH THE CLIENT.
 * Written by Danny ter Haar
 * Woensdag 18 november 2015
 * Oink!
 */

public interface IClientHandler{
    /**
     * Updates the player HP in the GUI at the request of the server
     */
    public void updatePlayerHP(){}

    /**
     * Updates the total amount of cards the player still has left in his/her deck at the request of the server.
     */
    public void updateDeckCount(){}

    /**
     * Places a card into the Hand of the player at the request of the server
     * @param card The card that is being added to the hand of the player.
     */
    public void addCardToHand(Card card){}

    /**
     * Places a card on the field at the request server.
     * @param card The card that is going to be placed on the field
     * @param point Represents on which spot the card is going to be placed on the field.
     */
    public void placeCard(Card card, int point){}

    /**
     * Removes a card from the field, and sends this request to the server.
     * This will only succeed if there is actually a card on that spot.
     * @param pointer Represents the spot on the field where the card is requested to be removed.
     */
    public void removeCard(int pointer){}

    /**
     * Updates the cards HP that is on the field at the request of the server
     * @param point Represents the spot on the field where the card is requested to be removed.
     * @param hp the new HP of the card.
     */
    public void setCardHp(int point, int hp){}

    /**
     * Removes a card from the player's hand at the request of the server.
     * @param index points to which Card in the array is going to be removed.
     */
    public void removeCardFromHand(int index){}

    /**
     * Returns the session key of the client to the server
     * @return The session key
     */
    public String getSessionKey(){}

    /**
     * Sets the session key of the client at the request of the server.
     * @param key The session key for the client.
     */
    public void setSessionKey(String key){}

    /**
     * Updates the enemy HP at the request of the server.
     * @param hp Represents the new HP of the enemy.
     */
    public void enemyUpdatePlayerHP(int hp){}

    /**
     * Updates the remaining cards in the Deck of the Enemy at the request of the server.
     * @param count The total amount of remaining cards of the enemy deck.
     */
    public void enemyUpdateDeckCount(int count){}

    /**
     * Adds a card to the hand of the enemy.
     * This will propably just update an int.
     */
    public void enemyAddCardToHand(){}

    /**
     * Places a card on the field of the enemy at the request of the server.
     * @param card The card that is going to be palced.
     * @param point Represents the spot or place where the card is placed.
     */
    public void enemyPlaceCard(Card card, int point){}

    /**
     * Removes a card from the enemy field.
     * @param point Represents the spot or place where the card is placed.
     */
    public void enemyRemoveCard(int point){}

    /**
     * Sets the HP of a card at the enemy field.
     * @param point Represents the place of the enemy card.
     * @param hp The new total amount of HP left on the enemy card.
     */
    public void enemySetCardHp(int point, int hp){}

    /**
     * Removes a Card from the enemy hand Represents the spot or place where the card is placed.
     */
    public void enemyRemoveCardFromHand(){}


}