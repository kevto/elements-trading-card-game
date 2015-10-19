package elementstcg.gui;

import elementstcg.Card;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CardPane extends Pane {

    private Card card;

    private Pane cardObject;
    private Pane ghostObject;

    private Pane ghostPane;
    private BoardController controller;

    private int cardWidth = 125;
    private int cardHeight = 200;

    private CardPane instance;

    private CardState cardState;

    public CardPane(Card card, Pane ghostPane, BoardController controller) {

        this.card = card;
        this.ghostPane = ghostPane;

        cardState = CardState.Hand;

        this.controller = controller;

        instance = this;

        cardObject = CreateCard(card);
        ghostObject = CreateCard(card);

        this.setScaleX(0.5);
        this.setScaleY(0.5);

        this.setTranslateY(-25);

        this.getChildren().add(cardObject);

        addEventListeners();
    }

    private void addEventListeners() {
        //MOUSE CLICK
        instance.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //LEFT CLICK
                if(event.getButton()  == MouseButton.PRIMARY) {
                    //TODO: Add LEFT click event logic (EG. call method when a card is clicked)

                    if(cardState == CardState.Hand) {
                        controller.selectCardInHandButtonAction(instance);
                    }
                    else if(cardState == CardState.PlayerField) {
                        controller.selectCardButtonAction(instance);
                    }
                    else if(cardState == CardState.EnemyField) {
                        controller.attackEnemyCardButtonAction(instance);
                    }
                }

                //RIGHT CLICK
                if(event.getButton()  == MouseButton.SECONDARY) {
                    //TODO: Add RIGHT click event logic (EG. call method when a card is clicked)
                }
            }
        });

        //MOUSE HOVER ENTER
        instance.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(cardState == CardState.Hand) {
                    showCard(true);
                }
            }
        });

        //MOUSE HOVER EXIT
        instance.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(cardState == CardState.Hand) {
                    showCard(false);
                }
            }
        });
    }

    public void showCard(boolean hover) {

        if(hover) {
            ghostPane.toFront();
            ghostPane.getChildren().add(ghostObject);
        }

        if(!hover) {
            ghostPane.toBack();
            ghostPane.getChildren().remove(ghostObject);
        }

    }

    public Pane CreateCard(Card card) {

        //Setup container pane
        Pane cardContainer = new Pane();
        cardContainer.setPrefWidth(cardWidth);
        cardContainer.setPrefHeight(cardHeight);

        //Create an label
        Label capacityLabel = new Label();
        Label attackLabel = new Label();
        Label healthLabel = new Label();
        Label nameLabel = new Label();
        capacityLabel.setText(String.valueOf(card.getCapacityPoints()));
        attackLabel.setText(String.valueOf(card.getAttack()));
        healthLabel.setText(String.valueOf(card.getHP()));
        nameLabel.setText(card.getName());

        Font infoFont = new Font(40);
        Font nameFont = new Font(25);

        capacityLabel.setFont(infoFont);
        attackLabel.setFont(infoFont);
        healthLabel.setFont(infoFont);
        nameLabel.setFont(nameFont);

        //Load all images
        Image elementImage = new Image("elementstcg/gui/images/icon_" + card.getElement().name() + ".png");
        Image cardImage = new Image("elementstcg/gui/images/card_template_lvl" + (card.getCapacityPoints()) + ".png");

        //Create ImageView objects and fill them with the images
        ImageView cardImageView = new ImageView();
        ImageView elementImageView = new ImageView();
        cardImageView.setImage(cardImage);
        elementImageView.setImage(elementImage);

        //Drop Shadow
        int depth = 45;

        DropShadow borderGlow = new DropShadow();
        borderGlow.setOffsetY(0f);
        borderGlow.setOffsetX(0f);
        borderGlow.setColor(Color.BLACK);
        borderGlow.setWidth(depth);
        borderGlow.setHeight(depth);

        cardImageView.setEffect(borderGlow);

        //Add images to the container Pane
        cardContainer.getChildren().add(cardImageView);
        cardContainer.getChildren().add(elementImageView);

        //Add labels to the container Pane
        cardContainer.getChildren().add(capacityLabel);
        cardContainer.getChildren().add(attackLabel);
        cardContainer.getChildren().add(healthLabel);
        cardContainer.getChildren().add(nameLabel);

        //Center element icon to card
        elementImageView.setX((cardImage.getWidth() / 2) - (elementImage.getWidth() / 2));
        elementImageView.setY(50);

        //Set capacityLabel pos
        capacityLabel.setTranslateX(25);
        capacityLabel.setTranslateY(5);

        //Set attackLabel pos
        attackLabel.setTranslateX(30);
        attackLabel.setTranslateY(440);

        //Set capacityLabel pos
        healthLabel.setTranslateX(170);
        healthLabel.setTranslateY(440);

        //Set nameLabel pos
        nameLabel.setTranslateX(20);
        nameLabel.setTranslateY(285);

        return cardContainer;
    }

    public Card getCard() {
        return card;
    }

    public CardPane getInstance() {
        return instance;
    }

    public void setSelected(boolean selected) {
        if(selected) {
            this.setTranslateY(-50);
        }
        else {
            this.setTranslateY(-25);
        }
    }

    public CardState getState() {
        return cardState;
    }

    public void setState(CardState cardState) {
        this.cardState = cardState;
    }
}

enum CardState {
    Hand, PlayerField, EnemyField;
}
