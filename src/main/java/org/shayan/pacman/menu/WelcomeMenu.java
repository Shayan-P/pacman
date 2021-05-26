package org.shayan.pacman.menu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.shayan.pacman.App;
import org.shayan.pacman.database.History;
import org.shayan.pacman.database.Settings;
import org.shayan.pacman.extendedNodes.BeautifulText;
import org.shayan.pacman.extendedNodes.MenuItem;
import org.shayan.pacman.extra.AlertBox;
import org.shayan.pacman.extra.DummyPacman;
import org.shayan.pacman.game.entity.Pacman;
import org.shayan.pacman.model.User;

public class WelcomeMenu extends AbstractMenu {
    static {
        App.primaryStage.setOnCloseRequest(e->Settings.saveToDatabase());
    }

    private void addMenuItems(){
        VBox menuItems = new VBox();
        root.getChildren().add(menuItems);
        menuItems.setTranslateX(Width/2);
        menuItems.setTranslateY(Height/3);
        if(History.getLastGameMenu() != null)
            menuItems.getChildren().add(new MenuItem("resume game", ()->{ History.getLastGameMenu().resumeStart(stage); }));
        menuItems.getChildren().add(new MenuItem("new game", ()->{ new GameMenu().start(stage); }));
        menuItems.getChildren().add(new MenuItem("scoreboard", ()->{ new ScoreboardMenu().start(stage); }));
        menuItems.getChildren().add(new MenuItem("settings", ()->{ new SettingsMenu().start(stage); }));
        if(User.getCurrentUser() == null){
            menuItems.getChildren().add(new MenuItem("login", ()->{ new LoginMenu().start(stage); }));
        }
        else {
            menuItems.getChildren().add(new MenuItem("logout", () -> {
                if (new AlertBox().display("Are you sure you want to logout?")) {
                    User.logoutCurrentUser();
                    new WelcomeMenu().start(stage);
                }
            }));
        }
        if(User.getCurrentUser() != null){
            menuItems.getChildren().add(new MenuItem("remove account", ()->{
                if (new AlertBox().display("Are you sure you want to delete your account?")) {
                    User.getCurrentUser().removeUser();
                    User.logoutCurrentUser();
                    new WelcomeMenu().start(stage);
                }
            }));
        }
        menuItems.getChildren().add(new MenuItem("quit", Utils::exitRequest));

        for(int i = 0; i < menuItems.getChildren().size(); i++){
            Node item = menuItems.getChildren().get(i);
            item.setTranslateX(-300);
            TranslateTransition tt = new TranslateTransition(Duration.millis(1000 + 500 * (i+1)), item);
            tt.setToX(0);
            tt.play();
        }
    }

    private void addDummyPacman(){
        DummyPacman dummyPacman = new DummyPacman(Width/2, Height/2);
        dummyPacman.activate();
        scene.setOnMouseMoved(e->{
            dummyPacman.setDestination(e.getX(), e.getY());
        });
        root.getChildren().add(dummyPacman);
    }

    private void addWelcomeToCurrentUser(){
        if(User.getCurrentUser() != null) {
            Text text = new BeautifulText("Hi " + User.getCurrentUser().getUsername() + "!", Color.DARKGREEN, 27);
            text.setLayoutX(10);
            text.setLayoutY(40);
            root.getChildren().add(text);
        }
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.root = new Pane();
        this.scene = new Scene(root, Width, Height);
        stage.setScene(scene);
        addTitle("pacman");
        addMenuItems();
        addBackGround();
        addDummyPacman();
        addWelcomeToCurrentUser();
        stage.show();
    }
}
