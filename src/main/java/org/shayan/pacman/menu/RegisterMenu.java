package org.shayan.pacman.menu;

import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.shayan.pacman.extendedNodes.BeautifulButton;
import org.shayan.pacman.extendedNodes.BeautifulText;
import org.shayan.pacman.model.PacmanException;
import org.shayan.pacman.model.User;

public class RegisterMenu extends AbstractMenu {
    private void addRegister(){
        VBox box = new VBox();
        box.setSpacing(15);
        box.setLayoutX(Width/2 - 100);
        box.setLayoutY(Height/3);

        BeautifulText errorText = new BeautifulText("", Color.RED, 15);

        TextField usernameText = new TextField();
        TextField passwordText = new TextField();

        box.getChildren().add(new HBox(new BeautifulText("username:", Color.WHEAT, 30), usernameText));
        box.getChildren().add(new HBox(new BeautifulText("password:", Color.WHEAT, 30), passwordText));
        box.getChildren().add(new HBox(
                new BeautifulButton("register!", 15, ()->{
                    try{
                        new User(usernameText.getText(), passwordText.getText());
                        errorText.setText("registered successfully!");
                        errorText.setFill(Color.GREENYELLOW);
                    } catch (PacmanException e){
                        errorText.setText(e.getMessage());
                    }
                })
        ));
        box.getChildren().add(errorText);
        root.getChildren().add(box);
    }

    @Override
    public void start(Stage stage) {
        super.start(stage);
        addTitle("register");
        addBackGround();
        addBackButton(new WelcomeMenu());
        addRegister();
        stage.show();
    }

}
