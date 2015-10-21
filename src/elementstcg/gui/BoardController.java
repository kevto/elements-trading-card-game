package elementstcg.gui;

import elementstcg.Card;
import elementstcg.Element;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
    @FXML HBox hboxPlayerHand;      //Player hand
    @FXML Pane ghostPane;           //Card info pane
    @FXML BorderPane bPaneField;
    @FXML Pane mainPane;

    private CardPane selectedCard;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hboxPlayerHand.getParent().prefWidth(hboxPlayerHand.getPrefWidth());
        hboxPlayerHand.getParent().prefHeight(hboxPlayerHand.getPrefHeight());

        FieldGrid field = new FieldGrid(847, 253, 2, 6, this);

        field.getStyleClass().add("field");

        field.setTranslateY(253);

        bPaneField.getChildren().add(field);

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

        ghostPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    hideGhostPane();
                }
            }
        });
    }


    public void setScreenParent(ScreenHandler screenParent) {
        myController = screenParent;
    }

    /**
     * This method is used to update the GUI.
     */
    public void updateUi()
    {

    }

    /**
     * This method is called when an FieldPane is selected
     */
    public void selectFieldPane(FieldPane field) {
        if(field.getCard() != null) {
            CardPane card = field.getCard();
        }
        else
        {
            if(selectedCard != null) {
                //TODO: Move logic to board
                field.setCard(selectedCard);
                selectedCard = null;
            }
        }
    }

    /**
     * This method is called when a player selects a card that is currently on the playing field.
     */
    public void selectCardButtonAction(CardPane cardPane)
    {

    }

    public void showCardButtonAction(CardPane cardPane) {
        showGhostPane(cardPane.getGhostObject());
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

    /**
     * This method is called to move the ghostPane to the front and display the provided node
     */
    public void showGhostPane(Node node) {
        if(ghostPane.getChildren().size() > 0) {
            ghostPane.getChildren().removeAll(ghostPane.getChildren());
        }

        ghostPane.getChildren().add(node);

        ghostPane.toFront();
    }

    /**
     * This method is called to move the ghostPane to the back and remove all nodes from the children.
     */
    public void hideGhostPane() {
        ghostPane.getChildren().removeAll(ghostPane.getChildren());

        ghostPane.toBack();
    }
}
