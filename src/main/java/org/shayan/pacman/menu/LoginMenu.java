package org.shayan.pacman.menu;

import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.shayan.pacman.model.PacmanException;
import org.shayan.pacman.model.User;

public class LoginMenu extends AbstractMenu {

    private void addLoginAndRegister(){
        VBox box = new VBox();
        box.setSpacing(15);
        box.setLayoutX(Width/2 - 100);
        box.setLayoutY(Height/3);

        BeautifulText errorText = new BeautifulText("", Color.RED, 15);

        TextField usernameText = new TextField();
        TextField passwordText = new TextField();

        box.getChildren().add(new HBox(new BeautifulText("username:", Color.WHEAT, 30), usernameText));
        box.getChildren().add(new HBox(new BeautifulText("password:", Color.WHEAT, 30), passwordText));
        HBox loginOrRegister = new HBox(
                new BeautifulButton("or register?", 15, ()->new RegisterMenu().start(stage)),
                new BeautifulButton("login!", 15, ()->{
                    try{
                        User.loginCurrentUser(usernameText.getText(), passwordText.getText());
                        errorText.setText("logged in successfully!");
                        errorText.setFill(Color.GREENYELLOW);
                    } catch (PacmanException e){
                        errorText.setText(e.getMessage());
                    }
                })
        );
        loginOrRegister.setSpacing(15);
        box.getChildren().add(loginOrRegister);
        box.getChildren().add(errorText);
        root.getChildren().add(box);
    }

    @Override
    public void start(Stage stage) {
        super.start(stage);
        addTitle("login");
        addBackGround();
        addBackButton(new WelcomeMenu());
        addLoginAndRegister();
        stage.show();
    }
}
