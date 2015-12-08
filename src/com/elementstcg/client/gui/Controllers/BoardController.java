package com.elementstcg.client.gui.Controllers;

import com.elementstcg.client.gui.*;
import com.elementstcg.client.*;
import com.elementstcg.client.handler.ClientHandler;
import com.elementstcg.client.util.CustomException.ExceedCapacityException;
import com.elementstcg.client.util.CustomException.OccupiedFieldException;
import com.elementstcg.client.util.DialogUtility;
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
import java.util.Map;

public class BoardController {

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
        ClientHandler.getInstance().setBoardController(null);
        board.setTurn(false);

    }

    public void attackCard(Card card, int point){
        CardPane cardPane = (CardPane)enemyField.getChildren().get(point);
        cardPane.getCard().modifyHP(card.getAttack());
    }

    public void setEnemyName(String name) {
        labelEnemyName.setText(name);
    }

    public String getEnemyName() {
        return labelEnemyName.getText();
    }

    public void setPlayerName(String name) {
        labelPlayerName.setText(name);
    }

    public String getPlayerName() {
        return labelPlayerName.getText();
    }

    public void updateEnemyHPCard(int point, int hp){
        board.getEnemyField().get(point).modifyHP(hp);
    }

    public void removeEnemyCardFromHand(int point){
        board.getEnemy().getHand().getCards().remove(point);
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

    public void nextTurn(){
        board.nextTurn();

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
        if (ghostPane.getChildren().size() > 0) {
            ghostPane.getChildren().removeAll(ghostPane.getChildren());
        }

        ghostPane.getChildren().add(node);

        ghostPane.toFront();
    }
    public void hideGhostPane() {
        ghostPane.getChildren().removeAll(ghostPane.getChildren());
        ghostPane.toBack();
    }
    public void attackEnemyDirectButtonAction() {
        if (selectedCard != null && selectedCard.isSelected() && selectedCard.onField()) {

        }

    }
    public BoardController(String enemyName, String sessionID, CardPane cardPane){
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
        updateUI();
        // Notify ClientHandler
        ClientHandler.getInstance().setBoardController(this);
    }

    public void selectCardInHandButtonAction(CardPane cardPane) {
        selectCard(cardPane);
    }
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
                    try {
                        board.putCardPlayer(point, handCard.getCard());
                    } catch (OccupiedFieldException e) {
                        DialogUtility.newDialog(e.getMessage());
                        board.forcePutCardPlayer(point, cardRemoved);
                        return;
                    } catch (ExceedCapacityException e) {
                        DialogUtility.newDialog(e.getMessage());
                        board.forcePutCardPlayer(point, cardRemoved);
                        return;
                    }

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
    public void selectFieldPane(FieldPane field) {
        if (!board.isGameOver()) {
            FieldGrid grid = (FieldGrid) field.getParent();

            if (grid.getFieldType() == FieldType.Player) {
                if (selectedCard != null) {
                    if (selectedCard.getCardState() == CardState.PlayerHand) {
                        //TODO: Notify Board object that an card has been placed on the playing field

                        for (int i = 0; i < ((FieldGrid) field.getParent()).getChildren().size(); i++) {
                            if (field.equals(((FieldGrid) field.getParent()).getChildren().get(i))) {
                                try {
                                    board.putCardPlayer((i < 6 ? i : i - 6 + 10), selectedCard.getCard());
                                    field.setCard(selectedCard);
                                } catch (OccupiedFieldException e) {
                                    DialogUtility.newDialog(e.getMessage());
                                    return;
                                } catch (ExceedCapacityException e) {
                                    DialogUtility.newDialog(e.getMessage());
                                    return;
                                }
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
    public void nextTurnButtonAction() {
        //Implement RMI action
        updateUI();
    }


    /**
     * This methode is called when a player right clicks on an card on the field
     *
     * @param cardPane that's been selected.
     */
    public void showCardButtonAction(CardPane cardPane) {
        showGhostPane(cardPane.getGhostObject());
    }

    public void attackEnemyCardButtonAction(CardPane cardPane) throws IOException {
        if (!board.isGameOver()) {
            //TODO: Implement RMI
            if (selectedCard != null && selectedCard.isSelected() && selectedCard.onField()) {
                // Check if the card is in a defend position.
            }
        }
    }

    public void addEnemyCardToHand(Card card){
        board.getEnemy().getHand().addCard(card);
    }


}