package com.elementstcg.client.gui;

import com.elementstcg.client.handler.ClientHandler;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Mick on 5-10-2015.
 */
public class LobbyController implements Initializable, ControlledScreen {

    private ScreenHandler myController;
    private ClientHandler clientHandler = ClientHandler.getInstance();

    @FXML Button ButtonPlayVsAi;
    @FXML Button ButtonNormalGame;
    @FXML Label lblSearchText;



    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
        //TODO implementation
    }

    /**
     * Button event occurs when the user clicks "NORMAL GAME"
     * Queues the user for searching match against other players.
     * @param event
     */
    public void clickedNormalGame(Event event) {
        //TODO implementation
        lblSearchText.setVisible(true);
        lblSearchText.setText("SEARCHING FOR GAME.... XX SECONDS");
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
