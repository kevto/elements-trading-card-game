package com.elementstcg.client.util;

import javafx.scene.media.MediaPlayer;

/**
 * Created by maart on 7-1-2016.
 */
public class FadeRunnable implements Runnable {

    private MediaPlayer player;
    private int seconds;
    private boolean out;
    private int steps;

    public FadeRunnable(MediaPlayer player, int seconds, boolean out) {
        this.player = player;
        this.seconds = seconds;
        this.out = out;
    }

    @Override
    public void run() {
        double fadeAmount = 1.0;

        steps = 10 * seconds;

        if(steps != 0) {
            fadeAmount = fadeAmount / steps;
        }

        double volume;

        if(out) {
            volume = 1.0;
        } else {
            volume = 0.0;
        }

        for(int i = 0; i < steps; i++) {
            if(player == null) {
                break;
            }
            player.volumeProperty().set(volume);
            System.out.println(player.getVolume());
            if(out) {
                volume -= fadeAmount;
            } else {
                volume += fadeAmount;
            }

            try {
                Thread.sleep((seconds * 1000) / steps);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(out) {
            if(player != null) {
                player.stop();
                player.dispose();
            }
        } else {
            if(player != null) {
                player.volumeProperty().set(1.0);
            }
        }
    }
}
