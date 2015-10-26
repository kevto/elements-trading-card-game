package elementstcg.gui;

import elementstcg.Board;
import elementstcg.Card;
import elementstcg.Deck;
import elementstcg.Element;
import elementstcg.util.CustomException.EmptyFieldException;
import elementstcg.util.CustomException.ExceedCapacityException;
import elementstcg.util.CustomException.OccupiedFieldException;
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

import java.io.IOException;
import java.net.URL;
import java.util.Map;
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

    @FXML Pane enemyInfo;

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

        // Set on click listener to enemy info box (pane).
        enemyInfo.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            attackEnemyDirectButtonAction();
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

        for(Node pane : playerField.getChildren()) {
            if(((FieldPane) pane).getCard() != null)
                ((FieldPane) pane).getCard().updateUi();
        }

        for(Node pane : enemyField.getChildren()) {
            if(((FieldPane) pane).getCard() != null)
                ((FieldPane) pane).getCard().updateUi();
        }
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

                    for(int i = 0; i < ((FieldGrid) field.getParent()).getChildren().size(); i++) {
                        if(field.equals(((FieldGrid) field.getParent()).getChildren().get(i))){
                            try {
                                board.putCardPlayer((i < 6 ? i : i - 6 + 10), selectedCard.getCard());
                            } catch (OccupiedFieldException e) {
                                e.printStackTrace();
                            } catch (ExceedCapacityException e) {
                                e.printStackTrace();
                            }
                        }
                    }

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

                int point = board.getEnemyCardPoint(fieldCard.getCard());
                board.removePlayerCard(point);
                try {
                    board.putCardPlayer(point, handCard.getCard());
                } catch (OccupiedFieldException e) {
                    e.printStackTrace();
                } catch (ExceedCapacityException e) {
                    e.printStackTrace();
                }

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
                //TODO: Attack Card (look at attackEnemyCardButtonAction)
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
     */
    public void attackEnemyCardButtonAction(CardPane cardPane) throws IOException {
        if(selectedCard != null && selectedCard.isSelected() && selectedCard.onField()) {
            // Check if the card is in a defend position.
            for(Map.Entry<Integer, Card> entry : board.getPlayerField().entrySet()) {
                if (entry.getKey() < 10 && entry.getValue().equals(selectedCard.getCard())) {
                    System.out.println("[kevto]: Selected card is a defense and therefore can not attack an enemy card.");
                    return;
                }
            }

            // Check if the card already attacked.
            if(selectedCard.getCard().getAttacked()) {
                System.out.println("[kevto]: Selected card already attacked this turn.");
                return;
            }

            // TODO implement so that the card of the enemy will be attacked.
            System.out.println("[kevto]: Enemy CardPane selected " + cardPane.getCard().getName());

            int point = -1;

            // Checking all the cards on the enemy field to get the correct point.
            for(Map.Entry<Integer, Card> entry : board.getEnemyField().entrySet())
                if(entry.getValue().equals(cardPane.getCard()))
                    point = entry.getKey();

            if(point == -1) {
                throw new IOException("Position (point) not found of selected card");
            }

            try {
                board.attackCard(selectedCard.getCard(), point, board.getEnemyField(), new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("[kevto]: Enemy card is dead!");
                        FieldPane field = (FieldPane) cardPane.getParent();
                        field.removeCard();
                    }
                });
            } catch (EmptyFieldException e) {
                e.printStackTrace();
            }

            updateUi();
        } else
            System.out.println("[kevto]: Select a card first..");
    }

    /**
     * This method is called when a player uses a selected card to attack the enemy directly.
     */
    public void attackEnemyDirectButtonAction() {
        if(selectedCard != null && selectedCard.isSelected() && selectedCard.onField()) {
            // Check if there are any cards on the defence line.
            for (Map.Entry<Integer, Card> entry : board.getEnemyField().entrySet()) {
                if (entry.getKey() >= 10) {
                    System.out.println("[kevto]: There's a card on the defense line on the enemy side. Can't attack directly.");
                    return;
                }
            }

            // Check if the card is in a defend position.
            for (Map.Entry<Integer, Card> entry : board.getPlayerField().entrySet()) {
                if (entry.getKey() < 10 && entry.getValue().equals(selectedCard.getCard())) {
                    System.out.println("[kevto]: Selected card is a defense and therefore can not attack an enemy card.");
                    return;
                }
            }

            // Check if the card already attacked.
            if (selectedCard.getCard().getAttacked()) {
                System.out.println("[kevto]: Selected card already attacked this turn.");
                return;
            }


            // Feel free to attack the enemy!
            board.updateEnemyHP(selectedCard.getCard().getAttack());
            selectedCard.getCard().setAttacked(true);
            updateUi();

            //TODO Do something when the enemy is dead!
            if(board.isGameOver())
                System.out.println("Game is over!");
        }
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
        resetCardsAttacked();

        if(!board.getTurn()) {
            Card card = board.getEnemy().drawCard();

            if(card != null) {
                // Put enemy card on the field.
                // TODO Clean this mess up.
                if(!board.getEnemyField().containsKey(0)) {
                    enemyCardToField(card, 0);
                } else if(!board.getEnemyField().containsKey(1)) {
                    enemyCardToField(card, 1);
                } else if(!board.getEnemyField().containsKey(2)) {
                    enemyCardToField(card, 2);
                } else if(!board.getEnemyField().containsKey(3)) {
                    enemyCardToField(card, 3);
                } else if(!board.getEnemyField().containsKey(4)) {
                    enemyCardToField(card, 4);
                } else if(!board.getEnemyField().containsKey(5)) {
                    enemyCardToField(card, 5);
                }
                // Attack the player or player's cards.
                // TODO Attack the player.
            }

            nextTurnButtonAction();
        } else {
            hboxPlayerHand.getChildren().add(new CardPane(board.getPlayer().drawCard(), ghostPane, this));
        }

        updateUi();

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

    /**
     * Sets an enemy card to the field.
     * @param card to add the board field.
     */
    private void enemyCardToField(Card card, int point) {
        board.putCardEnemy(point, card);

        FieldPane pane = (FieldPane) enemyField.getChildren().get(point);
        CardPane cardPane = new CardPane(card, ghostPane, this);

        pane.setCard(cardPane);
        cardPane.setCardState(CardState.EnemyField);

        // TODO Apparently I'd need this property to set the cards right. Find a better way to fix this.
        pane.translateYProperty().set(-70);
    }


    /**
     * New turn means that all cards will be able to return to hand again.
     * TODO Find a better solution for resetting the boolean of the cards whom attacked.
     */
    private void resetCardsAttacked() {
        // Resetting the boolean of player cards.
        for(Card card : board.getPlayerField().values())
            card.setAttacked(false);
        for(Card card : board.getPlayer().getHand().getCards())
            card.setAttacked(false);

        // Resetting the boolean of enemy cards.
        for(Card card : board.getEnemyField().values())
            card.setAttacked(false);
        for(Card card : board.getEnemy().getHand().getCards())
            card.setAttacked(false);
    }
}
