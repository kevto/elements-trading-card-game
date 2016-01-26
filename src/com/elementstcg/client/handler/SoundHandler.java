package com.elementstcg.client.handler;

import com.elementstcg.client.util.FadeRunnable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class SoundHandler {

    private static SoundHandler instance;

    private String dir;

    private MediaPlayer musicPlayer;
    private List<MediaPlayer> effectenPlayers;

    private Thread fadeOutThread;
    private Thread fadeInThread;

    private boolean loop;

    public static SoundHandler getInstance() {
        if(instance == null) {
            new SoundHandler();
        }

        return instance;
    }

    private SoundHandler() {
        dir = System.getProperty("user.dir").replace('\\', '/').replace(" ", "%20");
        effectenPlayers = new ArrayList<MediaPlayer>();
        fadeOutThread = new Thread();
        fadeInThread = new Thread();
        instance = this;
    }

    public void playEffect(String path){
        MediaPlayer player = new MediaPlayer(getMedia(getURI(path)));
        effectenPlayers.add(player);
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                if(player != null) {
                    player.stop();
                    player.dispose();
                    effectenPlayers.remove(player);
                }
            }
        });
        player.play();
    }

    public void playMusic(String path, boolean fade, boolean loop) {
        this.loop = loop;

        if(fade) {
            if(musicPlayer != null) {
                MediaPlayer fadeOut = musicPlayer;
                musicPlayer = new MediaPlayer(getMedia(getURI(path)));
                musicPlayer.play();

                fadeOutThread = new Thread(new FadeRunnable(fadeOut, 5, true));
                fadeOutThread.start();

                fadeInThread = new Thread(new FadeRunnable(musicPlayer, 5 , false));
                fadeInThread.start();
            }
            else {
                playMusic(path, false, loop);
            }
        } else {
            if(musicPlayer != null) {
                musicPlayer.stop();
                musicPlayer.dispose();
                musicPlayer = null;
            }

            musicPlayer = new MediaPlayer(getMedia(getURI(path)));
            musicPlayer.play();
        }

        if(loop) {
            musicPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    musicPlayer.seek(Duration.ZERO);
                }
            });
        } else {
            musicPlayer.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    if(musicPlayer != null) {
                        musicPlayer.stop();
                        musicPlayer.dispose();
                        musicPlayer = null;
                    }
                }
            });
        }
    }

    public void killAllEffecten() {
        for (MediaPlayer player : effectenPlayers) {
            if(player != null) {
                player.stop();
                player.dispose();
            }
        }

        effectenPlayers.clear();
    }

    public void stopMusic(boolean fade) {

        if(musicPlayer != null) {
            if(fade) {
                MediaPlayer fadeOut = musicPlayer;
                fadeOutThread = new Thread(new FadeRunnable(fadeOut, 5, true));
                fadeOutThread.start();
            }
            else {
                musicPlayer.stop();
                musicPlayer.dispose();
            }
        }
    }

    private Media getMedia(String path) {
        Media media = new Media(path);

        return media;
    }

    public String getURI(String path) {
        return "file:/" + dir + path;
    }
}