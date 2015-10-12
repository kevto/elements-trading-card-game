package elementstcg.gui;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Mick on 5-10-2015.
 */
public class BoardController implements Initializable, ControlledScreen {

    // Class variables
    ScreenHandler myController;

    // UI Components
    @FXML
    Pane hboxPlayerHand;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hboxPlayerHand.getParent().prefWidth(hboxPlayerHand.getPrefWidth());
        hboxPlayerHand.getParent().prefHeight(hboxPlayerHand.getPrefHeight());
    }


    public void setScreenParent(ScreenHandler screenParent) {
        myController = screenParent;
    }


    public void clickedHandSelf(Event event) {
        System.out.println(event.getSource().toString());

    }

    /**
     * This method is used to update the GUI.
     */
    public void updateUi()
    {

    }

    /**
     * This method is called when a player selects a card that is currently on the playing field.
     */
    public void selectCardButtonAction()
    {

    }

    /**
     * This method is called when a player selects a card that is currently in his hand.
     */
    public void selectCardInHandButtonAction()
    {

    }

    /**
     * This method is called when a player uses a selected card to attack a card of his opponent.
     */
    public void attackEnemyCardButtonAction()
    {

    }

    /**
     * This method is called when a player uses a selected card to attack the enemy directly.
     */
    public void attackEnemyDirectButtonAction()
    {

    }

    /**
     * This method is called when a player presses the forfeit button.
     */
    public void forfeitButtonAction()
    {

    }

    /**
     * This method is called when a player presses the next turn button.
     */
    public void nextTurnButtonAction()
    {

    }

    /**
     * This method is called when a player returns one of his played cards to his hand.
     */
    public void cardBackInHandButtonAction()
    {

    }
}
