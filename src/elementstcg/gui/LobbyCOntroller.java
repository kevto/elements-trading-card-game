package elementstcg.gui;

import elementstcg.Account;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Mick on 5-10-2015.
 */
public class LobbyController implements Initializable, ControlledScreen {

    @FXML Button
            ButtonPlayVsAi;
    @FXML Button
            ButtonNormalGame;
    @FXML Label lblSearchText;
    ScreensController myController;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


    public void setScreenParent(ScreensController screenParent) {
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
