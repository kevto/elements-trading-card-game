package elementstcg.gui;

import elementstcg.Board;
import elementstcg.Card;
import elementstcg.Deck;
import elementstcg.Element;
import elementstcg.util.DefaultDeck;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable, ControlledScreen {

    // Class variables
    ScreenHandler myController;

    // UI Components
    @FXML HBox hboxPlayerHand;
    @FXML Pane ghostPane;
    @FXML BorderPane bPaneField;
    @FXML Pane mainPane;

    @FXML Button nextTurnButton;

    @FXML Label labelEnemyCAP;
    @FXML Label labelPlayerCAP;

    @FXML Label labelEnemyDeckSize;
    @FXML Label labelPlayerDeckSize;

    @FXML Label labelEnemyHP;
    @FXML Label labelPlayerHP;

    @FXML Label labelEnemyName;
    @FXML Label labelPlayerName;

    FieldGrid playerField;
    FieldGrid enemyField;

    private CardPane selectedCard;

    // Board object.
    private Board board;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        board = new Board();

        // Setting the card decks for each player.
        board.getEnemy().setDeck(new Deck(DefaultDeck.getDeckOne()));
        board.getPlayer().setDeck(new Deck(DefaultDeck.getDeckOne()));

        // Initializing the UI.
        hboxPlayerHand.getParent().prefWidth(hboxPlayerHand.getPrefWidth());
        hboxPlayerHand.getParent().prefHeight(hboxPlayerHand.getPrefHeight());

        playerField = new FieldGrid(847, 253, 2, 6, this, FieldType.Player);
        enemyField = new FieldGrid(847, 253, 2, 6, this, FieldType.Enemy);

        playerField.getStyleClass().add("field");
        enemyField.getStyleClass().add("field-enemy");

        playerField.setTranslateY(253);

        bPaneField.getChildren().add(playerField);
        bPaneField.getChildren().add(enemyField);

        // Drawing 5 random cards for the player.
        for(int i = 0; i < 5; i++) {
            hboxPlayerHand.getChildren().add(new CardPane(board.getPlayer().drawCard(), ghostPane, this));
        }

        // Setting the names of both the players/
        labelEnemyName.setText(board.getEnemy().getName());
        labelPlayerName.setText(board.getPlayer().getName());


        /**
         * Hide the ghostPane object when an player selects it
         */
        ghostPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    hideGhostPane();
                }
            }
        });

        // Update the UI
        updateUi();
    }

    /**
     * Set the screenParent
     * @param screenParent screenParent
     */
    public void setScreenParent(ScreenHandler screenParent) {
        myController = screenParent;
    }

    /**
     * This method is used to update the GUI.
     */
    public void updateUi() {
        labelEnemyCAP.setText(String.valueOf(enemyField.getCapPoints()));
        labelPlayerCAP.setText(String.valueOf(playerField.getCapPoints()));

        labelEnemyDeckSize.setText(String.valueOf(board.getEnemy().getAmountCardsInDeck()));
        labelPlayerDeckSize.setText(String.valueOf(board.getPlayer().getAmountCardsInDeck()));

        labelEnemyHP.setText(String.valueOf(board.getEnemy().getHp()));
        labelPlayerHP.setText(String.valueOf(board.getPlayer().getHp()));
    }

    /**
     * This method is called when an FieldPane is selected
     */
    public void selectFieldPane(FieldPane field) {
        FieldGrid grid = (FieldGrid)field.getParent();

        if(grid.getFieldType() == FieldType.Player) {
            if(selectedCard != null) {
                if(selectedCard.getCardState() == CardState.PlayerHand) {
                    //TODO: Notify Board object that an card has been placed on the playing field
                    field.setCard(selectedCard);
                    selectCard(selectedCard);
                    updateUi();
                }
            }
            else {
                if(field.getCard() != null) {
                    selectCard(field.getCard());
                }
            }
        }
        if(grid.getFieldType() == FieldType.Enemy) {
            if(selectedCard != null) {
                //TODO: Attack Card
            }
        }
    }

    /**
     * This method is called when a player selects a card that is currently on the playing field.
     */
    public void selectCardButtonAction(CardPane cardPane)
    {
        FieldGrid grid = (FieldGrid)cardPane.getParent().getParent();

        if(grid.getFieldType() == FieldType.Player) {
            if(selectedCard != null && selectedCard != cardPane && selectedCard.onField() == false) {
                CardPane fieldCard = cardPane;
                CardPane handCard  = selectedCard;

                fieldCard.resetCardPos();
                handCard.resetCardPos();

                FieldPane field = (FieldPane)fieldCard.getParent();

                field.setCard(handCard);
                handCard.setCardState(CardState.PlayerField);
                handCard.resizeCard();

                field.getChildren().remove(fieldCard);
                hboxPlayerHand.getChildren().add(fieldCard);
                fieldCard.setCardState(CardState.PlayerHand);
                fieldCard.resizeCard();

                selectCard(fieldCard);
                updateUi();
            }
            selectCard(cardPane);
        }

        if(grid.getFieldType() == FieldType.Enemy) {
            if(selectedCard != null) {
                //TODO: Attack Card
            }
        }
    }

    /**
     * This methode is called when a player right clicks on an card on the field
     * @param cardPane that's been selected.
     */
    public void showCardButtonAction(CardPane cardPane) {
        showGhostPane(cardPane.getGhostObject());
    }

    /**
     * This method is called when a player selects a card that is currently in his hand.
     */
    public void selectCardInHandButtonAction(CardPane cardPane) {
        selectCard(cardPane);
    }

    /**
     * This method is called when a player uses a selected card to attack a card of his opponent.
     * @deprecated Is this being used in a different action method?
     */
    @Deprecated
    public void attackEnemyCardButtonAction(CardPane cardPane) {
        if(selectedCard != null && selectedCard.isSelected() && selectedCard.onField()) {
            // TODO implement so that the card of the enemy will be attacked.
            System.out.println("[kevto]: Enemy CardPane selected " + cardPane.getId());
        } else
            System.out.println("[kevto]: Select a card first..");
    }

    /**
     * This method is called when a player uses a selected card to attack the enemy directly.
     */
    public void attackEnemyDirectButtonAction() {
        //TODO check if a card is selected.
        //TODO check whether there are no cards on defense cards on the enemy field.
    }

    /**
     * This method is called when a player presses the forfeit button.
     */
    public void forfeitButtonAction() {
        //TODO end the game and lose!
    }

    /**
     * This method is called when a player presses the next turn button.
     */
    public void nextTurnButtonAction() {
        //TODO add a confirmation dialog.
        board.nextTurn();

        //TODO let the AI do his actions here. Nasty but it will work.
    }

    /**
     * This method is called when a player returns one of his played cards to his hand.
     * @deprecated Remove or implement this one??
     */
    @Deprecated
    public void cardBackInHandButtonAction() {
        //TODO Check deprecated message.
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

    /**
     * Gives the selected card a selected state.
     * @param card to give the selected state to.
     */
    private void selectCard(CardPane card) {
        if(selectedCard == null) {
            selectedCard = card;
            card.setSelected(true);
        }
        else {
            if(card != selectedCard) {
                selectedCard.setSelected(false);

                selectedCard = card;
                card.setSelected(true);
            }
            else {
                card.setSelected(false);
                selectedCard = null;
            }
        }
    }
}
