package elementstcg.gui;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class FieldGrid extends Pane {

    private ArrayList<FieldPane> fields = new ArrayList<FieldPane>();
    private BoardController controller;
    private FieldType fieldType;

    /**
     * Create a FieldGrid object with the provided width, height. The FieldGrid objects will
     * create new panes based on the provided amount of columns and rows.
     * All panes will be the width of width / columns and height of height / rows.
     * @param width The width the FieldGrid needs to be
     * @param height The height the FieldGrid needs to be
     * @param rows The amount of rows (if 0 no panes will be created)
     * @param columns The amount columns (if 0 no panes will be created)
     * @param controller The controller where all actions should be reported
     */
    public FieldGrid(int width, int height, int rows, int columns, BoardController controller, FieldType fieldType) {
        this.controller = controller;

        this.setWidth(width);
        this.setHeight(height);

        this.fieldType = fieldType;

        for (int i = 0; i < rows; i++)
        {
            for(int j = 0; j < columns; j++) {
                FieldPane pane = new FieldPane();
                Rectangle rec = new Rectangle((width / columns), (height / rows));

                //TODO: DEBUG
                /*
                if(i == 0) {
                    if(j % 2 == 0) {
                        rec.setFill(Color.GREY);
                    }
                    else
                    {
                        rec.setFill(Color.BLACK);
                    }
                }
                else{
                    if(j % 2 == 0) {
                        rec.setFill(Color.BLACK);
                    }
                    else
                    {
                        rec.setFill(Color.GREY);
                    }
                }
                */
                //END DEBUG
                rec.setFill(Color.TRANSPARENT);

                rec.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            controller.selectFieldPane((FieldPane)rec.getParent());
                        }
                    }
                });

                pane.setTranslateX(j * (width / columns));
                pane.setTranslateY(i * (height / rows));

                pane.getChildren().add(rec);
                this.getChildren().add(pane);
                fields.add(pane);
            }
        }
    }

    /**
     * Return the type of FieldGrid object this is (Player/Enemy)
     * @return the fieldType object
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    /**
     * Goes through the list of FieldPane objects and gets the capacity points of the cards
     * currently inside the fieldPane object.
     * @return the current amount of Capacity Points on the field
     */
    public int getCapPoints() {
        int capPoints = 0;

        for(FieldPane pane : fields) {
            capPoints += pane.getCapPoints();
        }

        return capPoints;
    }
}

enum FieldType {
    Player, Enemy
}
