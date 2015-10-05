package elementstcg.gui;

import javafx.fxml.FXML;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Created by Mick on 5-10-2015.
 */
public class InlogScreenController implements Initializable, ControlledScreen {

    @FXML Button buttonLogin;
    @FXML TextField tbPassword;
    @FXML TextField tbUsername;
    @FXML Label lblInfoMessage;

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
        if (tbUsername.getText().isEmpty() || tbPassword.getText().isEmpty()){
            lblInfoMessage.setText("Please enter both your username and password.");
        }
    }

    /**
     * Opens the registration form.
     */
    @FXML
    public void RegisterButtonAction(ActionEvent event) throws IOException {
        //TODO
        myController.setScreen(ScreensFramework.screen2ID);
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
