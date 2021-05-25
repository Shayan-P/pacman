package org.shayan.pacman.menu;

import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.text.Text;

public class MenuTitle extends Pane {
    MenuTitle(String name){
        Text text = new BeautifulText("  " + name.toUpperCase() + "  ",
                new LinearGradient(0, 0, 1, 0, true,
                    CycleMethod.REFLECT,
                    new Stop(0, Color.BLACK), new Stop(0.5, Color.WHEAT), new Stop(1, Color.BLACK)),
                40);
        getChildren().add(text);
    }
    public MenuTitle setX(double x){
        setLayoutX(x);
        return this;
    }
    public MenuTitle setY(double y){
        setLayoutY(y);
        return this;
    }
}
