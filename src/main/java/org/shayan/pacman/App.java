package org.shayan.pacman;

import javafx.application.Application;
import javafx.stage.Stage;
import org.shayan.pacman.database.Settings;
import org.shayan.pacman.extra.CentralMediaPlayer;
import org.shayan.pacman.menu.LoadingMenu;

public class App extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        App.primaryStage = primaryStage;
        Settings.initialize();
        CentralMediaPlayer.playBackgroundMusic();
        new LoadingMenu().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
