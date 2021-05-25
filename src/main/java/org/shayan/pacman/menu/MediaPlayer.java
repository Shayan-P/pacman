package org.shayan.pacman.menu;

import javafx.scene.media.AudioClip;
import org.shayan.pacman.database.Settings;

public class MediaPlayer {
    private static final AudioClip coinEating = new AudioClip(MediaPlayer.class.getResource("/audio/coin-eating.wav").toExternalForm());
    private static final AudioClip death = new AudioClip(MediaPlayer.class.getResource("/audio/death.wav").toExternalForm());
    private static final AudioClip startOfGame = new AudioClip(MediaPlayer.class.getResource("/audio/start-of-game.wav").toExternalForm());
    private static final AudioClip win = new AudioClip(MediaPlayer.class.getResource("/audio/win.wav").toExternalForm());

    public enum SoundType{
        COIN(coinEating), DEATH(death), START(startOfGame), WIN(win);
        private final AudioClip clip;
        SoundType(AudioClip clip){
            this.clip = clip;
        }
        protected AudioClip getClip(){
            return clip;
        }
    }

    public static boolean isPlaying(){
        return coinEating.isPlaying() || death.isPlaying() || startOfGame.isPlaying() || win.isPlaying();
    }
    private static void stopAll(){
        coinEating.stop();
        death.stop();
        startOfGame.stop();
        win.stop();
    }
    public static void play(SoundType type){
        if(Settings.isSoundOn()) {
            stopAll();
            type.getClip().play();
        }
    }
}
