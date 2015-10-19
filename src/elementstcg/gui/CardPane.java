package elementstcg.gui;

import elementstcg.Card;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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

    public CardPane(Card card, Pane ghostPane, BoardController controller) {
        //Store the card info in the card field
        this.card = card;
        //Store the ghostPane in the ghostPane field
        //ghostPane is a Pane in the center of the playing field for displaying card the player is hovering over
        this.ghostPane = ghostPane;

        //Store the controller in the controller field
        //controller handles all logic
        this.controller = controller;

        //Create instance object so eventHandlers can return the cardPane
        instance = this;


        //Create two card objects (JavaFX objects)
        //There are two created so they can be scaled separately
        cardObject = CreateCard(card);
        ghostObject = CreateCard(card);

        //Set the scale for the ghostCard (DEBUG)
        ghostObject.setScaleX(0.8);
        ghostObject.setScaleY(0.8);

        //Set the scale for the hand card (DEBUG)
        this.setScaleX(0.25);
        this.setScaleY(0.25);

        //Add the cardObject to the CardPane so it is displayed on the stage
        this.getChildren().add(cardObject);

        //Method for creating all needed Event Handlers
        addEventListeners();
    }

    private void addEventListeners() {
        //MOUSE CLICK
        instance.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //LEFT CLICK
                if(event.getButton()  == MouseButton.PRIMARY) {
                    System.out.println(event.getTarget().toString());

                    //TODO: Add click event logic (EG. call method when a card is clicked)
                }

                //RIGHT CLICK
                if(event.getButton()  == MouseButton.SECONDARY) {

                }


            }
        });

        //MOUSE HOVER ENTER
        instance.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                showCard(true);

                //System.out.println("Enter " + card.getName());
            }
        });

        //MOUSE HOVER EXIT
        instance.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                showCard(false);

                //System.out.println("Exit " + card.getName());
            }
        });
    }



    public void showCard(boolean hover) {

        if(hover) {
            ghostPane.getChildren().add(ghostObject);
        }

        if(!hover) {
            ghostPane.getChildren().remove(ghostObject);
        }

    }

    public Pane CreateCard(Card card) {

        //TODO: RESOLVE HARDCODING OR CREATE FINAL ASSET SIZE

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

        Font infoFont = new Font(80);
        Font nameFont = new Font(55);

        capacityLabel.setFont(infoFont);
        attackLabel.setFont(infoFont);
        healthLabel.setFont(infoFont);
        nameLabel.setFont(nameFont);

        //Load all images
        Image elementImage = new Image("images/icon_" + card.getElement().name() + ".png");
        Image cardImage = new Image("images/card_template_lvl" + (card.getCapacityPoints()) + ".png");

        //Create ImageView objects and fill them with the images
        ImageView cardImageView = new ImageView();
        ImageView elementImageView = new ImageView();
        cardImageView.setImage(cardImage);
        elementImageView.setImage(elementImage);

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
        elementImageView.setY(140);

        //Set capacityLabel pos
        capacityLabel.setTranslateX(15);

        //Set attackLabel pos
        attackLabel.setTranslateX(115);
        attackLabel.setTranslateY(690);

        //Set capacityLabel pos
        healthLabel.setTranslateX(350);
        healthLabel.setTranslateY(690);

        //Set nameLabel pos
        nameLabel.setTranslateX(30);
        nameLabel.setTranslateY(440);

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
            this.setTranslateY(-25);
        }
        else {
            this.setTranslateY(0);
        }
    }
}
