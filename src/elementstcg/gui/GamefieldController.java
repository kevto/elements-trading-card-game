package elementstcg.gui;

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
public class GamefieldController implements Initializable, ControlledScreen {

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
    public void ClickedVsAi(Event event) {
        //TODO implementation
    }

    /**
     * Button event occurs when the user clicks "NORMAL GAME"
     * Queues the user for searching match against other players.
     * @param event
     */
    public void ClickedNormalGame(Event event) {
        //TODO implementation
        lblSearchText.setVisible(true);
        lblSearchText.setText("SEARCHING FOR GAME.... XX SECONDS");
        
    }


}
