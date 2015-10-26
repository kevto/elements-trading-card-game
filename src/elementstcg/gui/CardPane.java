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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static elementstcg.gui.CardState.*;

public class CardPane extends StackPane {

    private Card card;

    private Pane cardObject;
    private Pane ghostObject;

    private Pane ghostPane;
    private BoardController controller;

    private int cardWidth = 125;
    private int cardHeight = 200;

    private DropShadow shadow;
    private DropShadow selectShadow;

    private CardPane instance;

    private CardState cardState;

    /**
     * Create a CardPane with the provided data. The CardPane object contains the Card object
     * All assets will be loaded and two Pane Objects will be created.
     * CardObject - The card object itself. Used to display the card on the field and in the hand.
     * GhostObject - An independent copy of the CardObject. Used to display info of the card, the
     * ghostObject is not a child of the CardPane object. This means that the size and position of
     * CardPane will not influence the ghostObject.
     * @param card The card that needs to be created and contained within CardPane
     * @param ghostPane The ghostPane in the scene, where the ghostObject needs to be displayed
     * @param controller The controller of the board
     */
    public CardPane(Card card, Pane ghostPane, BoardController controller) {

        instance = this;
        this.card = card;
        this.ghostPane = ghostPane;
        this.controller = controller;

        CreateShadow();

        cardState = PlayerHand;

        cardObject = CreateCard(card);
        ghostObject = CreateCard(card);

        getChildren().add(cardObject);

        setTranslateY(-25);

        resizeCard();
        addEventListeners();
    }

    /**
     * Create all the event listeners for the CardPane object and children.
     */
    private void addEventListeners() {
        //MOUSE CLICK
        instance.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //LEFT CLICK
                if (event.getButton() == MouseButton.PRIMARY) {
                    //TODO: Add LEFT click event logic (EG. call method when a card is clicked)

                    if (cardState == PlayerHand) {
                        controller.selectCardInHandButtonAction(instance);
                    } else if (cardState == PlayerField) {
                        controller.selectCardButtonAction(instance);
                    } else if (cardState == EnemyField) {
                        controller.attackEnemyCardButtonAction(instance);
                    }
                }

                //RIGHT CLICK
                if (event.getButton() == MouseButton.SECONDARY) {
                    //TODO: Add RIGHT click event logic (EG. call method when a card is clicked)

                    System.out.println("RIGHT");
                    System.out.println(cardState);

                    if(cardState == PlayerField) {
                        controller.showCardButtonAction(instance);
                    }
                }
            }
        });

        //MOUSE HOVER ENTER
        instance.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (cardState == PlayerHand) {
                    showCard(true);
                }
            }
        });

        //MOUSE HOVER EXIT
        instance.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (cardState == PlayerHand) {
                    showCard(false);
                }
            }
        });
    }

    /**
     * Called to display or hide the ghostObject on the field. The ghostPane is moved from and too the back
     * to make sure it doesn't interfere with any mouse events on the field.
     * @param hover is the mouse hovering over the cardObject
     */
    public void showCard(boolean hover) {
        if(hover) {
            controller.showGhostPane(ghostObject);
        }

        if(!hover) {
            controller.hideGhostPane();
        }
    }

    /**
     * Create an visual card object with the provided card info
     * All assets needed will be loaded here
     * @param card The card needed to be created
     * @return The finished card object
     */
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

        //cardContainer.setEffect(shadow);

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

    /**
     * Get the card object
     * @return Card
     */
    public Card getCard() {
        return card;
    }

    public Pane getGhostObject(){
        return ghostObject;
    }

    /**
     * Get the instance of the CardPane
     * @return CardPane
     */
    public CardPane getInstance() {
        return instance;
    }

    /**
     * Return the current state of the CardPane object
     * @return CardState
     */
    public CardState getCardState() {
        return cardState;
    }

    /**
     * Set the state of the CardPane object
     * @param cardState the cardState you want to set this object to
     */
    public void setCardState(CardState cardState) {
        this.cardState = cardState;
    }

    /**
     * Called to visually display if the card is selected. If the card is selected, the card will move its
     * translate by X amount.
     * @param selected true/false if the card should be selected/deselected
     */
    public void setSelected(boolean selected) {
        if(selected) {
            if(cardState == PlayerHand) {
                this.setTranslateY(-50);
            }
            if(cardState == PlayerField || cardState == EnemyField){
                cardObject.setEffect(selectShadow);
            }
        }
        else {
            if(cardState == PlayerHand) {
                this.setTranslateY(-25);
            }
            if(cardState == PlayerField || cardState == EnemyField){
                cardObject.setEffect(shadow);
            }
        }
    }

    /**
     * Resize the card based on the state of the card
     */
    public void resizeCard() {
        switch(cardState) {
            case PlayerHand:
                cardObject.setScaleX(0.5);
                cardObject.setScaleY(0.5);
                break;
            case EnemyHand:
                cardObject.setScaleX(0.5);
                cardObject.setScaleY(0.5);
                break;
            case PlayerField:
                cardObject.setScaleX(0.2);
                cardObject.setScaleY(0.2);
                break;
            case EnemyField:
                cardObject.setScaleX(0.2);
                cardObject.setScaleY(0.2);
                break;
        }
    }

    /**
     * Create all shadow objects needed for displaying the cards.
     */
    private void CreateShadow() {
        int depth = 30;

        shadow = new DropShadow();
        shadow.setOffsetY(0f);
        shadow.setOffsetX(0f);
        shadow.setColor(Color.BLACK);
        shadow.setWidth(depth);
        shadow.setHeight(depth);

        selectShadow = new DropShadow();
        selectShadow.setOffsetY(0f);
        selectShadow.setOffsetX(0f);
        selectShadow.setColor(Color.GREEN);
        selectShadow.setWidth(depth);
        selectShadow.setHeight(depth);
    }

    /**
     * Resets all the X and Y values of both
     * Translate and Layout
     */
    public void resetCardPos() {
        setTranslateX(0);
        setTranslateY(0);

        layoutXProperty().set(0);
        layoutYProperty().set(0);
    }
}

/**
 * The different states a card can have
 * PlayerHand: This object is currently in the hand of the player
 * EnemyHand: This object is currently in the hand of the enemy
 * PlayerField: This object is currently on the playing field on the player side
 * EnemyField: This ob ject is currently on the playing field on the enemy side
 */
enum CardState {
    PlayerHand, EnemyHand, PlayerField, EnemyField;
}
