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

    public FieldGrid(int width, int height, int rows, int columns, BoardController controller) {
        this.controller = controller;

        this.setWidth(width);
        this.setHeight(height);

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

                System.out.println(rec.getWidth());
            }
        }
    }
}
