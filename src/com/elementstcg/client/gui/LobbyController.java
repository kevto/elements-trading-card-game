package com.elementstcg.client.gui;

import com.elementstcg.client.Account;
import com.elementstcg.client.handler.ClientHandler;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
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
    @FXML Label lblPlayerWelcome;



    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


    public void setScreenParent(ScreenHandler screenParent) {
        myController = screenParent;
        //String playerName = Account.getInstance().getUserName();
        //lblPlayerWelcome.setText("Welcome, " + playerName + "!");
    }

    /**
     * Button event occurs when the user clicks "PLAY VS AI"
     * Starts a game with the computer as opponent.
     * @param event
     */
    public void clickedVsAi(Event event) {
        //TODO implementation
    }

    /**
     * Button event occurs when the user clicks "NORMAL GAME"
     * Queues the user for searching match against other players.
     * @param event
     */
    public void clickedNormalGame(Event event) {
        //TODO implementation
        if (!searchingmatch) {
            matchmakingTimer = new Timer();
            matchmakingTimer.scheduleAtFixedRate(new UpponTask(), 1000, 1000);

            lblSearchText.setVisible(true);
            lblSearchText.setText("SEARCHING FOR GAME.... " + waitTime + " SECONDS");
            searchingmatch = true;
        }
    }

    class UpponTask extends TimerTask{
        public void run() {
            waitTime++;
            System.out.println(waitTime);
            Platform.runLater(
                    new Runnable() {
                        @Override
                        public void run() {
                            lblSearchText.setText("SEARCHING FOR GAME.... "+ waitTime + " SECONDS");
                        }
                    }
            );
        }
    }

    /*
     * This method is called uppon found match.
     */
    public void foundMatch(){
        matchmakingTimer.cancel();
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
