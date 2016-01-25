package com.elementstcg.client.gui.Controllers;

import com.elementstcg.client.gui.ControlledScreen;
import com.elementstcg.client.gui.ScreenHandler;
import com.elementstcg.client.handler.ClientHandler;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Mick on 5-10-2015.
 */
public class LobbyController implements Initializable, ControlledScreen {

    private ScreenHandler myController;
    private ClientHandler clientHandler = ClientHandler.getInstance();
    Timer matchmakingTimer = new Timer();
    int waitTime = 0;
    boolean searchingmatch = false;

    @FXML Button ButtonPlayVsAi;
    @FXML Button ButtonNormalGame;
    @FXML Label lblSearchText;
    @FXML Label lbGold;
    @FXML Label lbElo;
    @FXML Label lbRatio;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        String playerName = Account.getInstance().getUserName();
//        lblPlayerWelcome.setText("Welcome, " + playerName + "!");
    }


    /**
     * Request finding match to the server.
     */
    public void findMatch() {
        try {
            if (!searchingmatch) {
                matchmakingTimer = new Timer();
                matchmakingTimer.scheduleAtFixedRate(new UpponTask(), 1000, 1000);
                lblSearchText.setVisible(true);
                lblSearchText.setText("SEARCHING FOR GAME.... " + waitTime + " SECONDS");
                searchingmatch = true;
                clientHandler.getServerHandler().findMatch(clientHandler.getSessionKey());
            }
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
            ex.printStackTrace();
        }
    }

    class UpponTask extends TimerTask {
        public void run() {
            waitTime++;
            System.out.println(waitTime);
            Platform.runLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            lblSearchText.setText("SEARCHING FOR GAME.... " + waitTime + " SECONDS");
                        }
                    }
            );

            if(waitTime > 30) {
                if(matchmakingTimer != null)
                    matchmakingTimer.cancel();

                //TODO: IMPLEMENT PROPER WAY TO SET SEARCHING FALSE
                searchingmatch = false;
                waitTime = 0;
                lblSearchText.setText("");
            }
        }
    }


    public void setScreenParent(ScreenHandler screenParent) {
        myController = screenParent;
    }

    /**
     * Button event occurs when the user clicks "PLAY VS AI"
     * Starts a game with the computer as opponent.
     * @param event
     */
    public void clickedVsAi(Event event) {
        //TODO optinonal implementation
    }

    public void updateStats(){
        List<String> playerStats = clientHandler.requestPlayerStats();
        if (playerStats != null) {
            lbElo.setText(playerStats.get(0));
            lbRatio.setText(playerStats.get(1));
            lbGold.setText(playerStats.get(2));
        }

    }

    /**
     * Button event occurs when the user clicks "NORMAL GAME"
     * Queues the user for searching match against other players.
     * @param event
     */
    public void clickedNormalGame(Event event) {
        findMatch();
        lblSearchText.setVisible(true);
    }

    /**
     * This method is called when the player presses the logout button.
     */
    public void logoutButtonAction()
    {

    }

    /**
     * this method is called when the player presses the button to search for a match.
     */
    public void playMatchButtonAction()
    {

    }
}
