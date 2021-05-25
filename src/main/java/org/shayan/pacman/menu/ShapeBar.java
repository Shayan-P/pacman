package org.shayan.pacman.menu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

abstract public class ShapeBar extends HBox {
    protected Image image;
    private void setShape(String path){
        image = new Image(getClass().getResource(path).toExternalForm());
    }
    abstract int getNumber();
    public void refresh(){
        setSpacing(2);
        getChildren().clear();
        for(int i = 0; i < getNumber(); i++)
            getChildren().add(new ImageView(image));
    }
    public ShapeBar(String path){
        setShape(path);
        refresh();
    }
}
