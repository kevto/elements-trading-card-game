package elementstcg.gui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Mick on 5-10-2015.
 */
public class GamefieldController implements Initializable, ControlledScreen {

    @FXML
    HBox hboxPlayerHand;

    ScreensController myController;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }


    public void clickedHandSelf(Event event) {
        System.out.println(event.getSource().toString());

    }
}
