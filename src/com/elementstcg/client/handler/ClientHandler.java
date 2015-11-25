package com.elementstcg.client.handler;

import com.elementstcg.client.Card;
import com.elementstcg.shared.trait.IClientHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientHandler extends UnicastRemoteObject implements IClientHandler {

    public ClientHandler() throws RemoteException {

    }

    public void updatePlayerHP(int hp) throws RemoteException {

    }

    public void updateDeckCount(int amount) throws RemoteException {

    }

    public void addCardToHand(Card card) throws RemoteException {

    }

    public void placeCard(Card card, int point) throws RemoteException {

    }

    public void removeCard(int pointer) throws RemoteException {

    }

    public void setCardHp(int point, int hp) throws RemoteException {

    }

    public void removeCardFromHand(int index) throws RemoteException {

    }

    public String getSessionKey() throws RemoteException {
        return null;
    }

    public void setSessionKey(String key) throws RemoteException {

    }

    public void enemyUpdatePlayerHP(int hp) throws RemoteException {

    }

    public void enemyUpdateDeckCount(int count) throws RemoteException {

    }

    public void enemyAddCardToHand() throws RemoteException {

    }

    public void enemyPlaceCard(Card card, int point) throws RemoteException {

    }

    public void enemyRemoveCard(int point) throws RemoteException {

    }

    public void enemySetCardHp(int point, int hp) throws RemoteException {

    }

    public void enemyRemoveCardFromHand(int index) throws RemoteException {

    }

    public void nextTurn(Boolean isThisClientsTurn) throws RemoteException {

    }
}
