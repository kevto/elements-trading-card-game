package com.elementstcg.client.handler;

import com.elementstcg.client.Card;
import com.elementstcg.shared.trait.IClientHandler;
import com.elementstcg.shared.trait.IResponse;
import com.elementstcg.shared.trait.IServerHandler;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientHandler extends UnicastRemoteObject implements IClientHandler {

    private static ClientHandler instance;
    private static IServerHandler serverHandler;

    private static String ip = "145.93.174.253";
    private static String port = "8112";
    private static String name = "server";

    private ClientHandler() throws RemoteException {
        setupServerConnection();
    }

    public static ClientHandler getInstance() {
        if(instance == null) {
            try {
                instance = new ClientHandler();
            } catch(RemoteException ex) {
                System.out.println(ex.getMessage());
                System.out.println(ex.getStackTrace());
            }
        }

        return instance;
    }
    public boolean setupServerConnection () {
        if(!"".equals(ip) && !"".equals(port) && !"".equals(name) )
        {
            try {
                serverHandler = (IServerHandler) Naming.lookup("rmi://" + ip + ":" + port + "/" + name);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                ex.getMessage();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                ex.getMessage();
            } catch (NotBoundException ex) {
                ex.printStackTrace();
                ex.getMessage();
            } catch (Exception ex) {
                ex.printStackTrace();
                ex.getMessage();
            }
        }
        else {
            System.out.println("[CRITICAL] ip, port or name was empty or null. Could not setup RMI connection!");
        }

        if(serverHandler != null) {
            return true;
        }
        else {
            System.out.println("serverHandler was null, no connection");
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        try {
            IResponse response = serverHandler.login(this, username, password);
            return response.wasSuccessful();
        }
        catch(RemoteException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace());
        }

        return false;
    }

    public static IServerHandler getServerHandler() {
        return serverHandler;
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
