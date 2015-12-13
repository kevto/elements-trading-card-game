package com.elementstcg.client.handler;

import com.elementstcg.client.Account;
import com.elementstcg.shared.trait.Card;
import com.elementstcg.client.gui.Controllers.BoardController;
import com.elementstcg.client.gui.ScreenHandler;
import com.elementstcg.client.gui.ScreensFramework;
import com.elementstcg.shared.trait.ICard;
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

    private static ScreenHandler screenHandler;

    private static String ip = "192.168.56.1";
    private static String port = "8112";
    private static String name = "server";

    private static BoardController boardController;

    private static String sessionKey;

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

    public void setScreenHandler(ScreenHandler screenHandler) {
        this.screenHandler = screenHandler;
    }

    public ScreenHandler getScreenHandler() {
        return screenHandler;
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
            System.out.println("[CRITICAL] serverHandler was null, no connection");
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        try {
            IResponse response = serverHandler.login(this, username, password);
            if(response.wasSuccessful()) {
                setSessionKey(response.getMessage());
                System.out.println("Session:" + getSessionKey());
            }
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

    public boolean setupMatch(String enemyName) throws RemoteException{
        System.out.println("Setting up the board..");
        //Check if there already is an board screen (there shouldn't)
        if(screenHandler.getScreen(ScreensFramework.screenBoardID) != null) {
            screenHandler.unloadScreen(ScreensFramework.screenBoardID);
        }
        //Create a board screen
        screenHandler.loadScreen(ScreensFramework.screenBoardID, ScreensFramework.screenBoardPath);

        //Load all needed data/set all needed data
        boardController.setPlayerName(Account.getInstance().getUserName());
        boardController.setEnemyName(enemyName);

        //Display screen
        screenHandler.setScreen(ScreensFramework.screenBoardID);

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

    public void setBoardController(BoardController BoardController){
        boardController = BoardController;
    }

    public void endMatch(String message) throws RemoteException {
        //TODO Find a better way to end the match.
        System.exit(1);
    }
}
