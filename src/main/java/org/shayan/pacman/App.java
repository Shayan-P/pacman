package org.shayan.pacman;

import javafx.application.Application;
import javafx.stage.Stage;
import org.shayan.pacman.database.Settings;
import org.shayan.pacman.menu.WelcomeMenu;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Settings.initialize();
        new WelcomeMenu().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
