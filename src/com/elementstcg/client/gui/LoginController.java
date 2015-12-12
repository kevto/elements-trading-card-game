package com.elementstcg.client.gui;

import com.elementstcg.client.Account;
import com.elementstcg.client.handler.ClientHandler;
import com.elementstcg.shared.trait.IResponse;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Mick on 5-10-2015.
 */
public class LoginController implements Initializable, ControlledScreen {

    @FXML Button LoginButton;
    @FXML Hyperlink RegisterButton;
    @FXML PasswordField tbPassword;
    @FXML TextField tbUsername;
    @FXML TextField tbEmail;
    @FXML Label lblMessage;

    ScreenHandler myController;
    boolean isRegistering = false;

    private ClientHandler clientHandler = ClientHandler.getInstance();



    public void initialize(URL url, ResourceBundle rb) {

    }


    public void setScreenParent(ScreenHandler screenParent) {
        myController = screenParent;
    }

    /**
     * Fires when the user clicks the login button.
     * Checks if the username and password field are entered.
     * When the user is registering the email will be checked for validity.
     * Password must be at least 8 characters.
     * Username must be at least 6 characters.
     */
    public void clickedLogin(Event event) {
        if (!isRegistering) {
            //User is logging in.

            String username = tbUsername.getText();
            String password = tbPassword.getText();

            if ("".equals(username) || "".equals(password)) {
                lblMessage.setText("Please enter both your username and password.");
            }
            else{
                if (clientHandler.loginUser(username, password)){
                    lblMessage.setText("Succesfully logged in.");
                    myController.setScreen(ScreensFramework.screenLobbyID);
                }
                else{
                    lblMessage.setText("Invalid username or password.");
                }
            }
        }
        else{
            //User is registering
            //Check email
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(tbEmail.getText());

            String username = tbUsername.getText();
            String password = tbPassword.getText();
            String email = tbEmail.getText();

            //Check the username and password
            if (username.isEmpty() || password.isEmpty() || email.isEmpty())  {
                lblMessage.setText("Please enter all fields.");
            }
            else if (username.length() < 6){
                lblMessage.setText("Username must be at least 6 characters.");
            }
            else if (password.length() < 8){
                lblMessage.setText("Password must be at least 8 characters.");
            }
            else if (!m.matches()){ //Is invalid email
                lblMessage.setText("Please use a valid Email address");
            }
            else{
                //All fields are valid.
                if (Account.register(username, password, email)) {
                    if(clientHandler.registerUser(username, password, email)) {
                        lblMessage.setText("Succesfully registered.");
                    } else {
                        lblMessage.setText("Username or email already in use.");
                    }
                }
            }
        }
    }

    /**
     * Opens the registration form.
     */
    public void clickedRegister(Event event) {
        //myController.setScreen(ScreensFramework.screen2ID);
        if (!isRegistering) {
            tbEmail.setVisible(true);
            LoginButton.setText("REGISTER");
            RegisterButton.setText("Login");
            isRegistering = true;
        }
        else{
            tbEmail.setVisible(false);
            LoginButton.setText("LOGIN");
            RegisterButton.setText("Register");
            isRegistering = false;
        }
    }
}
