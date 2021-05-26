package org.shayan.pacman.database;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.shayan.pacman.menu.GameMenu;
import org.shayan.pacman.model.PacmanException;
import org.shayan.pacman.model.User;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Settings {
    static String DefaultMapPath;
    static final String databasePath = "database/db.dat";
    static BooleanProperty soundIsOn = new SimpleBooleanProperty(false);
    static int gameDifficulty = 1;
    static int favoritePacmanId = 1;
    static int defaultPacmanHearts = 3;

    public static void initialize(){
        loadFromDatabase();
        if(DefaultMapPath == null)
            DefaultMapPath = MapDatabase.getMapPaths()[0];
    }

    public static void saveToDatabase() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(databasePath));
            for (User user : User.getUsers())
                objectOutputStream.writeObject(user);
            objectOutputStream.writeObject("this is the end of users");
            objectOutputStream.writeObject(User.getCurrentUser());

//            try {
//                objectOutputStream.writeObject(History.getLastGameMenu());
//                System.out.println("error in saving current game");
//            } catch (Exception e){
//                e.printStackTrace();
//                objectOutputStream.writeObject(null);
//            }

            objectOutputStream.writeObject(DefaultMapPath);
            objectOutputStream.writeObject(soundIsOn.getValue());
            objectOutputStream.writeObject(gameDifficulty);
            objectOutputStream.writeObject(favoritePacmanId);
            objectOutputStream.writeObject(defaultPacmanHearts);

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
                    if(object instanceof User) {
                        User user = (User) object;
                        user.save();
                    }
                    else {
                        object = objectInputStream.readObject();
                        if (object != null)
                            User.loginCurrentUser((User) object);

//                        try {
//                            object = objectInputStream.readObject();
//                            History.setLastGameMenu((GameMenu) object);
//                        } catch (Exception e){
//                            e.printStackTrace();
//                            System.out.println("error in loading current game");
//                        }
//
                        object = objectInputStream.readObject();
                        Settings.setDefaultMapPath((String) object);

                        object = objectInputStream.readObject();
                        Settings.soundIsOn.setValue((boolean) object);

                        object = objectInputStream.readObject();
                        Settings.gameDifficulty = (int) object;

                        object = objectInputStream.readObject();
                        Settings.favoritePacmanId = (int) object;

                        object = objectInputStream.readObject();
                        Settings.defaultPacmanHearts = (int) object;
                    }
                }
            } catch (Exception ignored) {
            }
        } catch (IOException exception) {
            System.out.println("error in reading from db");
        }
    }

    public static String getDefaultMapPath() {
        return DefaultMapPath;
    }

    public static void setDefaultMapPath(String defaultMapPath) {
        DefaultMapPath = defaultMapPath;
    }

    public static void toggleSound(){
        soundIsOn.setValue(!soundIsOn.get());
    }
    public static boolean isSoundOn(){
        return soundIsOn.get();
    }
    public static BooleanProperty getSoundIsOnProperty(){
        return soundIsOn;
    }

    public static int getGameDifficulty() {
        return gameDifficulty;
    }

    public static String getGameDifficultyString(int id){
        if(id == 0)
            return "Easy";
        if(id == 1)
            return "Normal";
        if(id == 2)
            return "Hard";
        return null;
    }
    public static void setGameDifficulty(String gameDifficulty) {
        if(gameDifficulty.equalsIgnoreCase(getGameDifficultyString(0)))
            Settings.gameDifficulty = 0;
        else if(gameDifficulty.equalsIgnoreCase(getGameDifficultyString(1)))
            Settings.gameDifficulty = 1;
        else if(gameDifficulty.equalsIgnoreCase(getGameDifficultyString(2)))
            Settings.gameDifficulty = 2;
        else
            throw new Error("invalid string for difficulty");
    }

    public static String getFavoritePacmanPathPrefix() {
        return "/pacman/" + favoritePacmanId;
    }
    public static int getFavoritePacmanId() {
        return favoritePacmanId;
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
