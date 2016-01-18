package com.elementstcg.client.handler;

import com.elementstcg.client.Account;
import com.elementstcg.client.gui.Controllers.BoardController;
import com.elementstcg.client.gui.ScreenHandler;
import com.elementstcg.client.gui.ScreensFramework;
import com.elementstcg.client.util.DialogUtility;
import com.elementstcg.shared.trait.*;
import javafx.application.Platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class ClientHandler extends UnicastRemoteObject implements IClientHandler {

    private static ClientHandler instance;
    private static IServerHandler serverHandler;

    private static ScreenHandler screenHandler;

    private static BoardController boardController;
    private static String sessionKey;

    private static SoundHandler soundHandler = SoundHandler.getInstance();

    private ClientHandler() throws RemoteException {
        setupServerConnection();
    }

    public static ClientHandler getInstance() {
        if (instance == null) {
            try {
                instance = new ClientHandler();
            } catch (RemoteException ex) {
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

    public boolean setupServerConnection() {
        File propsFile = new File("netconf.properties");

        String ip = "";
        int port = 0;
        String name = "";

        if (!propsFile.exists()) {
            System.err.println("[Critical] netconf.properties file doesn't exist.");
            return false;
        }

        Properties props = new Properties();
        try {
            props.load(new FileInputStream(propsFile));
            ip = props.getProperty("server.ip");
            port = Integer.valueOf(props.getProperty("server.port"));
            name = props.getProperty("server.name");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!"".equals(ip) && !"".equals(port) && !"".equals(name)) {
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
        } else {
            System.out.println("[CRITICAL] ip, port or name was empty or null. Could not setup RMI connection!");
        }

        if (serverHandler != null) {
            return true;
        } else {
            System.out.println("[CRITICAL] serverHandler was null, no connection");
            return false;
        }
    }

    public IResponse loginUser(String username, String password) {
        IResponse response = null;

        try {
            response = serverHandler.login(this, username, password);
            if (response.wasSuccessful()) {
                setSessionKey(response.getMessage());
                System.out.println("Session:" + getSessionKey());
            }
            return response;
        } catch (RemoteException ex) {
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
        } catch (RemoteException ex) {
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

    public boolean setupMatch(String enemyName, boolean startTurn) throws RemoteException {
        System.out.println("Setting up the board..");
        //Check if there already is an board screen (there shouldn't)
        if (screenHandler.getScreen(ScreensFramework.screenBoardID) != null) {
            screenHandler.unloadScreen(ScreensFramework.screenBoardID);
        }
        //Create a board screen
        screenHandler.loadScreen(ScreensFramework.screenBoardID, ScreensFramework.screenBoardPath);

        //Load all needed data/set all needed data
        boardController.setPlayerName(Account.getInstance().getUserName());
        boardController.setEnemyName(enemyName);

        //Display screen
        screenHandler.setScreen(ScreensFramework.screenBoardID);

        System.out.println(Account.getInstance().getUserName());

        Platform.runLater(() -> {
            DialogUtility.newDialog((startTurn ? "You start first!" : "Your opponent starts first!"));
        });

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

    public void setBoardController(BoardController BoardController) {
        boardController = BoardController;
    }

    public static void AttackCard(int playerPoint, int enemyPoint) {
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

    public static void sendMessage(String message) {
        try {
            serverHandler.sendMessage(sessionKey, message);
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

    public void playSound(Sounds sound) {
        switch(sound) {
            case airSound:
                soundHandler.playEffect("/src/com/elementstcg/client/music/airSound.wav");
                break;
            case waterSound:
                soundHandler.playEffect("/src/com/elementstcg/client/music/waterSound.wav");
                break;
            case fireSound:
                soundHandler.playEffect("/src/com/elementstcg/client/music/fireSound.wav");
                break;
            case earthSound:
                soundHandler.playEffect("/src/com/elementstcg/client/music/earthSound.wav");
                break;
            case electricSound:
                soundHandler.playEffect("/src/com/elementstcg/client/music/electricSound.wav");
                break;
            case victorySound:
                soundHandler.playEffect("/src/com/elementstcg/client/music/victorySound.wav");
                break;
            case kill:
                soundHandler.killAllEffecten();
                break;

        }
    }

    public boolean ping() {
        return true;
    }
}
