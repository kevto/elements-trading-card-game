package com.elementstcg.client.gui.Controllers;

import com.elementstcg.client.gui.*;
import com.elementstcg.client.*;
import com.elementstcg.client.handler.ClientHandler;
import com.elementstcg.client.util.CustomException.ExceedCapacityException;
import com.elementstcg.client.util.CustomException.OccupiedFieldException;
import com.elementstcg.client.util.DialogUtility;
import com.elementstcg.shared.trait.Card;
import com.elementstcg.shared.trait.IResponse;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.net.URL;
import java.rmi.RemoteException;

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

    @FXML Button btSendMessage;
    @FXML ListView chatBox;
    @FXML TextField chatField;

    @FXML Label labelEnemyName;
    @FXML Label labelPlayerName;


    @FXML Pane enemyInfo;

    private FieldGrid playerField;
    private FieldGrid enemyField;
    private CardPane selectedCard;
    private AttackUI attackUI;
    private ScreenHandler screenHandler;
    private ObservableList<String> chatMessages = FXCollections.observableArrayList();

    private double lastX;
    private double lastY;

    private double posX;
    private double posY;

    /**
     * Methode for the server to call if the game is over
     * Forces the player turn to be false so he cant issue an commands to the server
     * Removes this boardController from the clientHandler
     */
    public void SetGameOver(boolean won){
        board.setTurn(false);

        //TODO: Have a screen show with the result (win/lose) and a button to return to the lobby

        Platform.runLater(() -> {
            Image resultImage;

            if(won) {
                resultImage = new Image("com/elementstcg/client/gui/images/endgame_banner_victory.png");
            }
            else {
                resultImage = new Image("com/elementstcg/client/gui/images/endgame_banner_defeat.png");
            }

            ImageView resultImageView = new ImageView(resultImage);

            mainPane.getChildren().add(resultImageView);

            resultImageView.setTranslateX((mainPane.getWidth() / 2) - 250);
            resultImageView.setTranslateY((mainPane.getHeight() / 2) - 100);

            resultImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        ClientHandler.getInstance().returnLobby();
                    }
                }
            });
        });
    }

    /**
     * Add damage to the selected point with the given card
     * @param playerPoint The card that is attacking
     * @param enemyPoint The point that is getting attacked
     */
    public void attackCard(int playerPoint, int enemyPoint){
            ClientHandler.AttackCard(playerPoint, enemyPoint);

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
        Platform.runLater(() -> {
            FieldPane field = (FieldPane) playerField.getChildren().get((point > 5 ? point - 10 + 6 : point));
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
        Platform.runLater(() -> {
            labelPlayerHP.setText("" + board.getPlayer().getHp());
        });
    }

    /**
     * Removes the card at the provided point from the players field
     * @param point the point of the card that needs to be removed
     */
    public void removeCardPlayer(int point){
        board.getPlayerField().remove(point);

        int fieldPointer = (point > 5 ? point - 4 : point);
        FieldPane pane = (FieldPane) playerField.getChildren().get(fieldPointer);

        Platform.runLater(() -> {
            pane.removeCard();
            updateUI();
        });
    }

    /**
     * Places the given card on the enemy field at the provided point
     * @param card The card that needs to be placed
     * @param point The point on the playing field it has to be placed on
     */
    public void putCardEnemy(Card card, int point){
        //TODO Optimize method
        Platform.runLater(() -> {
            int fieldpointer = (point > 5 ? point - 4 : point);
            //fieldpointer = (fieldpointer > 5 ? fieldpointer - 6 : fieldpointer + 6);


            FieldPane pane = (FieldPane) enemyField.getChildren().get(fieldpointer);
            CardPane cardPane = new CardPane(card, ghostPane, this);
            pane.setCard(cardPane);
            cardPane.setCardState(CardState.EnemyField);
            board.putCardEnemy(point, card);

            // TODO Apparently I'd need this property to set the cards right. Find a better way to fix this.
            if (point > 9) {
                pane.translateYProperty().set(-70);
            } else {
                pane.translateYProperty().set(40);
            }

            updateUI();
        });
    }
/*
    public void showCardButtonAction(CardPane cardPane) {
        showGhostPane(cardPane.getGhostObject());
    }
    */

    /**
     * Decreases the value HP of the enemy object by the provided value
     * @param hp the value by with HP needs to be lowered
     */
    public void updateEnemyHp(int hp){
        board.getEnemy().modifyHp(hp);
        Platform.runLater(() -> {
            labelEnemyHP.setText("" + board.getEnemy().getHp());
        });
    }

    /**
     * Removes the card at the provided point from the enemy's field
     * @param point the point of the card that needs to be removed
     */
    public void removeCardEnemy(int point){
        board.getEnemyField().remove(point);

        int fieldPointer = (point > 5 ? point - 4 : point);
        FieldPane pane = (FieldPane) enemyField.getChildren().get(fieldPointer);

        Platform.runLater(() -> {
            pane.removeCard();
            updateUI();
        });
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
        labelEnemyCAP.setText(String.valueOf(Board.MAX_CAP_POINTS - enemyField.getCapPoints()));
        labelPlayerCAP.setText(String.valueOf(Board.MAX_CAP_POINTS - playerField.getCapPoints()));

        labelEnemyHP.setText(String.valueOf(board.getEnemy().getHp()));     // Only useful when the player hasn't attacked the enemy yet.
        labelPlayerHP.setText(String.valueOf(board.getPlayer().getHp()));   // Only useful when the player hasn't attacked the enemy yet.

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
        Platform.runLater(() -> {
            if (turn) {
                DialogUtility.newDialog("It's now your turn!");
            }
            board.setTurn(turn);
        });
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
            int playerPoint = -1;
            for (Map.Entry<Integer, Card> entry : board.getPlayerField().entrySet())
                if (entry.getValue().equals(selectedCard.getCard()))
                    playerPoint = entry.getKey();
            if (playerPoint != -1) {
                ClientHandler.AttackEnemy(playerPoint);
            }
        }

    }

    /**
     * Initializes the BoardController and sets up the game
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Create a board object
        board = new Board();

        chatBox.setItems(chatMessages);

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

        btSendMessage.setOnAction((event) -> {
            // Send a message
            String newChatMessage = "[" + board.getPlayer().getName() + "]"  + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + " :" + chatField.getText();
            ClientHandler.sendMessage(newChatMessage);
        });

        // Set on click listener to enemy info box (pane).
        enemyInfo.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            attackEnemyDirectButtonAction();
        });

        attackUI = new AttackUI();

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
                    CardPane fieldCard = cardPane;
                    CardPane handCard = selectedCard;

                    // Try to put the new card onto the field. Checking for limitations.
                    int point = board.getPlayerCardPoint(fieldCard.getCard());
                    int selectedIndex = hboxPlayerHand.getChildren().indexOf(handCard);

                    try {
                        IResponse response = ClientHandler.getInstance().getServerHandler().replaceCard(
                                ClientHandler.getInstance().getSessionKey(),
                                selectedIndex,
                                point);
                        Platform.runLater(() -> {
                            try {
                                if(!response.wasSuccessful()) {
                                    DialogUtility.newDialog(response.getMessage());
                                }
                            } catch (RemoteException ex) {
                                ex.printStackTrace();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        });
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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
                        //Notify Board object that an card has been placed on the playing field

                        for (int i = 0; i < ((FieldGrid) field.getParent()).getChildren().size(); i++) {
                            if (field.equals(((FieldGrid) field.getParent()).getChildren().get(i))) {
                                //Send request to the server to place the card on the selected field pane.

                                try {
                                    IResponse response = ClientHandler.getInstance().getServerHandler().placeCard(ClientHandler.getInstance().getSessionKey(),
                                            hboxPlayerHand.getChildren().indexOf(selectedCard),
                                            (i < 6 ? i : i - 6 + 10));

                                    Platform.runLater(() -> {
                                        try {
                                            if(!response.wasSuccessful()) {
                                                DialogUtility.newDialog(response.getMessage());
                                            }
                                        } catch (RemoteException ex) {
                                            ex.printStackTrace();
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    });
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

        try {
            final IResponse response = ClientHandler.getInstance().getServerHandler().nextTurn(ClientHandler.getInstance().getSessionKey());
            if(!response.wasSuccessful()) {
                Platform.runLater(() -> {
                    try {
                        DialogUtility.newDialog(response.getMessage());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (RemoteException e) {
            e.printStackTrace();
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
     * Called when an player clicks an enemy card
     * @param cardPane
     * @throws IOException
     */
    public void attackEnemyCardButtonAction(CardPane cardPane) throws IOException {
        if (!board.isGameOver()) {
            int playerPoint = -1;
            int enemyPoint = -1;
            for (Map.Entry<Integer, Card> entry : board.getPlayerField().entrySet())
                if (entry.getValue().equals(selectedCard.getCard()))
                    playerPoint = entry.getKey();

            for (Map.Entry<Integer, Card> entry : board.getEnemyField().entrySet())
                if (entry.getValue().equals(cardPane.getCard()))
                    enemyPoint = entry.getKey();
            if (playerPoint == -1 || enemyPoint == -1) {
                DialogUtility.newDialog("Een ongeldige actie, selecteer de juiste kaart");
                return;
            }

                try {
                    ClientHandler.AttackCard(playerPoint, enemyPoint);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


    /**
     * Add the provided card to the hand of the player and show the card in the game
     * @param card the card that needs to be added
     */
    public void AddCardToPlayerHand(Card card){
        board.getPlayer().getHand().addCard(card);

        Platform.runLater(() -> {
            hboxPlayerHand.getChildren().add(new CardPane(card, ghostPane, this));
            //TODO: Add the card to the hboxPlayerHand or find an alternative that uses the board.player.hand to automaticly do this
        });
    }

    /**
     * Sets the player deck size to the provided value. The value has to be the total size
     * @param amount the total size of the deck
     */
    public void UpdatePlayerDeckCount(int amount){
        board.getPlayer().getDeck().setRemainingCards(amount); // What good does this???

        Platform.runLater(() -> {
            labelPlayerDeckSize.setText(String.valueOf(amount));
        });
    }

    /**
     * Sets the enemy deck size to the provided value. The value has to be the total size
     * @param amount the total size of the deck
     */
    public void updateEnemyDeckCount(int amount){
        board.getEnemy().getDeck().setRemainingCards(amount); // What good does this???

        Platform.runLater(() -> {
            labelEnemyDeckSize.setText(String.valueOf(amount));
        });
    }

    /**
     * Sets the HP value of a card at the provided point to the provided value
     * @param point The point that needs to be edited
     * @param hp The value by which it needs to be lowered
     */
    public void SetPlayerCardHp(int point, int hp){
        board.getPlayerField().get(point).modifyHP(board.getPlayerField().get(point).getHP() - hp);

        int fieldPointer = (point > 5 ? point - 4 : point);
        FieldPane pane = (FieldPane) playerField.getChildren().get(fieldPointer);

        Platform.runLater(() -> {
            //pane.getCard().getCard().modifyHP(pane.getCard().getCard().getHP() - hp);
            pane.getCard().updateUi();
        });

    }

    /**
     * Removes the card from the players hand on the provided index
     * @param index the index of the card that needs to be removed
     */
    public void RemoveCardFromHandPlayer(int index){
        Platform.runLater(() -> {
            board.getPlayer().getHand().RemoveCard(index);
            hboxPlayerHand.getChildren().remove(index);
        });
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
        board.getEnemyField().get(point).modifyHP(board.getEnemyField().get(point).getHP() - hp);

        int fieldPointer = (point > 5 ? point - 4 : point);
        FieldPane pane = (FieldPane) enemyField.getChildren().get(fieldPointer);

        Platform.runLater(() -> {
            //pane.getCard().getCard().modifyHP(pane.getCard().getCard().getHP() - hp);
            pane.getCard().updateUi();
        });
    }

    public void recieveMessage(String message){
        Platform.runLater(() -> {
            chatMessages.add(message);
        });
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

    /**
     * Called when the mouse enters the node of an card placed on the enemy field
     * Shows the AttackUI helper
     *
     * If there is currently a card selected, the game wil show the damage this card will do against
     * the enemy card
     */
    public void enemyCardOnEnter(CardPane hoverCard) {
        if(selectedCard != null) {
            attackUI.onHoverEnter(selectedCard.getCard(), hoverCard.getCard(), mainPane);
        }
    }

    /**
     * Called when the mouse exits the node of an card placed on the enemy field
     * Hides the AttackUI helper
     */
    public void enemyCardOnExit() {
        attackUI.onHoverExit();
    }

    /**
     * Called when the mouse moves in the node of an card placed on the enemy field
     * Moves the AttackUI next to the mouse
     */
    public void enemyCardOnMove(double x, double y) {
        attackUI.setTranslateX(x - 400);
        attackUI.setTranslateY(y - 50);
    }

    public void onHelpClicked(Event event) {
        Stage helpStage = new Stage();
        helpStage.setTitle("Help");
        Pane stagePane = new Pane();
        Image image = new Image("com/elementstcg/client/gui/images/RULES.png");
        ImageView imageView = new ImageView(image);

        stagePane.getChildren().add(imageView);

        helpStage.setScene(new Scene(stagePane));

        helpStage.show();
    }
}