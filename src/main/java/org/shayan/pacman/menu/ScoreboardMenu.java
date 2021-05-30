package org.shayan.pacman.menu;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import org.shayan.pacman.extendedNodes.BeautifulText;
import org.shayan.pacman.model.User;

import java.util.Collections;
import java.util.List;

public class ScoreboardMenu extends AbstractMenu {
    private void addUsers(){
        GridPane userItems = new GridPane();
        userItems.setHgap(30);
        userItems.setVgap(30);
        root.getChildren().add(userItems);
        userItems.setLayoutX(Width/2-130);
        userItems.setLayoutY(Height/4);
        List<User> userList = User.getUsers();
        Collections.sort(userList);

        userItems.add(new BeautifulText("user", Color.AZURE, 30), 0, 0);
        userItems.add(new BeautifulText("score", Color.AZURE, 30), 1, 0);

        int lastCount = 0;
        for(int i = 0; i < Math.min(10, userList.size()); i++){
            int count = (i == 0 || userList.get(i).getScore() != userList.get(i-1).getScore() ? i : lastCount);
            lastCount = count;
            User user = userList.get(i);
            userItems.add(new BeautifulText("#" + (count + 1) + " " + user.getUsername(), Color.WHEAT, 20), 0, i+1);
            userItems.add(new BeautifulText(Integer.toString(user.getScore()), Color.WHEAT, 20), 1, i+1);
        }
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.root = new Pane();
        this.scene = new Scene(root, Width, Height);
        stage.setScene(scene);
        addTitle("scoreboard");
        addBackGround();
        addBackButton(new WelcomeMenu());
        addUsers();
        stage.show();
    }
}
