package com.elementstcg.client.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Description here.
 *
 * @author Kevin Berendsen
 * @since 2015-10-29
 */
public class DialogUtility {
    public static void newDialog(String content) {
        final Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setHeaderText(null);
        dialog.setTitle("Elements TCG");
        dialog.setContentText(content);
        dialog.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    dialog.close();
                });
                timer.cancel();
            }
        }, 3000);
    }
}
