package com.elementstcg.client.gui.controllers;

import com.elementstcg.client.gui.*;
import com.elementstcg.client.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class BoardControlller {

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
    private CardPane selectedCard;
    private FieldGrid enemyField;

    /**
     * Method is called when the game is over.
     * Forces the turn to false so the player cant preform any actions
     */
    public void SetGameOver(){
        board.setTurn(false);
    }

    public void attackCard(Card card, int point){
        CardPane cardPane = (CardPane)enemyField.getChildren().get(point);
        cardPane.getCard().modifyHP(card.getAttack());
    }

    public void putCardPlayer(Card card, int point){
        board.getPlayerField().put(point, card);
    }

    public void updatePlayerHP(int hp){
        board.getPlayer().modifyHp(hp);
    }

    public void removeCardPlayer(int point){
        board.getPlayerField().remove(point);
    }

    public void putCardEnemy(Card card, int point){
        board.getEnemyField().put(point, card);
    }

    public void updateEnemyHp(int hp){
        board.getEnemy().modifyHp(hp);
    }

    public void removeCardEnemy(int point){
        board.getEnemyField().remove(point);
    }

    public void updateUI(String playerCapPoints, String enemyCapPoints){
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

    public void nextTurn(){

    }
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
    public void showGhostPane(Node node){

    }
    public void hideGhostPane(){

    }
    public void attackEnemyDirectButtonAction(CardPane cardPane) {
        if (selectedCard != null && selectedCard.isSelected() && selectedCard.onField()) {

        }

    }
    public BoardControlller(String enemyName, String sessionID, CardPane cardPane){
        board = new Board();
        //Even overleggen of de game een deck van de server krijgt of dat de client een deck maakt
        hboxPlayerHand.getChildren().add(cardPane);
        // Initializing the UI.
        labelEnemyName.setText(enemyName);
        hboxPlayerHand.getParent().prefWidth(hboxPlayerHand.getPrefWidth());
        hboxPlayerHand.getParent().prefHeight(hboxPlayerHand.getPrefHeight());

        playerField = new FieldGrid(847, 253, 2, 6, this, FieldType.Player);
        enemyField = new FieldGrid(847, 253, 2, 6, this, FieldType.Enemy);

        playerField.getStyleClass().add("field");
        enemyField.getStyleClass().add("field-enemy");

        playerField.setTranslateY(253);

        bPaneField.getChildren().add(playerField);
        bPaneField.getChildren().add(enemyField);

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
        updateUI(String.valueOf(0), String.valueOf(0));

    }


}