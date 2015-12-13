package com.elementstcg.client.gui.Controllers;

import com.elementstcg.client.gui.*;
import com.elementstcg.client.*;
import com.elementstcg.client.handler.ClientHandler;
import com.elementstcg.client.util.CustomException.ExceedCapacityException;
import com.elementstcg.client.util.CustomException.OccupiedFieldException;
import com.elementstcg.client.util.DialogUtility;
import com.elementstcg.shared.trait.Card;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class BoardController implements Initializable, ControlledScreen {

    private Board board;

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

    private FieldGrid playerField;
    private FieldGrid enemyField;
    private CardPane selectedCard;
    private ScreenHandler screenHandler;

    /**
     * Methode for the server to call if the game is over
     * Forces the player turn to be false so he cant issue an commands to the server
     * Removes this boardController from the clientHandler
     */
    public void SetGameOver(){
        ClientHandler.getInstance().setBoardController(null);
        board.setTurn(false);

        //TODO: Have a screen show with the result (win/lose) and a button to return to the lobby
    }

    /**
     * Add damage to the selected point with the given card
     * @param card The card that is attacking
     * @param point The point that is getting attacked
     */
    public void attackCard(Card card, int point){
        CardPane cardPane = (CardPane)enemyField.getChildren().get(point);
        //TODO: Catch exception like there is no card in that pos
        if(cardPane != null) {
            cardPane.getCard().modifyHP(card.getAttack());
        }
    }

    /**
     * Set the value of the enemyNameLabel to the given name value
     * @param name the name of the enemy
     */
    public void setEnemyName(String name) {
        labelEnemyName.setText(name);
    }

    /**
     * Returns the value of the enemyNameLabel
     * @return String
     */
    public String getEnemyName() {
        return labelEnemyName.getText();
    }

    /**
     * Set the value of the playerNameLabel to the given name value
     * @param name the name of the player
     */
    public void setPlayerName(String name) {
        labelPlayerName.setText(name);
    }

    /**
     * Returns the value of the playerNameLabel
     * @return String
     */
    public String getPlayerName() {
        return labelPlayerName.getText();
    }

    /**
     * Places the given card on the player field at the provided point
     * @param card The card that needs to be placed
     * @param point The point on the playing field it has to be placed on
     */
    public void PutCardPlayer(Card card, int point){
        //TODO Show the card visually.
        Platform.runLater(() -> {
            FieldPane field = (FieldPane) playerField.getChildren().get((point > 5 ? point - 5 : point));
            board.putCardPlayer(point, card);
            CardPane cardPane = new CardPane(card, ghostPane, this);
            field.setCard(cardPane);

            cardPane.setTranslateY(-70);

            updateUI();
        });
    }

    /**
     * Decreases the value HP of the player object by the provided value
     * @param hp the value by with HP needs to be lowered
     */
    public void updatePlayerHP(int hp){
        board.getPlayer().modifyHp(hp);
    }

    /**
     * Removes the card at the provided point from the players field
     * @param point the point of the card that needs to be removed
     */
    public void removeCardPlayer(int point){
        board.getPlayerField().remove(point);
    }

    /**
     * Places the given card on the enemy field at the provided point
     * @param card The card that needs to be placed
     * @param point The point on the playing field it has to be placed on
     */
    public void putCardEnemy(Card card, int point){
        //TODO Optimize method
        Platform.runLater(() -> {
            int fieldpointer;

            if (point >= 6) {
                fieldpointer = (point - 4);
            } else {
                fieldpointer = point;
            }


            FieldPane pane = (FieldPane) enemyField.getChildren().get(fieldpointer);
            CardPane cardPane = new CardPane(card, ghostPane, this);
            pane.setCard(cardPane);
            cardPane.setCardState(CardState.EnemyField);


            for (int i = 0; i < ((FieldGrid) pane.getParent()).getChildren().size(); i++) {
                if (pane.equals(((FieldGrid) pane.getParent()).getChildren().get(i))) {
                    try {
                        board.putCardEnemy((i < 6 ? i : i - 6 + 10), card);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            // TODO Apparently I'd need this property to set the cards right. Find a better way to fix this.
            if (point < 9) {
                pane.translateYProperty().set(-70);
            } else {
                pane.translateYProperty().set(40);
            }
        });
    }

    /**
     * Decreases the value HP of the enemy object by the provided value
     * @param hp the value by with HP needs to be lowered
     */
    public void updateEnemyHp(int hp){
        board.getEnemy().modifyHp(hp);
    }

    /**
     * Removes the card at the provided point from the enemy's field
     * @param point the point of the card that needs to be removed
     */
    public void removeCardEnemy(int point){
        board.getEnemyField().remove(point);
    }

    /**
     * Updates the complete UI this includes
     * - Enemy field CAP points
     * - Player field CAP points
     *
     * - Enemy deck size
     * - Player deck size
     *
     * - Enemy HP value
     * - Player HP value
     *
     * - HP of all enemy cards
     * - HP of all player cards
     */
    public void updateUI(){
        labelEnemyCAP.setText(String.valueOf(enemyField.getCapPoints()));
        labelPlayerCAP.setText(String.valueOf(playerField.getCapPoints()));

        labelEnemyDeckSize.setText(String.valueOf(board.getEnemy().getAmountCardsInDeck()));
        labelPlayerDeckSize.setText(String.valueOf(board.getPlayer().getAmountCardsInDeck()));

        labelEnemyHP.setText(String.valueOf(board.getEnemy().getHp()));
        labelPlayerHP.setText(String.valueOf(board.getPlayer().getHp()));

        for (Node pane : playerField.getChildren()) {
            if (((FieldPane) pane).getCard() != null)
                ((FieldPane) pane).getCard().updateUi();
        }

        for (Node pane : enemyField.getChildren()) {
            if (((FieldPane) pane).getCard() != null)
                ((FieldPane) pane).getCard().updateUi();
        }
    }

    /**
     * Sets the turn to false if true and vice versa
     */
    public void nextTurn(){
        board.nextTurn();

    }

    /**
     * Forces the value of turn to the provided value
     * @param turn the value to wich turn has to be set
     */
    public void setTurn(boolean turn) {
        board.setTurn(turn);
    }

    /**
     * If the provided card is not equal to selectedCard this card will be selected card.
     * If the provided card is equal to the selectedCard, selectedCard will be set to null.
     * @param cardPane
     */
    public void selectCard(CardPane cardPane){
        if (!board.isGameOver()) {
            if (selectedCard == null) {
                selectedCard = cardPane;
                cardPane.setSelected(true);
            } else {
                if (cardPane != selectedCard) {
                    selectedCard.setSelected(false);

                    selectedCard = cardPane;
                    cardPane.setSelected(true);
                } else {
                    cardPane.setSelected(false);
                    selectedCard = null;
                }
            }
        }

    }

    /**
     * Removes all children from ghostPane and moves the ghostPane object to the front of the stage
     * and adds the provided node to the ghostPane showing it.
     * @param node
     */
    public void showGhostPane(Node node){
        if (ghostPane.getChildren().size() > 0) {
            ghostPane.getChildren().removeAll(ghostPane.getChildren());
        }

        ghostPane.getChildren().add(node);

        ghostPane.toFront();
    }

    /**
     * Removes all children from ghostPane and moves the ghostPane to the back of the stage
     */
    public void hideGhostPane() {
        ghostPane.getChildren().removeAll(ghostPane.getChildren());
        ghostPane.toBack();
    }

    public void attackEnemyDirectButtonAction() {

        //TODO: Finish this methode

        if (selectedCard != null && selectedCard.isSelected() && selectedCard.onField()) {

        }

    }

    /**
     * Initializes the BoardController and sets up the game
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Create a board object
        board = new Board();

        //Setup the player hands
        hboxPlayerHand.getParent().prefWidth(hboxPlayerHand.getPrefWidth());
        hboxPlayerHand.getParent().prefHeight(hboxPlayerHand.getPrefHeight());

        //Setup the player fields
        playerField = new FieldGrid(847, 253, 2, 6, this, FieldType.Player);
        enemyField = new FieldGrid(847, 253, 2, 6, this, FieldType.Enemy);

        //Set the styles for both fields
        playerField.getStyleClass().add("field");
        enemyField.getStyleClass().add("field-enemy");

        //Move the playerField down (the height of the enemyField)
        playerField.setTranslateY(253);

        //Add the fields to the stage (a special pane for the play fields)
        bPaneField.getChildren().add(playerField);
        bPaneField.getChildren().add(enemyField);

        //Add an methode where the ghostPane hides itself when clicked
        //Both the make sure the player can hide it after clicking it
        //and to give the player a way to hide it, if it bugs for whatever reason
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
        updateUI();
    }

    /**
     * Registers the boardController with clientHandler
     */
    public BoardController(){
        // Notify ClientHandler
        ClientHandler.getInstance().setBoardController(this);
    }

    /**
     * Sets the provided cardPane as select (should only be called if the card is currently in the hand of the player)
     * @param cardPane
     */
    public void selectCardInHandButtonAction(CardPane cardPane) {
        if(!board.isGameOver()) {
            selectCard(cardPane);
        }
    }

    /**
     * Is called when a card is selected thats on the field. This methode will handle switching of cards
     * and attacking enemy cards
     * @param cardPane
     */
    public void selectCardButtonAction(CardPane cardPane) {
        if (!board.isGameOver()) {
            FieldGrid grid = (FieldGrid) cardPane.getParent().getParent();

            if (grid.getFieldType() == FieldType.Player) {
                if (selectedCard != null && selectedCard != cardPane && selectedCard.onField() == false) {
                    // Check if the card already attacked.
                    if (cardPane.getCard().getAttacked()) {
                        DialogUtility.newDialog("Card on the field already has attacked this turn and can not be swapped!");
                        return;
                    }

                    CardPane fieldCard = cardPane;
                    CardPane handCard = selectedCard;

                    // Try to put the new card onto the field. Checking for limitations.
                    int point = board.getPlayerCardPoint(fieldCard.getCard());
                    Card cardRemoved = board.getPlayerField().get(point);
                    board.removePlayerCard(point);

                    //TODO Replace card by sending a request to the server
                    //board.putCardPlayer(point, handCard.getCard());

                    // Go on with swapping cards.
                    fieldCard.resetCardPos();
                    handCard.resetCardPos();
                    FieldPane field = (FieldPane) fieldCard.getParent();

                    field.setCard(handCard);
                    handCard.setCardState(CardState.PlayerField);
                    handCard.resizeCard();

                    field.getChildren().remove(fieldCard);
                    hboxPlayerHand.getChildren().add(fieldCard);
                    fieldCard.setCardState(CardState.PlayerHand);
                    fieldCard.resizeCard();

                    selectCard(fieldCard);
                    updateUI();
                }
                selectCard(cardPane);
            }

            if (grid.getFieldType() == FieldType.Enemy) {
                if (selectedCard != null) {
                    //TODO: Attack Card (look at attackEnemyCardButtonAction)
                }
            }
        }
    }

    /**
     * Called when an fieldPane is clicked
     * @param field the field that has been clicked
     */
    public void selectFieldPane(FieldPane field) {
        if (!board.isGameOver()) {
            FieldGrid grid = (FieldGrid) field.getParent();

            if (grid.getFieldType() == FieldType.Player) {
                if (selectedCard != null) {
                    if (selectedCard.getCardState() == CardState.PlayerHand) {
                        //TODO: Notify Board object that an card has been placed on the playing field

                        for (int i = 0; i < ((FieldGrid) field.getParent()).getChildren().size(); i++) {
                            if (field.equals(((FieldGrid) field.getParent()).getChildren().get(i))) {
                                //TODO Send request to the server to place the card on the selected field pane.

                                try {
                                    ClientHandler.getInstance().getServerHandler().placeCard(ClientHandler.getInstance().getSessionKey(),
                                            hboxPlayerHand.getChildren().indexOf(selectedCard),
                                            (i < 6 ? i : i - 6 + 10));
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                                //board.putCardPlayer((i < 6 ? i : i - 6 + 10), selectedCard.getCard());
                                //field.setCard(selectedCard);
                            }
                        }

                        selectCard(selectedCard);
                        updateUI();
                    }
                } else {
                    if (field.getCard() != null) {
                        selectCard(field.getCard());
                    }
                }
            }
        }
    }

    /**
     * Called when the player clicks on the next turn button
     */
    public void nextTurnButtonAction() {
        //TODO Implement RMI action
        updateUI();
    }


    /**
     * This methode is called when a player right clicks on an card on the field
     * @param cardPane that's been selected.
     */
    public void showCardButtonAction(CardPane cardPane) {
        showGhostPane(cardPane.getGhostObject());
    }

    /**
     * Called when an player clicks an enemy card
     * @param cardPane
     * @throws IOException
     */
    public void attackEnemyCardButtonAction(CardPane cardPane) throws IOException {
        if (!board.isGameOver()) {
            //TODO: Implement RMI
            if (selectedCard != null && selectedCard.isSelected() && selectedCard.onField()) {
                // Check if the card is in a defend position.
            }
        }
    }

    /**
     * Add the provided card to the hand of the player and show the card in the game
     * @param card the card that needs to be added
     */
    public void AddCardToPlayerHand(Card card){
        board.getPlayer().getHand().addCard(card);

        hboxPlayerHand.getChildren().add(new CardPane(card, ghostPane, this));
        //TODO: Add the card to the hboxPlayerHand or find an alternative that uses the board.player.hand to automaticly do this
    }

    /**
     * Sets the player deck size to the provided value. The value has to be the total size
     * @param amount the total size of the deck
     */
    public void UpdatePlayerDeckCount(int amount){
        board.getPlayer().getDeck().setRemainingCards(amount);
    }

    /**
     * Sets the enemy deck size to the provided value. The value has to be the total size
     * @param amount the total size of the deck
     */
    public void updateEnemyDeckCount(int amount){
        board.getEnemy().getDeck().setRemainingCards(amount);
    }

    /**
     * Sets the HP value of a card at the provided point to the provided value
     * @param point The point that needs to be edited
     * @param hp The value by which it needs to be lowered
     */
    public void SetPlayerCardHp(int point, int hp){
        board.getPlayerField().get(point).modifyHP(hp);

    }

    /**
     * Removes the card from the players hand on the provided index
     * @param index the index of the card that needs to be removed
     */
    public void RemoveCardFromHandPlayer(int index){
        board.getPlayer().getHand().RemoveCard(index);
    }

    /**
     * Adds a card to the enemy hand (no card needs to be provided)
     */
    public void AddCardToEnemyHand(){
        board.getEnemy().getHand().AddCardFromEnemy();
    }

    /**
     * Sets the HP value of a card at the provided point to the provided value
     * @param point The point that needs to be edited
     * @param hp The value by which it needs to be lowered
     */
    public void SetEnemyCardHP(int point, int hp){
        board.getEnemyField().get(point).modifyHP(hp);
    }

    /**
     * Removes a card from the enemy hand (no index needs to be provided)
     */
    public void RemoveCardFromEnemyHand(){
        board.getEnemy().getHand().RemoveCardFromEnemy();
    }

    /**
     * Sets the screenHandler to the provided value
     * @param screenParent the screenHandler
     */
    @Override
    public void setScreenParent(ScreenHandler screenParent) {
        screenHandler = screenParent;
    }
}