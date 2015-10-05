package elementstcg.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Mick on 5-10-2015.
 */
public class LobbyScreenController implements Initializable, ControlledScreen {



    ScreensController myController;

    public void initialize(URL url, ResourceBundle rb) {

    }


    /**
     * Fires when the user clicks the login button.
     * Checks if the username and password field are entered.
     */
    @FXML
    public void LoginButtonAction(ActionEvent event){
        //TODO

    }

    /**
     * Opens the registration form.
     */
    @FXML
    public void RegisterButtonAction(ActionEvent event) throws IOException {
        //TODO

    }

    /**
     * Opens the forgot password form.
     */
    public void ForgotPasswordAction(){
        //TODO
    }


    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
}
