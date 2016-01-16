package com.elementstcg.client.shared.trait.dummy;

import com.elementstcg.client.Account;
import com.elementstcg.client.util.DialogUtility;
import com.elementstcg.shared.trait.Card;
import com.elementstcg.client.gui.Controllers.BoardController;
import com.elementstcg.client.gui.ScreenHandler;
import com.elementstcg.client.gui.ScreensFramework;
import com.elementstcg.shared.trait.ICard;
import com.elementstcg.shared.trait.IClientHandler;
import com.elementstcg.shared.trait.IResponse;
import com.elementstcg.shared.trait.IServerHandler;
import javafx.application.Platform;

import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class ClientHandlerMock extends UnicastRemoteObject implements IClientHandler {

    private static ClientHandlerMock instance;
    private static IServerHandler serverHandler;

    private static ScreenHandler screenHandler;

    private static BoardControllerMock boardController;
    private static String sessionKey;

    public ClientHandlerMock() throws RemoteException {
        setupServerConnection();
    }

    public static ClientHandlerMock getInstance() {
        if(instance == null) {
            try {
                instance = new ClientHandlerMock();
                boardController = new BoardControllerMock();
            } catch(RemoteException ex) {
                System.out.println(ex.getMessage());
                System.out.println(ex.getStackTrace());
            }
        }

        return instance;
    }

    public void setScreenHandler(ScreenHandler screenHandler) {
        this.screenHandler = screenHandler;
    }

    public ScreenHandler getScreenHandler() {
        return screenHandler;
    }

    public boolean setupServerConnection () {
        try {
            serverHandler = new ServerHandlerMock();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return true;
    }

    public IResponse loginUser(String username, String password) {
        IResponse response = null;

        try {
            response = serverHandler.login(this, username, password);
            if(response.wasSuccessful()) {
                setSessionKey(response.getMessage());
                System.out.println("Session:" + getSessionKey());
            }
            return response;
        }
        catch(RemoteException ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println(ex.getStackTrace());
        }

        return response;
    }

    public boolean registerUser(String username, String password, String email) {

        try {
            IResponse response = serverHandler.register(username, password, email);
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

    public IServerHandler getServerHandler() {
        return serverHandler;
    }

    public boolean setupMatch(String enemyName, boolean startTurn) throws RemoteException{
        System.out.println("Setting up the board..");
        //Load all needed data/set all needed data
        boardController.setPlayerName("Player");
        boardController.setEnemyName(enemyName);

        return true;
    }

    public void updatePlayerHP(int hp) throws RemoteException {
        boardController.updatePlayerHP(hp);
    }

    public void updateDeckCount(int amount) throws RemoteException {
        boardController.UpdatePlayerDeckCount(amount);
    }

    @Override
    public void addCardToHand(ICard card) throws RemoteException {
        boardController.AddCardToPlayerHand((Card) card);
    }

    @Override
    public void placeCard(ICard card, int point) throws RemoteException {
        boardController.PutCardPlayer((Card) card, point);
    }

    public void removeCard(int pointer) throws RemoteException {
        boardController.removeCardPlayer(pointer);
    }
    public void setCardHp(int point, int hp) throws RemoteException {
        boardController.SetPlayerCardHp(point, hp);

    }
    public void removeCardFromHand(int index) throws RemoteException {
        boardController.RemoveCardFromHandPlayer(index);
    }
    public String getSessionKey() throws RemoteException {
        return sessionKey;
    }
    public void setSessionKey(String key) throws RemoteException {
        sessionKey = key;
    }

    public void enemyUpdatePlayerHP(int hp) throws RemoteException {
        boardController.updateEnemyHp(hp);
    }
    public void enemyUpdateDeckCount(int count) throws RemoteException {
        boardController.updateEnemyDeckCount(count);
    }
    public void enemyAddCardToHand() throws RemoteException {
        //TODO: This is a placeholder until I have discussed this issue
        Card card = null;
        boardController.AddCardToEnemyHand();
    }


    public void enemyRemoveCard(int point) throws RemoteException {
        boardController.removeCardEnemy(point);
    }


    public void enemyPlaceCard(ICard card, int point) throws RemoteException {
        boardController.putCardEnemy((Card) card, point);
    }

    public void enemySetCardHp(int point, int hp) throws RemoteException {
        boardController.SetEnemyCardHP(point, hp);

    }
    public void enemyRemoveCardFromHand() throws RemoteException {
        boardController.RemoveCardFromEnemyHand();
    }

    public void nextTurn(Boolean isThisClientsTurn) throws RemoteException {
        boardController.setTurn(isThisClientsTurn);
    }

    public void setBoardController(BoardControllerMock BoardController){
        boardController = BoardController;
    }

    public static void AttackCard(int playerPoint, int enemyPoint){
        try {
            IResponse response = serverHandler.attackCard(sessionKey, playerPoint, enemyPoint);
            if (!response.wasSuccessful()) {
                Platform.runLater(() -> {
                    try {
                        DialogUtility.newDialog(response.getMessage());
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void AttackEnemy(int playerPoint) {
        try {
            IResponse response = serverHandler.attackEnemy(sessionKey, playerPoint);
            Platform.runLater(() -> {
                try {
                    if (!response.wasSuccessful()) {
                        DialogUtility.newDialog(response.getMessage());
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void endMatch(String message, boolean won) throws RemoteException {
        Platform.runLater(() -> {
            DialogUtility.newDialog(message);
        });

        boardController.SetGameOver(won);
    }

    public void recieveMessage(String message) throws RemoteException {
        boardController.recieveMessage(message);

    }

    public static void sendMessage(String message){
        try {
            serverHandler.sendMessage(sessionKey ,message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Will change the current screen to the lobby and create a thread to kill this board after a second
     */
    public void returnLobby() {

        boardController = null;

        //Go to lobby
        screenHandler.setScreen(ScreensFramework.screenLobbyID);

        //Create a thread that will delete this board after 1 second
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    screenHandler.unloadScreen(ScreensFramework.screenBoardID);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //Start thread
        thread.start();
    }

    public boolean ping() {
        return true;
    }

    public BoardControllerMock getBoardController() {
        return boardController;
    }
}
