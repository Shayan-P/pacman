package org.shayan.pacman.menu;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.shayan.pacman.database.MapDatabase;
import org.shayan.pacman.database.Settings;
import org.shayan.pacman.model.GameMap;
import org.shayan.pacman.model.PacmanException;

import java.util.ArrayList;
import java.util.List;

public class SettingsMenu extends AbstractMenu {
    private VBox settingItems;
    private final ComboBox<String> mapChoices = new ComboBox<>();
    private final ShapeBar hearts;
    private final BeautifulText alertBox = new BeautifulText("", Color.RED, 20);
    interface ExceptionThrowingRunnable{
        void run() throws PacmanException;
    }

    {
        hearts = new ShapeBar("/icons/heart.png") {
            @Override
            int getNumber() {
                return Settings.getDefaultPacmanHearts();
            }
        };
    }

    private void refreshMapChoices(){
        mapChoices.getItems().clear();
        for(String path: MapDatabase.getMapPaths()) {
            String[] tmp = path.split("/");
            String name = tmp[tmp.length-1];
            mapChoices.getItems().add(name);
        }
    }

    private void communicate(ExceptionThrowingRunnable runner, String onSuccess){
        try {
            runner.run();
            if(onSuccess != null)
                showSuccess(onSuccess);
        } catch (PacmanException e){
            showAlert(e.getMessage());
        }
    }

    private void addMapSetting(){
        HBox container = new HBox(
                new HBox(new BeautifulText("map: ", Color.WHEAT, 27), mapChoices),
                new HBox(new BeautifulButton("delete map!", 27, ()->{
                    communicate(()-> {
                                if (mapChoices.getValue() == null)
                                    throw new PacmanException("no map is selected");
                                MapDatabase.removeMap(mapChoices.getValue());
                            }, "deleted successfully!");
                    refreshMapChoices();
                })));
        container.setSpacing(10);
        settingItems.getChildren().add(container);

        mapChoices.setOnAction(e->{
            if(mapChoices.getValue() != null)
                Settings.setDefaultMapPath("maps/" + mapChoices.getValue());
        });
        mapChoices.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        refreshMapChoices();
    }

    private void addRandomMapGeneratorSetting(){
        BeautifulButton generate = new BeautifulButton("generate a random map", 20, ()->{
            MapDatabase.saveToTempMap(new GameMap());
            refreshMapChoices();
        });
        BeautifulButton saveTmp = new BeautifulButton("save the random map", 20, ()->{
            communicate(()-> {
                MapDatabase.saveTempMap();
                refreshMapChoices();
            }, "saved successfully!");
        });

        HBox hBox = new HBox(
                new ImageView(new Image(getClass().getResource("/icons/dice.png").toExternalForm())),
                generate,
                saveTmp
        );
        hBox.setSpacing(7);
        settingItems.getChildren().add(hBox);
    }

    private void addDifficultySetting(){
        ComboBox<String> choices = new ComboBox<>();
        settingItems.getChildren().add(new HBox(new BeautifulText("difficulty: ", Color.WHEAT, 27), choices));

        choices.setOnAction(e->{
            Settings.setGameDifficulty(choices.getValue());
        });

        choices.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        choices.getItems().addAll("Easy", "Normal", "Hard");
    }
    private void addSoundSetting(){
        Image on = new Image(getClass().getResource("/icons/sound-on.png").toExternalForm());
        Image off = new Image(getClass().getResource("/icons/sound-off.png").toExternalForm());
        ImageView imageView = new ImageView();
        StackPane container = new StackPane(imageView);
        container.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        settingItems.getChildren().add(container);
        if(Settings.isSoundOn())
            imageView.setImage(on);
        else
            imageView.setImage(off);
        container.setOnMouseClicked(e->{
            Settings.toggleSound();
            if(Settings.isSoundOn())
                imageView.setImage(on);
            else
                imageView.setImage(off);
        });
    }

    public void addPacmanChoice(){
        ComboBox<ImageView> choices = new ComboBox<>();
        choices.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));

        settingItems.getChildren().add(new HBox(new BeautifulText("avatar: ", Color.WHEAT, 27), choices));

        List<ImageView> imageViews = new ArrayList<>();

        // todo fix this!
        int counter = 0;
        for(String path : Settings.getPacmanPrefixPaths()){
            System.out.println(path);
            ImageView tmp = new ImageView(new Image(Settings.class.getResource(String.format("/pacman/%d/front/0.png", counter)).toExternalForm()));
            counter += 1;
            choices.getItems().add(tmp);
            imageViews.add(tmp);
        }

        choices.setOnAction(e->{
            int innerCounter = 0;
            for(ImageView imageView : imageViews){
                if(imageView.equals(choices.getValue())){
//                    String[] tmp = imageView.getImage().getUrl().split("/");
//                    Settings.setFavoritePacmanId(Integer.parseInt(tmp[tmp.length-3]));
                    Settings.setFavoritePacmanId(innerCounter);
                }
                innerCounter += 1;
            }
        });
    }

    private void addDefaultPacmanHeartsSettings(){
        settingItems.getChildren().add(
                new HBox(20,
                        new HBox(5,
                                new BeautifulButton("<", 30, ()->{
                                    communicate(Settings::decreaseDefaultPacmanHearts, null);
                                    hearts.refresh();
                                }).setNewShape(new Circle(10)),
                                new BeautifulText("Hearts", Color.DEEPPINK, 27),
                                new BeautifulButton(">", 30, ()->{
                                    communicate(Settings::increaseDefaultPacmanHearts, null);
                                    hearts.refresh();
                                }).setNewShape(new Circle(10))
                        ),
                        hearts
                )
        );
        hearts.refresh();
    }

    private void addAlertBox(){
        settingItems.getChildren().add(alertBox);
    }

    private void setTimeOutForAlertBox(String before, int seconds){
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(seconds), e->{
            if(alertBox.getText().equals(before))
                alertBox.setText("");
        }));
        tl.setCycleCount(1);
        tl.play();
    }
    private void showSuccess(String s){
        alertBox.setText(s);
        alertBox.setFill(Color.GREENYELLOW);
        setTimeOutForAlertBox(s, 5);
    }
    private void showAlert(String s){
        alertBox.setText(s);
        alertBox.setFill(Color.RED);
        setTimeOutForAlertBox(s, 5);
    }

    @Override
    public void start(Stage stage) {
        super.start(stage);
        addTitle("settings");
        addBackGround();
        addBackButton(new WelcomeMenu());

        settingItems = new VBox();
        settingItems.layoutXProperty().bind(settingItems.widthProperty().divide(2).negate().add(Width/2));
        settingItems.setLayoutY(Height/3);
        settingItems.setSpacing(40);
        root.getChildren().add(settingItems);

        addMapSetting();
        addRandomMapGeneratorSetting();
        addDifficultySetting();
        addSoundSetting();
        addPacmanChoice();
        addDefaultPacmanHeartsSettings();
        addAlertBox();

        stage.show();
    }
}
