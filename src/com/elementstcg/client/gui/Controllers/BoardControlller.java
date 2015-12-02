package com.elementstcg.client.gui.Controllers;

import com.elementstcg.client.gui.*;
import com.elementstcg.client.*;
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
    private com.elementstcg.client.gui.FieldGrid enemyField;
    //
    //Dat is de methode die aangeroepen word als de speler wint of
    //verliest dan word game over aangeroepen en als het game over is dan krijg je een menu of je wilt stoppen of naar de lobby wilt
    public void SetGameOver(){

    }
    public void attackCard(Card card, int point){

    }
    public void putCardPlayer(Card card, int point){

    }
    public void updatePlayerHP(int hp){

    }
    public void removeCardPlayer(int point){

    }
    public void putCardEnemy(Card card, int point){

    }
    public void updateEnemyHp(int hp){

    }
    public void removeCardEnemy(int point){

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