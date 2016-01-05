package com.elementstcg.client.gui;

import com.elementstcg.shared.trait.Card;
import com.elementstcg.shared.trait.Element;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AttackUI extends Pane {

    private ImageView backgroundImageView;

    private int attackValue;
    private int defenceValue;

    private ImageView attackImageView;
    private ImageView defenceImageView;
    private ImageView sword;

    private Text damageText;
    private double scale;

    private static final Image backgroundImage = new Image("com/elementstcg/client/gui/images/damageBackground.png");
    private static final Image fireImage = new Image("com/elementstcg/client/gui/images/icon_Fire.png");
    private static final Image waterImage = new Image("com/elementstcg/client/gui/images/icon_Water.png");
    private static final Image earthImage = new Image("com/elementstcg/client/gui/images/icon_Earth.png");
    private static final Image thunderImage = new Image("com/elementstcg/client/gui/images/icon_Thunder.png");
    private static final Image airImage = new Image("com/elementstcg/client/gui/images/icon_Air.png");
    private static final Image swordImage = new Image("com/elementstcg/client/gui/images/atk_icon.png");

    private static final double strongMultiplier = 2.0;
    private static final double weakMultiplier = 0.5;

    public AttackUI() {
        backgroundImageView = new ImageView(backgroundImage);

        attackValue = 1;
        defenceValue = 1;

        attackImageView = new ImageView(fireImage);
        defenceImageView = new ImageView(fireImage);
        damageText = new Text(525, 145, "5.0");
        damageText.textAlignmentProperty().set(TextAlignment.CENTER);
        damageText.setFont(new Font(125));

        sword = new ImageView(swordImage);
        sword.setTranslateX(190);
        sword.setTranslateY(25);

        defenceImageView.setTranslateX(325);

        this.getChildren().add(backgroundImageView);
        this.getChildren().add(attackImageView);
        this.getChildren().add(defenceImageView);
        this.getChildren().add(sword);
        this.getChildren().add(damageText);

        scale = 0.35;
        setScale(scale);
    }

    /**
     * !-Should only be called when the player hovers over a enemy card when the player has selected an card-!
     *
     * Changes the attacking and defending element images to the elements of the provided card objects.
     * Sets the damageText to the value of the attacking card.
     * If the element is strong versus this element it will show the multiplied attack value.
     * Adds the AttackUI to the pane
     * @param attack the attacking card
     * @param defence the defending card
     */
    public void onHoverEnter(Card attack, Card defence, Pane pane) {
        Element attackElement = attack.getElement();
        Element defenceElement = defence.getElement();

        attackImageView.setImage(getElementImage(attackElement));
        defenceImageView.setImage(getElementImage(defenceElement));

        if(isStrongAgainst(attackElement, defenceElement)) {
            damageText.setFill(Color.GREEN);
            damageText.setText(String.valueOf(attack.getAttack() * strongMultiplier));
        } else {
            damageText.setFill(Color.BLACK);
            damageText.setText(String.valueOf(attack.getAttack()));
        }

        pane.getChildren().add(this);
    }

    /**
     * Should be called when the player no longer hovers over an card
     *
     * Removes the AttackUI from the pane
     */
    public void onHoverExit() {
        Pane parent = (Pane)this.getParent();

        if(parent != null) {
            parent.getChildren().remove(this);
        }
    }

    /**
     * Returns the image file for the requested element
     * @param element the requested element
     * @return image object for the element
     */
    public Image getElementImage(Element element) {
        switch(element) {
            case Fire:
                return fireImage;
            case Water:
                return waterImage;
            case Earth:
                return earthImage;
            case Thunder:
                return thunderImage;
            case Air:
                return airImage;
        }

        return null;
    }

    @Deprecated
    /**
     * Returns if the attack elemenmt is weaks against the defending element described by the rules of the game
     * @param attack the element that will attack
     * @param defence the element that will defend
     * @return true if weak, else false
     */
    private boolean isWeakAgainst(Element attack, Element defence) {
        //The rules do not describe a weakness for an element.
        //Placeholder if needed

        return false;
    }

    /**
     * Returns if the given attack element is strong against the defending element described by the rules of the game
     * @param attack the element that will attack
     * @param defence the element that will defend
     * @return true if strong, else false
     */
    private boolean isStrongAgainst(Element attack, Element defence) {
        switch(attack) {
            case Fire:
                return defence == Element.Air;
            case Water:
                return defence == Element.Fire;
            case Air:
                return defence == Element.Thunder;
            case Earth:
                return defence == Element.Water;
            case Thunder:
                return defence == Element.Earth;
        }

        return false;
    }

    /**
     * Sets the scale of the AttackUI to the given value and scales the object onscreen
     * @param scale the desired scale
     */
    public void setScale(double scale) {
        this.scale = scale;

        this.setScaleX(scale);
        this.setScaleY(scale);
    }

    /**
     * returns the value of the scale field
     * @return scale
     */
    public double getScale() {
        return scale;
    }
}
