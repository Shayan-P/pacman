package org.shayan.pacman.menu;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.shayan.pacman.extendedNodes.BeautifulButton;
import org.shayan.pacman.extendedNodes.MenuTitle;

abstract public class AbstractMenu extends Application {
    protected final double Width = 1000;
    protected final double Height = 1000;
    protected Stage stage;
    protected Scene scene;
    protected Pane root;

    protected void addBackButton(AbstractMenu menu){
        Button button = new BeautifulButton("back", 30, ()->menu.start(stage));
        StackPane container = new StackPane(button);
        container.layoutXProperty().bind(container.widthProperty().negate().add(Width));
        container.layoutYProperty().bind(container.heightProperty().negate().add(Height));
        root.getChildren().add(container);
    }

    protected void addBackGround(){
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    protected void addTitle(String title){
        MenuTitle menuTitle = new MenuTitle(title);
        menuTitle.layoutXProperty().bind(menuTitle.widthProperty().divide(2).negate().add(Width/2));
        menuTitle.setLayoutY(Height/6);
        root.getChildren().add(menuTitle);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.root = new Pane();
        this.scene = new Scene(root, Width, Height);
        stage.setScene(scene);
    }
}
