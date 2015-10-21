package elementstcg.gui;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class FieldPane extends StackPane {

    public FieldPane instance;

    public FieldPane() {
        instance = this;
    }

    private CardPane card;

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

    private void positionCard() {
        card.translateYProperty().set(-(card.getHeight() / 2) + 5);
        card.translateXProperty().set(-20);
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
