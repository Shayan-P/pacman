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
import javafx.stage.Stage;
import javafx.util.Duration;
import org.shayan.pacman.database.MapDatabase;
import org.shayan.pacman.database.Settings;
import org.shayan.pacman.extendedNodes.BeautifulButton;
import org.shayan.pacman.extendedNodes.BeautifulText;
import org.shayan.pacman.extendedNodes.ShapeBar;
import org.shayan.pacman.extra.DummyPacman;
import org.shayan.pacman.game.entity.Pacman;
import org.shayan.pacman.model.GameMap;
import org.shayan.pacman.model.PacmanException;

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
            public int getNumber() {
                return Settings.getDefaultPacmanHearts();
            }
        };
    }

    private String mapPathToName(String path){
        String[] tmp = path.split("/");
        return tmp[tmp.length-1];
    }

    private void refreshMapChoices(){
        mapChoices.getItems().clear();
        for(String path: MapDatabase.getMapPaths())
            mapChoices.getItems().add(mapPathToName(path));
        mapChoices.setValue(mapPathToName(Settings.getDefaultMapPath()));
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
                new HBox(new BeautifulText("map: ", Color.WHEAT, 30), mapChoices),
                new HBox(new BeautifulButton("delete map!", 27, ()->{
                    communicate(()-> {
                        if (mapChoices.getValue() == null)
                            throw new PacmanException("no map is selected");
                        MapDatabase.removeMap(mapChoices.getValue());
                        if(MapDatabase.getMapPaths().length == 0)
                            MapDatabase.saveToTempMap(new GameMap());
                        Settings.setDefaultMapPath(MapDatabase.getMapPaths()[0]);
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
            communicate(()-> {
                MapDatabase.saveToTempMap(new GameMap());
                Settings.setDefaultMapPath("maps/tmp.txt");
                refreshMapChoices();
            }, "generated successfully!");
        });
        BeautifulButton saveTmp = new BeautifulButton("save the random map", 20, ()->{
            communicate(()-> {
                Settings.setDefaultMapPath(MapDatabase.saveTempMap());
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
        choices.setValue(Settings.getGameDifficultyString(Settings.getGameDifficulty()));
        choices.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        choices.getItems().addAll(
                Settings.getGameDifficultyString(0),
                Settings.getGameDifficultyString(1),
                Settings.getGameDifficultyString(2)
        );
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
        Pacman pacman = new DummyPacman(0, 0);
        pacman.setFaceAnimation();
        StackPane pacmanPane = new StackPane(pacman);

        ComboBox<Integer> choices = new ComboBox<>();
        choices.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));

        settingItems.getChildren().add(new HBox(20, new BeautifulText("avatar skin: ", Color.WHEAT, 27), choices, pacmanPane));

        int numberOfPacman = Settings.getPacmanPrefixPaths().size();

        for(int i = 0; i < numberOfPacman; i++) {
            choices.getItems().add(i);
        }

        choices.setOnAction(e->{
            Settings.setFavoritePacmanId(choices.getValue());
            pacmanPane.getChildren().clear();
            Pacman p = new DummyPacman(0, 0);
            p.setFaceAnimation();
            pacmanPane.getChildren().add(p);
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
