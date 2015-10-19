package elementstcg.gui;

import elementstcg.Card;
import elementstcg.Element;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by Mick on 5-10-2015.
 */
public class BoardController implements Initializable, ControlledScreen {

    // Class variables
    ScreenHandler myController;

    // UI Components
    @FXML HBox hboxPlayerHand;
    @FXML Pane ghostPane;

    private CardPane selectedCard;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hboxPlayerHand.getParent().prefWidth(hboxPlayerHand.getPrefWidth());
        hboxPlayerHand.getParent().prefHeight(hboxPlayerHand.getPrefHeight());

        //TODO: REMOVE DEBUG
        Card fireCard = new Card(Element.Fire, 5, 5, "Fire", 3);
        Card airCard = new Card(Element.Air, 5, 5, "Air", 2);
        Card waterCard = new Card(Element.Water, 5, 5, "Water", 2);
        Card earthCard = new Card(Element.Earth, 5, 5, "Earth", 1);
        Card energyCard = new Card(Element.Thunder, 5, 5, "Energy", 3);

        hboxPlayerHand.getChildren().add(new CardPane(fireCard, ghostPane, this));
        hboxPlayerHand.getChildren().add(new CardPane(airCard, ghostPane, this));
        hboxPlayerHand.getChildren().add(new CardPane(waterCard, ghostPane, this));
        hboxPlayerHand.getChildren().add(new CardPane(airCard, ghostPane, this));
        hboxPlayerHand.getChildren().add(new CardPane(energyCard, ghostPane, this));
        hboxPlayerHand.getChildren().add(new CardPane(fireCard, ghostPane, this));
        hboxPlayerHand.getChildren().add(new CardPane(earthCard, ghostPane, this));
        hboxPlayerHand.getChildren().add(new CardPane(energyCard, ghostPane, this));

        //END DEBUG
    }


    public void setScreenParent(ScreenHandler screenParent) {
        myController = screenParent;
    }


    public void onGridClick(Event event) {
        if(event.getSource() instanceof GridPane) {
            ((GridPane) event.getSource()).getChildren().add(selectedCard);
            selectedCard.setSelected(false);
            selectedCard.setState(CardState.PlayerField);
        }
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
    public void selectCardButtonAction(CardPane cardPane)
    {

    }

    /**
     * This method is called when a player selects a card that is currently in his hand.
     */
    public void selectCardInHandButtonAction(CardPane cardPane)
    {
        if(selectedCard == null) {
            selectedCard = cardPane;
            cardPane.setSelected(true);
        }
        else {
            if(cardPane != selectedCard) {
                selectedCard.setSelected(false);

                selectedCard = cardPane;
                cardPane.setSelected(true);
            }
            else {
                cardPane.setSelected(false);
                selectedCard = null;
            }
        }
    }

    /**
     * This method is called when a player uses a selected card to attack a card of his opponent.
     */
    public void attackEnemyCardButtonAction(CardPane cardPane)
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

    public void onMouseClickHbox(Event event) {
        //System.out.println("Clicked");
    }
}
