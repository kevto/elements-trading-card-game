package com.elementstcg.shared.trait;

import java.lang.Boolean;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * THIS INTERFACE IS USED BY THE SERVER TO COMMUNICATE WITH THE CLIENT.
 * Written by Danny ter Haar
 * Woensdag 18 november 2015
 * Oink, said the little piggy.
 */

public interface IClientHandler extends Remote {
    /**
     * When the player is matched to another player this methoded is called to make the client ready for the match
     * @param enemyName The name of the enemyPlayer
     */
    public boolean setupMatch(String enemyName);
    /**
     * Updates the player HP in the GUI at the request of the server
     * @param hp The new HP total of the player
     */
    public void updatePlayerHP(int hp) throws RemoteException;

    /**
     * Updates the total amount of cards the player still has left in his/her deck at the request of the server.
     * @param amount The new amount of cards left of the player deck
     */
    public void updateDeckCount(int amount) throws RemoteException;

    /**
     * Places a card into the Hand of the player at the request of the server
     * @param card The card that is being added to the hand of the player.
     */
    public void addCardToHand(ICard card) throws RemoteException;

    /**
     * Places a card on the field at the request server.
     * @param card The card that is going to be placed on the field
     * @param point Represents on which spot the card is going to be placed on the field.
     */
    public void placeCard(ICard card, int point) throws RemoteException;

    /**
     * Removes a card from the field, and sends this request to the server.
     * This will only succeed if there is actually a card on that spot.
     * @param pointer Represents the spot on the field where the card is requested to be removed.
     */
    public void removeCard(int pointer) throws RemoteException;

    /**
     * Updates the cards HP that is on the field at the request of the server
     * @param point Represents the spot on the field where the card is requested to be removed.
     * @param hp the new HP of the card.
     */
    public void setCardHp(int point, int hp) throws RemoteException;

    /**
     * Removes a card from the player's hand at the request of the server.
     * @param index points to which ICard in the array is going to be removed.
     */
    public void removeCardFromHand(int index) throws RemoteException;

    /**
     * Returns the session key of the client to the server
     * @return The session key
     */
    public String getSessionKey() throws RemoteException;

    /**
     * Sets the session key of the client at the request of the server.
     * @param key The session key for the client.
     */
    public void setSessionKey(String key) throws RemoteException;

    /**
     * Updates the enemy HP at the request of the server.
     * @param hp Represents the new HP of the enemy.
     */
    public void enemyUpdatePlayerHP(int hp) throws RemoteException;

    /**
     * Updates the remaining cards in the Deck of the Enemy at the request of the server.
     * @param count The total amount of remaining cards of the enemy deck.
     */
    public void enemyUpdateDeckCount(int count) throws RemoteException;

    /**
     * Adds a card to the hand of the enemy.
     * This will propably just update an int.
     */
    public void enemyAddCardToHand() throws RemoteException;

    /**
     * Places a card on the field of the enemy at the request of the server.
     * @param card The card that is going to be palced.
     * @param point Represents the spot or place where the card is placed.
     */
    public void enemyPlaceCard(ICard card, int point) throws RemoteException;

    /**
     * Removes a card from the enemy field.
     * @param point Represents the spot or place where the card is placed.
     */
    public void enemyRemoveCard(int point) throws RemoteException;

    /**
     * Sets the HP of a card at the enemy field.
     * @param point Represents the place of the enemy card.
     * @param hp The new total amount of HP left on the enemy card.
     */
    public void enemySetCardHp(int point, int hp) throws RemoteException;

    /**
     * Removes a ICard from the enemy hand Represents the spot or place where the card is placed.
     * @param index Specifies which card to remove from the enemy hand
     */
    public void enemyRemoveCardFromHand(int index) throws RemoteException;

    /**
     * Handles the next turn in the client. At the request of the server
     * @param isThisClientsTurn Specifies wetether it's this clients turn or not
     */
    public void nextTurn(Boolean isThisClientsTurn) throws RemoteException;

    /**
     * Ends the match and returns the players to the main screen.
     * @param message shows who won.
     * @throws RemoteException
     */
    public void endMatch(String message) throws RemoteException;


}