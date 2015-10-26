package elementstcg.gui;

import javafx.scene.layout.StackPane;

public class FieldPane extends StackPane {

    private FieldPane instance;
    private FieldGrid fieldGrid;
    private CardPane card;

    public FieldPane() {
        instance = this;
        fieldGrid = (FieldGrid)getParent();
    }

    /**
     * Get the card currently in the field
     * If no card is on this field null will be returned
     * @return The card on this field (Can be null)
     */
    public CardPane getCard() {
        return card;
    }

    /**
     * Place the provided card object on the field
     * This includes resizing and translate
     * @param card The CardPane object to be placed
     */
    public void setCard(CardPane card) {
        this.card = card;
        this.getChildren().add(card);

        card.setCardState(CardState.PlayerField);

        card.resetCardPos();

        card.resizeCard();
        card.setSelected(false);

        positionCard();
    }

    /**
     * Center the card on the fieldPane object
     */
    private void positionCard() {
        card.translateYProperty().set(-(card.getHeight() / 2) + 5);
        card.translateXProperty().set(-20);
    }

    /**
     * Get the capacity points from the card object contained with this object.
     * If the card object is NULL return 0
     * @return the value of the card Object
     */
    public int getCapPoints() {
        if(card != null) {
            return card.getCard().getCapacityPoints();
        }
        else {
            return 0;
        }
    }

    /**
     * Return the instance of the current object
     * @return FieldPane
     */
    public FieldPane getInstance() {
        return instance;
    }

    public FieldGrid getFieldGrid() {
        return fieldGrid;
    }

    /**
     * Remove the CardPane object from this field
     * !CardPane object will be set to null, if you need the object use getCard first!
     */
    public void removeCard() {
        if(card != null) {
            this.getChildren().remove(card);
        }
        card = null;
    }

}
