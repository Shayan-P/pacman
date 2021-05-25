package org.shayan.pacman.database;

import org.shayan.pacman.model.PacmanException;
import org.shayan.pacman.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Settings {
    static String DefaultMapPath;
    static final String databasePath = "database/db.dat";
    static boolean soundIsOn = true;
    static int gameDifficulty = 0;
    static int favoritePacmanId = 0;
    static int defaultPacmanHearts = 3;

    public static void initialize(){
        loadFromDatabase();
        DefaultMapPath = MapDatabase.getMapPaths()[0];
    }

    public static void saveToDatabase() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(databasePath));
            for (User user : User.getUsers())
                objectOutputStream.writeObject(user);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException exception) {
            throw new Error("failed to save!");
        }
    }

    public static void loadFromDatabase() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(databasePath));
            try {
                while (true) {
                    Object object = objectInputStream.readObject();
                    assert object instanceof User;
                    User user = (User)object;
                    user.save();
                }
            } catch (Exception ignored) {
            }
        } catch (IOException exception) {
            throw new Error("failed to load!");
        }
    }

    public static String getDefaultMapPath() {
        return DefaultMapPath;
    }

    public static void setDefaultMapPath(String defaultMapPath) {
        DefaultMapPath = defaultMapPath;
    }

    public static void toggleSound(){
        soundIsOn ^= true;
    }
    public static boolean isSoundOn(){
        return soundIsOn;
    }

    public static int getGameDifficulty() {
        return gameDifficulty;
    }

    public static void setGameDifficulty(String gameDifficulty) {
        if(gameDifficulty.equalsIgnoreCase("easy"))
            Settings.gameDifficulty = 0;
        else if(gameDifficulty.equalsIgnoreCase("normal"))
            Settings.gameDifficulty = 1;
        else if(gameDifficulty.equalsIgnoreCase("hard"))
            Settings.gameDifficulty = 2;
        else
            throw new Error("invalid string for difficulty");
    }

    public static String getFavoritePacmanPathPrefix() {
        return "/pacman/" + favoritePacmanId;
    }
    public static void setFavoritePacmanId(int id){
        favoritePacmanId = id;
    }

    public static List<String> getPacmanPrefixPaths(){
        ArrayList<String> ret = new ArrayList<>();
        File pacDir = new File("target/classes/pacman");
        Arrays.stream(pacDir.listFiles()).forEach(e->{
            ret.add(e.getPath());
        });
        return ret;
    }

    public static void increaseDefaultPacmanHearts() throws PacmanException {
        if(defaultPacmanHearts == 5)
            throw new PacmanException("hearts must be from 2 to 5");
        defaultPacmanHearts++;
    }
    public static void decreaseDefaultPacmanHearts() throws PacmanException {
        if(defaultPacmanHearts == 2)
            throw new PacmanException("hearts must be from 2 to 5");
        defaultPacmanHearts--;
    }
    public static int getDefaultPacmanHearts() {
        return defaultPacmanHearts;
    }
}
