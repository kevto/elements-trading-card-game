package elementstcg.gui;

import elementstcg.Card;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class FieldPane extends Pane {

    private CardPane card;

    public CardPane getCard() {
        return card;
    }

    public void setCard(CardPane card) {
        this.card = card;

        HBox hbox = (HBox)card.getParent();

        card.setScaleX(0.2);
        card.setScaleY(0.2);
        card.setSelected(false);

        this.getChildren().add(card);
    }

}
