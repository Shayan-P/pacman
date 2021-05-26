package org.shayan.pacman.game;

import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.shayan.pacman.database.Settings;
import org.shayan.pacman.game.entity.*;
import org.shayan.pacman.game.entity.ai.AI;
import org.shayan.pacman.game.entity.ai.BfsAI;
import org.shayan.pacman.game.entity.ai.LineChaserAI;
import org.shayan.pacman.game.event.EndOfRoundEvent;
import org.shayan.pacman.game.event.PacmanWinsEvent;
import org.shayan.pacman.menu.GameMenu;
import org.shayan.pacman.model.GameMap;
import org.shayan.pacman.model.User;

import java.io.*;
import java.util.*;

public class GameWorld {
    private static GameWorld instance;
    private final List<Wall> walls = new ArrayList<>();
    private final List<AI> ais = new ArrayList<>();
    private final List<Coin> coins = new ArrayList<>();
    private final double mapWidth;
    private final double mapHeight;
    private final Pane root;
    private final double BLOCK_LENGTH;
    private final GameMap gameMap;

    private Pacman pacman;
    private int eatenCoins;
    private int lives = Settings.getDefaultPacmanHearts();
    private int highestScore;
    private boolean isFirstRound;

    {
        isFirstRound = true;
        highestScore = User.getCurrentUser() == null ? 0 : User.getCurrentUser().getScore();
        eatenCoins = 0;
    }

    public static GameWorld getInstance(){
        if(instance == null)
            throw new Error("no instance!");
        return instance;
    }

    public GameWorld(Pane root){
        instance = this;
        this.root = root;

        try {
            gameMap = new GameMap(new FileReader(Settings.getDefaultMapPath()));
        } catch (Exception e){
            throw new Error("error in reading map");
        }
        BLOCK_LENGTH = 960.0 / Math.max(gameMap.getWidth(), gameMap.getHeight());

        mapWidth = gameMap.getWidth() * BLOCK_LENGTH;
        mapHeight = gameMap.getHeight() * BLOCK_LENGTH;

        loadWalls();
        loadCoins();
        loadPacmanAndAIs();
    }

    public void startNewRound(){
        if(isFirstRound){
            isFirstRound = false;
            return;
        }

        restartPacmanAndAIs();

        if (coins.isEmpty())
            loadCoins();
    }

    public void pacmanEatsCoin(Coin coin){
        coins.remove(coin);
        root.getChildren().remove(coin);
        eatenCoins += 1;
        // todo change + eaten guys
        highestScore = Math.max(highestScore, eatenCoins * 5);
        if(User.getCurrentUser() != null)
            User.getCurrentUser().updateScore(eatenCoins * 5);
        if(coins.isEmpty()){
            lives += 1;
            GameMenu.getInstance().fireEvent(new PacmanWinsEvent());
        }
    }
    public void pacmanAndAIIntersect(AI ai){
        // todo add super mode

        lives -= 1;
        GameMenu.getInstance().fireEvent(new EndOfRoundEvent());
    }

    private void loadPacmanAndAIs(){
        for(int i = 0; i < gameMap.getWidth(); i++){
            for(int j = 0; j < gameMap.getHeight(); j++){
                MapEntity cell = gameMap.getCells()[i][j];
                double x = BLOCK_LENGTH * i, y = BLOCK_LENGTH * j;
                if(cell.equals(MapEntity.PACMAN)) {
                    this.pacman = new Pacman(x, y);
                    this.pacman.activate();
                    root.getChildren().add(this.pacman);
                }
                if(cell.equals(MapEntity.AI)){
                    AI ai = new BfsAI(500, ais.size(), x, y);
//                    AI ai = new LineChaserAI(x, y);
                    ai.activate();
                    root.getChildren().add(ai);
                    ais.add(ai);
                }
            }
        }
    }

    private void restartPacmanAndAIs(){
        int aiCounter = 0;
        for(int i = 0; i < gameMap.getWidth(); i++){
            for(int j = 0; j < gameMap.getHeight(); j++){
                MapEntity cell = gameMap.getCells()[i][j];
                double x = BLOCK_LENGTH * i, y = BLOCK_LENGTH * j;
                if(cell.equals(MapEntity.PACMAN)) {
                    this.pacman.setX(x);
                    this.pacman.setY(y);
                }
                if(cell.equals(MapEntity.AI)){
                    ais.get(aiCounter).setX(x);
                    ais.get(aiCounter).setY(y);
                    aiCounter += 1;
                }
            }
        }
    }

    private void loadCoins() {
        for (int i = 0; i < gameMap.getWidth(); i++) {
            for (int j = 0; j < gameMap.getHeight(); j++) {
                MapEntity cell = gameMap.getCells()[i][j];
                double x = BLOCK_LENGTH * i, y = BLOCK_LENGTH * j;
                if (cell.equals(MapEntity.COIN)) {
                    Coin coin = new Coin(x, y);
                    coin.activate();
                    root.getChildren().add(coin);
                    coins.add(coin);
                }
            }
        }
    }
    private void loadWalls(){
        for (int i = 0; i < gameMap.getWidth(); i++) {
            for (int j = 0; j < gameMap.getHeight(); j++) {
                MapEntity cell = gameMap.getCells()[i][j];
                double x = BLOCK_LENGTH * i, y = BLOCK_LENGTH * j;
                if(cell.equals(MapEntity.WALL)){
                    Wall wall = new Wall(x, y);
                    root.getChildren().add(wall);
                    walls.add(wall);
                }
            }
        }
    }

    public Pacman getPacman() {
        return pacman;
    }

    public double getMapWidth() {
        return mapWidth;
    }

    public double getMapHeight() {
        return mapHeight;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<AI> getAIs() {
        return ais;
    }

    public List<Coin> getCoins() {
        List<Coin> other = new ArrayList<>();
        other.addAll(coins);
        return other;
    }

    public int getLives() {
        return lives;
    }

    public int getEatenCoins() {
        return eatenCoins;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public double getBlockLength() {
        return BLOCK_LENGTH;
    }

    public GameMap getGameMap() {
        return gameMap;
    }
}
